package com.tca.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

/**
 * @author zhoua
 * @Date 2021/2/26
 *
 * ByteBuf内部是一个字节数组, 逻辑上分为四部分
 *  |--------|-------|--------|--------|
 *  |  废弃  | 可读  |  可写  | 可扩容  |
 *
 *  重要属性:
 *      readerIndex 读指针 指示读取的起始位置, 每读取一个字节, readerIndex加1, 当readerIndex==writerIndex时, 表示不可读了
 *      writerIndex 写指针 指示写的起始位置, 每写一个字节, writerIndex加1, 当writerIndex==capacity时, 表示不可写了
 *      maxCapacity 最大容量
 */
public class ByteBufTest {

    /**
     * 初始化 (四种方式)
     * 分为 堆缓存 直接缓存 池化 非池化
     */
    private static ByteBuf allocate() {
        ByteBuf byteBuf = null;
        // 方式一
        byteBuf = ByteBufAllocator.DEFAULT.buffer(9, 100);
        // 方式二
        byteBuf = ByteBufAllocator.DEFAULT.buffer();
        // 方式三 (非池化堆内存)
        byteBuf = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
        // 方式四 (池化直接内存)
        byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer();
        return byteBuf;
    }

    /**
     * ByteBuf创建时 引用数  = 1 retain()引用数 +1 release()引用数 -1
     * 引用数 = 0时会被GC回收 不能再retain
     */
    private static void referenceCount() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.retain();

        byteBuf.release();
        byteBuf.release();
    }

    public static void main(String[] args) {
        allocate();
    }
}
