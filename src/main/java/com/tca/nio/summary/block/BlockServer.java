package com.tca.nio.summary.block;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author zhoua
 * @Date 2020/2/14
 */
public class BlockServer {

    public static void main(String[] args) throws IOException {
        // 1.获取ServerSocket通道, 绑定ip, 端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        // 2.获取文件通道
        FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\DELL\\Desktop\\password_copy.txt"),
                StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        // 3.获取客户端连接
        SocketChannel clinetSocketChannel = serverSocketChannel.accept();
        // 4.获取Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 5.将客户端传过来的数据保存
        while (clinetSocketChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        // 6.关闭通道
        fileChannel.close();
        clinetSocketChannel.close();
        serverSocketChannel.close();
    }
}
