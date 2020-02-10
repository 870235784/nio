package com.tca.bio.web;

import java.io.IOException;
import java.net.Socket;

/**
 * @author zhoua
 * @Date 2020/2/8
 * BIO client_1.0
 */
public class Client_1x {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8080);
            socket.getOutputStream().write("向服务器发数据".getBytes());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
