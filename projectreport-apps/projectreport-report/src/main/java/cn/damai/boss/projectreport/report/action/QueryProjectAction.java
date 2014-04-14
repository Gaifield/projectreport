package cn.damai.boss.projectreport.report.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.action.base.BaseAction;
import cn.damai.boss.projectreport.report.enums.ProjectStatusEnum;
import cn.damai.boss.projectreport.report.service.QueryProjectService;
import cn.damai.boss.projectreport.report.service.SearchTemplateService;
import cn.damai.boss.projectreport.report.vo.ProjectClassVo;
import cn.damai.boss.projectreport.report.vo.RegionVo;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;
import cn.damai.boss.projectreport.report.vo.RmvenueVo;
import cn.damai.boss.projectreport.report.vo.SearchTemplateVo;
import cn.damai.boss.projectreport.report.vo.TemplateContentVo;

/**
 * 注释：项目筛选action
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-26 下午3:15
 */
@Namespace("/")
public class QueryProjectAction extends BaseAction {
	private static final Log log = LogFactory.getFactory().getInstance(
			QueryProjectAction.class);
    @Resource
    private SearchTemplateService searchTemplateService;
    @Resource
    private QueryProjectService queryProjectService;

    /**
     * 项目vo对象
     */
    private ReportProjectVo reportProjectVo;

    /**
     * 项目类别vo对象List
     */
    private List<ProjectClassVo> projectClassVoList;

    /**
     * 项目vo对象List
     */
    private List<ReportProjectVo> reportProjectVoList;

    /**
     * 项目城市Vo列表
     */
    private List<RegionVo> regionVoList;
    /**
     * 项目场馆Vo列表
     */
    private List<RmvenueVo> rmvenueVo;
    /**
     * 已经开始的项目
     */
    private List<ReportProjectVo> startVoList;

    /**
     * 已经结束的项目
     */
    private List<ReportProjectVo> endVoList;


    /**
     * 分页对象
     */
    private PageResultData pageResult;

    /**
     * 页面默认记录数
     */
    private final static int DEFAULT_PAGE_SIZE = 10;


    /**
     * wjr模板内容Vo
     */
    private TemplateContentVo templateContentVo;
    /**
     * wjr模板名称
     */
    private String templateName;
    /**
     * wjr模板Vo
     */
    private List<SearchTemplateVo> searchTemplateVos;
    /**
     * wjr模板id
     */
    private long templateId;

    /**
     * 查询项目类别
     *
     * @return
     */
    @Action(value = "queryProjectClass", results = {@Result(type = "json", params = {"root", "projectClassVoList", "contentType", "text/html"})})
    public String queryProjectClass() {
        try {
            projectClassVoList = queryProjectService.queryProjectClass();
        } catch (ApplicationException ae) {
            log.error(ae.getMessage(), ae);
        }
        return SUCCESS;
    }

    /**
     * 查询演出城市
     *
     * @return
     */
    @Action(value = "queryPerformCityByKeyWord", results = {@Result(type = "json", params = {"root", "regionVoList", "contentType", "text/html"})})
    public String queryProjectCityByKeyWord() {
        try {
            String performCity = reportProjectVo.getPerformCity();
            if (performCity != null && performCity.trim().length() != 0) {
                regionVoList = queryProjectService.queryPerformCityByKeyWord(performCity);
            }
        } catch (Exception ae) {
        	 log.error(ae.getMessage(), ae);
        }
        return SUCCESS;
    }

    /**
     * 根据项目名称查询项目
     *
     * @return
     */
    @Action(value = "queryProjectNameByKeyWord", results = {@Result(type = "json", params = {"root", "reportProjectVoList", "contentType", "text/html"})})
    public String queryProjectNameByKeyWord() {
        try {
            String projectName = reportProjectVo.getProjectName();
            if (projectName != null && projectName.trim().length() != 0) {
                reportProjectVoList = queryProjectService.queryProjectNameByKeyWord(projectName);
            }
        } catch (ApplicationException ae) {
        	 log.error(ae.getMessage(), ae);
        }
        return SUCCESS;
    }

