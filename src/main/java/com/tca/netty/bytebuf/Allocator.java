package com.tca.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

/**
 * @author zhoua
 * @Date 2020/7/30
 */
public class Allocator {

    public static void main(String[] args) {
        ByteBuf byteBuf = null;
        // 方式一
        byteBuf = ByteBufAllocator.DEFAULT.buffer(9, 100);
        // 方式二
        byteBuf = ByteBufAllocator.DEFAULT.buffer();
        // 方式三 (非池化堆内存)
        byteBuf = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
        // 方式四 (池化直接内存)
        byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer();
    }
}
