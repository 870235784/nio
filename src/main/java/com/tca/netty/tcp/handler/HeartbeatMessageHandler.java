package com.tca.netty.tcp.handler;

import com.alibaba.fastjson.JSONObject;
import com.tca.netty.tcp.config.Session;
import com.tca.netty.tcp.config.SessionManager;
import com.tca.netty.tcp.enums.MessageTypeEnum;
import com.tca.netty.tcp.message.PackageData;
import com.tca.netty.tcp.message.outbound.HeartbeatRespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhouan
 * @Date 2020/7/17
 */
@Slf4j
@Component
public class HeartbeatMessageHandler extends AbstractMessageHandler {

    @Override
    public String getMessageType() {
        return MessageTypeEnum.HEARTBEAT.getType();
    }

    @Override
    public void handle(String channelId, PackageData packageData) {
        Session session = SessionManager.getInstance().getByChannelId(channelId);
        HeartbeatRespMessage heartbeatRespMessage = new HeartbeatRespMessage();
        session.getTcpServerHandler().channelWrite(session.getChannelId(),
                JSONObject.toJSONString(heartbeatRespMessage));
    }

}
