package cn.damai.boss.projectreport.manager.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注释：自定义权限注解
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-20 下午5:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission
{
    /**
     * 模块
     *
     * @return
     */
    String module();

    /**
     * 权限级别
     *
     * @return
     */
    int pemissionLevel();
}
