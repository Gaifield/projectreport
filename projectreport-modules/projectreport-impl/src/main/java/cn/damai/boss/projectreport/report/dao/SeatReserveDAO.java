package cn.damai.boss.projectreport.report.dao;

import java.util.List;

import cn.damai.boss.projectreport.report.vo.ReserveDetailVo;


/**
 * 座位预留DAO
 * @author Administrator
 *
 */
public interface SeatReserveDAO {
	
	/**
	 * 查询座位预留名称
	 * @param projectId Maitix项目ID
	 * @param performInfoIds Maitix场次ID
	 * @param statType 统计类型：null,1:总预留,2:管理端预留、3:客户端预留
	 * @return
	 */
	public List<ReserveDetailVo> querySeatReserveList(long projectId, List<Long> performInfoIds,short statType);
}