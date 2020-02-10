package com.tca.nio.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zhoua
 * @Date 2020/2/9
 * NIO 输出文件(操作步骤与文件输入类似)
 */
public class FileOutput {

    public static void main(String[] args) {
        try {
            // step1 创建文件流对象
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\DELL\\Desktop\\test.txt");
            // step2 获取通道
            FileChannel fileChannel = fileOutputStream.getChannel();
            // step3 创建缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // step4 数据移到缓冲区
            byte[] message = "hello world".getBytes();
            for (int i = 0; i < message.length; i++) {
                byteBuffer.put(message[i]);
            }
            byteBuffer.flip();
            // step5 输出
            fileChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
