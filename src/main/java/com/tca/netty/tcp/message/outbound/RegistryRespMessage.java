package com.tca.netty.tcp.message.outbound;

import com.tca.netty.tcp.enums.MessageTypeEnum;
import com.tca.netty.tcp.message.IMessage;
import lombok.Data;

/**
 * @author zhouan
 * @Date 2020/7/20
 */
@Data
public class RegistryRespMessage implements IMessage {

    private String messageType = MessageTypeEnum.REGISTER_RESP.getType();

    private String sn;

    private Integer code;

    private String msg;

    private String serverTime;

    @Override
    public String messageType() {
        return messageType;
    }

    @Override
    public String sn() {
        return sn;
    }
}
