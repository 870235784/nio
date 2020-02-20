package com.tca.netty.tomcat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author zhoua
 * @Date 2020/2/20
 */
public class TCATomcat {

    public void start(int port) throws Exception {
        // Boss线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // Worker线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // Netty服务
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    // 主线程处理类
                    .channel(NioServerSocketChannel.class)
                    // 子线程处理类
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {
                            // 业务逻辑处理 --- 无锁化串行编程
                            // 编码器
                            client.pipeline().addLast(new HttpResponseEncoder());
                            // 解码器
                            client.pipeline().addLast(new HttpRequestDecoder());
                            // 业务逻辑处理器
                            client.pipeline().addLast(new TCATomcatHandler());
                        }

                    })
                    // 主线程配置
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 子线程配置
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = server.bind(port).sync();
            System.out.println("服务器启动, port : 8080");
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 启动类
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new TCATomcat().start(8080);
    }
}
