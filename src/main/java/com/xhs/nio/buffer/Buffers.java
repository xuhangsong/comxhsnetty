package com.xhs.nio.buffer;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * @author xuhan  build  2019/1/28
 */
public class Buffers {
    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;

    public Buffers(int readSize,int writeSize) {
        readBuffer = ByteBuffer.allocate(readSize);
        writeBuffer = ByteBuffer.allocate(writeSize);
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        byteBuffer.put("哈哈".getBytes("UTF-8"));
        System.out.println(byteBuffer);
        byteBuffer.flip();
        CharBuffer charBuffer = Charset.forName("UTF-8").decode(byteBuffer);
        System.out.println(charBuffer.toString());


    }
}
