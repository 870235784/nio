package com.tca.netty.encode_decode.config;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * @author zhoua
 * @Date 2020/8/2
 */
@Data
public class NewSession {

    private String channelId;

    private String sn;

    private Channel channel;

    private boolean isAuthenticated;

    private NewTcpServerHandler tcpServerHandler;

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
    public static NewSession getSession(String sn, Channel channel, NewTcpServerHandler tcpServerHandler) {
        NewSession session = new NewSession();
        session.setAuthenticated(false);
        session.setChannel(channel);
        session.setChannelId(channel.id().asLongText());
        session.setSn(sn);
        session.setTcpServerHandler(tcpServerHandler);
        session.setLastCommunicateTimeStamp(System.currentTimeMillis());
        return session;
    }
}
