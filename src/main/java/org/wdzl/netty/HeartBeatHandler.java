package org.wdzl.netty;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;


public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //判断evt是否是用户出发的IdleStateEvent(READER_IDLE_STATE_EVENT;WRITER_IDLE_STATE_EVENT;ALL_IDLE_STATE_EVENT;)
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state() == IdleStateEvent.READER_IDLE_STATE_EVENT.state()){
                //读空闲
                System.out.println("进入读空闲......");
            }else if(event.state() == IdleStateEvent.WRITER_IDLE_STATE_EVENT.state()){
                System.out.println("进入写空闲......");
            }else if(event.state() == IdleStateEvent.ALL_IDLE_STATE_EVENT.state()){
                System.out.println("进入读写空闲");
                Channel channel = ctx.channel();
                System.out.println("关闭前channel的数量:" + ChatHandler.users.size());
                //关闭无用的channel，防止资源浪费
                channel.close();
                System.out.println("关闭后channel的数量:" + ChatHandler.users.size());
            }
        }
    }
}
