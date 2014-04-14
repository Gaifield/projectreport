package cn.damai.boss.projectreport.manager.service;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.vo.ReportVo;

import java.util.List;

/**
 * 注释：报表service
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-23 下午1:45
 */
public interface ReportService
{

    /**
     * 查询报表信息
     *
     * @return
     */
    public List<ReportVo> findAllReport() throws ApplicationException;
}
