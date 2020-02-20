package com.tca.netty.tomcat.req;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * @author zhoua
 * @Date 2020/2/20
 */
public class TCARequest {

    private ChannelHandlerContext ctx;

    private HttpRequest httpRequest;

    public TCARequest(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        this.ctx = ctx;
        this.httpRequest = httpRequest;
    }

    public String getUri() {
        return httpRequest.uri();
    }

    public String getMethod() {
        return httpRequest.method().name();
    }

    public Map<String, List<String>> getParameters() {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httpRequest.uri());
        return queryStringDecoder.parameters();
    }

    public String getParameter(String parameter) {
        Map<String, List<String>> parameters = getParameters();
        List<String> result = parameters.get(parameter);
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }


}
