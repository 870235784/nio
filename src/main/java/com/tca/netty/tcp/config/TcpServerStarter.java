package com.tca.netty.tcp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author zhouan
 * @Date 2020/7/16
 */
@Component
public class TcpServerStarter implements CommandLineRunner {

    @Autowired
    private TcpServer tcpServer;

    @Override
    public void run(String... args) throws Exception {
        tcpServer.startServer();
    }
}
