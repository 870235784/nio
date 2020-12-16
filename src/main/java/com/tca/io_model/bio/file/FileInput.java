package com.tca.io_model.bio.file;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhoua
 * @Date 2020/2/8
 * BIO 读文件
 */
@Slf4j
public class FileInput {

    public static void main(String[] args) {
        try {
            InputStream fileInputStream = new FileInputStream("C:\\Users\\DELL\\Desktop\\password.txt");
            int length;
            byte[] cache = new byte[1024];
            while ((length = fileInputStream.read(cache)) != -1) {
                log.info(new String(cache, 0, length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
