package com.xhs;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

/**
 * @author xuhan  build  2019/4/17
 */
public class NettyPoolByteBufTest {
    public static void main(String[] args) {
        final byte[] CONTENT = new byte[1024];
        int count = 180000;
        long start = System.currentTimeMillis();
        ByteBuf byteBuf = null;
        for(int i=0;i<count;i++){
             byteBuf= Unpooled.directBuffer(1024);
            byteBuf.writeBytes(CONTENT);
        }
        System.out.println("结束耗时："+(System.currentTimeMillis()-start));
        start = System.currentTimeMillis();
        ByteBuf  poolBuf= null;
        for(int i=0;i<count;i++){
            poolBuf = PooledByteBufAllocator.DEFAULT.buffer(1024);
            poolBuf.writeBytes(CONTENT);
        }
        System.out.println("pool结束耗时："+(System.currentTimeMillis()-start));


    }
}
