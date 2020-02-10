package com.tca.bio.web;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author zhoua
 * @Date 2020/2/8
 * BIO client_2.0
 */
public class Client_2x {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1",8080);
            String message = null;
            Scanner sc = new Scanner(System.in);
            message = sc.next();
            socket.getOutputStream().write(message.getBytes());
            socket.close();
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
