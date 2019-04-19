package com.xhs.netty.ch01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import javax.swing.*;

/**
 * @author xuhan  build  2019/1/29
 */
public class NettyServer {
    private final static String IP = "127.0.0.1";
    private final static int PORT = 6666;
    private final static int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors();
    private final static int BIZTHREADSIZE = 100;

    private final static EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private final static EventLoopGroup workGroup = new NioEventLoopGroup(BIZTHREADSIZE);

    public static void start() throws InterruptedException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new TcpServerHandler());
                    }
                });
        ChannelFuture channelFuture = serverBootstrap.bind(IP,PORT).sync();

        channelFuture.channel().closeFuture().sync();

        System.out.println("server start");
    }

    protected static void shutdown(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("启动Server...");
        NettyServer.start();
    }
}
