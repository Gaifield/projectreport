package cn.damai.boss.projectreport.report.service.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.manager.enums.BooleanEnum;
import cn.damai.boss.projectreport.report.service.UserReportManageService;
import cn.damai.soa.maitix.user.service.OrgUserManagerService;

/**
 * 实现用户管理接口
 * 
 * 创建人：guwei【顾炜】 创建时间：2014-2-27 上午11:46:25
 */
@Service
public class UserReportManageServiceImpl implements UserReportManageService {

	private static final Log log = LogFactory.getLog(UserReportManageServiceImpl.class);

	/**
	 * 调用maitix组织机构用户接口
	 */
	@Resource(name = "soaOrgUserManagerService")
	private OrgUserManagerService orgUserManagerService;

	/**
	 * 验证当前登录用户的旧密码是否正确
	 * 
	 * @param oldPassword
	 * @return
	 * @throws ApplicationException
	 *             创建人：guwei【顾炜】 创建时间：2014-2-27 上午11:48:37
	 */
	@Override
	public boolean validateOldPassword(Long oldPassword) throws ApplicationException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 修改用户密码
	 * 
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * @throws ApplicationException
	 *             创建人：guwei【顾炜】 创建时间：2014-2-27 上午11:48:58
	 */
	@Override
	public boolean modifyUserPassword(long userId, String oldPassword, String newPassword) throws ApplicationException {
		// TODO Auto-generated method stub
		// 修改成功：1；原密码错误：2
		int modifyStatus = orgUserManagerService.modifyUserPassword(userId, oldPassword, newPassword);
		if (modifyStatus == 1) {
			return true;
		} else if (modifyStatus == 2) {
			return false;
		} else {
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), String.valueOf(modifyStatus));
		}
	}

}
