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

import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.enums.PageActionEnum;
import cn.damai.boss.projectreport.report.service.ProjectReportReserveStatService;
import cn.damai.boss.projectreport.report.vo.ReserveStatVo;

/**
 * 预留统计
 * 
 * @ClassName: StandStatAction
 * @Description:
 * @author zhangbinghong
 * @date 2014-3-1 下午2:41:41
 */
@Namespace("/")
@Controller
public class ReserveStatAction extends ReportIndexAction {
	private static final long serialVersionUID = -310768887962340142L;
	private static final Log log = LogFactory.getFactory().getInstance(
			ReserveStatAction.class);

	@Resource
	private ProjectReportReserveStatService projectReportReserveStatService;

	// 查询参数
	private short action = 0; // 0:查询；1：导出excel；2：导出pdf
	private String performIds;

	private ReserveStatVo reserveStatVo;

	@Action(value = "reserveStatReport", results = {
			@Result(name = "success", location = "/projectreport/reservestat.jsp"),
			@Result(name = "stream", type = "stream", params = { "inputName",
					"inputStream", "contentType", "${contentType}",
					"Content-Disposition", "${contentDisposition}", "Pragma",
					"No-cache", "Cache-Control", "No-cache" }) })
	public String initReserveStatReport() {
		try {
			List<Long> performIdList = Utils.convert2IdList(performIds);
			reserveStatVo = projectReportReserveStatService
					.findProjectReportReserveStatList(source,projectId, performIdList);

			if (action == PageActionEnum.Excel.getCode()) {
				// 导出Excel
				ByteArrayOutputStream outStream = projectReportReserveStatService
						.outExcel(reserveStatVo, projectName);
				addDownloadParameters(outStream, "预留统计", EXT_XLS);
				return RESULT_STREAM;
			} else if (action == PageActionEnum.Pdf.getCode()) {
				// 导出Pdf
				ByteArrayOutputStream outStream = projectReportReserveStatService
						.outPdf(reserveStatVo, projectName);
				addDownloadParameters(outStream, "预留统计", EXT_PDF);
				return RESULT_STREAM;
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return SUCCESS;
	}

	public void setAction(short action) {
		this.action = action;
	}

	public ReserveStatVo getReserveStatVo() {
		return reserveStatVo;
	}

	public void setReserveStatVo(ReserveStatVo reserveStatVo) {
		this.reserveStatVo = reserveStatVo;
	}

	public short getAction() {
		return action;
	}
}