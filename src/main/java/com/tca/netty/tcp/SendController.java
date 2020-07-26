package com.tca.netty.tcp;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouan
 * @Date 2020/7/17
 */
@RestController
public class SendController {


   /* @GetMapping("/toClient")
    public Result send2Client() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messageType", "default");
        jsonObject.put("sn", "123");
        Session session = SessionManager.getInstance().getBySn("123");
        new TcpServerHandler().channelWrite(session.getChannelId(), jsonObject.toJSONString());
        return Result.success();
    }*/
}
