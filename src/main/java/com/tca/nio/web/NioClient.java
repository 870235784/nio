package com.tca.nio.web;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author zhouan
 * @Date 2020/7/25
 */
public class NioClient {

    public static void main(String[] args) throws IOException {
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
