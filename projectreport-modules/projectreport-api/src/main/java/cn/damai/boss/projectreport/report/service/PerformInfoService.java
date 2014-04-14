package cn.damai.boss.projectreport.report.service;

import java.util.Date;
import java.util.List;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.vo.PerformVo;
import cn.damai.crius.core.model.PageData;

public interface PerformInfoService {
	
	
	/**
	 * 根据项目id查询场次信息
	 * @param projectId	项目id
	 * @param site 站点
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ApplicationException
	 * @author：guwei 【顾炜】
	 * time：2014-3-1 上午3:29:39
	 */
	public List<PerformVo> queryPerformInfoByProjectId(Long projectId,Short site,String startTime,String endTime) throws ApplicationException;
	
	/**
	 * 分区出票统计分页查询
	 * 
	 * @Title: findProjectPerformList
	 * @Description:
	 * @date 2014-2-27 下午2:11:05
	 * @param projectId
	 *            项目来源：1：北京库；2：上海库；3：广州库
	 * @param projectId
	 *            项目Id
	 * @param performIds
	 *            场次Id条件列表
	 * @param startTime 场次开始日期
	 * @param endTime 场次截至日期
	 * @param pageSize
	 *            每页记录数
	 * @param pageIndex
	 *            页码
	 * @return PageData/ProjectReportStandStatVo对象列表
	 * @throws ApplicationException
	 */
	public PageResultData findProjectPerformList(short source,long projectId,
			List<Long> performIds,Date startTime,Date endTime, int pageSize, int pageIndex)
			throws ApplicationException;
}
