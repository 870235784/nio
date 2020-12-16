package com.tca.io_model.bio.web;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhoua
 * @Date 2020/2/8
 * BIO Server_1.0
 * 同步阻塞-单线程模式:
 *  当有多个客户端连接通信时会相互阻塞
 *  bio accept(), read()方法都会阻塞线程-阻塞, 且需要引用程序主动调用read(), write()方法将数据传入内核-同步
 */
@Slf4j
public class Server_bio_1x {

    public static void main(String[] args) {
        byte[] buffer = new byte[1024];
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            log.info("服务器已启动并监听8080端口");
            while (!Thread.currentThread().isInterrupted()) {
                log.info("服务器正在等待连接...");
                Socket socket = serverSocket.accept();
                log.info("服务器已接收到连接请求...");
                log.info("服务器正在等待数据...");
                socket.getInputStream().read(buffer);
                log.info("服务器已经接收到数据");
                String content = new String(buffer);
                log.info("接收到的数据:" + content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
