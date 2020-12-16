package com.tca.netty.encode_decode.config;

import com.tca.common.utils.ValidateUtils;
import com.tca.netty.tcp.config.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhoua
 * @Date 2020/8/2
 */
public class NewSessionManager {

    private static volatile NewSessionManager instance = null;

    /**
     * sn - Session的对应关系
     */
    private Map<String, NewSession> snSessionMap;

    /**
     * channelId - session 关联关系 (当前size >= snSessionMap.size 因为刚连接上来只会保存当前
     *  channelId -- session 关联关系)
     */
    private Map<String, NewSession> channelIdSessionMap;


    public static NewSessionManager getInstance() {
        if (instance == null) {
            synchronized (NewSessionManager.class) {
                if (instance == null) {
                    instance = new NewSessionManager();
                }
            }
        }
        return instance;
    }

    private NewSessionManager() {
        this.snSessionMap = new ConcurrentHashMap<>();
        this.channelIdSessionMap = new ConcurrentHashMap<>();
    }

    /**
     * 是否包含sn
     * @param sn
     * @return
     */
    public boolean containsSn(String sn) {
        return snSessionMap.containsKey(sn);
    }

    /**
     * 是否包含channelId
     * @param channelId
     * @return
     */
    public boolean containsChannelId(String channelId) {return channelIdSessionMap.containsKey(channelId);}

    /**
     * 是否包含session
     * @param session
     * @return
     */
    public boolean containsSession(Session session) {
        return snSessionMap.containsValue(session);
    }

    /**
     * 根据sn获取session
     * @param sn
     * @return
     */
    public NewSession getBySn(String sn) {
        return snSessionMap.get(sn);
    }

    /**
     * 根据channelId获取session
     * @param channelId
     * @return
     */
    public NewSession getByChannelId(String channelId) {
        return channelIdSessionMap.get(channelId);
    }



    /**
     * 存储 sn - session 关联关系
     * @param sn
     * @param session
     * @return
     */
    public NewSession putSnSession(String sn, NewSession session) {
        if (ValidateUtils.isEmpty(sn) || ValidateUtils.isEmpty(session)) {
            return null;
        }
        return snSessionMap.put(sn, session);
    }

    /**
     * 存储 channelId - session 关联关系
     * @param channelId
     * @param session
     * @return
     */
    public NewSession putChannelIdSession(String channelId, NewSession session) {
        if (ValidateUtils.isEmpty(channelId) || ValidateUtils.isEmpty(session)) {
            return null;
        }
        return channelIdSessionMap.put(channelId, session);
    }

    /**
     * 移除 sn - session 关联关系
     * 移除 channelId - session 关联关系
     * @param sn
     * @return
     */
    public NewSession removeBySn(String sn, boolean removeChannelId) {
        if (sn == null){
            return null;
        }
        NewSession session = snSessionMap.remove(sn);
        if (removeChannelId && ValidateUtils.isNotEmpty(session)) {
            channelIdSessionMap.remove(session.getChannelId());
        }
        return session;
    }

    public NewSession removeBySn(String sn) {
        return removeBySn(sn, true);
    }

    /**
     * 移除 sn - session 关联关系
     * 移除 channelId - session 关联关系
     * @param channelId
     * @return
     */
    public NewSession removeByChannelId(String channelId, boolean removeSn) {
        if (channelId == null){
            return null;
        }
        NewSession session = channelIdSessionMap.remove(channelId);
        if (removeSn && ValidateUtils.isNotEmpty(session) && ValidateUtils.isNotEmpty(session.getSn())) {
            snSessionMap.remove(session.getSn());
        }
        return session;
    }

    public NewSession removeByChannelId(String channelId) {
        return removeByChannelId(channelId, true);
    }

    /**
     * 获取连接数量
     * @return
     */
    public int size() {
        return channelIdSessionMap.size();
    }
}
