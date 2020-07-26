package com.tca.netty.tcp.message.inbound;

import com.tca.netty.tcp.message.IMessage;
import lombok.Data;

/**
 * @author zhouan
 * @Date 2020/7/22
 */
@Data
public class CardRecordMessage implements IMessage {

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 设备sn
     */
    private String sn;

    /**
     * 识别时间
     */
    private String recordTime;

    /**
     * 车牌号
     */
    private String carNo;

    /**
     * 记录id
     */
    private String recordId;

    @Override
    public String messageType() {
        return messageType;
    }

    @Override
    public String sn() {
        return null;
    }
}
