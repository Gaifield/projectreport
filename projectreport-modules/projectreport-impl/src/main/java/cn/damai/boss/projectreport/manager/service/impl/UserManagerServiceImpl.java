package cn.damai.boss.projectreport.manager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.maitix.vo.BusinessUserVo;
import cn.damai.boss.maitix.vo.PageReturnData;
import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.commons.utils.ConfigHelpUtil;
import cn.damai.boss.projectreport.domain.Upt01MaitixUser;
import cn.damai.boss.projectreport.domain.Upt01ReportRole;
import cn.damai.boss.projectreport.manager.context.ManagerUserContextUtil;
import cn.damai.boss.projectreport.manager.dao.MaitixUserDao;
import cn.damai.boss.projectreport.manager.dao.OperatorLogDAO;
import cn.damai.boss.projectreport.manager.dao.ReportRoleDAO;
import cn.damai.boss.projectreport.manager.enums.BusinessUserStatusEnum;
import cn.damai.boss.projectreport.manager.enums.OperatorLogTypeEnum;
import cn.damai.boss.projectreport.manager.service.UserManagerService;
import cn.damai.boss.projectreport.manager.vo.MaitixUserVo;
import cn.damai.component.common.ReturnData;
import cn.damai.component.organization.facade.OrganizationFacade;
import cn.damai.component.organization.facade.OrganizationPersonFacade;
import cn.damai.component.organization.vo.Organization;
import cn.damai.soa.maitix.user.service.OrgUserManagerService;

/**
 * Created by 炜 on 14-2-21.
 */
@Service
public class UserManagerServiceImpl implements UserManagerService {

	private static final Log log = LogFactory.getLog(UserManagerServiceImpl.class);

	@Autowired
	private MaitixUserDao maitixUserDao;
	@Autowired
	private ReportRoleDAO reportRoleDAO;
	/**
	 * 组织机构接口
	 */
	@Resource
	private OrganizationPersonFacade organizationPersonFacade;

	@Resource
	private OrganizationFacade organizationFacade;

	@Resource
	private OperatorLogDAO operatorLogDAO;

	/**
	 * 调用maitix组织机构用户接口
	 */
	@Resource(name = "soaOrgUserManagerService")
	private OrgUserManagerService orgUserManagerService;

	/**
	 * 查找OA组织机构名称
	 * 
	 * @param organizationId
	 * @return
	 */
	/*
	 * private String findOrganizationName(Long organizationId) { //
	 * 根据用户归属大区id查询组织机构信息 ReturnData<Organization> organizationData =
	 * organizationFacade.findOrganizationById(organizationId);
	 * 
	 * if (organizationData != null && organizationData.getResultData() != null)
	 * { return organizationData.getResultData().getOrganizationName(); } return
	 * null; }
	 */

