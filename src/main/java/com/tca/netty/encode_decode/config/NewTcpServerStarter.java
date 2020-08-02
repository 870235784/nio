package com.tca.netty.encode_decode.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author zhouan
 * @Date 2020/7/16
 */
@Component
public class NewTcpServerStarter implements CommandLineRunner {

    @Autowired
    private NewTcpServer newTcpServer;

    @Override
    public void run(String... args) throws Exception {
        newTcpServer.startServer();
    }
}
