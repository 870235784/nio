package com.tca.netty.tcp.message;

import com.alibaba.fastjson.JSONObject;
import com.tca.utils.ValidateUtils;
import lombok.Data;

/**
 * @author zhouan
 * 设备上传数据
 */
@Data
public class PackageData {

    public PackageData() {}

    public PackageData(String msg) {
        this.messageBody = JSONObject.parseObject(msg);
    }

    /**
     * 消息实体
     */
    private JSONObject messageBody;

    /**
     * 获取消息类型
     * @return
     */
    public String getMessageType() {
        if (ValidateUtils.isEmpty(messageBody)) {
            return null;
        }
        return messageBody.getString("messageType");
    }

    /**
     * 获取当前消息的对应的sn码
     * @return
     */
    public String getSn() {
        if (ValidateUtils.isEmpty(messageBody)) {
            return null;
        }
        return messageBody.getString("sn");
    }

    public void setCharsetName(String charsetName) {
        messageBody.put("charsetName", charsetName);
    }

}
