package com.xhs.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author xuhan  build  2019/1/25
 */
@Slf4j
public class Client {

    private static int DEFALT_SERVER_PORT = 7777;

    private Socket socket;
    private PrintWriter printWriter;

    public Client() throws IOException {
        socket = new Socket("127.0.0.1", DEFALT_SERVER_PORT);
        printWriter = new PrintWriter(socket.getOutputStream(), true);
    }

    public void send(String str) throws IOException {
        if (socket == null) {
            System.out.println(("socket 没有初始化"));
            return;
        }
        printWriter.println(str);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            String result = null;
            if ((result = bufferedReader.readLine()) == null) break;
            System.out.println(result);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client();
        client.send("我是徐杭淞");
    }

}
