package cn.damai.boss.projectreport.manager.service.impl;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.manager.dao.ReportDAO;
import cn.damai.boss.projectreport.domain.Upt01Report;
import cn.damai.boss.projectreport.manager.service.ReportService;
import cn.damai.boss.projectreport.manager.vo.ReportVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 注释：
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-23 下午1:47
 */
@Service
public class ReportServiceImpl implements ReportService
{
    @Resource
    private ReportDAO reportDAO;

    /**
     * 查询所有报表信息
     *
     * @return
     */
    @Override
    public List<ReportVo> findAllReport() throws ApplicationException
    {
        List<ReportVo> voList = new ArrayList<ReportVo>();
        try
        {
            List<Upt01Report> reportList = reportDAO.findAll();
            for (Upt01Report report : reportList)
            {
                voList.add(convertUpt01ReportToVO(report));
            }
        } catch (Exception e)
        {
            throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
        }
        return voList;
    }


    /**
     * 将po转换成vo
     *
     * @param report
     * @return
     */

    private ReportVo convertUpt01ReportToVO(Upt01Report report)
    {
        ReportVo vo = new ReportVo();
        vo.setReportId(report.getReportId());
        vo.setReportName(report.getReportName());
        vo.setReportUrl(report.getReportUrl());
        return vo;
    }
}
