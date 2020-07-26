package com.tca.netty.tcp.message;

/**
 * @author zhouan
 * @Date 2020/7/20
 */
public interface IMessage {

    /**
     * 获取消息类型
     * @return
     */
    String messageType();

    /**
     * 获取sn码
     * @return
     */
    String sn();
}
