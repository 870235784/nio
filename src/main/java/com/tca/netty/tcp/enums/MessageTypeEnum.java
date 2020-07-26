package com.tca.netty.tcp.enums;

import lombok.Getter;

/**
 * @author zhouan
 * @Date 2020/7/16
 * 设备上行消息类型
 */
@Getter
public enum MessageTypeEnum {

    /**
     * 默认消息
     */
    DEFAULT("default", "默认消息"),

    /**
     * 设备注册
     */
    REGISTER_REQ("registerReq", "设备注册"),

    /**
     * 设备注册响应
     */
    REGISTER_RESP("registerResp", "设备注册响应"),

    /**
     * 心跳
     */
    HEARTBEAT("heartbeat", "心跳"),

    /**
     * 心跳响应
     */
    HEARTBEAT_RESP("heartbeatResp", "心跳响应"),

    /**
     * 车牌识别记录
     */
    CARD_RECORD("cardRecord", "车牌识别记录"),

    /**
     * 语音播报
     */
    BROADCAST("broadcast", "语音播报")

    ;
    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息名称
     */
    private String remark;

    /**
     * 构造器
     * @param type
     * @param remark
     */
    MessageTypeEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }
}
