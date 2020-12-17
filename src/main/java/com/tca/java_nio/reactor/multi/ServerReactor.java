package com.tca.java_nio.reactor.multi;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhoua
 * @Date 2020/12/17
 * 多线程reactor模型
 *  1.引入多个selector选择器
 *  2.引入多个子反应器(SubReactor)
 *
 */
@Slf4j
public class ServerReactor implements Runnable{

    private ServerSocketChannel serverSocketChannel;

    /**
     * serverSocketChannel对应的selector
     */
    private Selector selector;



    /**
     * 构造器
     */
    public ServerReactor(ServerReactorConfig config, int port) throws IOException{
        // 初始化所有selector
        selector = Selector.open();
        for (int i = 0; i < config.getCore(); i++) {
            Selector subSelector = Selector.open();
            config.getSelectors().add(subSelector);
            SubReactor subReactor = new SubReactor(subSelector);
            config.getSubReactors().add(subReactor);
            new Thread(subReactor).start();
        }
        // 初始化ServerSocketChannel
        serverSocketChannel = ServerSocketChannel.open();
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);
        // 监听端口
        serverSocketChannel.bind(new InetSocketAddress(port));
        log.info("服务端启动成功, port = {}", port);

        // serverSocketChannel选择器selector,负责监听所有连接事件
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new AcceptorHandler(serverSocketChannel, config));
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                dispatch(selectionKey);
            }
            selectionKeySet.clear();
        }
    }

    private void dispatch(SelectionKey selectionKey) {
        Runnable handler = (Runnable)selectionKey.attachment();
        if (handler != null) {
            handler.run();
        }
    }

    public static void main(String[] args) throws IOException {
        new ServerReactor(new ServerReactorConfig(), 9999).run();
    }
}
