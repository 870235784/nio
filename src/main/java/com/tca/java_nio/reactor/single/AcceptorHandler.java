package com.tca.java_nio.reactor.single;


import com.tca.common.utils.ValidateUtils;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author zhoua
 * @Date 2020/7/26
 */
public class AcceptorHandler implements Runnable {

    ServerSocketChannel serverSocketChannel;

    Selector selector;

    public AcceptorHandler(ServerSocketChannel serverSocketChannel, Selector selector) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (ValidateUtils.isNotEmpty(socketChannel)) {
                new BusinessHandler(selector, socketChannel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
