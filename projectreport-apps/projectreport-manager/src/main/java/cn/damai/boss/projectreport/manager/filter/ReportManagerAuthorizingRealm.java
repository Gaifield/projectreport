package cn.damai.boss.projectreport.manager.filter;

import cn.damai.boss.projectreport.domain.Upt01Operator;
import cn.damai.boss.projectreport.manager.context.ManagerUserContextUtil;
import cn.damai.boss.projectreport.manager.dao.OperatorDAO;
import cn.damai.boss.projectreport.manager.enums.OperatorStatusEnum;
import cn.damai.component.common.ReturnData;
import cn.damai.component.user.facade.UserFacade;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ReportManagerAuthorizingRealm extends AuthorizingRealm {
    /**
     * 组织机构接口
     */
    @Resource
    private UserFacade userFacade;

    /**
     * 操作员DAO
     */
    @Resource
    private OperatorDAO operatorDAO;


    /**
     * 设置用户权限
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        int permissionLevel = ManagerUserContextUtil.getPermissionLevel();
        String roleName = permissionLevel == 1 ? "operator" : "admin";
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(roleName);
        return info;
    }

    /**
     * 用户认证登录
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        char[] password = token.getPassword();
        String md5Password = new String(DigestUtils.md5Hex("~!@#" + new String(password)));
        ReturnData<Long> returnData = userFacade.checkLoginByUserNameAndUserPassword(userName, md5Password);
        Long userId = returnData.getResultData();
        if (userId != null && userId != 0) {
            Upt01Operator operator = operatorDAO.findByUserId(userId);
            if (operator.getStatus() == OperatorStatusEnum.Enable.getCode()) {
                return new SimpleAuthenticationInfo(token.getPrincipal(), token.getPassword(), token.getUsername());
            } else {
                throw new AuthenticationException("PROJECTREPORT:用户被禁用或删除");
            }
        } else {
            throw new AuthenticationException("PROJECTREPORT:用户名或密码错误，请重试");
        }
    }
}