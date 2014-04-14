package cn.damai.boss.projectreport.report.action;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.report.enums.PageActionEnum;
import cn.damai.boss.projectreport.report.enums.QueryDateTypeEnum;
import cn.damai.boss.projectreport.report.service.SaleDetailService;
import cn.damai.boss.projectreport.report.service.SaleService;
import cn.damai.boss.projectreport.report.vo.SaleFilterVo;
import cn.damai.boss.projectreport.report.vo.SaleVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 注释：出票汇总action 作者：liutengfei 【刘腾飞】 时间：14-3-6 下午2:02
 */

@Namespace("/")
public class SaleAction extends ReportIndexAction {
    private static final Log log = LogFactory.getFactory().getInstance(SaleAction.class);
    /**
     * 出票汇总service
     */
    @Resource
    private SaleService saleService;

    /**
     * 出票汇总明细service
     */
    @Resource
    private SaleDetailService saleDetailService;

    /**
     * 出票汇总vo
     */
    private SaleVo saleVo;

    /**
     * 出票汇总明细vo
     */
    private List<SaleVo> saleVoList;

    /**
     * 过滤条件vo
     */
    private SaleFilterVo filterVo;

    // 查询参数
    private short action = 0; // 0:查询；1：导出excel；2：导出pdf

    /**
     * 默认每页显示5条
     */
    private final int DEFAULT_PAGE_SIZE = 5;

    /**
     * 跳转到出票汇总表
     *
     * @return
     */
    @Action(value = "saleReport", results = {@Result(name = "success", location = "/projectreport/sale.jsp"), @Result(name = "stream", type = "stream", params = {"inputName", "inputStream", "contentType", "${contentType}", "Content-Disposition", "${contentDisposition}", "Pragma", "No-cache", "Cache-Control", "No-cache"})})
    public String saleReport() {
        try {
            //设置过滤条件
            setFilterValue();
            //是否分页
            filterVo.setSplitPage(false);

            saleVo = saleService.querySale(filterVo);

            // 导出	判断数据查询不为空
            if (saleVo != null) {
                saleVoList = new ArrayList<SaleVo>();
                saleVoList.add(saleVo);
                if (action == PageActionEnum.Excel.getCode()) {
                    ByteArrayOutputStream outStream = saleService.outExcel(saleVoList, projectName, true);// false导出汇总表
                    addDownloadParameters(outStream, "出票明细表", EXT_XLS);
                    return RESULT_STREAM;
                } else if (action == PageActionEnum.Pdf.getCode()) {
                    ByteArrayOutputStream outStream = saleService.outPdf(saleVoList, projectName, true);
                    addDownloadParameters(outStream, "出票明细表", EXT_PDF);
                    return RESULT_STREAM;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return SUCCESS;
    }

    /**
     * 出票汇总明细表
     *
     * @return
     */
    @Action(value = "saleReportDetail", results = {@Result(name = "success", location = "/projectreport/saledetail.jsp"), @Result(name = "stream", type = "stream", params = {"inputName", "inputStream", "contentType", "${contentType}", "Content-Disposition", "${contentDisposition}", "Pragma", "No-cache", "Cache-Control", "No-cache"})})
    public String saleReportDetail() {
        try {
            setRequestUrl("saleReport.do");

            //设置过滤条件
            setFilterValue();
            //分页
            filterVo.setSplitPage(true);
            filterVo.setPage(getPage());
            filterVo.setPageSize(DEFAULT_PAGE_SIZE);
            saleVoList = saleDetailService.querySaleDetail(filterVo);
            int total = saleDetailService.querySaleDetailTotal(filterVo);
            PageResultData<SaleVo> pageData = new PageResultData();
            pageData.setRows(saleVoList);
            pageData.setTotal(total);
            setPageData(pageData);
            setRows(DEFAULT_PAGE_SIZE);

            if (action == PageActionEnum.Excel.getCode()) {
                ByteArrayOutputStream outStream = saleService.outExcel(saleVoList, projectName, false);// false导出汇总表
                addDownloadParameters(outStream, "出票明细表", EXT_XLS);
                return RESULT_STREAM;
            } else if (action == PageActionEnum.Pdf.getCode()) {
                ByteArrayOutputStream outStream = saleService.outPdf(saleVoList, projectName, false);
                addDownloadParameters(outStream, "出票明细表", EXT_PDF);
                return RESULT_STREAM;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return SUCCESS;
    }

    /**
     * 设置过滤条件
     */
    private void setFilterValue() {
        if (filterVo == null) {
            filterVo = new SaleFilterVo();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //设置日期
        String dateRadio = filterVo.getDateRadio();
        if (QueryDateTypeEnum.ThisMonth.getCodeStr().equals(dateRadio)) {
            //获取当月第一天：
            Calendar start = Calendar.getInstance();
            start.add(Calendar.MONTH, 0);
            start.set(Calendar.DAY_OF_MONTH, 1);
            String startTime = format.format(start.getTime());
            startTime += " 00:00";
            //获取当月最后一天
            Calendar end = Calendar.getInstance();
            end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
            String endTime = format.format(end.getTime());
            endTime += " 23:59";

            filterVo.setStartTime(startTime);
            filterVo.setEndTime(endTime);
        } else if (QueryDateTypeEnum.UpMonth.getCodeStr().equals(dateRadio)) {
            //获取上月的第一天
            Calendar start = Calendar.getInstance();
            start.add(Calendar.MONTH, -1);
            start.set(Calendar.DAY_OF_MONTH, 1);
            String startTime = format.format(start.getTime());
            startTime += " 00:00";

            //获取上月的最后一天
            Calendar end = Calendar.getInstance();
            end.set(Calendar.DAY_OF_MONTH, 0);
            String endTime = format.format(end.getTime());
            endTime += " 23:59";

            filterVo.setStartTime(startTime);
            filterVo.setEndTime(endTime);
        }
        filterVo.setProjectId(projectId);
        filterVo.setPerformIds(performIds);
        filterVo.setSource(source);
        filterVo.setSeatType(project.getChooseSeatOn());
    }

    public SaleVo getSaleVo() {
        return saleVo;
    }

    public void setSaleVo(SaleVo saleVo) {
        this.saleVo = saleVo;
    }

    public List<SaleVo> getSaleVoList() {
        return saleVoList;
    }

    public void setSaleVoList(List<SaleVo> saleVoList) {
        this.saleVoList = saleVoList;
    }

    public SaleFilterVo getFilterVo() {
        return filterVo;
    }

    public void setFilterVo(SaleFilterVo filterVo) {
        this.filterVo = filterVo;
    }

    public short getAction() {
        return action;
    }

    public void setAction(short action) {
        this.action = action;
    }
}
