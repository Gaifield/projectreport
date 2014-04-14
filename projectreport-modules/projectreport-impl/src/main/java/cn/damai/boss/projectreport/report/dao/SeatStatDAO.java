package cn.damai.boss.projectreport.report.dao;

import java.util.List;

import cn.damai.boss.projectreport.report.vo.SeatStatVo;

/**
 * 注释：座位汇总DAO 作者：liutengfei 【刘腾飞】 时间：14-3-3 下午1:58
 */
public interface SeatStatDAO {

	/**
	 * 根据项目id查询座位汇总信息
	 * 
	 * @param performIds
	 *            项目id
	 * @return
	 * @author：guwei 【顾炜】 time：2014-3-3 下午4:05:33
	 */
	public List<SeatStatVo> findSeatStatInfo(Long projectId ,List<Long> performIds);
	
	public List<SeatStatVo> queryPromotionSeatInfo(Long projectId,List<Long> performIds);

	/**
	 * 根据场次ids 查询座位汇总信息
	 * 
	 * @param performIds
	 *            场次ids
	 * @return
	 * @author：guwei 【顾炜】 time：2014-3-3 下午7:05:33
	 */
	public List<SeatStatVo> findSeatStatDetail(List<Long> performIds);

	/**
	 * 套票
	 * 
	 * @param performIds
	 * @return
	 * @author：guwei 【顾炜】 2014-3-18 下午7:58:16
	 */
	public List<SeatStatVo> queryPromotionSeatDetail(List<Long> performIds);
}
