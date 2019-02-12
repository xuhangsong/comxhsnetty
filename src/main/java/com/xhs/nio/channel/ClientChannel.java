package com.xhs.nio.channel;

import com.xhs.nio.buffer.Buffers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xuhan  build  2019/1/28
 */
public class ClientChannel implements Runnable{

    private String name;
    private InetSocketAddress remoteAddress;

    public ClientChannel(String name, InetSocketAddress remoteAddress) {
        this.name = name;
        this.remoteAddress = remoteAddress;
    }

    @Override
    public void run() {
        int index = 0;
        SelectionKey key = null;
        try {
            Selector selector = Selector.open();
            SocketChannel sc = SocketChannel.open();
            //设置为非堵塞
            sc.configureBlocking(false);
            int  intereste = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
            //将该channel注册到selector，感兴趣的事件是read和write
            sc.register(selector,intereste,new Buffers(8,256));
            sc.connect(remoteAddress);
            while(!sc.finishConnect()){
                //完成连接则跳出
            }
            System.out.println(name +"连接成功");
            while(!Thread.currentThread().isInterrupted()){
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while(it.hasNext()){
                    key = it.next();
                    it.remove();
                    Buffers buffers = (Buffers) key.attachment();
                    ByteBuffer readBuffer = buffers.getReadBuffer();
                    ByteBuffer writeBuffer = buffers.getWriteBuffer();
                    SocketChannel sc1 = (SocketChannel) key.channel();
                    if(key.isReadable()){
                        CharBuffer charBuffer = null;
                        ArrayList<Byte> bytes = new ArrayList<Byte>();
                        while(sc1.read(readBuffer)>0){
//                        sc1.read(readBuffer);
                        readBuffer.flip();
                        for(int i=0;i<readBuffer.limit();i++){
                            bytes.add(readBuffer.get(i));
                        }
                        readBuffer.clear();
                      }
                      ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.size());
                      for(int i=0;i<bytes.size();i++){
                            byteBuffer.put(bytes.get(i));
                      }
                      byteBuffer.flip();
                        charBuffer = Charset.forName("utf-8").decode(byteBuffer);
                        System.out.println("客户端读取到的为："+charBuffer.toString());
                    }
                    if(key.isWritable()){
                        writeBuffer.put(("my name is xhs this is count"+(++index)).getBytes("utf-8"));
                        writeBuffer.flip();
                        sc1.write(writeBuffer);
                        writeBuffer.clear();
                    }
                }
            TimeUnit.SECONDS.sleep(2);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(key+"的连接被关闭");
            if(key!=null){
                key.cancel();
                try {
                    key.channel().close();
                } catch (IOException e1) {
                    System.err.println(key+"的channel关闭失败");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1",6666);
        Thread t1 = new Thread(new ClientChannel("aaaaa",address));
        t1.start();
        TimeUnit.SECONDS.sleep(60000);
        t1.interrupt();
    }
}
