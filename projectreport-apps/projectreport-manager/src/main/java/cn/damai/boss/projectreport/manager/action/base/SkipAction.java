package cn.damai.boss.projectreport.manager.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;


/**
 * 纯跳转页面的action
 *
 * @author guwei
 */
@ParentPackage("json-default")
@Namespace("/")
public class SkipAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    /**
     * 跳转登陆页
     *
     * @return
     */
    @Action(value = "welcome", results = {@Result(name = "success", location = "/projectreport/login.jsp")})
    public String welcome() {
        return SUCCESS;
    }



    /**
     * 跳转新建操作员
     */
    @Action(value = "skitOperator", results = {@Result(name = "success", location = "/projectreport/operatorcreate.jsp")})
    public String skitOperator() {
        return SUCCESS;
    }

    //********************角色管理************************

    /**
     * 跳转到“角色管理”界面
     *
     * @return
     */
    @Action(value = "skipToRoleManager", results = {@Result(name = "success", location = "/projectreport/rolemanager.jsp")})
    public String skipToRoleManager() {
        return SUCCESS;
    }
}
