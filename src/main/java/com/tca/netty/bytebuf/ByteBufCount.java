package com.tca.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author zhoua
 * @Date 2020/7/30
 * ByteBuf创建时 引用数  = 1 retain()引用数 +1 release()引用数 -1
 * 引用数 = 0时会被GC回收 不能再retain
 */
public class ByteBufCount {

    public static void main(String[] args) {

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.retain();

        byteBuf.release();
        byteBuf.release();

//        byteBuf.retain();

    }
}
