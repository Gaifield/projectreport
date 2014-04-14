package cn.damai.boss.projectreport.report.context;

/**
 * 注释：用户上下文工具类
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-20 下午3:28
 */
public class ReportThreadLocalContext
{
    public static final ThreadLocal<ReportUserContext> threadLocal = new ThreadLocal<ReportUserContext>();


    /**
     * 设置用户上下文信息
     *
     * @param context
     */
    public static void setUserContext(ReportUserContext context)
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
    public static ReportUserContext getUserContext()
    {
        ReportUserContext reportUserContext = threadLocal.get();
        return reportUserContext;
    }

}
