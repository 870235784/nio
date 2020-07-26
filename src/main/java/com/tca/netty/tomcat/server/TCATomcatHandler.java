package com.tca.netty.tomcat.server;

import com.alibaba.fastjson.JSONObject;
import com.tca.netty.tomcat.req.TCARequest;
import com.tca.netty.tomcat.resp.TCAResponse;
import com.tca.netty.tomcat.servlet.TCAServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhoua
 * @Date 2020/2/20
 * tomcat业务处理器
 */
@Slf4j
public class TCATomcatHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        log.info("收到客户端消息: {}", JSONObject.toJSONString(msg));

        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;

            TCARequest tcaRequest = new TCARequest(ctx, httpRequest);
            log.info("请求参数: {}", tcaRequest.getParameters());
            TCAResponse tcaResponse = new TCAResponse(ctx, httpRequest);

            new TCAServlet().doGet(tcaRequest, tcaResponse);


        }

//        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
