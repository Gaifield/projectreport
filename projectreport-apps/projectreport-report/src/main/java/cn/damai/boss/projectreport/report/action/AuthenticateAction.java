package cn.damai.boss.projectreport.report.action;

import cn.damai.boss.projectreport.common.service.ContextService;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.commons.model.ReturnData;
import cn.damai.boss.projectreport.report.action.base.BaseAction;
import cn.damai.boss.projectreport.report.context.ReportUserContext;
import cn.damai.boss.projectreport.report.context.ReportUserContextUtil;
import cn.damai.boss.projectreport.report.service.ReportAuthenticateService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 注释：用户认证action 作者：liutengfei 【刘腾飞】 时间：14-2-20 下午1:24
 */
@Namespace("/")
@Controller
public class AuthenticateAction extends BaseAction {
    private static final Log log = LogFactory.getFactory().getInstance(AuthenticateAction.class);
    @Resource
    private ReportAuthenticateService reportAuthenticateService;

    @Resource
    private ContextService contextService;

    // 登录名
    private String userName;

    // 密码
    private String password;

    /**
     * 用户登录验证
     *
     * @return
     */
    @Action(value = "userLogin", results = {@Result(type = "json", params = {"root", "returnData", "contentType", "text/html"})})
    public String userLogin() {
        ReturnData<ReportUserContext> returnData = new ReturnData<ReportUserContext>();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken();
            token.setUsername(userName);
            token.setPassword(password.toCharArray());
            token.setRememberMe(true);
            currentUser.login(token);
            ReportUserContext reportUserContext = reportAuthenticateService.buildUserContext(userName, password);
            setCookie(reportUserContext);
            returnData.setData(reportUserContext);
            returnData.setStatus(HttpStatusEnum.Success.getCode());
            currentUser.getSession().setAttribute("userName", userName);
        } catch (ApplicationException ae) {
            log.error(ae.getMessage(), ae);
            returnData.setData(null);
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
        String sessionID = ReportUserContextUtil.getSessionID();
        if (sessionID != null && sessionID.trim().length() > 0) {
            contextService.remove(sessionID);
        }
        return SUCCESS;
    }

    /**
     * 设置cookie
     *
     * @param reportUserContext
     */
    private void setCookie(ReportUserContext reportUserContext) {
        HttpServletResponse response = ServletActionContext.getResponse();
        Cookie sessionIDCookie = new Cookie("sessionID", reportUserContext.getSessionID());
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
