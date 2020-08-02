package com.tca.netty.encode_decode.handler;

import com.alibaba.fastjson.JSONObject;
import com.tca.netty.encode_decode.config.NewSession;
import com.tca.netty.encode_decode.config.NewSessionManager;
import com.tca.netty.tcp.enums.MessageTypeEnum;
import com.tca.netty.tcp.message.PackageData;
import com.tca.netty.tcp.message.outbound.HeartbeatRespMessage;
import org.springframework.stereotype.Component;

/**
 * @author zhoua
 * @Date 2020/8/2
 */
@Component
public class NewHeartbeatMessageHandler extends NewAbstractMessageHandler {

    @Override
    public String getMessageType() {
        return MessageTypeEnum.HEARTBEAT.getType();
    }

    @Override
    public void handle(String channelId, PackageData packageData) {
        NewSession session = NewSessionManager.getInstance().getByChannelId(channelId);
        HeartbeatRespMessage heartbeatRespMessage = new HeartbeatRespMessage();
        session.getTcpServerHandler().channelWrite(session.getChannelId(),
                JSONObject.toJSONString(heartbeatRespMessage));
    }
}
