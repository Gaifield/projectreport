package cn.damai.boss.projectreport.report.context;

/**
 * 注释：用户上下文信息
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-20 下午1:39
 */
public class ReportUserContext {
    /**
     * 主办方用户id
     */
    private Long businessUserId;
    /**
     * maitix主办方用户id
     */
    private Long maitixBusinessUserId;

    /**
     * 北京主办方id
     */
    private Long bjBusinessId;
    /**
     * 上海主办方id
     */
    private Long shBusinessId;
    /**
     * 广州主办方id
     */
    private Long gzBusinessId;

    /**
     * 主办方用户名
     */
    private String businessUserName;

    /**
     * 用户类型
     */
    private int userType;

    /**
     * sessionID
     */
    private String sessionID;

    /**
     * 时间戳
     */
    private String timeStamp;

    public Long getBusinessUserId() {
        return businessUserId;
    }

    public void setBusinessUserId(Long businessUserId) {
        this.businessUserId = businessUserId;
    }

    public Long getMaitixBusinessUserId() {
        return maitixBusinessUserId;
    }

    public void setMaitixBusinessUserId(Long maitixBusinessUserId) {
        this.maitixBusinessUserId = maitixBusinessUserId;
    }

    public Long getBjBusinessId() {
        return bjBusinessId;
    }

    public void setBjBusinessId(Long bjBusinessId) {
        this.bjBusinessId = bjBusinessId;
    }

    public Long getShBusinessId() {
        return shBusinessId;
    }

    public void setShBusinessId(Long shBusinessId) {
        this.shBusinessId = shBusinessId;
    }

    public Long getGzBusinessId() {
        return gzBusinessId;
    }

    public void setGzBusinessId(Long gzBusinessId) {
        this.gzBusinessId = gzBusinessId;
    }

    public String getBusinessUserName() {
        return businessUserName;
    }

    public void setBusinessUserName(String businessUserName) {
        this.businessUserName = businessUserName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
