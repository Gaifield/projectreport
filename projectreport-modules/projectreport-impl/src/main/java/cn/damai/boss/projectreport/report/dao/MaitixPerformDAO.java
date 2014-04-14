package cn.damai.boss.projectreport.report.dao;

import java.util.Date;
import java.util.List;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.report.vo.PerformStandStatVo;

public interface MaitixPerformDAO {

	/**
	 * 分页查询场次列表
	* @Title: queryPerformList 
	* @Description: 
	* @date 2014-2-27 下午7:35:28 
	* @param projectId 项目Id
	* @param performInfoIds Maitix场次Id列表
	* @param startTime 场次开始时间
	* @param endTime 场次截止时间
	* @param pageSize 每页大小
	* @param pageIndex 每页记录数
	* @return
	 */
	public PageResultData queryPerformList(long projectId,List<Long> performInfoIds,Date startTime,Date endTime, int pageSize,int pageIndex);
	
	/**
	* 统计分分区出票
	* @Title: queryPerformStandStat 
	* @Description: 
	* @date 2014-2-28 下午2:29:41
	* @param projectInfoId Maitix项目Id
	* @param performInfoIds Maitix场次Id列表
	* @return
	 */
	public List<PerformStandStatVo> queryPerformStandStat(long projectInfoId,
			List<Long> performInfoIds);
}