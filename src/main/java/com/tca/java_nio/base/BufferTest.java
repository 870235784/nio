package com.tca.java_nio.base;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * @author zhoua
 * @Date 2020/11/7
 *
 * Buffer类是抽象类
 * 其实现有: ByteBuffer CharBuffer DoubleBuffer FloatBuffer IntBuffer LongBuffer ShortBuffer MappedByteBuffer
 *
 * Buffer的重要属性
 * capacity 容量, 即可以容纳的最大数据量; 在Buffer创建时设置且不能修改
 * limit 上限, 缓冲区中当前的数据量
 * position 位置, 缓冲区中下一个要被读或写的元素的索引
 * mark 调用mark()设置 mark=position 调用reset()设置position=mark
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
        // 表示capacity是1024个Byte; 如果使用LongBuffer.allocate(1024),表示capacity是1024个Long
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        log.info("byteBuffer init...");
        // 在写模式下, limit == capacity, 即limit==最多可以写到的位置
        log.info("capacity = {}, limit = {}, position = {}", byteBuffer.capacity(), byteBuffer.limit(), byteBuffer.position());
        byteBuffer.put("hello world".getBytes());
        log.info("put hello world.getBytes()");
        log.info("capacity = {}, limit = {}, position = {}", byteBuffer.capacity(), byteBuffer.limit(), byteBuffer.position());
        byteBuffer.flip();
        log.info("flip()");
        // 切换成读模式时, limit == 写模式下的position值, 即limit==最多可读取的position
        log.info("capacity = {}, limit = {}, position = {}", byteBuffer.capacity(), byteBuffer.limit(), byteBuffer.position());
        log.info("result = {}", new String(byteBuffer.array()));
        byteBuffer.clear();
        log.info("clear()");
        log.info("capacity = {}, limit = {}, position = {}", byteBuffer.capacity(), byteBuffer.limit(), byteBuffer.position());
    }
}
