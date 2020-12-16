package com.tca.io_model.bio.web;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhoua
 * @Date 2020/2/8
 * BIO server_2.0
 * 同步阻塞-多线程模式:
 * 每个客户端连接使用一个单独的线程处理, 如果客户端连接较多, 线程数也会很多, 由于不会一直通信, 导致线程资源浪费
 *  bio accept(), read()方法都会阻塞线程-阻塞, 且需要引用程序主动调用read(), write()方法将数据传入内核-同步
 */
@Slf4j
public class Server_bio_2x {

    public static void main(String[] args) {
        final byte[] buffer = new byte[1024];

        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            log.info("服务器已启动并监听8080端口");
            while (!Thread.currentThread().isInterrupted()) {
                log.info("服务器正在等待连接...");
                final Socket socket = serverSocket.accept();
                executorService.execute(() -> {
                    log.info("服务器已与客户端建立连接...");
                    log.info("服务器正在等待数据...");
                    while (true) {
                        try {
                            int read = socket.getInputStream().read(buffer);
                            if (read < 0) {
                                socket.close();
                                log.info("关闭连接");
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                socket.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            log.info("关闭连接");
                        }
                        String content = new String(buffer);
                        log.info("接收到的数据: {}", content);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
