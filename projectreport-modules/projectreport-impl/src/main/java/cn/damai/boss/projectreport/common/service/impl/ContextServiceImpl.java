package cn.damai.boss.projectreport.common.service.impl;

import cn.damai.boss.projectreport.common.service.ContextService;
import cn.damai.boss.projectreport.manager.context.ManagerUserContext;
import cn.damai.boss.projectreport.report.context.ReportUserContext;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeoutException;

/**
 * 注释：报表上下文service
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-22 下午1:30
 */
@Service
public class ContextServiceImpl implements ContextService {

    private static final Log log = LogFactory.getLog(ContextServiceImpl.class);

    /**
     * json 解析处理器
     */
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @Resource(name = "memcachedClient")
    private MemcachedClient memcachedClient;

    @Value("${memcached.session.timeout}")
    private String timeout;

    /**
     * 增加上下文配置项
     *
     * @param key   key
     * @param value 值
     */
    public void add(String key, Object value) {
        try {
            Object o = get(key);
            if (o != null) {
                memcachedClient.replace(key, Integer.parseInt(timeout), value);
            } else {
                memcachedClient.add(key, Integer.parseInt(timeout), value);
            }

        } catch (TimeoutException e) {
            e.printStackTrace();
            log.error("缓存超时", e);
        } catch (Exception e) {
            log.error("设置缓存发生错误", e);
            e.printStackTrace();
        }
    }

    /**
     * 根据key获取上下文配置信息项
     *
     * @param key
     * @return
     * @throws InterruptedException
     * @throws net.rubyeye.xmemcached.exception.MemcachedException
     * @throws java.util.concurrent.TimeoutException
     */
    public Object get(String key) throws InterruptedException, MemcachedException, TimeoutException {
        return memcachedClient.get(key);
    }

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
    public Object delay(String key, int time) throws InterruptedException, MemcachedException, TimeoutException {
        Object o = memcachedClient.get(key);
        if (o != null) {
            memcachedClient.set(key, time, o);
        }
        return o;
    }

    /**
     * 延迟配置项失效时间
     *
     * @param key
     * @return
     * @throws InterruptedException
     * @throws net.rubyeye.xmemcached.exception.MemcachedException
     * @throws java.util.concurrent.TimeoutException
     */
    public Object delay(String key) throws InterruptedException, MemcachedException, TimeoutException {
        Object o = memcachedClient.get(key);
        if (o != null) {
            memcachedClient.set(key, Integer.parseInt(timeout), o);
        }
        return o;
    }

    /**
     * 移除会话
     *
     * @param key
     */
    public void remove(String key) {
        try {

            memcachedClient.delete(key);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取【授权管理平台】缓存用户上下文
     *
     * @param sessionId 用户全局唯一id
     * @return 获取缓存用户上下文
     */
    public ManagerUserContext getManagerUserContext(String sessionId) {
        ManagerUserContext managerUserContext = null;
        try {
            Object o = get(sessionId);
            if (o != null) {
                managerUserContext = jsonMapper.readValue(String.valueOf(o), ManagerUserContext.class);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return managerUserContext;
    }


    /**
     * 获取【报表平台】缓存用户上下文
     *
     * @param sessionId 用户全局唯一id
     * @return 获取缓存用户上下文
     */
    @Override
    public ReportUserContext getReportUserContext(String sessionId) {
        ReportUserContext reportUserContext = null;
        try {
            Object o = get(sessionId);
            if (o != null) {
                reportUserContext = jsonMapper.readValue(String.valueOf(o), ReportUserContext.class);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportUserContext;
    }


}
