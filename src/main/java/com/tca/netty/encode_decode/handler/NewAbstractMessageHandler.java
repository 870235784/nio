package com.tca.netty.encode_decode.handler;

import com.tca.netty.tcp.message.PackageData;

/**
 * @author zhoua
 * @Date 2020/8/2
 */
public abstract class NewAbstractMessageHandler {

    public NewAbstractMessageHandler() {
        NewMessageDispatcher.register(this);
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
}
