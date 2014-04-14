package cn.damai.boss.projectreport.manager.action;

import cn.damai.boss.projectreport.common.service.ContextService;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.commons.model.ReturnData;
import cn.damai.boss.projectreport.manager.action.base.BaseAction;
import cn.damai.boss.projectreport.manager.context.ManagerUserContext;
import cn.damai.boss.projectreport.manager.context.ManagerUserContextUtil;
import cn.damai.boss.projectreport.manager.service.AuthenticateService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 注释：用户认证action
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-20 下午1:24
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
public class AuthenticateAction extends BaseAction {

    @Resource
    private AuthenticateService authenticateService;

    @Resource
    private ContextService contextService;

    //登录名
    private String userName;

    //密码
    private String password;

    /**
     * 用户登录验证
     *
     * @return
     */
    @Action(value = "userLogin", results = {@Result(type = "json", params = {"root", "returnData", "contentType", "text/html"})})
    public String userLogin() {
        ReturnData<ManagerUserContext> returnData = new ReturnData<ManagerUserContext>();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken();
            token.setUsername(userName);
            token.setPassword(password.toCharArray());
            token.setRememberMe(true);
            currentUser.login(token);
            ManagerUserContext managerUserContext = authenticateService.buildUserContext(userName);
            setCookie(managerUserContext);
            returnData.setData(managerUserContext);
            returnData.setStatus(HttpStatusEnum.Success.getCode());
            currentUser.getSession().setAttribute("userName", userName);
        } catch (ApplicationException ae) {
            returnData.setStatus(ae.getErrorCode());
            returnData.setDescription("用户名或密码错误，请重试");
        } catch (AuthenticationException e) {
            returnData.setStatus(HttpStatusEnum.Success.getCode());
            String message = e.getMessage();
            int index = message.indexOf("PROJECTREPORT:");
            if (index < 0) {
                returnData.setDescription("用户名或密码错误，请重试");
            } else {
                returnData.setDescription(message.substring("PROJECTREPORT:".length(), message.length()));
            }
        } finally {
            setReturnData(returnData);
        }
        return SUCCESS;
    }

    /**
     * 用户退出
     *
     * @return
     */
    @Action(value = "userLogOut", results = {@Result(name = "success", location = "/projectreport/login.jsp")})
    public String userLogOut() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        String sessionID = ManagerUserContextUtil.getSessionID();
        if (sessionID != null && sessionID.trim().length() > 0) {
            contextService.remove(sessionID);
        }
        return SUCCESS;
    }


    /**
     * 设置cookie
     *
     * @param managerUserContext
     */
    private void setCookie(ManagerUserContext managerUserContext) {
        HttpServletResponse response = ServletActionContext.getResponse();
        Cookie sessionIDCookie = new Cookie("sessionID", managerUserContext.getSessionID());
        sessionIDCookie.setPath("/");
        response.addCookie(sessionIDCookie);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
