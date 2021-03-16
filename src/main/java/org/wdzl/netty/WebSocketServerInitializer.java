package org.wdzl.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //初始化channel
        //获取管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        //===================基于http协议的支持====================================
        //webSocket基于http协议，所需要的http的编码解码器
        pipeline.addLast(new HttpServerCodec());
        //使用netty对http上的大数据流写提供支持，该类叫做ChunkedWriteHandler
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMsg进行聚合处理，聚合成request或者response
        pipeline.addLast(new HttpObjectAggregator(1024*64));



        //================================增加心跳支持 start====================================
        pipeline.addLast(new IdleStateHandler(8,10,12));
        //只添加这个handler还不够，因为idelevent无法被触发，还需要添加一个idleStateHandler
        pipeline.addLast(new HeartBeatHandler());


        //================================增加心跳支持 end====================================
        /**
         * 处理握手操作的handler(handshaking, close ping、pong) ping+pong=心跳,/ws代表客户端连接访问的路由
         * 对于websocket而言，基于frames进行数据传输，不同数据的frames也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        /**
         * 自定义的用于显示前端消息的handler
         */
//       pipeline.addLast(new TestHandler());


        /**
         * 自定义的用于传送聊天信息的的handler
         */
       pipeline.addLast(new ChatHandler());
    }
}
