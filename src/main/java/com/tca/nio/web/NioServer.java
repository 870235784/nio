package com.tca.nio.web;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhouan
 * @Date 2020/7/25
 */
@Slf4j
public class NioServer {

    private static volatile boolean isRunning;

    public static void start(int port) throws IOException {
        if (isRunning) {
            log.error("当前服务端已启动...");
        }
        // 1.初始化ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        isRunning = true;
        serverSocketChannel.bind(new InetSocketAddress(port));
        log.info("服务端启动成功, port = {}", port);
        // 2.初始化Selector, 并注册
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 3.监听事件并处理
        // selector.select() > 0 ---> 有监听到事件
        while (selector.select() > 0) {
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 4.监听到连接事件
                if (selectionKey.isAcceptable()) {
                    // 5.获取server-client通道
                    ServerSocketChannel channel = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    // 6.监听到客户端数据
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    StringBuffer sb = new StringBuffer();
                    int length;
                    while ((length = socketChannel.read(byteBuffer)) > 0) {
                        // flip ---> 转到读模式
                        byteBuffer.flip();
                        sb.append(new String(byteBuffer.array(), 0, length));
                        // clear ---> 转到写模式
                        byteBuffer.clear();
                    }
                    log.info("收到客户端数据: {}", sb.toString());
                    socketChannel.close();
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        NioServer.start(9999);
    }
}
