package cn.damai.boss.projectreport.manager.context;

/**
 * 注释：用户上下文信息
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-20 下午1:39
 */
public class ManagerUserContext
{
    /**
     * 操作员ID
     */
    private long operatorId;

    /**
     * 对应的OA用户ID
     */
    private long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 权限级别
     */
    private int permissionLevel;

    /**
     * sessionID
     */
    private String sessionID;

    /**
     * 时间戳
     */
    private String timeStamp;

    public long getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(long operatorId)
    {
        this.operatorId = operatorId;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public int getPermissionLevel()
    {
        return permissionLevel;
    }

    public void setPermissionLevel(int permissionLevel)
    {
        this.permissionLevel = permissionLevel;
    }

    public String getSessionID()
    {
        return sessionID;
    }

    public void setSessionID(String sessionID)
    {
        this.sessionID = sessionID;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }
}
