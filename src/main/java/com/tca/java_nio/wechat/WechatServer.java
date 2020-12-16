package com.tca.java_nio.wechat;

import com.tca.common.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhoua
 * @Date 2020/11/14
 */
@Slf4j
public class WechatServer {

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private Integer port;

    public WechatServer(Integer port) throws IOException {
        if (ValidateUtils.isEmpty(port)) {
            throw new IllegalArgumentException("端口不能为空");
        }
        this.port = port;
        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws IOException{
        log.info("wechat服务启动, port : {}", port);
        while (true) {
            if (selector.select() == 0) {
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    accept(selectionKey);
                }
                if (selectionKey.isReadable()) {
                    read(selectionKey);
                }
                iterator.remove();
            }
        }
    }

    /**
     * 处理服务器读事件
     * @param selectionKey
     */
    private void read(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        StringBuffer sb = new StringBuffer();
        String message = "";
        try {
            while (socketChannel.read(byteBuffer) > 0) {
                sb.append(new String(byteBuffer.array()));
            }
            message = socketChannel.getRemoteAddress().toString().substring(1) + ": " + sb.toString();
        } catch(IOException e) {
            log.error("发生错误", e);
            try {
                socketChannel.close();
            } catch (IOException e1) {
                log.error("发生错误", e);
            }
        }
        if (ValidateUtils.isEmpty(message)) {
            return;
        }
        log.info(message);
        // 发给别的连接
        Set<SelectionKey> allSelectionKey = selector.keys();
        Set<Channel> channels = allSelectionKey.stream().map(SelectionKey::channel).filter(channel -> channel != socketChannel
                && channel != serverSocketChannel).collect(Collectors.toSet());
        sendMessageToAll(message, channels);
    }

    /**
     * 处理连接事件
     * @param selectionKey
     */
    private void accept(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        String message = "新的客户端上线 ,address = " + socketChannel.getRemoteAddress().toString().substring(1);
        log.info(message);
        // 发给别的连接
        Set<SelectionKey> allSelectionKey = selector.keys();
        Set<Channel> channels = allSelectionKey.stream().map(SelectionKey::channel).filter(channel -> channel != socketChannel
                && channel != serverSocketChannel).collect(Collectors.toSet());
        sendMessageToAll(message, channels);
    }

    /**
     * 发送消息给其他连接
     * @param message
     * @param channels
     */
    private void sendMessageToAll(String message, Set<Channel> channels) {
        if (ValidateUtils.isEmpty(channels) || ValidateUtils.isEmpty(message)) {
            return;
        }
        channels.forEach(channel -> {
            // 重要:这里必须使用trim(), 防止发送多余空格消息
            ByteBuffer byteBuffer = ByteBuffer.wrap(message.trim().getBytes());
            try {
                ((SocketChannel)channel).write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws IOException{
        new WechatServer(8080).start();
    }
}
