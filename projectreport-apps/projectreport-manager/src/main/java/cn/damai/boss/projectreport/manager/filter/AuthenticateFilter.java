package cn.damai.boss.projectreport.manager.filter;

import cn.damai.boss.projectreport.common.service.ContextService;
import cn.damai.boss.projectreport.manager.context.ManagerUserContext;
import cn.damai.boss.projectreport.manager.context.ThreadLocalContext;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 注释：用户登录过滤器
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-26 上午9:43
 */
@Component
public class AuthenticateFilter implements Filter {

    /**
     * 报表上下文service
     */
    @Resource
    private ContextService contextService;

    /**
     * 日志记录
     */
    private static final Log log = LogFactory.getLog(AuthenticateFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String sessionID = getSessionID(request, "sessionID");
            String welcomePath = request.getContextPath() + "/welcome.do";
            if (sessionID == null || sessionID.trim().length() == 0) {
                response.sendRedirect(welcomePath);
                return;
            }
            ManagerUserContext managerUserContext = contextService.getManagerUserContext(sessionID);
            if (managerUserContext != null) {
                ThreadLocalContext.setUserContext(managerUserContext);
                //设置用户名
                HttpSession session = request.getSession();
                Object name = session.getAttribute("userName");
                if (name == null) {
                    String userName = managerUserContext.getUserName();
                    session.setAttribute("userName", userName);
                }
                contextService.delay(sessionID);
                chain.doFilter(request, response);
            } else {
                if (log.isDebugEnabled())
                    log.debug("登录超时，请重新登录");
                response.sendRedirect(welcomePath);
            }
        } catch (AuthenticationException e) {
            if (log.isErrorEnabled())
                log.error("认证出现异常");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }


    /**
     * 得到sessionID的值
     *
     * @param request
     * @return
     */
    private String getSessionID(HttpServletRequest request, String key) {
        Cookie[] cookieArr = request.getCookies();
        if (cookieArr == null || cookieArr.length == 0) {
            return null;
        }
        String sessionID = null;
        for (Cookie cookie : cookieArr) {
            if ("sessionID".equals(cookie.getName()) && "sessionID".equals(key)) {
                sessionID = cookie.getValue();
                break;
            }
        }
        return sessionID;
    }
}
