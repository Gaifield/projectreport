package cn.damai.boss.projectreport.report.action;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.stereotype.Controller;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.enums.PageActionEnum;
import cn.damai.boss.projectreport.report.service.ProjectReportStandStatService;

/**
 * 分区出票统计
 * 
 * @ClassName: StandStatAction
 * @Description:
 * @author zhangbinghong
 * @date 2014-3-1 下午2:41:41
 */
@Namespace("/")
@Controller
public class StandStatAction extends ReportIndexAction {
	private static final long serialVersionUID = -310768887962340142L;
	private static final Log log = LogFactory.getFactory().getInstance(
			StandStatAction.class);
	private final int DEFAULT_PAGE_SIZE = 5;

	@Resource
	private ProjectReportStandStatService projectReportStandStatService;

	// 查询参数
	private short action = 0; // 0:查询；1：导出excel；2：导出pdf
	private String performIds;
	private Date startTime;
	private Date endTime;
	private PageResultData pageResult;

	@Action(value = "standStatReport", results = {@Result(name = "success", location = "/projectreport/standstat.jsp"),
			@Result(name = "stream", type = "stream", params = { "inputName","inputStream", "contentType", "${contentType}","Content-Disposition", "${contentDisposition}",
					"Pragma","No-cache", "Cache-Control", "No-cache" })})
	public String initStandStatReport() {
		try {
			setRows(DEFAULT_PAGE_SIZE);
			List<Long> performIdList = Utils.convert2IdList(performIds);
			if (action == PageActionEnum.Excel.getCode()) {
				pageResult = projectReportStandStatService
						.findProjectReportStandStatList(source,projectId,
								performIdList, 0, 0);
				
				ByteArrayOutputStream outStream = projectReportStandStatService.OutExcel(pageResult,projectName);
				addDownloadParameters(outStream, "分区出票统计", EXT_XLS);
				return RESULT_STREAM;			
			} else if (action == PageActionEnum.Pdf.getCode()) {
				pageResult = projectReportStandStatService
						.findProjectReportStandStatList(source,projectId,
								performIdList, 0, 0);

				ByteArrayOutputStream outStream = projectReportStandStatService
						.OutPdf(pageResult, projectName);
				addDownloadParameters(outStream, "分区出票统计", EXT_PDF);
				return RESULT_STREAM;
			} else {
				pageResult = projectReportStandStatService
						.findProjectReportStandStatList(source,projectId,
								performIdList, getRows(), getPage());
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return SUCCESS;
	}	

	public short getAction() {
		return action;
	}

	public String getPerformIds() {
		return performIds;
	}

	public void setPerformIds(String performIds) {
		this.performIds = performIds;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public PageResultData getPageResult() {
		return pageResult;
	}

	public void setPageResult(PageResultData pageResult) {
		this.pageResult = pageResult;
	}

	public void setAction(short action) {
		this.action = action;
	}
}