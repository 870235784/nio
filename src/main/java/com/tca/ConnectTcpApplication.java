package com.tca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动加载类
 * @author Lee
 * @creed: Eating our own dog food
 * @date 2019-9-2 11:06
 */
@ComponentScan("com.tca")
@SpringBootApplication
public class ConnectTcpApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConnectTcpApplication.class, args);
    }
}
