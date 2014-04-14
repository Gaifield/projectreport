package cn.damai.boss.projectreport.report.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.stereotype.Controller;

import cn.damai.boss.projectreport.common.service.ContextService;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.service.RoleManagerService;
import cn.damai.boss.projectreport.manager.vo.ReportVo;
import cn.damai.boss.projectreport.report.action.base.BaseAction;
import cn.damai.boss.projectreport.report.context.ReportUserContext;
import cn.damai.boss.projectreport.report.context.ReportUserContextUtil;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.service.QueryProjectService;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;

/**
 * 报表查看基类
 * 
 * @author Administrator
 */

@Namespace("/")
@Controller
public class ReportIndexAction extends BaseAction {
	private static final long serialVersionUID = -1694676824663334728L;

	private static final Log log = LogFactory.getFactory().getInstance(ReportIndexAction.class);
	// 座位汇总报表
	private final static Set<String> SEATREPORTS = new HashSet<String>(Arrays.asList(new String[] { "座位汇总表", "预留明细统计", "分区出票统计" }));

	/**
	 * 报表上下文service
	 */
	@Resource
	private ContextService contextService;

	@Resource
	protected QueryProjectService queryProjectService;
	@Resource
	private RoleManagerService roleManagerService;

	protected long projectId = 0; // 项目Id
	protected String performIds;
	protected String projectName = ""; // 项目名称
	protected String source = ""; // 项目来源
	protected ReportProjectVo project; // 项目对象
	private List<ReportVo> reports; // 报表列表
	private String redirecturl;
	private String requestUrl = "";

	public void validate() {
		try {
			// 检查数据源
			DataSourceEnum.checkDataSourceEnum(source);
			// 验证项目Id
			if (projectId <= 0) {
				throw new IllegalArgumentException(getText("projectreport.message.illegalargument"));
			}

			// 获取当前登录的用户的北上广的主办id
			Map<String, Long> siteTradeIdMap = ReportUserContextUtil.getBSGBusinessId();
			// 获取当前站点的主办方id
			Long traderId = siteTradeIdMap.get(source);
			// 查询该用户是否授权查询项目(应改为直接返回项目对象，兼容下面的查询项目)
			project = queryProjectService.findIsOwnerProject(source, projectId, traderId);
			if (project == null) {
				throw new IllegalArgumentException("无项目权限，请联系管理员！");
			}

			projectName = project.getProjectName();

			initPageBase();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	private void initPageBase() {
		HttpServletRequest request = ServletActionContext.getRequest();
		requestUrl = request.getRequestURI();
		try {
			// 检索当前登录用户授权的报表
			String sessionID = ReportUserContextUtil.getSessionID();
			ReportUserContext reportUserContext = contextService.getReportUserContext(sessionID);
			List<ReportVo> reportVoList = roleManagerService.findUserReportList(reportUserContext.getMaitixBusinessUserId());
			if (reportVoList != null) {
				// 是否选坐项目
				boolean isSeatProject = project.getChooseSeatOn() == 1 ? true : false;
				reports = new ArrayList<ReportVo>();
				for (ReportVo reportVo : reportVoList) {
					// 无座项目不加载座位相关报表
					if (!isSeatProject && SEATREPORTS.contains(reportVo.getReportName())) {
						continue;
					}
					reports.add(reportVo);
				}
			}
			// 默认切换到项目来源对应的数据库
			DynamicDataSourceHolder.putDataSourceName(source);
		} catch (ApplicationException ex) {
			log.error(ex);
		}
	}

	/**
	 * 报表目录
	 * 
	 * @return
	 */
	@Action(value = "reportIndex", results = @Result(name = "success", type = "redirect", location = "${redirecturl}"))
	public String reportIndex() {
		String reportUrl = reports.get(0).getReportUrl();
		redirecturl = reportUrl + "?projectId=" + project.getProjectId() + "&source=" + source;
		return SUCCESS;
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

	public ReportProjectVo getProject() {
		return project;
	}

	public List<ReportVo> getReports() {
		return reports;
	}

	public String getRedirecturl() {
		return redirecturl;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	protected void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getProjectName() {
		return projectName;
	}
}