package com.tca.netty.encode_decode.handler;

import com.tca.netty.tcp.enums.MessageTypeEnum;
import com.tca.utils.ValidateUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhoua
 * @Date 2020/8/2
 */
public class NewMessageDispatcher {


    private static Map<String, NewAbstractMessageHandler> dispatcher = new ConcurrentHashMap<>();

    /**
     * 注册
     * @param messageHandler
     */
    public static void register (NewAbstractMessageHandler messageHandler) {
        dispatcher.put(messageHandler.getMessageType(), messageHandler);
    }

    /**
     * 调度
     * @param messageType
     * @return
     */
    public static NewAbstractMessageHandler dispatch (String messageType) {
        NewAbstractMessageHandler messageHandler = dispatcher.get(messageType);
        return ValidateUtils.isEmpty(messageHandler)? dispatcher.get(MessageTypeEnum.DEFAULT.getType()):
                messageHandler;
    }

}
