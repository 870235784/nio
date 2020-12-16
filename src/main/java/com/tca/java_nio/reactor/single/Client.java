package com.tca.java_nio.reactor.single;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author zhoua
 * @Date 2020/7/26
 */
public class Client {

    public static void main(String[] args) throws IOException {
        // 1.获取Socket通道
        SocketChannel clientSocketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        clientSocketChannel.configureBlocking(false);
        // 2.获取buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello serverReactor".getBytes());
        byteBuffer.flip();
        // 3.发送给服务端
        clientSocketChannel.write(byteBuffer);
        byteBuffer.clear();
        // 5.关闭流
        clientSocketChannel.close();
    }
}
