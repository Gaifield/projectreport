package cn.damai.boss.projectreport.report.action;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.commons.model.ReturnData;
import cn.damai.boss.projectreport.manager.enums.BooleanEnum;
import cn.damai.boss.projectreport.report.action.base.BaseAction;
import cn.damai.boss.projectreport.report.context.ReportUserContextUtil;
import cn.damai.boss.projectreport.report.service.UserReportManageService;

/**
 * 报表前台用户信息管理
 * 
 * 创建人：guwei【顾炜】 创建时间：2014-2-26 下午7:46:48
 */

@Namespace("/")
public class UserReportAction extends BaseAction {
	private static final Log log = LogFactory.getFactory().getInstance(UserReportAction.class);
	/**
	 * 旧密码
	 */
	private String oldPassword;
	/**
	 * 新密码
	 */
	private String newPassword;

	@Resource
	private UserReportManageService userReportManageService;

	/**
	 * 用户密码修改方法调用
	 * 
	 * @return
	 * 
	 *         创建人：guwei【顾炜】 创建时间：2014-2-27 上午11:46:25
	 */
	@Action(value = "modifyPassword", results = { @Result(type = "json", params = { "root", "returnData", "contentType", "text/html" }) })
	public String modifyPassword() {
		ReturnData<String> returnData = new ReturnData<String>();
		try {
			// 获取用户id
			Long currentUserId = ReportUserContextUtil.getMaitixBusinessUserId();
			if (currentUserId != null) {
				// 若旧密码数据正确，修改密码
				boolean mdfBool = userReportManageService.modifyUserPassword(currentUserId, oldPassword, newPassword);
				returnData.setStatus(BooleanEnum.getCode(mdfBool));
				returnData.setData(mdfBool ? "1" : "2");
				setReturnData(returnData);
			} else {
				returnData.setStatus(HttpStatusEnum.ClientError.getCode());
				returnData.setData("用户id不存在，请重新登录后修改密码！");
				setReturnData(returnData);

			}
		} catch (ApplicationException e) {
			log.error(e.getMessage(), e);
			returnData.setStatus(e.getErrorCode());
			returnData.setData(e.getMessage());
			setReturnData(returnData);
		}
		return SUCCESS;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
