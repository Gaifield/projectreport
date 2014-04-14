package cn.damai.boss.projectreport.manager.service;

import java.util.List;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.vo.OAUserVo;
import cn.damai.boss.projectreport.manager.vo.OperatorVo;


/**
 * 注释：新建操作员service
 * 作者：wenjunrong 【温俊荣】
 * 时间：14-2-20 下午2:50
 */
public interface OperatorService {

	/**
	 * 模糊查询OA用户
	 * @param userName
	 * @return
	 */
	public List<OAUserVo> findUserByUserName(String userName) throws ApplicationException;
	
	
	
	/**
	 * 通过OA用户id查询用户组织机构
	 * @param userId
	 * @return
	 */
	public String findOrganizationByUserId(Long userId) throws ApplicationException;
	
	
	
	/**
	 * 新建操作员
	 * @return
	 */
	public void saveOperator(String userName,Short level,Short status,Long createUserId) throws ApplicationException ;
	
	/**
	 * 更改普通操作员为管理员
	 * @throws ApplicationException
	 */
	public void modifyOperatorLevel(Long operatorId,Short level) throws ApplicationException;
	
	
	/**
	 * 更改操作员状态
	 * @throws ApplicationException
	 */
	public void modifyOperatorStatus(Long operatorId,Short status) throws ApplicationException;

	/**
	 * 1、查询所有非删除状态下的操作员
	 * 2、且查询结果中不包括执行操作的管理员本身
	 * 3、按创建时间排序，新创建的操作员在上面
	 * @throws ApplicationException
	 */
	public List<OperatorVo> findAllOperator(String operatorName,Short status,Short level) throws ApplicationException;
}
