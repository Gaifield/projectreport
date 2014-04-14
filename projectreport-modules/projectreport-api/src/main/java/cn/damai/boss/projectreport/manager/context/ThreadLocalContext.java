package cn.damai.boss.projectreport.manager.context;

/**
 * 注释：用户上下文工具类
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-20 下午3:28
 */
public class ThreadLocalContext
{
    public static final ThreadLocal<ManagerUserContext> threadLocal = new ThreadLocal<ManagerUserContext>();


    /**
     * 设置用户上下文信息
     *
     * @param context
     */
    public static void setUserContext(ManagerUserContext context)
    {
        if (context != null)
        {
            threadLocal.remove();
        }
        threadLocal.set(context);
    }

    /**
     * 得到用户上下文信息
     *
     * @return
     */
    public static ManagerUserContext getUserContext()
    {
        ManagerUserContext managerUserContext = threadLocal.get();
        return managerUserContext;
    }

}
