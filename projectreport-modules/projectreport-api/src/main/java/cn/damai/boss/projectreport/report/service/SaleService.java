package cn.damai.boss.projectreport.report.service;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.vo.SaleFilterVo;
import cn.damai.boss.projectreport.report.vo.SaleVo;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * 注释：出票汇总Service 作者：liutengfei 【刘腾飞】 时间：14-3-14 上午10:28
 */
public interface SaleService {
    /**
     * 查询出票汇总
     *
     * @param filterVo 过滤参数vo
     * @return
     */
    public SaleVo querySale(SaleFilterVo filterVo);

    /**
     * 导出excel
     *
     * @param saleDetailList 出票汇总表对象
     * @param projectName    项目名称
     * @param isHuiZong      是否是汇总
     * @return
     * @throws ApplicationException
     */
    public ByteArrayOutputStream outExcel(List<SaleVo> saleDetailList, String projectName, boolean isHuiZong) throws ApplicationException;

    /**
     * 导出Pdf
     *
     * @param saleDetailList 出票汇总表对象
     * @param projectName    项目名称
     * @param isHuiZong      是否是汇总
     * @return
     * @throws ApplicationException
     */
    public ByteArrayOutputStream outPdf(List<SaleVo> saleDetailList, String projectName, boolean isHuiZong) throws ApplicationException;
}
