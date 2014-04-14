package cn.damai.boss.projectreport.manager.service;

import cn.damai.boss.projectreport.manager.context.ManagerUserContext;
import cn.damai.boss.projectreport.commons.ApplicationException;

/**
 * 注释：用户认证service
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-20 下午1:31
 */
public interface AuthenticateService {
    /**
     * 建立用户上下文信息
     *
     * @param userName 用户名
     * @return
     */
    public ManagerUserContext buildUserContext(String userName) throws ApplicationException;

}
