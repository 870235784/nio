package com.tca.nio.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zhoua
 * @Date 2020/2/9
 * NIO 读取文件
 * 在 NIO 系统中, 任何时候执行一个读操作, 都是从通道中读取, 但是不是直接从通道读取。
 * 因为所有数据最终都驻留在缓冲区中, 所以您是从通道读到缓冲区中。
 * 因此读取文件涉及三个步骤:
 *  (1)从 FileInputStream 获取 Channel
 *  (2)创建 Buffer
 *  (3)将数据从 Channel 读到 Buffer 中。
 */
public class FileInput {

    public static void main(String[] args) {
        try {
            // step1 创建文件流对象
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\DELL\\Desktop\\password.txt");
            // step2 获取通道
            FileChannel fileChannel = fileInputStream.getChannel();
            // step3 创建缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // step4 将数据从管道移动到缓冲区
            fileChannel.read(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
