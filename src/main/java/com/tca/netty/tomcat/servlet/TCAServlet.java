package com.tca.netty.tomcat.servlet;

import com.tca.netty.tomcat.req.TCARequest;
import com.tca.netty.tomcat.resp.TCAResponse;

/**
 * @author zhoua
 * @Date 2020/2/20
 */
public class TCAServlet extends AbstractServlet {

    @Override
    public void doGet(TCARequest request, TCAResponse response) {
        try {
            response.write("hello " + request.getParameter("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doPost(TCARequest request, TCAResponse response) {

    }
}
