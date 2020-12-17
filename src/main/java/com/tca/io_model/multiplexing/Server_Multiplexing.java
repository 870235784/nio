package com.tca.io_model.multiplexing;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

/**
 * @author zhoua
 * @Date 2020/2/14
 * IO 多路复用 (严格上属于bio, 因为调用select()函数会阻塞)
 *  多路复用指的是 多个socket注册到同一个selector上被监听
 *  可以实现一个线程管理所有客户端连接
 *  IO 多路复用 底层可用 select poll epoll 三种方式实现
 *  IO 多路复用使用反应器模式：单线程Reactor(当前demo采用该模式) 多线程Reactor 主从Reactor
 *
 */
public class Server_Multiplexing {

    public static void main(String[] args) throws IOException {
        // 1.获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 2.切换成非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 3. 绑定连接
        serverSocketChannel.bind(new InetSocketAddress(9999));
        // 4. 获取选择器
        Selector selector = Selector.open();
        // 4.1将通道注册到选择器上，指定接收"监听通道"事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 5. 轮询获取选择器上已 "就绪" 的事件 ---> 只要select()>0, 说明已就绪
        while (selector.select() > 0) {
            // 6. 获取当前选择器所有注册的"选择键"(已就绪的监听事件)
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            // 7. 获取已"就绪"的事件, (不同的事件做不同的事)
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 接收事件就绪
                if (selectionKey.isAcceptable()) {
                    // 8. 获取客户端的链接
                    SocketChannel clientSocketChannel = serverSocketChannel.accept();
                    // 8.1 切换成非阻塞状态
                    clientSocketChannel.configureBlocking(false);
                    // 8.2 注册到选择器上-->拿到客户端的连接为了读取通道的数据(监听读就绪事件)
                    clientSocketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    // 9. 获取当前选择器读就绪状态的通道
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    // 9.1读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    // 9.2得到文件通道, 将客户端传递过来的图片写到本地项目下(写模式、没有则创建)
                    FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\zhouan\\Desktop\\hello.txt"),
                            StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                    while (client.read(buffer) > 0) {
                        // 在读之前都要切换成读模式
                        buffer.flip();
                        fileChannel.write(buffer);
                        buffer.clear();
                    }
                }
                // 10. 取消选择键(已经处理过的事件, 就应该取消掉了)
                iterator.remove();
            }
        }

    }
}
