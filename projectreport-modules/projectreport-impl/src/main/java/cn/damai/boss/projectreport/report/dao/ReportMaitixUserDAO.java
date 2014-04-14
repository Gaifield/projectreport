package cn.damai.boss.projectreport.report.dao;

import cn.damai.boss.projectreport.manager.vo.MaitixUserVo;

/**
 * 注释：报表主办方用户DAO
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-9 下午2:21
 */
public interface ReportMaitixUserDAO {

    /**
     * 根据组织机构用户Id查询用户记录
     *
     * @param userId 组织机构用户Id
     * @return
     */
    public MaitixUserVo findByUserId(Long userId);


}