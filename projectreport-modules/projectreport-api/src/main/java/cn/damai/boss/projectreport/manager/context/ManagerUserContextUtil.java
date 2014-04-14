package cn.damai.boss.projectreport.manager.context;

/**
 * 注释：用户上下文信息工具类
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-21 下午2:21
 */
public class ManagerUserContextUtil {
    /**
     * 得到操作员ID，即当前登录人ID
     * @return
     */
    public static long getOperatorId() {
        ManagerUserContext managerUserContext = ThreadLocalContext.getUserContext();
        if (managerUserContext != null) {
            return managerUserContext.getOperatorId();
        }
        return -1;
    }

    /**
     * 得到当前登录人的名称
     *
     * @return
     */
    public static String getUserName() {
        ManagerUserContext managerUserContext = ThreadLocalContext.getUserContext();
        if (managerUserContext != null) {
            return managerUserContext.getUserName();
        }
        return null;
    }

    /**
     * 得到sessionID
     *
     * @return
     */
    public static String getSessionID() {
        String sessionID = null;
        ManagerUserContext managerUserContext = ThreadLocalContext.getUserContext();
        if (managerUserContext != null)
            sessionID = managerUserContext.getSessionID();
        return sessionID;
    }

    /**
     * 得到oa用户id
     *
     * @return
     */
    public static Long getUserId() {
        Long userId = null;
        ManagerUserContext managerUserContext = ThreadLocalContext.getUserContext();
        if (managerUserContext != null)
            userId = managerUserContext.getUserId();
        return userId;
    }

    /**
     * 得到用户权限级别
     *
     * @return
     */
    public static int getPermissionLevel() {
        int permissionLevel = -1;
        ManagerUserContext managerUserContext = ThreadLocalContext.getUserContext();
        if (managerUserContext != null) {
            permissionLevel = managerUserContext.getPermissionLevel();
        }
        return permissionLevel;
    }

}
