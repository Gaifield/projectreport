package cn.damai.boss.projectreport.report.dao;

import java.util.List;

import cn.damai.boss.projectreport.report.vo.BoxOfficeVo;

/**
 * 票房统计dao
 * @author：guwei 【顾炜】
 * time：2014-3-6 下午4:31:33
 *
 */
public interface BoxOfficeDao {
	
	
	/**
	 * 判断项目是否是选座项目
	 * @param projectId
	 * @return
	 * @author：guwei 【顾炜】
	 * time：2014-3-6 下午5:42:24
	 */
	boolean findProjectIsChooseSeatOn(Long projectId);
	
	List<BoxOfficeVo> findBoxOfficeByProjectIdOrPerformIds(Long projectId,List<Long> performIds);

}