	/**
	 * 根据条件查询用户信息
	 * 
	 * @param userName
	 *            用户名称
	 * @param userRoleId
	 *            用户角色id
	 * @param userStatus
	 *            用户状态id
	 * @param pageSize
	 *            分页大小
	 * @param pageIndex
	 *            页码
	 * @return
	 * @author guwei
	 */
	@Override
	@Transactional("projectReportTransactionManager")
	public PageResultData<MaitixUserVo> findMaitixUserList(String userName, long userRoleId, Short userStatus, int pageSize, int pageIndex)
			throws ApplicationException {
		PageResultData<MaitixUserVo> result = null;
		try {
			Long organizationId = null;
			// 1、判断是否是管理员
			/*
			 * if (ManagerUserContextUtil.getPermissionLevel() !=
			 * PermissionLevelEnum.Admin.getCode()) { // 获取用户 的组织机构id，为null则直接返回
			 * organizationId = getOrganizationId(); if (organizationId == null)
			 * { return null; } }
			 */
			// 2、如果角色不为空则查询maitix组织机构数据
			if (userRoleId <= 0) {
				result = findMaitixOrgUserList(userName, null, userStatus, null, organizationId, pageSize, pageIndex);
			} else {
				// 3、先查询大麦项目报表数据库中的userId
				PageResultData<Upt01MaitixUser> ret = maitixUserDao.queryMaitixUsers(userRoleId, pageSize, pageIndex);

				if (ret != null && ret.getTotal() > 0) {
					List<Long> userIds = new ArrayList<Long>();
					for (Upt01MaitixUser user : ret.getRows()) {
						userIds.add(user.getUserId());
					}
					result = findMaitixOrgUserList(userName, userIds, userStatus, null, organizationId, pageSize, pageIndex);
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
		}
		return result;
	}

	/**
	 * 查询Maitix组织机构库中的用户列表
	 * 
	 * @param loginName
	 *            登陆账号
	 * @param userIds
	 *            maitix用户Id列表
	 * @param businessStatus
	 *            maitix用户状态
	 * @param businessRegion
	 *            所属大区
	 * @param businessBranch
	 *            所属分公司
	 * @param pageSize
	 *            每页大小
	 * @param pageIndex
	 *            每页记录数
	 * @return PageResultData<MaitixUserVo>
	 */
	private PageResultData<MaitixUserVo> findMaitixOrgUserList(String loginName, List<Long> userIds, Short businessStatus, Long businessRegion,
			Long businessBranch, int pageSize, int pageIndex) {
		PageResultData<MaitixUserVo> result = new PageResultData<MaitixUserVo>();

		PageReturnData<BusinessUserVo> pageData = orgUserManagerService.findOrgBusinessUserList(loginName, userIds, businessStatus, businessRegion,
				businessBranch, pageSize, pageIndex);

		if (pageData != null && pageData.getData() != null) {

			List<MaitixUserVo> users = new ArrayList<MaitixUserVo>(pageData.getData().size());

			for (BusinessUserVo bu : pageData.getData()) {
				MaitixUserVo maitixUserVo = new MaitixUserVo();
				maitixUserVo.setUserId(bu.getUserId());
				maitixUserVo.setUserName(bu.getUserName());
				maitixUserVo.setUserStatusId(bu.getBusinessStatus());
				maitixUserVo.setUserStatusName(BusinessUserStatusEnum.getName(bu.getBusinessStatus()));
				maitixUserVo.setUserOrganizationId(bu.getBusinessBranch());
				/*
				 * maitixUserVo.setUserOrganization(findOrganizationName(bu
				 * .getBusinessBranch()));
				 */

				Upt01MaitixUser upt01MaitixUser = maitixUserDao.findByUserId(bu.getUserId());
				if (upt01MaitixUser != null) {
					maitixUserVo.setRoleId(upt01MaitixUser.getUpt01ReportRole().getRoleId());
					maitixUserVo.setRoleName(upt01MaitixUser.getUpt01ReportRole().getRoleName());
				} else {
					maitixUserVo.setRoleId(0L);
					maitixUserVo.setRoleName("");
				}

				users.add(maitixUserVo);
			}

			result.setRows(users);
			result.setTotal(pageData.getTotal());
		}
		return result;
	}

	/**
	 * 根据用户id，用户角色id 修改用户角色
	 * 
	 * @param userIdList
	 *            用户id
	 * @param userRoleId
	 *            用户角色id
	 * @return
	 * @throws ApplicationException
	 */
	@Override
	public boolean modifyUserRole(List<Long> userIdList, long userRoleId) throws ApplicationException {
		boolean bool = false;
		try {
			Upt01ReportRole reportRole = reportRoleDAO.findOne(userRoleId);
			// 验证角色对象不为空
			if (reportRole == null) {
				return false;
			}
			for (Long userId : userIdList) {
				Upt01MaitixUser maitixUser = maitixUserDao.findByUserId(userId);
				// 验证如果用户对象不为空
				if (maitixUser == null) {
					maitixUser = new Upt01MaitixUser();
					maitixUser.setUserId(userId);
					// 获取当前登录用户id
					Long currentUserId = ManagerUserContextUtil.getUserId();
					maitixUser.setCreateUserId(currentUserId);
					maitixUser.setCreateTime(new Date());
					maitixUser.setModifyUserId(currentUserId);
					maitixUser.setModifyTime(new Date());
				}
				maitixUser.setUpt01ReportRole(reportRole);
				maitixUserDao.save(maitixUser);
			}
			bool = true;
			// 若用户角色修改成功，则记录修改日志
			if (bool) {
				recordModifyUserPasswordLog(userIdList, reportRole);
			}
		} catch (Exception e) {
			log.error(e);
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), ConfigHelpUtil.getText("projectreport.maitix.error.sql"));
		}
		return bool;
	}

	/**
	 * 用户修改角色日志记录
	 * 
	 * @param userIdList
	 *            被修改用户id list
	 * @param reportRole
	 *            被修改用户角色
	 * @author：guwei 【顾炜】 2014-3-19 上午10:45:39
	 */
	private void recordModifyUserPasswordLog(List<Long> userIdList, Upt01ReportRole reportRole) {
		try {
			// 1.0 读取当前用户id
			// 获取当前登录用户id
			// Long currentUserId = ManagerUserContextUtil.getUserId();
			long operatorId = ManagerUserContextUtil.getOperatorId();
			// 获取当前登录用户
			String userName = ManagerUserContextUtil.getUserName();
			// 用户角色名称
			String roleName = reportRole.getRoleName();
			int size = userIdList.size();
			// 获取被修改的用户信息
			PageReturnData<BusinessUserVo> pageData = orgUserManagerService.findOrgBusinessUserList(null, userIdList, null, null, null, size, 1);
			// 批量插入日志信息
			if (pageData != null && pageData.getData() != null) {
				for (BusinessUserVo bvo : pageData.getData()) {
					operatorLogDAO.insertOperatorLog(operatorId, userName + "修改" + bvo.getUserName() + "用户角色为" + roleName,
							OperatorLogTypeEnum.MotifyUserRole.getCode());
				}
			}
		} catch (Exception e) {
			log.error("日志记录失败。", e);
		}
	}

	/**
	 * 获取当前登录用户的id
	 * 
	 * @return 用户id，不存在返回null
	 * @author 炜
	 */
	private Long getOrganizationId() {
		// 获取当前登录用户id
		Long currentUserId = ManagerUserContextUtil.getUserId();

		// 验证当前登录用户id为空，返回空
		if (currentUserId == null || currentUserId == 0) {
			return null;
		}
		ReturnData<List<Organization>> organizationUser = organizationPersonFacade.findOrganizationsByPersonId(currentUserId, null, null);
		List<Organization> resultData = organizationUser.getResultData();
		if (organizationUser.getReturnCode() != 200 && resultData == null) {
			return null;
		}

		// 当前用户的组织机构id
		Long organizationId = resultData.get(0).getOrganizationId();
		return organizationId;
	}
}