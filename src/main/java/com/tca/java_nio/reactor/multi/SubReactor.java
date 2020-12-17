package com.tca.java_nio.reactor.multi;

import lombok.Data;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhoua
 * @Date 2020/12/18
 * 子反应器
 *  1.一个子反应器对应一个selector
 *  2.用一个线程监听一个selector
 */
@Data
public class SubReactor implements Runnable{

    private Selector selector;

    public SubReactor(Selector selector) {
        this.selector = selector;
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
}
