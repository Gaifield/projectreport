package cn.damai.boss.projectreport.report.dao;

import java.util.List;

import cn.damai.boss.projectreport.report.vo.ReserveTyleVo;

/**
 * 预留级别DAO
 * @author Administrator
 *
 */
public interface ReserveDAO {
	
	/**
	 * 查询项目预留级别列表
	 * @param projectId 项目Id
	 * @return
	 */
	public List<ReserveTyleVo> queryProjectReserveTyleList(long projectId);
}
