package com.tca.netty.tcp.handler;

import com.tca.netty.tcp.config.Session;
import com.tca.netty.tcp.config.SessionManager;
import com.tca.netty.tcp.message.PackageData;
import lombok.extern.slf4j.Slf4j;

/***
 * 统一协议抽象处理类
 * @author ghost
 * @date 2019-07-29 10:57
 */
@Slf4j
public abstract class AbstractMessageHandler {

    protected SessionManager sessionManager = SessionManager.getInstance();

    public AbstractMessageHandler() {
        MessageDispatcher.register(this);
    }

    /***
     * 获取数据域
     * @return String
     */
    public abstract String getMessageType();


    /**
     * 处理 PackageData
     * @param channelId
     * @param packageData
     */
    public abstract void handle(String channelId, PackageData packageData);

    /**
     * 校验是否注册, 未注册需要断开连接
     * @param channelId
     */
    protected Session getSession(String channelId) {
        Session session = SessionManager.getInstance().getByChannelId(channelId);
        if (!session.isAuthenticated()) {
            log.error("当前连接未认证, 需要关闭连接! channelId = {}", channelId);
            sessionManager.close(session.getChannel());
            return null;
        }
        return session;
    }

}
