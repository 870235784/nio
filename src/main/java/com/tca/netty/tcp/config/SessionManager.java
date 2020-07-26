package com.tca.netty.tcp.config;


import com.tca.utils.ValidateUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouan
 * Session管理
 */
public class SessionManager {

    private static volatile SessionManager instance = null;

    /**
     * sn - Session的对应关系
     */
    private Map<String, Session> snSessionMap;

    /**
     * channelId - session 关联关系 (当前size >= snSessionMap.size 因为刚连接上来只会保存当前
     *  channelId -- session 关联关系)
     */
    private Map<String, Session> channelIdSessionMap;


    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    private SessionManager() {
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
    public Session getBySn(String sn) {
        return snSessionMap.get(sn);
    }

    /**
     * 根据channelId获取session
     * @param channelId
     * @return
     */
    public Session getByChannelId(String channelId) {
        return channelIdSessionMap.get(channelId);
    }



    /**
     * 存储 sn - session 关联关系
     * @param sn
     * @param session
     * @return
     */
    public Session putSnSession(String sn, Session session) {
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
    public Session putChannelIdSession(String channelId, Session session) {
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
    public Session removeBySn(String sn, boolean removeChannelId) {
        if (sn == null){
            return null;
        }
        Session session = snSessionMap.remove(sn);
        if (removeChannelId && ValidateUtils.isNotEmpty(session)) {
            channelIdSessionMap.remove(session.getChannelId());
        }
        return session;
    }

    public Session removeBySn(String sn) {
        return removeBySn(sn, true);
    }

    /**
     * 移除 sn - session 关联关系
     * 移除 channelId - session 关联关系
     * @param channelId
     * @return
     */
    public Session removeByChannelId(String channelId, boolean removeSn) {
        if (channelId == null){
            return null;
        }
        Session session = channelIdSessionMap.remove(channelId);
        if (removeSn && ValidateUtils.isNotEmpty(session) && ValidateUtils.isNotEmpty(session.getSn())) {
            snSessionMap.remove(session.getSn());
        }
        return session;
    }

    public Session removeByChannelId(String channelId) {
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