package cn.damai.boss.projectreport.report.filter;

import cn.damai.boss.maitix.vo.BusinessUserVo;
import cn.damai.boss.projectreport.manager.vo.MaitixUserVo;
import cn.damai.boss.projectreport.report.dao.ReportMaitixUserDAO;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.soa.maitix.user.service.OrgUserManagerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ReportAuthorizingRealm extends AuthenticatingRealm {
    private static final Log log = LogFactory.getFactory().getInstance(ReportAuthorizingRealm.class);
    /**
     * maitix dubbo服务接口
     */
    @Resource
    private OrgUserManagerService orgUserManagerService;

    /**
     * maitix主办方dao
     */
    @Resource
    private ReportMaitixUserDAO reportMaitixUserDAO;

    /**
     * 用户认证登录
     *
     * @param authenticationToken
     * @return
     * @throws org.apache.shiro.authc.AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        char[] password = token.getPassword();
        List<BusinessUserVo> businessUserVoList = orgUserManagerService.validateUserLogining(userName, new String(password));
        if (businessUserVoList != null && businessUserVoList.size() != 0) {
            DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.Report.getCodeStr());
            long userId = businessUserVoList.get(0).getUserId();
            MaitixUserVo maitixUserVo = reportMaitixUserDAO.findByUserId(userId);
            if (maitixUserVo == null) {
                throw new AuthenticationException("PROJECTREPORT:还未开通报表权限，请联系管理员");
            }
            return new SimpleAuthenticationInfo(token.getPrincipal(), token.getPassword(), token.getUsername());
        } else {
            throw new AuthenticationException("PROJECTREPORT:用户名或密码错误，请重试");
        }
    }
}