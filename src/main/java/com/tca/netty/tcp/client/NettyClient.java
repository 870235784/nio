package com.tca.netty.tcp.client;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouan
 * @Date 2020/7/17
 */
@Slf4j
public class NettyClient {

//    static final String HOST = System.getProperty("host", "changan.huaepay.com");
//    static final String HOST = System.getProperty("host", "202.105.146.90");
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "9000"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    private String content;

    private static final String charset = "UTF-8";
//    private static final String charset = "GBK";

    public NettyClient(String content) {
        this.content = content;
    }

    public static void main(String[] args) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messageType", "registerReq");
//        jsonObject.put("messageType", "heartbeat");
//        jsonObject.put("sn", "30-dc-8a-69-8d-69");
        jsonObject.put("sn", "bbb");
        jsonObject.put("randomCode", "00000000");
//        jsonObject.put("sign", "ed435524d44ac55c3d57987cee453dc4");
        jsonObject.put("sign", "878cc1e0bbf6947800d20acd782bb633");
        jsonObject.put("charsetName", charset);
        sendMessage(jsonObject.toJSONString());
    }

    public static void sendMessage(String content) throws InterruptedException {
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
                            pipeline.addLast(new StringEncoder(Charset.forName(charset)));
                            pipeline.addLast(new StringDecoder(Charset.forName(charset)));

                            pipeline.addLast(new NettyClientHandler());
                        }
                    });

            ChannelFuture channelFuture = b.connect(HOST, PORT).sync();
            log.info("给服务端发送消息: {}", content);
            channelFuture.channel().writeAndFlush(content);
            channelFuture.addListener((ChannelFutureListener) future -> {
                // 6. 关闭连接
                if (future.isSuccess()) {
                    log.info("发送成功!");
                } else {
                    log.info("发送失败！");
                }
            });


            for (int i = 0; i < 10; i++) {
                TimeUnit.SECONDS.sleep(30);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("messageType", "heartbeat");
                jsonObject.put("sn", "bbb");
                log.info("发送心跳消息: {}", jsonObject.toJSONString());
                channelFuture.channel().writeAndFlush(jsonObject.toJSONString());
                channelFuture.addListener((ChannelFutureListener) future -> {
                    // 6. 关闭连接
                    if (future.isSuccess()) {
                        log.info("发送成功!");
                    } else {
                        log.info("发送失败！");
                    }
                });
            }
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
