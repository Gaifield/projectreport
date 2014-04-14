package cn.damai.boss.projectreport.manager.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.stereotype.Controller;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.commons.model.ReturnData;
import cn.damai.boss.projectreport.manager.action.base.BaseAction;
import cn.damai.boss.projectreport.manager.context.ManagerUserContextUtil;
import cn.damai.boss.projectreport.manager.service.OperatorService;
import cn.damai.boss.projectreport.manager.vo.OAUserVo;
import cn.damai.boss.projectreport.manager.vo.OperatorVo;

/**
 * 注释：操作员/管理员Action 作者：wenjunrong 【温俊荣】 时间：14-2-20 上午10:45
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
public class OperatorAction extends BaseAction {
	private String userName; // OA用户名
	private Long userId; // OA用户id
	private List<OAUserVo> oaUsers = null;
	private short level; // 权限级别 1：操作员 2：管理员
	private Long operatorId; // 操作员id
	private short status; // 操作员状态
	@Resource
	private OperatorService operatorService;

	/**
	 * 模糊查询OA用户
	 */
	@Action(value = "findOperatorsByKeyword", results = { @Result(name = "success", type = "json", params = { "root", "oaUsers", "contentType",
			"text/html" }) })
	public String findOperatorsByKeyword() {
		try {
			if (userName != null && !"".equals(userName)) {
				userName = userName.trim();
				oaUsers = operatorService.findUserByUserName(userName);
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 新建操作员
	 */
	@Action(value = "saveOperator", results = { @Result(name = "success", type = "json", params = { "root", "returnData", "contentType", "text/html" }) })
	public String saveOperator() {
		ReturnData<String> returnData = new ReturnData<String>();
		try {
			operatorService.saveOperator(userName, level, (short) 1, ManagerUserContextUtil.getUserId());
			returnData.setStatus(HttpStatusEnum.Success.getCode());
			returnData.setData(HttpStatusEnum.Success.getName());
			setReturnData(returnData);
		} catch (ApplicationException ex) {
			returnData.setStatus(ex.getErrorCode());
			returnData.setData(ex.getLocalizedMessage());
			setReturnData(returnData);
		} catch (Exception ex) {
			returnData.setStatus(HttpStatusEnum.ServerError.getCode());
			returnData.setData(ex.getLocalizedMessage());
			setReturnData(returnData);
		}
		return SUCCESS;
	}

	/**
	 * 跳转至操作员管理，查询操作员（非删除状态）
	 */
	@Action(value = "operatorManage", results = { @Result(name = "success", location = "/projectreport/operatormanager.jsp") })
	public String operatorManage() {
		return SUCCESS;
	}

	/**
	 * 加载操作员列表
	 * 
	 * @return
	 */

	@Action(value = "showAllOperator", results = { @Result(name = "success", type = "json", params = { "root", "pageData", "contentType", "text/html" }) })
	public String showAllOperator() {

		try {
			PageResultData<OperatorVo> pageData = new PageResultData<OperatorVo>();
			if (userName != null && !"".equals(userName)) {
				userName = userName.trim();
			}
			List<OperatorVo> operatorVos = operatorService.findAllOperator(userName, status, level);
			;
			if (operatorVos != null && operatorVos.size() != 0) {
				pageData.setRows(getPagerList(operatorVos));
				pageData.setTotal(operatorVos.size());
				setPageData(pageData);
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 更改操作员权限级别
	 * 
	 * @return
	 */
	@Action(value = "updateLevel", results = { @Result(name = "success", type = "json", params = { "root", "returnData", "contentType", "text/html" }) })
	public String updateLevel() {
		ReturnData<String> returnData = new ReturnData<String>();
		try {
			operatorService.modifyOperatorLevel(operatorId, level);
			returnData.setStatus(HttpStatusEnum.Success.getCode());
			returnData.setData(HttpStatusEnum.Success.getName());
			setReturnData(returnData);
		} catch (ApplicationException ex) {
			returnData.setStatus(ex.getErrorCode());
			returnData.setData(ex.getMessage());
			setReturnData(returnData);
		}
		return SUCCESS;
	}

	/**
	 * 更改操作员状态
	 * 
	 * @return
	 */
	@Action(value = "updateStatus", results = { @Result(name = "success", type = "json", params = { "root", "returnData", "contentType", "text/html" }) })
	public String updateStatus() {
		ReturnData<String> returnData = new ReturnData<String>();
		try {
			operatorService.modifyOperatorStatus(operatorId, status);
			returnData.setStatus(HttpStatusEnum.Success.getCode());
			returnData.setData(HttpStatusEnum.Success.getName());
			setReturnData(returnData);
		} catch (ApplicationException ex) {
			returnData.setStatus(ex.getErrorCode());
			;
			returnData.setData(ex.getMessage());
			setReturnData(returnData);
		}
		return SUCCESS;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<OAUserVo> getOaUsers() {
		return oaUsers;
	}

	public void setOaUsers(List<OAUserVo> oaUsers) {
		this.oaUsers = oaUsers;
	}

	public Short getLevel() {
		return level;
	}

	public void setLevel(Short level) {
		this.level = level;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

}
