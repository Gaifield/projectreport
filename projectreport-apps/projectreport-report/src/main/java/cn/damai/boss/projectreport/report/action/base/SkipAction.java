package cn.damai.boss.projectreport.report.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

/**
 * 纯跳转页面的action
 * 
 * @author guwei
 */
@Namespace("/")
public class SkipAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private String message;

	/**
	 * 跳转登陆页
	 * 
	 * @return
	 */
	@Action(value = "welcome", results = { @Result(name = "success", location = "/projectreport/login.jsp") })
	public String welcome() {
		return SUCCESS;
	}

	/**
	 * 跳转密码修改页面
	 * 
	 * @return 创建人：guwei【顾炜】 创建时间：2014-2-27 上午11:46:25
	 */
	@Action(value = "modifypwd", results = { @Result(name = "success", location = "/projectreport/modifypwd.jsp") })
	public String modifypwd() {
		return SUCCESS;
	}

	/**
	 * 跳转跨项目查询页
	 * 
	 * @return 创建人：guwei【顾炜】 创建时间：2014-2-27 上午11:46:25
	 */
	@Action(value = "querystepproject", results = { @Result(name = "success", location = "/projectreport/stepproject.jsp") })
	public String queryStepProject() {
		return SUCCESS;
	}

	// *************************项目筛选**************************
	@Action(value = "skipToProjectindex", results = { @Result(name = "success", location = "/projectreport/reportindex.jsp") })
	public String skipToProjectindex() {
		return SUCCESS;
	}

	// *****************************提示***********************************************
	@Action(value = "dataError", results = { @Result(name = "success", location = "/projectreport/warning/dataerror.jsp") })
	public String dataError() {
		message = super.getWarnMessage();
		return SUCCESS;
	}

	@Action(value = "requestError", results = { @Result(name = "success", location = "/projectreport/warning/error.jsp") })
	public String requestError() {
		throw new IllegalArgumentException("请求失效，请返回后重新刷新页面！");
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
