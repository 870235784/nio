package com.tca.nio.web;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhoua
 * @Date 2020/2/10
 */
public class Server {

    /**
     * port
     */
    private int port = 8080;

    /**
     * socketAddress
     */
    private InetSocketAddress inetSocketAddress;

    /**
     * selector
     */
    private Selector selector;

    /**
     * 构造器
     */
    public Server() {
        try {
            this.inetSocketAddress = new InetSocketAddress(port);
            // 1.打开通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 2.绑定ip:port
            serverSocketChannel.bind(inetSocketAddress);
            // 3.Channel默认为阻塞, 可以设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 4.打开选择器
            selector = Selector.open();
            // 5.注册选择器, 通道
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务端准备就绪, 监听端口: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听客户端连接并处理
     */
    public void listen() {
        try {
            while (true) {
                // 1.查看等待的客户端数
                int wait = selector.select();
                // 2.没有客户端
                if (wait == 0) {
                    continue;
                }
                // 3.获取等待的客户端集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey selectionKey = keyIterator.next();
                    // 4.客户端处理
                    process(selectionKey);
                    keyIterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理客户端请求
     * @param selectionKey
     */
    private void process(SelectionKey selectionKey) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        if (selectionKey.isAcceptable()) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            int length = socketChannel.read(byteBuffer);
            if (length > 0) {
                byteBuffer.flip();
                String content = new String(byteBuffer.array(), 0, length);
                System.out.println("读取数据: " + content);
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            }
            byteBuffer.clear();
        } else if (selectionKey.isWritable()) {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            byteBuffer = byteBuffer.wrap("Hello World".getBytes());
        }
    }

    public static void main(String[] args) {
        new Server().listen();
    }

}
