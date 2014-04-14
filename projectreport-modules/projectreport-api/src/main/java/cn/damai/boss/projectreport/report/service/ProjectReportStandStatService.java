package cn.damai.boss.projectreport.report.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.crius.core.model.PageData;

/**
 * 分区出票报表统计接口
 * 
 * @ClassName: ProjectReportStandStatService
 * @Description: 项目分区出票统计接口
 * @author zhangbinghong
 * @date 2014-2-27 下午1:58:30
 * 
 */
public interface ProjectReportStandStatService {

	/**
	 * 分区出票统计分页查询
	 * 
	 * @Title: findProjectReportStandStatList
	 * @Description:
	 * @date 2014-2-27 下午2:11:05
	 * @param projectInfoId
	 *            maitix项目Id
	 * @param performIds
	 *            maitix场次Id条件列表
	 * @param pageSize
	 *            每页记录数
	 * @param pageIndex
	 *            页码
	 * @return PageData/ProjectReportStandStatVo对象列表
	 * @throws ApplicationException
	 */
	public PageResultData findProjectReportStandStatList(String source,long projectInfoId,
			List<Long> performIds, int pageSize, int pageIndex)
			throws ApplicationException;
	
	/**
	 * 导出分区Excle
	 * @param pageResult 分区统计数据
	 * @param projectName 项目名称
	 * @throws ApplicationException
	 */
	public ByteArrayOutputStream OutExcel(PageResultData pageResult,String projectName) throws ApplicationException;
	
	/**
	 * 导出分区pdf
	 * @param pageResult 分区统计数据
	 * @param projectName 项目名称
	 * @throws ApplicationException
	 */
	public ByteArrayOutputStream OutPdf(PageResultData pageResult, String projectName) throws ApplicationException;
	
}