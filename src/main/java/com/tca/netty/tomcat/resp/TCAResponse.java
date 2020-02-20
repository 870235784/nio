package com.tca.netty.tomcat.resp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

/**
 * @author zhoua
 * @Date 2020/2/20
 */
public class TCAResponse {

    private ChannelHandlerContext ctx;

    private HttpRequest httpRequest;

    public TCAResponse(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        this.ctx = ctx;
        this.httpRequest = httpRequest;
    }

    public void write(String outMessage) throws Exception {
        if (outMessage == null) {
            return;
        }

        try {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(outMessage.getBytes("UTF-8"))
            );

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(HttpHeaderNames.EXPIRES, 0);
            if (HttpUtil.isKeepAlive(httpRequest)) {
                response.headers().set(HttpHeaderNames.CONNECTION, "keep-alive");
            }

            ctx.write(response);
        } finally {
            ctx.flush();
        }

    }
}
