package cn.damai.boss.projectreport.report.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.utils.KeyValueUtils;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;
import cn.damai.boss.projectreport.report.vo.ReserveStatVo;

/**
 * 跨项目统计报表接口
 * @author Administrator
 *
 */
public interface MultiProjectStatService {
	
	/**
	 * 跨项目汇总报表
	 * @param projectIds 项目Id：来源列表
	 * @return
	 * @throws ApplicationException
	 */
	public List<ReportProjectVo> findMultiProjectStatList(List<KeyValueUtils> projectIds) throws ApplicationException;
	
	/**
	 * 导出excel
	 * @param reserveStatVo 预留统计表对象
	 * @return
	 * @throws ApplicationException
	 */
	public ByteArrayOutputStream outExcel(List<ReportProjectVo> projects) throws ApplicationException;
			
	/**
	 * 导出Pdf
	 * @param reserveStatVo 项目列表
	 * @return
	 * @throws ApplicationException
	 */
	public ByteArrayOutputStream outPdf(List<ReportProjectVo> projects) throws ApplicationException;
}
