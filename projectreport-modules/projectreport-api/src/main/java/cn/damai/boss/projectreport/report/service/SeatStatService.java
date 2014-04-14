package cn.damai.boss.projectreport.report.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.vo.SeatStatVo;

/**
 * 注释：座位汇总service
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-3 下午1:57
 */
public interface SeatStatService {
	
	/**
	 * 根据项目id获取座位汇总信息
	 * @param performIds
	 * @return
	 * @author：guwei 【顾炜】
	 * time：2014-3-3 下午5:29:37
	 */
	public List<SeatStatVo> querySeatStatInfo(List<Long> performIds,Long projectId) throws ApplicationException;
	
	/**
	 * 根据藏瓷idlist 去查询座位汇总信息
	 * @param performIds
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 * @throws ApplicationException
	 * @author：guwei 【顾炜】
	 * time：2014-3-3 下午8:13:09
	 */
	public PageResultData<SeatStatVo> querySeatStatDetail(Long projectInfoId,List<Long> performIds, int pageSize, int pageIndex) throws ApplicationException;
	
	/**
	 * 座位汇总导出excel
	 * @return
	 * @throws ApplicationException
	 */
	public ByteArrayOutputStream outExcel(List<SeatStatVo> seatList,boolean tag) throws ApplicationException;
	
	/**
	 * 座位汇总导出pdf
	 */
	public ByteArrayOutputStream outPdf(List<SeatStatVo> seatList,boolean tag) throws ApplicationException;
}
