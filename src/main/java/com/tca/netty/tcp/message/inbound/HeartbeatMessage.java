package com.tca.netty.tcp.message.inbound;

import com.tca.netty.tcp.message.IMessage;
import lombok.Data;

/**
 * @author zhouan
 * @Date 2020/7/20
 */
@Data
public class HeartbeatMessage implements IMessage {

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 设备sn
     */
    private String sn;

    @Override
    public String messageType() {
        return messageType;
    }

    @Override
    public String sn() {
        return sn;
    }
}
