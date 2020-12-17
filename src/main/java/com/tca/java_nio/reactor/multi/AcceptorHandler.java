package com.tca.java_nio.reactor.multi;

import com.tca.java_nio.reactor.single.BusinessHandler;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author zhoua
 * @Date 2020/12/17
 */
public class AcceptorHandler implements Runnable {

    private ServerSocketChannel serverSocketChannel;

    private ServerReactorConfig config;

    public AcceptorHandler(ServerSocketChannel serverSocketChannel, ServerReactorConfig serverReactorConfig) {
        this.serverSocketChannel = serverSocketChannel;
        this.config = serverReactorConfig;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                int next = config.getAtomicInteger().getAndIncrement() % config.getCore();
                new BusinessHandler(config.getSelectors().get(next), socketChannel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
