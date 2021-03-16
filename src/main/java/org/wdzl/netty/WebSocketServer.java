package org.wdzl.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jdk.jfr.Event;
import org.springframework.stereotype.Component;

/**
 * webSocket的服务
 */
@Component
public class WebSocketServer {

    //单例模式
    private static class SingletonWebSocketServer{
        static final WebSocketServer instance = new WebSocketServer();
    }

    public static WebSocketServer getInstance(){
        return SingletonWebSocketServer.instance;
    }

    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;

    //构造方法中完成WebsocketServer的初始化
    public WebSocketServer(){
        //创建主从线程池
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(mainGroup,subGroup).channel(NioServerSocketChannel.class).childHandler(new WebSocketServerInitializer());
    }

    public void start(){
        this.channelFuture = serverBootstrap.bind(8888);
            //主线程跑的太快，如果不在此处sleep 1s，channelFuture的状态还没来得及修改，到下面isSuccess是false
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(channelFuture.isSuccess()){
            System.err.println("netty websocket server 启动成功...");
        }else {
            System.err.println("netty websocket server 启动失败...");
        }
    }
}
