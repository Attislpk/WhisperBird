package org.wdzl.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

public class TestHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //从前端获取的数据
        String content = msg.text();
        System.out.println("从前端接受到的数据是:" + content);

        //发送数据
//        clients.writeAndFlush(new TextWebSocketFrame("服务器在 "+ LocalDateTime.now() + "接受到的数据是: "+content));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        clients.remove(ctx.channel());
        System.out.println("客户端断开连接，channel的长id为： "+ ctx.channel().id().asLongText());
    }
}
