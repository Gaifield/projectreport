package cn.damai.boss.projectreport.commons.model;

/**
 * Json数据对象封装类
 *
 * @param <T>
 * @author Administrator
 *         2014年2月24日10:03:32 刘腾飞修改
 */
public class ReturnData<T>
{
    //返回状态
    private int status;
    //返回数据
    private T data;
    //描述信息
    private String description;

    public ReturnData()
    {
        super();
    }

    public ReturnData(int status, T data)
    {
        super();
        this.status = status;
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

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}