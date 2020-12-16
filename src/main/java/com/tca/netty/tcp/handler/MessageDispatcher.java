package com.tca.netty.tcp.handler;

import com.tca.common.utils.ValidateUtils;
import com.tca.netty.tcp.enums.MessageTypeEnum;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/***
 * 统一协议处理调度器
 * @author ghost
 * @date 2019-07-29 10:57
 */
@NoArgsConstructor
public class MessageDispatcher {

    private static Map<String, AbstractMessageHandler> dispatcher = new ConcurrentHashMap<>();

    /**
     * 注册
     * @param messageHandler
     */
    public static void register (AbstractMessageHandler messageHandler) {
        dispatcher.put(messageHandler.getMessageType(), messageHandler);
    }

    /**
     * 调度
     * @param messageType
     * @return
     */
    public static AbstractMessageHandler dispatch (String messageType) {
        AbstractMessageHandler messageHandler = dispatcher.get(messageType);
        return ValidateUtils.isEmpty(messageHandler)? dispatcher.get(MessageTypeEnum.DEFAULT.getType()):
                messageHandler;
    }
}

