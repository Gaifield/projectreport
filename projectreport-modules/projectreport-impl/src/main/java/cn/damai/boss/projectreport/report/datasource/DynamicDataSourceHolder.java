package cn.damai.boss.projectreport.report.datasource;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-5
 * Time: 下午12:07
 * To change this template use File | Settings | File Templates.
 */
public class DynamicDataSourceHolder {
    private static final ThreadLocal<String> holder = new ThreadLocal<String>();

    public static void putDataSourceName(String name){
        holder.set(name);
    }

    public static String getDataSourceName(){
        return holder.get();
    }
}
