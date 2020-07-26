package com.tca.netty.tcp.message.inbound;

import com.tca.netty.tcp.message.IMessage;
import lombok.Data;

/**
 * @author zhouan
 * @Date 2020/7/20
 */
@Data
public class RegistryReqMessage implements IMessage {

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * sn
     */
    private String sn;

    /**
     * 签名
     */
    private String sign;

    /**
     * 随机数(8位)
     */
    private String randomCode;

    /**
     * 字符编码
     */
    private String charsetName;


    @Override
    public String messageType() {
        return null;
    }

    @Override
    public String sn() {
        return sn;
    }
}
