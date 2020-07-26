package com.tca.netty.tcp.message.outbound;

import com.tca.netty.tcp.enums.MessageTypeEnum;
import com.tca.netty.tcp.message.IMessage;
import lombok.Data;

/**
 * @author zhouan
 * @Date 2020/7/21
 */
@Data
public class HeartbeatRespMessage implements IMessage {

    private String messageType = MessageTypeEnum.HEARTBEAT_RESP.getType();

    private Integer code = 200;

    private String msg = "操作成功！";

    private String serverTime;

    @Override
    public String messageType() {
        return messageType;
    }

    @Override
    public String sn() {
        return null;
    }
}
