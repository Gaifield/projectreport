package cn.damai.boss.projectreport.manager.vo;

import java.util.UUID;

/**
 * 注释：客户端与服务端数据传递
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-19 上午11:34
 */
public class Message
{
    // 返回数据
    private Object data;

    // 状态   正常：200 警告：300 服务器错误：500
    private int status = 200;

    // 信息描述
    private String describe;

    // 异常信息
    private String exception;

    // UUID
    private String id = UUID.randomUUID().toString();

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getDescribe()
    {
        return describe;
    }

    public void setDescribe(String describe)
    {
        this.describe = describe;
    }

    public String getException()
    {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
