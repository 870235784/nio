package com.tca.netty.encode_decode.client;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author zhouan
 * @Date 2020/7/17
 */
@Slf4j
public class NettyClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "9999"));

    private static final String charset = "UTF-8";

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });

            ChannelFuture channelFuture = b.connect(HOST, PORT).sync();



//            while (true) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("messageType", "heartbeat");
                jsonObject.put("sn", "bbb");
                log.info("发送心跳消息: {}", jsonObject.toJSONString());
            String jsonString = jsonObject.toJSONString();
            ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(4 + jsonString.length());
            buffer.writeBytes(int2Bytes(jsonString.length()));
            log.info("可读长度: {}", buffer.readableBytes());
            buffer.writeBytes(jsonString.getBytes());
            log.info("可读长度: {}", buffer.readableBytes());
            ChannelFuture channelFuture1 = channelFuture.channel().writeAndFlush(buffer);
            channelFuture1.addListener((ChannelFutureListener) future -> {
                // 6. 关闭连接
                if (future.isSuccess()) {
                    log.info("发送成功!");
                } else {
                    log.info("发送失败！");
                }
            });
            buffer.retain();
            buffer.release();
//            }


            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static byte[] int2Bytes(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        log.info("byte = {}", Arrays.toString(b));
        return b;

    }

}
