package com.xhs.bio;

import lombok.extern.slf4j.Slf4j;
import sun.java2d.pipe.RenderBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author xuhan  build  2019/1/25
 */
@Slf4j
public class ServerHandler implements Runnable{

    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            String expression = null;
            String result = null;
            while (true){
                if((expression=in.readLine())==null) break;
                System.out.println(("服务器接收到消息 " + expression));
                result = Calculator.cal(expression);
                out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getLocalizedMessage());

        }finally {
            if(in !=null){
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out !=null){
                    out.close();
                    out = null;
            }
        }
    }
}
