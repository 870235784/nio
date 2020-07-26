package com.tca.netty.tcp.protocol;

import com.alibaba.fastjson.JSONObject;
import io.netty.util.CharsetUtil;
import lombok.Data;

import java.nio.charset.Charset;

/**
 * @author zhouan
 * @Date 2020/7/17
 */
@Data
public class Result {

    public Result() {}

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;

    private JSONObject data;

    public static Result success() {
        return new Result(200, "success");
    }


    public static void main(String[] args) {
        String str = "我是中国人";
        String gbk = new String(str.getBytes(CharsetUtil.UTF_8), Charset.forName("GBK"));
        System.out.println(gbk);

        String str1 = "鎴戞槸涓\uE15E浗浜�";
        new String(gbk.getBytes());
    }
}
