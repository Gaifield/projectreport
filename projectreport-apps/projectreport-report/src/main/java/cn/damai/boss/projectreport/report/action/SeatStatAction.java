package cn.damai.boss.projectreport.report.action;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.enums.PageActionEnum;
import cn.damai.boss.projectreport.report.service.PerformInfoService;
import cn.damai.boss.projectreport.report.service.SeatStatService;
import cn.damai.boss.projectreport.report.vo.SeatStatVo;

/**
 * 注释：座位汇总action 作者：liutengfei 【刘腾飞】 时间：14-3-3 下午1:56
 */
@Namespace("/")
public class SeatStatAction extends ReportIndexAction {

	private static final Log log = LogFactory.getLog(SeatStatAction.class);

	private final int DEFAULT_PAGE_SIZE = 5;

	@Resource
	private SeatStatService seatStatService;
	@Resource
	private PerformInfoService performInfoService;

	private List<SeatStatVo> seatList;

	private short action = 0;

	/**
	 * 场次idlist集合
	 */
	private String performIds;

	@Action(value = "seatStatInfoReport", results = {
			@Result(name = "success", location = "/projectreport/seatstat.jsp"),
			@Result(name = "stream", type = "stream", params = { "inputName", "inputStream", "contentType", "${contentType}", "Content-Disposition",
					"${contentDisposition}", "Pragma", "No-cache", "Cache-Control", "No-cache" }) })
	public String querySeatStatInfo() {
		try {

			// 如果场次id为空，这查询整个项目的场次座位汇总
			List<Long> performIdList = Utils.convert2IdList(performIds);

			seatList = seatStatService.querySeatStatInfo(performIdList, projectId);

			if (action == PageActionEnum.Excel.getCode()) {
				ByteArrayOutputStream outStream = seatStatService.outExcel(seatList, false);// false导出汇总表
				addDownloadParameters(outStream, "座位汇总表", EXT_XLS);
				return RESULT_STREAM;
			} else if (action == PageActionEnum.Pdf.getCode()) {
				ByteArrayOutputStream outStream = seatStatService.outPdf(seatList, false);
				addDownloadParameters(outStream, "座位汇总表", EXT_PDF);
				return RESULT_STREAM;
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			log.error(e);
		}
		return SUCCESS;

	}

	@Action(value = "seatStatDetailReport", results = {
			@Result(name = "success", location = "/projectreport/seatstatdetail.jsp"),
			@Result(name = "stream", type = "stream", params = { "inputName", "inputStream", "contentType", "${contentType}", "Content-Disposition",
					"${contentDisposition}", "Pragma", "No-cache", "Cache-Control", "No-cache" }) })
	public String querySeatStatDetail() {
		try {
			// 控制左侧导航栏，显示当前位置
			setRequestUrl("seatStatInfoReport.do");
			// 如果场次id为空，这查询整个项目的场次座位汇总
			List<Long> performIdList = Utils.convert2IdList(performIds);

			if (action == PageActionEnum.Default.getCode()) {
				// 设置默认分页
				setRows(DEFAULT_PAGE_SIZE);
				PageResultData<SeatStatVo> seatStatPager = seatStatService.querySeatStatDetail(projectId, performIdList, getRows(), getPage());
				seatList = seatStatPager.getRows();
				// 显示当前查询总条数
				PageResultData pg = new PageResultData();
				pg.setTotal(seatStatPager.getTotal());
				setPageData(pg);
			} else if (action == PageActionEnum.Excel.getCode()) {
				PageResultData<SeatStatVo> seatStatPager = seatStatService.querySeatStatDetail(projectId, performIdList, 0, 0);
				seatList = seatStatPager.getRows();
				ByteArrayOutputStream outStream = seatStatService.outExcel(seatList, true);// false导出汇总表
				addDownloadParameters(outStream, "座位汇总表", EXT_XLS);
				return RESULT_STREAM;
			} else if (action == PageActionEnum.Pdf.getCode()) {
				PageResultData<SeatStatVo> seatStatPager = seatStatService.querySeatStatDetail(projectId, performIdList, 0, 0);
				seatList = seatStatPager.getRows();
				ByteArrayOutputStream outStream = seatStatService.outPdf(seatList, true);
				addDownloadParameters(outStream, "座位汇总表", EXT_PDF);
				return RESULT_STREAM;
			} else {

			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			log.error(e);
		}
		return SUCCESS;
	}

	public String getPerformIds() {
		return performIds;
	}

	public void setPerformIds(String performIds) {
		this.performIds = performIds;
	}

	public List<SeatStatVo> getSeatList() {
		return seatList;
	}

	public void setSeatList(List<SeatStatVo> seatList) {
		this.seatList = seatList;
	}

	public short getAction() {
		return action;
	}

	public void setAction(short action) {
		this.action = action;
	}

}