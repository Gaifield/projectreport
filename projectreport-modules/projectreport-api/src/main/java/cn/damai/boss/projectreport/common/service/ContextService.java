package cn.damai.boss.projectreport.common.service;

import cn.damai.boss.projectreport.manager.context.ManagerUserContext;
import cn.damai.boss.projectreport.report.context.ReportUserContext;
import net.rubyeye.xmemcached.exception.MemcachedException;

import java.util.concurrent.TimeoutException;

/**
 * 注释：报表上下文service
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-22 下午1:24
 */
public interface ContextService {

    /**
     * 增加上下文配置项
     *
     * @param key   key
     * @param value 值
     */
    void add(String key, Object value);

    /**
     * 根据key获取上下文配置信息项
     *
     * @param key
     * @return
     * @throws InterruptedException
     * @throws net.rubyeye.xmemcached.exception.MemcachedException
     * @throws java.util.concurrent.TimeoutException
     */
    Object get(String key) throws InterruptedException, MemcachedException, TimeoutException;

    /**
     * 延迟配置项失效时间
     *
     * @param key
     * @param time
     * @return
     * @throws InterruptedException
     * @throws net.rubyeye.xmemcached.exception.MemcachedException
     * @throws java.util.concurrent.TimeoutException
     */
    Object delay(String key, int time) throws InterruptedException, MemcachedException, TimeoutException;

    /**
     * 延迟配置项失效时间
     *
     * @param key
     * @return
     * @throws InterruptedException
     * @throws net.rubyeye.xmemcached.exception.MemcachedException
     * @throws java.util.concurrent.TimeoutException
     */
    public Object delay(String key) throws InterruptedException, MemcachedException, TimeoutException;

    /**
     * 移除会话
     *
     * @param key
     */
    public void remove(String key);

    /**
     * 获取【授权管理平台】缓存用户上下文
     *
     * @param sessionId 用户全局唯一id
     * @return 获取缓存用户上下文
     */
    public ManagerUserContext getManagerUserContext(String sessionId);

    /**
     * 获取【报表平台】缓存用户上下文
     *
     * @param sessionId 用户全局唯一id
     * @return 获取缓存用户上下文
     */
    public ReportUserContext getReportUserContext(String sessionId);

}
