package com.tca.nio.summary.block;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author zhoua
 * @Date 2020/2/14
 */
public class BlockClient {

    public static void main(String[] args) throws Exception{
        // 1.获取Socket通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
        // 2.获取文件通道
        FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\DELL\\Desktop\\password.txt"),
                StandardOpenOption.READ);
        // 3.获取buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 4.读取本地文件, 发送给服务端
        while (fileChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        // 5.关闭流
        fileChannel.close();
        socketChannel.close();
    }
}
