package cn.damai.boss.projectreport.report.service;

import cn.damai.boss.projectreport.commons.ApplicationException;

/**
 * 报表用户管理服务
 *
 * 创建人：guwei【顾炜】   
 * 创建时间：2014-2-27 上午9:48:04
 */
public interface UserReportManageService {
	
	
	/**
	 * 验证当前登录用户的旧密码是否正确
	 * @param oldPassword
	 * @return
	 * @throws ApplicationException
	 * 创建人：guwei【顾炜】   
	 * 创建时间：2014-2-27 上午11:13:01
	 */
	boolean validateOldPassword(Long oldPassword) throws ApplicationException;
	
	
	/**
	 * 修改用户密码
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * @throws ApplicationException
	 * 创建人：guwei【顾炜】   
	 * 创建时间：2014-2-27 上午11:12:50
	 */
	boolean modifyUserPassword(long userId,String oldPassword,String newPassword) throws ApplicationException;
	
	

}
