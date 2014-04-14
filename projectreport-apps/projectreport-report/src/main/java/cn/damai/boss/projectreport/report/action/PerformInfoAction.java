package cn.damai.boss.projectreport.report.action;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.service.PerformInfoService;

/**
 * 场次
 * 
 * @author：guwei 【顾炜】 time：2014-3-1 上午5:09:27
 * 
 */
@Namespace("/")
public class PerformInfoAction extends ReportIndexAction {

	private static final long serialVersionUID = 8803416901250080068L;
	private static final Log log = LogFactory.getFactory().getInstance(
			PerformInfoAction.class);

	@Resource
	private PerformInfoService performInfoService;
	
	private Date startTime;
	private Date endTime;
	private PageResultData pageResult;
	/**
	 * 查询项目场次
	 * 
	 * @Title: queryPerform
	 * @Description:
	 * @date 2014-3-2 下午12:25:17
	 * @return
	 */
	@Action(value = "queryPerform", results = { @Result(type = "json", params = {
			"root", "pageResult", "contentType", "text/html" }) })
	public String queryPerform() {
		try {
			pageResult = performInfoService.findProjectPerformList((short)0,
					projectId, null, startTime, endTime, 0, 0);
		} catch (ApplicationException e) {
			log.error(e.getMessage(), e);
		}
		return SUCCESS;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = Utils.parseDateTime(startTime);
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = Utils.parseDateTime(endTime);
	}
	public PageResultData getPageResult() {
		return pageResult;
	}
	public void setPageResult(PageResultData pageResult) {
		this.pageResult = pageResult;
	}
	
}
