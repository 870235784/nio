package com.tca.netty.tcp.message.outbound;

import com.tca.netty.tcp.enums.MessageTypeEnum;
import com.tca.netty.tcp.message.IMessage;
import lombok.Data;

/**
 * @author zhouan
 * @Date 2020/7/20
 */
@Data
public class BroadcastMessage implements IMessage {

    /**
     * 消息类型
     */
    private String messageType = MessageTypeEnum.BROADCAST.getType();

    /**
     * 播报内容
     */
    private String text;

    @Override
    public String messageType() {
        return messageType;
    }

    @Override
    public String sn() {
        return null;
    }
}
