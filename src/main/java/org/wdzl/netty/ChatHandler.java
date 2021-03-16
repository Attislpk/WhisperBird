package org.wdzl.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.codehaus.plexus.util.StringUtils;
import org.wdzl.SpringUtil;
import org.wdzl.enums.MsgActionEnum;
import org.wdzl.netty.chatentity.ChatMsg;
import org.wdzl.netty.chatentity.DataContent;
import org.wdzl.netty.chatentity.UserChannelRel;
import org.wdzl.service.ChatMsgService;
import org.wdzl.utils.JsonUtils;


import java.util.ArrayList;
import java.util.List;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //用于记录和管理所有客户端channel的clients
    static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取客户端传递来的消息
        String content = msg.text();
        Channel currentChannel = ctx.channel();
        System.out.println("从前端接受到的数据是:" + content);

        //1. 判断客户端发来的消息
        DataContent dataContent = JsonUtils.jsonToPojo(content, DataContent.class);
        assert dataContent != null;
        Integer action = dataContent.getAction();

        //2. 判断消息类型，根据不同的类型来处理不同的业务
        if(action == MsgActionEnum.CONNECT.getState()){
            //   2.1 当websocket第一次open的时候，初始化channel，将用户id和该channel进行关联
            String senderId = dataContent.getChatMsg().getSenderId();
            UserChannelRel.put(senderId,currentChannel);
        }else if(action == MsgActionEnum.CHAT.getState()){
            //   2.2 聊天类型的消息：将聊天记录保存到数据库, 同时标记消息的签收状态[未签收]
            ChatMsg chatMsg = dataContent.getChatMsg();
            String msgText = chatMsg.getMsg();
            String receiverId = chatMsg.getReceiverId();
            String senderId = chatMsg.getSenderId();
            //将数据插入数据库中, 并标记为未签收
            ChatMsgService chatMsgService = (ChatMsgService) SpringUtil.getBean("chatMsgServiceImpl");
            //传入netty包中的ChatMsg对象
            String msgId = chatMsgService.insert(chatMsg);
            chatMsg.setMsgId(msgId);
            DataContent dataContentMsg = new DataContent();
            dataContentMsg.setChatMsg(chatMsg);

            //发送消息, 从全局用户channel中获取到接收方的channel
            Channel receiverChannel = UserChannelRel.get(receiverId);
            if(receiverChannel == null){
                //websocket没有与后台进行过连接，因此map中没有对应的channel 推送消息 TODO
            }else{
                //receiverChannel不为空，则从channelGroup中查找对应的channel是否存在
                Channel findedChannel = users.find(receiverChannel.id());
                if(findedChannel != null){
                    //用户在线, 此时向用户推送的是datacontent(可能是websocket要求更新好友列表或者是其他人发送的聊天信息)
                    findedChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContentMsg)));
                }else{
                    //channelgroup中没有对应的channel，用户离线 TODO 推送消息
                }
            }
        }else if(action == MsgActionEnum.SIGNED.getState()){
            //2.3 签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态[已签收]
            ChatMsgService chatMsgService = (ChatMsgService) SpringUtil.getBean("chatMsgServiceImpl");
            //扩展字段在signed类型的消息中，代表需要去签收的消息id，使用逗号间隔
            String msgIdStr = dataContent.getExtend();
            String[] msgIds = msgIdStr.split(",");

            //存储所有需要签收的消息的msgId
            List<String> msgIdList = new ArrayList<>();
            //将msgIds[]数组中的msgId添加到list中
            for(String mid: msgIds){
                if(StringUtils.isNotBlank(mid)){
                    msgIdList.add(mid);
                }
            }
            System.out.println(msgIdList.toString());

            //批量进行消息的签收
            if(!msgIdList.isEmpty()){
                chatMsgService.updateMsgSigned(msgIdList);
            }
        }else if(action == MsgActionEnum.KEEPALIVE.getState()){
            //   2.4 心跳类型的消息
            System.out.println("收到来自:" +currentChannel +"的心跳包");
        }
    }

    /**
     * 当客户端连接服务端之后，获取客户端的channel并放到ChannelGroup中去进行管理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //当触发handlerRemoved时候，channelGroup会自动移除掉相应客户端的channel
        System.out.println("channel被移除" + ctx.channel().id().asLongText());
        users.remove(ctx.channel());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常
        cause.printStackTrace();
        //发送异常之后先关闭连接(关闭channel)，随后从channelgroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
