package com.tca.netty.tcp.enums;

import lombok.Getter;

/**
 * @author zhouan
 * @Date 2020/7/17
 */
@Getter
public enum EquipmentOnlineEnum {

    /**
     * 上线
     */
    ONLINE(1, "上线"),

    /**
     * 下线
     */
    OFFLINE(0, "下线"),

    ;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 消息名称
     */
    private String remark;

    /**
     * 构造器
     * @param type
     * @param remark
     */
    EquipmentOnlineEnum(Integer type, String remark) {
        this.type = type;
        this.remark = remark;
    }
}
