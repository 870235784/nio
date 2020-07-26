package com.tca.netty.tcp.config;

import com.tca.netty.tcp.handler.MessageDispatcher;
import com.tca.netty.tcp.message.PackageData;
import com.tca.netty.tcp.util.EncodingUtils;
import com.tca.utils.ValidateUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author zhouan
 * 服务端处理
 */
@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

    public TcpServerHandler() {
        log.info(" --- 初始化TCPServerHandler --- ");
        this.sessionManager = SessionManager.getInstance();
    }


    /**
     * SessionManager
     */
    private final SessionManager sessionManager;


    /**
     * 客户端连接上
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 1.获取客户端
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();
        int clientPort = inSocket.getPort();
        // 2.获取连接通道唯一标识
        Channel channel = ctx.channel();
        String channelId = channel.id().asLongText();
        //如果map中不包含此连接，就保存连接
        if (sessionManager.containsChannelId(channelId)) {
            log.info(" --- 客户端【" + channelId + "】是连接状态，连接通道数量: " + sessionManager.size());
        } else {
            //保存连接
            Session session = Session.getSession(null, channel,this);
            sessionManager.putChannelIdSession(channelId, session);
            log.info(" --- 客户端【" + channelId + "】连接netty服务器[IP:" + clientIp + "--->PORT:" + clientPort + "]");
            log.info(" --- 连接通道数量: " + sessionManager.size());
        }
    }

    /**
     * 连接断开
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 获取客户端
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();
        String channelId = ctx.channel().id().asLongText();
        // 包含此客户端才去删除
        if (sessionManager.containsChannelId(channelId)) {
            close(ctx.channel());
            log.info(" --- 客户端【" + channelId + "】退出netty服务器[IP:" + clientIp + "--->PORT:" + inSocket.getPort() + "]");
            log.info(" -- 连接通道数量: " + sessionManager.size());
        }
    }


    /**
     *  读取数据
     * @return void
     * @author Lee
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        try {
            // 1.转成统一协议 PackageData
            ByteBuf buf = (ByteBuf) msg;
            if (buf.readableBytes() <= 0) {
                return;
            }
            byte[] bs = new byte[buf.readableBytes()];
            buf.readBytes(bs);

            Session session = SessionManager.getInstance().getByChannelId(ctx.channel().id().asLongText());
            Charset charset = ValidateUtils.isEmpty(session.getCharsetName()) ?
                    CharsetUtil.UTF_8 : Charset.forName(session.getCharsetName());

            String messageStr = new String(bs, charset);
            log.info("从客户端读取数据, channelId = {}, msg = {}", ctx.channel().id().asLongText(), messageStr);
            PackageData data = new PackageData(messageStr);
            // 2.消息处理
            MessageDispatcher.dispatch(data.getMessageType()).handle(ctx.channel().id().asLongText(), data);
        } catch (Exception e) {
            log.error("接收处理数据错误", e);
        } finally {
            release(msg);
        }
    }

    /**
     * 写数据
     * @param channelId
     * @param msg
     */
    public ChannelFuture channelWrite(String channelId, String msg) {
        Session session = sessionManager.getByChannelId(channelId);
        if (ValidateUtils.isEmpty(session)) {
            log.error("session不存在, channelId = {}", channelId);
            return null;
        }
        ChannelFuture channelFuture = session.getChannel().writeAndFlush(
                Unpooled.copiedBuffer(msg, Charset.forName(session.getCharsetName())));
        channelFuture.addListener((ChannelFutureListener) future -> {
            // 6. 关闭连接
            if (future.isSuccess()) {
                log.info("发送成功!");
            } else {
                log.info("发送失败！");
            }
        });
        return channelFuture;
//        return session.getChannel().writeAndFlush(msg);
//        return session.getChannel().writeAndFlush(msg.getBytes(Charset.forName(session.getCharsetName())));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        log.error("发生异常:", cause);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            // 读超时 主动断开连接
            if (event.state() == IdleState.READER_IDLE) {
                log.error("服务器主动断开连接: {}", ctx.channel().id().asLongText());
                Session session = sessionManager.removeByChannelId(ctx.channel().id().asLongText());
                ctx.close();
            }
        }
    }

    private void release(Object msg) {
        try {
            ReferenceCountUtil.release(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭通道
     * @param channel
     */
    public void close(Channel channel) {
        if (ValidateUtils.isEmpty(channel)) {
            return;
        }
        String channelId = channel.id().asLongText();
        log.info("通道关闭: channelId = {}", channelId);
        sessionManager.removeByChannelId(channelId);
        channel.close();
    }
}