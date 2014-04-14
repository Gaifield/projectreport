package cn.damai.boss.projectreport.report.dao;

import java.util.List;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.vo.PerformVo;

/**
 * 场次
 * @author：guwei 【顾炜】
 * time：2014-3-1 上午2:03:46
 *
 */
public interface PerformInfoDao {
	
	/**
	 * 根据项目id查询项目场次信息
	 * @param projectId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ApplicationException
	 * @author：guwei 【顾炜】
	 * time：2014-3-1 上午3:24:11
	 */
	public List<PerformVo> findPerformInfoByProjectId(Long projectId,String startTime,String endTime) throws ApplicationException;

}
