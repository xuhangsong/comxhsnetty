package com.xhs.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xuhan  build  2019/1/25
 * BIO 服务端源码
 */
@Slf4j
public class Server {
    private static int DEFALT_PORT = 7777;

    private static ServerSocket serverSocket;

    public static void start() throws IOException {
        start(DEFALT_PORT);
    }

    public static synchronized  void start(int port) throws IOException {
        if (serverSocket != null) return;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(("服务端已启动 ！ port = " + port));
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ServerHandler(socket)).start();
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
                System.out.println(("socket server 结束"));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server.start();
    }
}
