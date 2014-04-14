package cn.damai.boss.projectreport.report.dao;

import java.util.List;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;

/**
 * 跨项目查询
 *
 * 创建人：guwei【顾炜】   
 * 创建时间：2014-2-27 上午11:46:25
 *
 */
public interface MultiProjectStatDao {
	
	/**
	 * 
	 * @param site
	 * @param projectIds
	 * @return
	 * @throws ApplicationException
	 */
	public List<ReportProjectVo> queryMulitProjectInfoList(String site,List<Long> projectIds) throws ApplicationException;

}
