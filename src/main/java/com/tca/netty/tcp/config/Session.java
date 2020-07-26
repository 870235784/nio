package com.tca.netty.tcp.config;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * @author zhouan
 * @Date 2020/7/16
 */
@Data
public class Session {

    private String channelId;

    private String sn;

    private Channel channel;

    private boolean isAuthenticated;

    private TcpServerHandler tcpServerHandler;

    private String charsetName;

    /**
     * 客户端上次的连接时间，该值改变的情况:
     * 1. terminal --> server 心跳包
     * 2. terminal --> server 数据包
     */
    private long lastCommunicateTimeStamp = 0L;

    /**
     * 获取channel
     * @param channel
     * @return
     */
    public static Session getSession(String sn, Channel channel, TcpServerHandler tcpServerHandler) {
        Session session = new Session();
        session.setAuthenticated(false);
        session.setChannel(channel);
        session.setChannelId(channel.id().asLongText());
        session.setSn(sn);
        session.setTcpServerHandler(tcpServerHandler);
        session.setLastCommunicateTimeStamp(System.currentTimeMillis());
        return session;
    }
}
