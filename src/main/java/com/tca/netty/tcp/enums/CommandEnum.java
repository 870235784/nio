package com.tca.netty.tcp.enums;

import lombok.Getter;

/**
 * @author zhouan
 * @Date 2020/7/17
 */
@Getter
public enum CommandEnum {

    /**
     * 设备上下线指令
     */
    EQUIPMENT_ONLINE("equipmentOnline", "设备上下线"),

    /**
     * 设备注册指令
     */
    EQUIPMENT_REGISTRY_REQ("equipmentRegistryReq", "设备注册"),

    /**
     * 设备注册响应
     */
    EQUIPMENT_REGISTRY_RESP("equipmentRegistryResp", "设备注册响应"),


    /**
     * 车牌记录
     */
    CARD_RECORD("cardRecord", "车牌记录"),


    /**
     * 语音播报
     */
    BROADCAST("broadcast", "语音播报"),



    ;

    /**
     * 指令类型
     */
    private String type;

    /**
     * 指令名称
     */
    private String remark;

    /**
     * 构造器
     * @param type
     * @param remark
     */
    CommandEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }
}
