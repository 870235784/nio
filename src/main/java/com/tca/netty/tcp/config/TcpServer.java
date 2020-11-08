package com.tca.netty.tcp.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouan
 */
@Slf4j
@Component
public class TcpServer {


    @Autowired
    private TcpServerHandler tcpServerHandler;

    /**
     * 状态
     */
    private volatile boolean isRunning;

    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 服务端NIO线程组
     */
    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

    /**
     * 端口
     */
    @Value("${tcp.server.port:9000}")
    private int port;

    @Value("${tcp.server.idleTime:60}")
    private int idleTime;

    public TcpServer() {
    }

    public TcpServer(int port) {
        this();
        this.port = port;
    }

    private void bind() throws Exception {
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                // 绑定线程组
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel client) throws Exception {
                        // 超时设置
                        client.pipeline().addLast(new IdleStateHandler(idleTime, 0, 0, TimeUnit.SECONDS));
                        // 业务逻辑处理
                        client.pipeline().addLast(tcpServerHandler);
                    }

                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        log.info("TCP服务启动完毕, port = {}", this.port);
        channelFuture.channel().closeFuture().sync();
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }

    /**
     * 服务启动
     */
    public synchronized void startServer() {
        if (this.isRunning) {
            throw new IllegalStateException(this.getName() + " is already started .");
        }
        this.isRunning = true;

        new Thread(() -> {
            try {
                this.bind();
            } catch (Exception e) {
                log.info("TCP服务启动出错:{}", e.getMessage());
                e.printStackTrace();
            }
        }, this.getName()).start();
    }

    /**
     * 关闭服务
     */
    public synchronized void stopServer() {
        if (!this.isRunning) {
            throw new IllegalStateException(this.getName() + " is not yet started .");
        }
        this.isRunning = false;

        try {
            Future<?> future = this.workerGroup.shutdownGracefully().await();
            if (!future.isSuccess()) {
                log.error("workerGroup 无法正常停止:{}", future.cause());
            }

            future = this.bossGroup.shutdownGracefully().await();
            if (!future.isSuccess()) {
                log.error("bossGroup 无法正常停止:{}", future.cause());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("TCP服务已经停止...");
    }

    private String getName() {
        return "TCP-Server";
    }

}