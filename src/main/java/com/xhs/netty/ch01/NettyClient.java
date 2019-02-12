package com.xhs.netty.ch01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author xuhan  build  2019/1/31
 */
public class NettyClient implements Runnable{
    @Override
    public void run() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("frameEncoder",new LengthFieldPrepender(4));
                            pipeline.addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast("encode",new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast("myClient",new MyClient());
                        }
                    });
            for(int i=0;i<10;i++){
                ChannelFuture f = bootstrap.connect("127.0.0.1",6666).sync();
                f.channel().writeAndFlush("my client send message>>>>>>>>>>>"+i);

                f.channel().closeFuture().sync();

            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        for(int i=0;i<3;i++){
            new Thread(new NettyClient(),Thread.currentThread().getName()+"======"+i).start();
        }

    }
}
