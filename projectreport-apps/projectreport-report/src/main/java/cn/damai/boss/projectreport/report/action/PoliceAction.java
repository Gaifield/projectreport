package cn.damai.boss.projectreport.report.action;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.stereotype.Controller;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.action.base.BaseAction;
import cn.damai.boss.projectreport.report.context.ReportUserContextUtil;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.enums.PageActionEnum;
import cn.damai.boss.projectreport.report.service.ProjectReportStandStatService;
import cn.damai.boss.projectreport.report.service.QueryProjectService;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;

/**
 * 公安首页Action
 * 
 * @author wenjunrong【温俊荣】 2014-03-05上午10:41
 * 
 */
@Namespace("/")
@Controller
public class PoliceAction extends BaseAction {

	private static final long serialVersionUID = -310768887962340142L;
	private static final Log log = LogFactory.getFactory().getInstance(PoliceAction.class);
	private final int DEFAULT_PAGE_SIZE = 5;

	@Resource
	private ProjectReportStandStatService projectReportStandStatService;
	@Resource
	protected QueryProjectService queryProjectService;

	protected long projectId = 0; // 项目Id
	protected String performIds;
	protected String projectName = ""; // 项目名称
	protected String source = ""; // 项目来源
	private short action = 0; // 0:查询；1：导出excel；2：导出pdf

	protected ReportProjectVo project; // 项目对象
	private PageResultData pageResult;
	private List<ReportProjectVo> projects;

	@Action(value = "policeReport", results = {
			@Result(name = "success", location = "/projectreport/police.jsp"),
			@Result(name = "stream", type = "stream", params = { "inputName", "inputStream", "contentType", "${contentType}", "Content-Disposition",
					"${contentDisposition}", "Pragma", "No-cache", "Cache-Control", "No-cache" }) })
	public String policeReport() {
		validateProject();

		try {
			setRows(DEFAULT_PAGE_SIZE);
			List<Long> performIdList = Utils.convert2IdList(performIds);
			if (action == PageActionEnum.Excel.getCode()) {
				pageResult = projectReportStandStatService.findProjectReportStandStatList(source, projectId, performIdList, 0, 0);

				ByteArrayOutputStream outStream = projectReportStandStatService.OutExcel(pageResult, projectName);
				addDownloadParameters(outStream, "分区出票统计", EXT_XLS);
				return RESULT_STREAM;
			} else if (action == PageActionEnum.Pdf.getCode()) {
				pageResult = projectReportStandStatService.findProjectReportStandStatList(source, projectId, performIdList, 0, 0);
				ByteArrayOutputStream outStream = projectReportStandStatService.OutPdf(pageResult, projectName);
				addDownloadParameters(outStream, "分区出票统计", EXT_PDF);
				return RESULT_STREAM;
			} else {
				// 加载授权的项目列表
				Map<String, Long> siteTradeIdMap = ReportUserContextUtil.getBSGBusinessId();
				projects = queryProjectService.findOwnerProjectList(siteTradeIdMap);
				if (projects != null && projects.size() > 0 && projectId <= 0) {
					projectId = projects.get(0).getProjectId();
					source = projects.get(0).getDataResource();
					projectName = projects.get(0).getProjectName();
				}

				// 加列表数据
				if (projectId > 0) {
					pageResult = projectReportStandStatService.findProjectReportStandStatList(source, projectId, performIdList, getRows(), getPage());
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return SUCCESS;
	}

	private void validateProject() {
		if (projectId > 0) {
			// 检查数据源
			DataSourceEnum.checkDataSourceEnum(source);

			// 获取当前登录的用户的北上广的主办id
			Map<String, Long> siteTradeIdMap = ReportUserContextUtil.getBSGBusinessId();
			// 获取当前站点的主办方id
			Long traderId = siteTradeIdMap.get(source);

			try {
				// 查询该用户是否授权查询项目
				project = queryProjectService.findIsOwnerProject(source, projectId, traderId);
				if (project != null) {
					projectName = project.getProjectName();
				} else {
					throw new IllegalArgumentException(getText("projectreport.message.illegalargument"));
				}
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getPerformIds() {
		return performIds;
	}

	public void setPerformIds(String performIds) {
		this.performIds = performIds;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public short getAction() {
		return action;
	}

	public void setAction(short action) {
		this.action = action;
	}

	public String getProjectName() {
		return projectName;
	}

	public ReportProjectVo getProject() {
		return project;
	}

	public PageResultData getPageResult() {
		return pageResult;
	}

	public List<ReportProjectVo> getProjects() {
		return projects;
	}
}