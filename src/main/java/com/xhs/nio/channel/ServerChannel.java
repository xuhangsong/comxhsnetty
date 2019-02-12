package com.xhs.nio.channel;

import com.xhs.nio.buffer.Buffers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author xuhan  build  2019/1/28
 */
public class ServerChannel implements Runnable{

    private InetSocketAddress localAddress;

    public ServerChannel(int port) {
        this.localAddress = new InetSocketAddress(port);
    }

    @Override
    public void run() {
        SelectionKey key = null;
        Selector selector = null;
        ServerSocketChannel ssc = null;
        try {
            selector = Selector.open();
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.register(selector,SelectionKey.OP_ACCEPT,new Buffers(256,256));
            ssc.bind(localAddress);
            System.out.println("服务器启动。。。");
        } catch (IOException e) {
            System.err.println("IOException");
        }
            int index = 0;
            while(!Thread.currentThread().isInterrupted()){
                key=null;
                try {
                    selector.select();
                } catch (IOException e) {
                    System.err.println("select.select IO Exception");
                }
                Set<SelectionKey> selectionKeys =  selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        if(key.isAcceptable()){
                            ServerSocketChannel ssc1 = (ServerSocketChannel) key.channel();
                            SocketChannel sc = ssc1.accept();
                            sc.configureBlocking(false);
                            int  intereste = SelectionKey.OP_READ|SelectionKey.OP_WRITE;
                            sc.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE,new Buffers(256,256));

                            System.out.println("accept socket channel");
                        }
                        if(key.isReadable()){
                            Buffers buffers = (Buffers) key.attachment();
                            ByteBuffer readBuffer = buffers.getReadBuffer();
                            SocketChannel sc = (SocketChannel) key.channel();
                            StringBuilder sb = new StringBuilder();
                            CharBuffer cb = null;
                        while(sc.read(readBuffer)>0){
//                            sc.read(readBuffer);
                            readBuffer.flip();
                            cb= Charset.forName("utf-8").decode(readBuffer);
                            sb.append(cb.array());
                            readBuffer.clear();
                        }
                            System.out.println("接收到的消息："+sb);
                            ByteBuffer writeBuffer = buffers.getWriteBuffer();
                            writeBuffer.put(("服务器返回数据："+(++index)).getBytes("utf-8"));
                            writeBuffer.flip();
                            sc.write(writeBuffer);
                            writeBuffer.clear();
                        }
                        if(key.isWritable()){

                        }
                    }catch (IOException e){
                        System.err.println("while 内部IOException");
                        if(key!=null){
                            System.err.println("key cancel");
                            key.cancel();
                            try {
                                key.channel().close();
                            } catch (IOException e1) {
                                System.err.println("关闭channel 失败");
                            }

                        }
                    }
//                    TimeUnit.SECONDS.sleep(10);
                }

            }


//        catch (InterruptedException e) {
//            System.err.println("中断服务器线程");
//        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new ServerChannel(6666));
        t1.start();
        TimeUnit.SECONDS.sleep(1000);
    }
}
