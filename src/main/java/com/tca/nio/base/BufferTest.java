package com.tca.nio.base;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * @author zhoua
 * @Date 2020/11/7
 *
 * 使用Buffer类的基本步骤
 * 1.使用创建子类实例对象的allocate()方法, 创建一个Buffer类的实例对象
 * 2.调用put方法, 将数据写入到缓冲区
 * 3.写入完成后, 在开始读取数据前, 调用Buffer.flip()方法, 将缓冲区转成可读模式
 * 4.调用get方法读数据
 * 5.读取完成后, 调用clear()或compact()方法, 将缓冲区转换成写入模式
 */
@Slf4j
public class BufferTest {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        log.info("byteBuffer init...");
        log.info("capacity = {}, limit = {}, position = {}", byteBuffer.capacity(), byteBuffer.limit(), byteBuffer.position());
        byteBuffer.put("hello world".getBytes());
        log.info("put hello world.getBytes()");
        log.info("capacity = {}, limit = {}, position = {}", byteBuffer.capacity(), byteBuffer.limit(), byteBuffer.position());
        byteBuffer.flip();
        log.info("flip()");
        log.info("capacity = {}, limit = {}, position = {}", byteBuffer.capacity(), byteBuffer.limit(), byteBuffer.position());
        log.info("result = {}", new String(byteBuffer.array()));
        byteBuffer.clear();
        log.info("clear()");
        log.info("capacity = {}, limit = {}, position = {}", byteBuffer.capacity(), byteBuffer.limit(), byteBuffer.position());
    }
}
