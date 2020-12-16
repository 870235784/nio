package com.tca.java_nio.reactor.single;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author zhoua
 * @Date 2020/7/26
 */
@Slf4j
public class BusinessHandler implements Runnable {

    static final int RECEIVING = 0;
    static final int SENDING = 1;

    SocketChannel socketChannel;

    SelectionKey selectionKey;

    Selector selector;

    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    int state = RECEIVING;

    public BusinessHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.selector = selector;
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            if (state == SENDING) {
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
                selectionKey.interestOps(SelectionKey.OP_READ);
                state = RECEIVING;
            } else if (state == RECEIVING) {
                int length;
                while ((length = socketChannel.read(byteBuffer)) > 0) {
                    log.info(new String(byteBuffer.array(), 0, length));
                }
                byteBuffer.flip();
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                state = SENDING;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
