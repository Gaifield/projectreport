package cn.damai.boss.projectreport.report.action;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.stereotype.Controller;

import cn.damai.boss.projectreport.commons.utils.KeyValueUtils;
import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.action.base.BaseAction;
import cn.damai.boss.projectreport.report.enums.PageActionEnum;
import cn.damai.boss.projectreport.report.service.MultiProjectStatService;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;

@Namespace("/")
@Controller
public class MultiProjectAction extends BaseAction {
	private static final long serialVersionUID = -4185788203269518607L;
	private static final Log log = LogFactory.getFactory().getInstance(
			MultiProjectAction.class);

	@Resource
	private MultiProjectStatService multiProjectStatService;
	
	private short action = 0; // 0:查询；1：导出excel；2：导出pdf
	private String projectIds=null; //"1584:1";
	private List<ReportProjectVo> projects;
	
	@Action(value = "multiProjectReport", results = {
			@Result(name = "success", location = "/projectreport/multiproject.jsp"),
			@Result(name = "stream", type = "stream", params = { "inputName",
					"inputStream", "contentType", "${contentType}",
					"Content-Disposition", "${contentDisposition}", "Pragma",
					"No-cache", "Cache-Control", "No-cache" })})
	public String initMultiProjectReport() {
		try {
			List<KeyValueUtils> projectSources = Utils.convertProjectSourceList(projectIds);
			projects = multiProjectStatService.findMultiProjectStatList(projectSources);
			
			if (action == PageActionEnum.Excel.getCode()) {
				ByteArrayOutputStream outStream = multiProjectStatService
						.outExcel(projects);
				addDownloadParameters(outStream, "总销售金额统计", EXT_XLS);
				return RESULT_STREAM;	
			} else if (action == PageActionEnum.Pdf.getCode()) {
				ByteArrayOutputStream outStream = multiProjectStatService
						.outPdf(projects);
				addDownloadParameters(outStream, "总销售金额统计", EXT_PDF);
				return RESULT_STREAM;	
			}else{
				//checkRequest();
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return SUCCESS;
	}

	public short getAction() {
		return action;
	}

	public void setAction(short action) {
		this.action = action;
	}

	public String getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(String projectIds) {
		this.projectIds = projectIds;
	}

	public List<ReportProjectVo> getProjects() {
		return projects;
	}

	public void setProjects(List<ReportProjectVo> projects) {
		this.projects = projects;
	}	
}