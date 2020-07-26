package com.tca.nio.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zhoua
 * @Date 2020/2/9
 * NIO 复制文件
 */
public class FileCopy {

    public static void main(String[] args) {
        try {
            // step1 创建文件输入流和输出流对象
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\DELL\\Desktop\\test.txt");
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\DELL\\Desktop\\test_copy.txt");
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
            System.out.println("复制文件结束!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
