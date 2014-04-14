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

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.enums.PageActionEnum;
import cn.damai.boss.projectreport.report.service.SellerSaleService;
import cn.damai.boss.projectreport.report.vo.SellerSaleReportVo;

/**
 * 渠道销售统计
 * 
 * @author：guwei 【顾炜】 time：2014-3-6 下午3:48:59
 * 
 */
@Namespace("/")
public class SellerSaleAction extends ReportIndexAction {
	private final int DEFAULT_PAGE_SIZE = 5;
	private static final long serialVersionUID = 1747013552454070552L;
	private static final Log log = LogFactory.getLog(SellerSaleAction.class);

	@Resource
	private SellerSaleService sellerSaleService;

	private short action = 0; // 0:查询；1：导出excel；2：导出pdf
	private int dateChoseType = 0;

	/**
	 * 接收的id串
	 */
	private String performIds;
	/**
	 * 开始时间
	 */
	private Date saleTimeStart;
	/**
	 * 结束时间
	 */
	private Date saleTimeEnd;
	/**
	 * 渠道商名称
	 */
	private String agentName;

	private SellerSaleReportVo sellerSaleReportVo;

	@Action(value = "sellerSaleReport", results = {
			@Result(name = "success", location = "/projectreport/sellersale.jsp"),
			@Result(name = "stream", type = "stream", params = { "inputName",
					"inputStream", "contentType", "${contentType}",
					"Content-Disposition", "${contentDisposition}", "Pragma",
					"No-cache", "Cache-Control", "No-cache" }) })
	public String initSellerSaleReport() {
		try {
			setRows(DEFAULT_PAGE_SIZE);
			List<Long> performIdList = Utils.convert2IdList(performIds);
			Date[] dates = getSaleSearchDateTime();
			
			if (action == PageActionEnum.Excel.getCode()) {
				sellerSaleReportVo = sellerSaleService.findSellerSaleStat(
						source, projectId, performIdList, dates[0], dates[1],
						agentName, 0, getPage(),true);
				ByteArrayOutputStream outStream = sellerSaleService.outExcel(
						sellerSaleReportVo, projectName);
				addDownloadParameters(outStream, "渠道销售统计", EXT_XLS);
				return RESULT_STREAM;
			} else if (action == PageActionEnum.Pdf.getCode()) {
				sellerSaleReportVo = sellerSaleService.findSellerSaleStat(
						source, projectId, performIdList, dates[0], dates[1],
						agentName, 0, getPage(),true);

				ByteArrayOutputStream outStream = sellerSaleService.outPdf(
						sellerSaleReportVo, projectName);
				addDownloadParameters(outStream, "渠道销售统计", EXT_PDF);
				return RESULT_STREAM;
			} else {
				sellerSaleReportVo = sellerSaleService.findSellerSaleStat(
						source, projectId, performIdList, dates[0], dates[1],
						agentName, getRows(), getPage(),getPage()==1);				
			}

		} catch (ApplicationException e) {
			log.error("销售渠道统计报表查询时发生错误。", e);
			e.printStackTrace();
		}

		return SUCCESS;
	}

	private Date[] getSaleSearchDateTime() {
		Date[] dates = new Date[2];
		switch (dateChoseType) {
		case 1:
			dates = Utils.getCurrentMonthDates();
			break;
		case 2:
			dates = Utils.getAddMonthDates(-1);
			break;
		case 3:
			dates[0] = saleTimeStart;
			dates[1] = saleTimeEnd;
			break;
		}
		return dates;
	}

	public short getAction() {
		return action;
	}

	public void setAction(short action) {
		this.action = action;
	}

	public int getDateChoseType() {
		return dateChoseType;
	}

	public void setDateChoseType(int dateChoseType) {
		this.dateChoseType = dateChoseType;
	}

	public String getPerformIds() {
		return performIds;
	}

	public void setPerformIds(String performIds) {
		this.performIds = performIds;
	}

	public Date getSaleTimeStart() {
		return saleTimeStart;
	}

	public void setSaleTimeStart(String saleTimeStart) {
		this.saleTimeStart = Utils.parseDateTime(saleTimeStart);
	}

	public Date getSaleTimeEnd() {
		return saleTimeEnd;
	}

	public void setSaleTimeEnd(String saleTimeEnd) {
		this.saleTimeEnd = Utils.parseDateTime(saleTimeEnd);
		
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public SellerSaleReportVo getSellerSaleReportVo() {
		return sellerSaleReportVo;
	}

	public void setSellerSaleReportVo(SellerSaleReportVo sellerSaleReportVo) {
		this.sellerSaleReportVo = sellerSaleReportVo;
	}

}
