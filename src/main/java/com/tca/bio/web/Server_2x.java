package com.tca.bio.web;

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
 * 每创建一个新的连接, 将连接对象socket交给一个新的线程处理
 */
@Slf4j
public class Server_2x {

    public static void main(String[] args) {
        final byte[] buffer = new byte[1024];

        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            log.info("服务器已启动并监听8080端口");
            while (true) {
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
