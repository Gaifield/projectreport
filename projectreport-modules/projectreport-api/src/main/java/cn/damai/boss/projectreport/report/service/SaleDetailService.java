package cn.damai.boss.projectreport.report.service;

import cn.damai.boss.projectreport.report.vo.SaleFilterVo;
import cn.damai.boss.projectreport.report.vo.SaleRowVo;
import cn.damai.boss.projectreport.report.vo.SaleVo;

import java.util.List;

/**
 * 注释：出票汇总明细service
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-6 下午2:03
 */
public interface SaleDetailService {


    /**
     * 查询出票汇总明细
     *
     * @param filterVo 过滤参数vo
     * @return
     */
    public List<SaleVo> querySaleDetail(SaleFilterVo filterVo);

    /**
     * 查询出票汇总明细总数
     *
     * @param filterVo
     * @return
     */
    public int querySaleDetailTotal(SaleFilterVo filterVo);

    /**
     * 得到折扣名称
     * <p>
     * 先将折扣集合排序，然后设置折扣名称
     * </p>
     *
     * @param allDisaccountList 全部折扣
     */
    public List<String> getDisaccountName(List<String> allDisaccountList);

    /**
     * 设置SaleRowVo信息，即把源行数据进行累加后，赋值到目标行数据
     *
     * @param srcRowVo 源行数据
     * @param tgtRowVo 目标行数据
     * @return
     */
    public void setRowVo(SaleRowVo srcRowVo, SaleRowVo tgtRowVo);
}
