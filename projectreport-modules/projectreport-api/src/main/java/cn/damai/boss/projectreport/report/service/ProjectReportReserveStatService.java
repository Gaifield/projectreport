package cn.damai.boss.projectreport.report.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.vo.ReserveStatVo;
import cn.damai.crius.core.model.PageData;

/**
 * 预留统计报表接口
 * 
 * @ClassName: ProjectReportReserveStatService
 * @Description: 
 * @author zhangbinghong
 * @date 2014-2-27 下午1:58:30
 * 
 */
public interface ProjectReportReserveStatService {

	/**
	 * 预留统计报表
	 * 
	 * @Title: findProjectReportStandStatList
	 * @Description:
	 * @date 2014-2-27 下午2:11:05
	 * @param projectId
	 *            项目Id
	 * @param performInfoIds
	 *            场次Id条件列表
	 * @return ProjectReserveStatVo对象
	 * @throws ApplicationException
	 */
	public ReserveStatVo findProjectReportReserveStatList(String source,long projectId,
			List<Long> performInfoIds)
			throws ApplicationException;
	
	/**
	 * 导出excel
	 * @param reserveStatVo 预留统计表对象
	 * @param projectName 项目名称
	 * @return
	 * @throws ApplicationException
	 */
	public ByteArrayOutputStream outExcel(ReserveStatVo reserveStatVo,
			String projectName) throws ApplicationException;
			
	/**
	 * 导出Pdf
	 * @param reserveStatVo 预留统计表对象
	 * @param projectName 项目名称
	 * @return
	 * @throws ApplicationException
	 */
	public ByteArrayOutputStream outPdf(ReserveStatVo reserveStatVo,
			String projectName) throws ApplicationException;
}