package cn.damai.boss.projectreport.report.service;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.context.ReportUserContext;

/**
 * 注释：用户认证service
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-20 下午1:31
 */
public interface ReportAuthenticateService {
    /**
     * 建立用户上下文信息
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public ReportUserContext buildUserContext(String userName, String password) throws ApplicationException;

}