    /**
     * 查询演出场馆
     *
     * @return
     */
    @Action(value = "queryPerformFieldByKeyWord", results = {@Result(type = "json", params = {"root", "rmvenueVo", "contentType", "text/html"})})
    public String queryPerformFieldByKeyWord() {
        try {
            String performField = reportProjectVo.getPerformField();
            if (performField != null && performField.trim().length() != 0) {
                rmvenueVo = queryProjectService.queryPerformFieldByKeyWord(performField);
            }
        } catch (ApplicationException ae) {
        	 log.error(ae.getMessage(), ae);
        }
        return SUCCESS;
    }

    /**
     * 根据过滤条件查询项目
     *
     * @return
     */
    @Action(value = "queryProjectByFilter", results = {@Result(name = "success", location = "/projectreport/selectproject.jsp")})
    public String queryProjectByFilter() {
        try {
            if (reportProjectVo == null) {
                reportProjectVo = new ReportProjectVo();
            }
            if (reportProjectVo.getPage() <= 0) {
                reportProjectVo.setPage(1);
            }
            if (reportProjectVo.getPageSize() == 0) {
                reportProjectVo.setPageSize(DEFAULT_PAGE_SIZE);
            }
            pageResult = queryProjectService.queryProjectByFilter(reportProjectVo);
            searchTemplateVos = searchTemplateService.findSearchTemplate();
            setSearchTemplateVos(searchTemplateVos);
            
        } catch (Exception ae) {
        	 log.error(ae.getMessage(), ae);
        }
        return SUCCESS;
    }

    /**
     * @return
     */
    @Action(value = "queryProjectSellAndOver", results = {@Result(name = "success", location = "/projectreport/reportindex.jsp")})
    public String queryProjectSellAndOver() {
        try {
            Map<Integer, List<ReportProjectVo>> listMap = queryProjectService.queryProjectSellAndOver();
            startVoList = listMap.get(Integer.valueOf(ProjectStatusEnum.Approve.getCodeStr()));
            endVoList = listMap.get(Integer.valueOf(ProjectStatusEnum.Over.getCodeStr()));
        } catch (ApplicationException ae) {
        	 log.error(ae.getMessage(), ae);
        }
        return SUCCESS;
    }


    public ReportProjectVo getReportProjectVo() {
        return reportProjectVo;
    }

    public void setReportProjectVo(ReportProjectVo reportProjectVo) {
        this.reportProjectVo = reportProjectVo;
    }

    public List<ProjectClassVo> getProjectClassVoList() {
        return projectClassVoList;
    }

    public void setProjectClassVoList(List<ProjectClassVo> projectClassVoList) {
        this.projectClassVoList = projectClassVoList;
    }

    public List<ReportProjectVo> getReportProjectVoList() {
        return reportProjectVoList;
    }

    public List<ReportProjectVo> getStartVoList() {
        return startVoList;
    }

    public void setStartVoList(List<ReportProjectVo> startVoList) {
        this.startVoList = startVoList;
    }

    public List<ReportProjectVo> getEndVoList() {
        return endVoList;
    }

    public void setEndVoList(List<ReportProjectVo> endVoList) {
        this.endVoList = endVoList;
    }

    public TemplateContentVo getTemplateContentVo() {
        return templateContentVo;
    }

    public void setTemplateContentVo(TemplateContentVo templateContentVo) {
        this.templateContentVo = templateContentVo;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<SearchTemplateVo> getSearchTemplateVos() {
        return searchTemplateVos;
    }

    public void setSearchTemplateVos(List<SearchTemplateVo> searchTemplateVos) {
        this.searchTemplateVos = searchTemplateVos;
    }

    public List<RegionVo> getRegionVoList() {
        return regionVoList;
    }

    public void setRegionVoList(List<RegionVo> regionVoList) {
        this.regionVoList = regionVoList;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public void setReportProjectVoList(List<ReportProjectVo> reportProjectVoList) {
        this.reportProjectVoList = reportProjectVoList;
    }

    public PageResultData getPageResult() {
        return pageResult;
    }

    public void setPageResult(PageResultData pageResult) {
        this.pageResult = pageResult;
    }

    public List<RmvenueVo> getRmvenueVo() {
        return rmvenueVo;
    }

    public void setRmvenueVo(List<RmvenueVo> rmvenueVo) {
        this.rmvenueVo = rmvenueVo;
    }


}
