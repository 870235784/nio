package com.tca.io_model.nio;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoua
 * @Date 2020/12/16
 * java_nio server (non-blocking 非阻塞io)
 * 同步非阻塞:
 *  调用serverSocketChannel的 configureBlocking(false),切换成非阻塞模式 - 之后调用accept()方法不会阻塞
 *  调用socketChannel的 configureBlocking(false),切换成非阻塞模式 - 之后调用read()方法不会阻塞
 *  但是需要每次遍历所有的socket, 判断是否有accept() 或者 read() 事件发生
 *  可以实现一个线程管理所有客户端连接
 */
@Slf4j
public class Server_nio {

    private static List<SocketChannel> socketChannelList = Lists.newArrayList();

    private static ServerSocketChannel serverSocketChannel;

    public static void main(String[] args) throws IOException, InterruptedException {
        // 1.获取通道
        serverSocketChannel = ServerSocketChannel.open();
        // 2.切换成非阻塞模式 - 调用accept()方法不会阻塞
        serverSocketChannel.configureBlocking(false);
        // 3. 绑定连接
        serverSocketChannel.bind(new InetSocketAddress(9999));

        while (!Thread.currentThread().isInterrupted()) {
            TimeUnit.SECONDS.sleep(1L);
            SocketChannel client = serverSocketChannel.accept();
            // accept()方法不会阻塞, 当获取到新的socket连接时, 添加到socketChannel列表中
            if (client != null) {
                // 客户端也切换成非阻塞模式 -- 调用read()方法不会阻塞
                String clientAddr = client.getRemoteAddress().toString();
                log.info("clientAddr = {}, 接入服务器", clientAddr);
                client.configureBlocking(false);
                socketChannelList.add(client);
            }

            ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
            socketChannelList.forEach(socketChannel -> {
                try {
                    // 前面设置了非阻塞, 所以调用read()方法不会阻塞
                    int count = socketChannel.read(buffer);
                    if (count > 0) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.limit()];
                        buffer.get(bytes);

                        String message = new String(bytes);
                        log.info(message);
                        buffer.clear();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }



    }
}
