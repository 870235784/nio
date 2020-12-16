package com.tca.netty.tcp.handler;

import com.alibaba.fastjson.JSONObject;
import com.tca.common.utils.ValidateUtils;
import com.tca.netty.tcp.config.Session;
import com.tca.netty.tcp.config.SessionManager;
import com.tca.netty.tcp.enums.MessageTypeEnum;
import com.tca.netty.tcp.message.PackageData;
import com.tca.netty.tcp.message.inbound.RegistryReqMessage;
import com.tca.netty.tcp.message.outbound.RegistryRespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhouan
 * @Date 2020/7/17
 */
@Component
@Slf4j
public class RegisterMessageHandler extends AbstractMessageHandler {

    @Override
    public String getMessageType() {
        return MessageTypeEnum.REGISTER_REQ.getType();
    }

    @Override
    public void handle(String channelId, PackageData packageData) {
        RegistryReqMessage registryReqMessage = JSONObject.toJavaObject(packageData.getMessageBody(),
                RegistryReqMessage.class);
        // 1.绑定sn -- channel
        SessionManager sessionManager = SessionManager.getInstance();
        Session session = sessionManager.getByChannelId(channelId);
        // 2.当前channelId发送的当前sn码已经校验成功
        if (ValidateUtils.isNotEmpty(session) && session.isAuthenticated() && registryReqMessage.getSn()
                .equals(session.getSn())) {
            log.info("当前设备sn码已注册! sn = {}", registryReqMessage.getSn());
            return;
        }
        session.setCharsetName(ValidateUtils.isEmpty(registryReqMessage.getCharsetName())? "UTF-8":
            registryReqMessage.getCharsetName());
        session.setAuthenticated(true);
        session.setSn(registryReqMessage.getSn());
        sessionManager.putSnSession(registryReqMessage.getSn(), session);
        sessionManager.channelWrite(session.getChannelId(),
                        JSONObject.toJSONString(new RegistryRespMessage()));
    }


}
