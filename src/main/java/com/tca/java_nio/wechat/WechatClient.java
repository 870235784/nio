package com.tca.java_nio.wechat;

import com.tca.common.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author zhoua
 * @Date 2020/11/14
 */
@Slf4j
public class WechatClient {

    private static final String HOST = "127.0.0.1";

    private int port;

    private SocketChannel socketChannel;

    private Selector selector;

    public WechatClient(Integer port) throws IOException {
        this.port = port;
        this.selector = Selector.open();
        this.socketChannel = SocketChannel.open(new InetSocketAddress(HOST, port));
        log.info("连接服务端成功");
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        new Thread(() -> write()).start();
    }

    private void connect() throws IOException {
        while (true) {
            if (selector.select() == 0) {
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    read(selectionKey);
                }
                iterator.remove();
            }
        }
    }

    /**
     * 从服务端读数据
     * @param selectionKey
     */
    private void read(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        StringBuffer sb = new StringBuffer();
        String message = "";
        try {
            while (socketChannel.read(byteBuffer) > 0) {
                sb.append(new String(byteBuffer.array()));
            }
            message = sb.toString();
        } catch(IOException e) {
            log.error("发生错误", e);
            try {
                socketChannel.close();
            } catch (IOException e1) {
                log.error("发生错误", e);
            }
        }
        if (ValidateUtils.isEmpty(message) ) {
            return;
        }
        log.info(message);
    }

    private void write() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextLine()) {
                String message = scanner.nextLine().trim();
                try {
                    socketChannel.write(ByteBuffer.wrap(message.getBytes()));
                } catch (IOException e) {
                    log.error("发送消息错误", e);
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new WechatClient(9999).connect();
    }
}
