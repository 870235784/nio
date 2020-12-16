package com.tca.java_nio.base;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhoua
 * @Date 2020/11/7
 *
 * java NIO中的Channel对应着文件描述符, 一个Channel对应着一个文件描述符 包括:
 *  FileChannel SocketChannel ServerSocketChannel DatagramChannel
 *
 */
@Slf4j
public class ChannelTest {


    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException, IOException {
        // FileChannel
//        fileChannelTest();

        // ServerSocketChannel
        new Thread(() -> {
            try {
                serverSocketChannelTest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        countDownLatch.await();

        // socketChannel
        new Thread(() -> {
            try {
                socketChannelTest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

//        fileCopyTest();
    }

    /**
     * FileChannel
     */
    private static void fileChannelTest() {
        try {
            // step1 创建文件输入流和输出流对象
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\DELL\\Desktop\\nacos问题.txt");
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\DELL\\Desktop\\nacos问题.txt_copy.txt");
            // step2 获取管道
            FileChannel inputStreamChannel = fileInputStream.getChannel();
            FileChannel outputStreamChannel = fileOutputStream.getChannel();
            // step3 创建缓冲流对象
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // step4 读取文件, 拷贝文件
            while (true) {
                // 清空 position=0 limit=capacity (--> 转成ByteBuffer写模式)
                byteBuffer.clear();
                // 输入流管道-> buffer
                int read = inputStreamChannel.read(byteBuffer);
                // 没有数据读入时, 停止循环
                if (read == -1) {
                    break;
                }
                // 调用flip limit=position position=0 (--> 转成ByteBuffer读模式)
                byteBuffer.flip();
                // buffer-> 输出流管道
                outputStreamChannel.write(byteBuffer);
            }
            // 结束
            log.info("复制文件结束!");
            outputStreamChannel.close();
            inputStreamChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件复制
     */
    private static void fileCopyTest() throws IOException{
        // step1 创建文件输入流和输出流对象
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\DELL\\Desktop\\nacos问题.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\DELL\\Desktop\\nacos问题.txt_copy.txt");
        // step2 获取管道
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        // step3 使用transferFrom实现文件复制
        outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size());
        // step4 关闭流
        inputStreamChannel.close();
        outputStreamChannel.close();
    }

    /**
     * ServerSocketChannel
     */
    private static void serverSocketChannelTest() throws IOException {
        // 1.初始化ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(9999));
        log.info("服务端启动成功, port = {}", 9999);
        // 2.初始化Selector, 并注册
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        countDownLatch.countDown();
        // 3.监听事件并处理
        // selector.select() > 0 ---> 有监听到事件
        while (selector.select() > 0) {
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 4.监听到连接事件
                if (selectionKey.isAcceptable()) {
                    log.info("获取连接");
                    // 5.获取server-client通道
                    ServerSocketChannel channel = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                } else if (selectionKey.isReadable()) {
                    // 6.监听到客户端数据
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    ByteBuffer byteBuffer = (ByteBuffer)selectionKey.attachment();
                    StringBuffer sb = new StringBuffer();
                    int length;
                    while ((length = socketChannel.read(byteBuffer)) > 0) {
                        // flip ---> 转到读模式
                        byteBuffer.flip();
                        sb.append(new String(byteBuffer.array(), 0, length));
                        // clear ---> 转到写模式
                        byteBuffer.clear();
                    }
                    log.info("收到客户端数据: {}", sb.toString());
                    socketChannel.close();
                }
                iterator.remove();
            }
        }
    }

    /**
     * socketChannel
     */
    private static void socketChannelTest() throws IOException {
        // 1.获取Socket通道
        SocketChannel clientSocketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        clientSocketChannel.configureBlocking(false);
        // 2.获取文件通道
        FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\DELL\\Desktop\\password.txt"),
                StandardOpenOption.READ);
        // 3.获取buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 4.读取本地文件, 发送给服务端
        while (fileChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            clientSocketChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        // 5.关闭流
        fileChannel.close();
        clientSocketChannel.close();
    }
}
