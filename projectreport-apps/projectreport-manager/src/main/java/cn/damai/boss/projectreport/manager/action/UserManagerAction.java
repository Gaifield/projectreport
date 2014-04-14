package cn.damai.boss.projectreport.manager.action;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.commons.model.ReturnData;
import cn.damai.boss.projectreport.commons.utils.KeyValueUtils;
import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.manager.action.base.BaseAction;
import cn.damai.boss.projectreport.manager.enums.BooleanEnum;
import cn.damai.boss.projectreport.manager.enums.ReportRoleEnum;
import cn.damai.boss.projectreport.manager.service.RoleManagerService;
import cn.damai.boss.projectreport.manager.service.UserManagerService;
import cn.damai.boss.projectreport.manager.vo.MaitixUserVo;
import cn.damai.boss.projectreport.manager.vo.ReportRoleVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理 Created by 炜 on 14-2-20.
 */
@ParentPackage("json-default")
@Namespace("/")
public class UserManagerAction extends BaseAction {
	private static final Log LOG = LogFactory.getFactory().getInstance(
			UserManagerAction.class);

	@Resource
	private RoleManagerService roleManagerService;
	@Resource
	private UserManagerService userManagerService;
	
	private List list;

	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 用户角色Id
	 */
	private long userRoleId;
	/**
	 * 用户状态Id
	 */
	private Short userStatusId;
	/**
	 * 判断前台获取角色下拉是否有全部
	 */
	private long id;

	private List<ReportRoleVo> roles;
	
	private String userIds;
	
	/**
	 * 跳转用户管理页
	 */
	@Action(value = "userManager", results = { @Result(name = "success", location = "/projectreport/usermanager.jsp") })
	public String userManager() {
		try {
			roles = roleManagerService.queryAllReportRoles();
		} catch (ApplicationException e) {
			LOG.error(e);
		}
		return SUCCESS;
	}

	/**
	 * 获取角色信息列表
	 * 
	 * @return
	 */
	@Action(value = "findRoleList", results = { @Result(type = "json", params = {
			"root", "list", "contentType", "text/html" }) })
	public String findRoleList() {
		List<ReportRoleVo> reportRoleVoList = null;
		try {
			reportRoleVoList = roleManagerService.queryAllReportRoles();
			list = new ArrayList();
			if (id == 0) {
				list.add(new KeyValueUtils(ReportRoleEnum.All.getCode(),
						ReportRoleEnum.All.getName()));
			}
			for (ReportRoleVo reportRoleVo : reportRoleVoList) {
				list.add(new KeyValueUtils(reportRoleVo.getRoleId().toString(),
						reportRoleVo.getRoleName()));
			}
		} catch (ApplicationException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 获取用户列表
	 * 
	 * @return
	 */
	@Action(value = "findUserList", results = { @Result(type = "json", params = {
			"root", "pageData", "contentType", "text/html" }) })
	public String findUserList() {
		try {
			// 判断用户角色是否是全部
			if (userStatusId != null && userStatusId == 0) {
				userStatusId = null;
			}
			PageResultData<MaitixUserVo> pageResultData = userManagerService
					.findMaitixUserList(userName, userRoleId, userStatusId,getRows(),getPage());
			setPageData(pageResultData);
		} catch (ApplicationException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 保存用户角色修改
	 * 
	 * @return
	 */
	@Action(value = "saveUserRole", results = { @Result(type = "json", params = {
			"root", "returnData", "contentType", "text/html" }) })
	public String saveUserRole() {
		ReturnData<Integer> ret = new ReturnData<Integer>();
		try {
			List<Long> userIdList = Utils.convert2IdList(userIds);
			boolean bool = userManagerService.modifyUserRole(userIdList,
					userRoleId);
			ret.setStatus(HttpStatusEnum.Success.getCode());
			ret.setData(BooleanEnum.getCode(bool));
		} catch (ApplicationException e) {
			e.printStackTrace();
			ret.setStatus(HttpStatusEnum.Success.getCode());
			ret.setData(BooleanEnum.FALSE.getCode());
		}
		setReturnData(ret);
		
		return SUCCESS;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Short getUserStatusId() {
		return userStatusId;
	}

	public void setUserStatusId(Short userStatusId) {
		this.userStatusId = userStatusId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<ReportRoleVo> getRoles() {
		return roles;
	}

	public void setRoles(List<ReportRoleVo> roles) {
		this.roles = roles;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}	
}