package cn.damai.boss.projectreport.report.dao;

import cn.damai.boss.projectreport.report.vo.PriceVo;

import java.util.List;
import java.util.Map;


/**
 * 报表价格查询DAO
 *
 * @author Administrator
 */
public interface ReportPriceDAO {

    /**
     * 查询报表价格列表，包括普通票/套票
     *
     * @param projectId      maitix项目Id
     * @param performInfoIds maitix场次Id列表
     * @return
     */
    public List<PriceVo> queryProjectReportPrice(long projectId, List<Long> performInfoIds);

    /**
     * 查询场次价格
     *
     *
     * @param projectId      项目id
     * @param performInfoIds 场次id
     * @return
     */
    public Map<Long, List<PriceVo>> queryPerformPrice(long projectId, String performInfoIds);
}