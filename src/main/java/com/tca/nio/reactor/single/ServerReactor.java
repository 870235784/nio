package com.tca.nio.reactor.single;

import com.tca.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhoua
 * @Date 2020/7/26
 */
@Slf4j
public class ServerReactor implements Runnable {

    Selector selector;

    ServerSocketChannel serverSocketChannel;

    /**
     * 构造器
     * @throws Exception
     */
    public ServerReactor(int port) throws Exception {
        // 1.初始化ServerSocketChannel
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
        log.info("服务端启动成功, port = {}", port);
        // 2.初始化Selector, 并注册
        selector = Selector.open();
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 3.绑定新连接处理器到selectKey
        selectionKey.attach(new AcceptorHandler(serverSocketChannel, selector));
    }


    @Override
    public void run() {
        while (!Thread.interrupted()) {
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
        if (ValidateUtils.isNotEmpty(handler)) {
            handler.run();
        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(new ServerReactor(9999)).start();
    }
}
