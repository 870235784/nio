package com.tca.netty.tomcat.servlet;

import com.tca.netty.tomcat.req.TCARequest;
import com.tca.netty.tomcat.resp.TCAResponse;

/**
 * @author zhoua
 * @Date 2020/2/20
 */
public abstract class AbstractServlet {

    /**
     * 处理get请求
     */
    public abstract void doGet(TCARequest request, TCAResponse response);

    /**
     * 处理post请求
     */
    public abstract void doPost(TCARequest request, TCAResponse response);

}
