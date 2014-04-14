package cn.damai.boss.projectreport.manager.action;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.action.base.BaseAction;
import cn.damai.boss.projectreport.manager.service.ReportService;
import cn.damai.boss.projectreport.manager.vo.ReportVo;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import javax.annotation.Resource;
import java.util.List;

/**
 * 注释：报表action
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-23 下午1:42
 */
@ParentPackage("json-default")
@Namespace("/")
public class ReportAction extends BaseAction {

    @Resource
    private ReportService reportService;

    /**
     * 查询所有报表信息
     *
     * @return
     */
    @Action(value = "findAllReport", results = {@Result(type = "json", params = {"root", "pageData", "contentType", "text/html"})})
    public String findAllReport() {
        PageResultData<ReportVo> pageData = new PageResultData<ReportVo>();
        try {
            List<ReportVo> reportVoList = reportService.findAllReport();
            pageData.setRows(reportVoList);
            setPageData(pageData);
        } catch (ApplicationException ae) {
        }
        return SUCCESS;
    }
}
