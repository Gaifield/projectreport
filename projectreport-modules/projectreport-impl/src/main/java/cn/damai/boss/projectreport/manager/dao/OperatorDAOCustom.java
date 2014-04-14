package cn.damai.boss.projectreport.manager.dao;

import cn.damai.boss.projectreport.domain.Upt01Operator;

import java.util.List;

/**
 * 注释：操作员DAO扩展接口
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-20 下午2:24
 */
public interface OperatorDAOCustom
{	
	/**
	 * 查看是否已存在非删除状态下的操作员
	 */
	public List<Upt01Operator> queryOperatorByUserId(Long userId);
	
	/**
	 * 1、查询所有非删除状态下的操作员
	 * 2、且查询结果中不包括执行操作的管理员本身
	 * 3、按创建时间排序，新创建的操作员在上面
	 * @param operatorId
	 * @return
	 */
	public List<Upt01Operator> queryAllOperator(Long operatorId,Short status,Short level);
	
	/**
	 * 1、查询符合非删除状态下的操作员
	 * 2、且查询结果中不包括执行操作的管理员本身
	 * 3、按创建时间排序，新创建的操作员在上面
	 * 4、符合条件的操作员
	 * @param userIds
	 * @param status
	 * @param level
	 * @return
	 */
	public List<Upt01Operator> queryOperatorByStatusAndLevel(List<Long> userIds,Short status,Short level);
}
