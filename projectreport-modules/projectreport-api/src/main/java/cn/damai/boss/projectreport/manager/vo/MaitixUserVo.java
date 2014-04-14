package cn.damai.boss.projectreport.manager.vo;

/**
 * maitix 用户vo
 * Created by 炜 on 14-2-21.
 */
public class MaitixUserVo {

    /**
     * 报表系统用户id
     */
    private Long maitixUserId;

    /**
     * maitix用户id
     */
    private Long userId;
    /**
     * maitix用户名称
     */
    private String userName;
    /**
     * 用户归属组织机构id
     */
    private Long userOrganizationId;
    /**
     * 用户归属组织机构名称
     */
    private String userOrganization;
    /**
     * 用户状态id
     */
    private Short userStatusId;
    /**
     * 用户状态名称
     */
    private String userStatusName;
    /**
     * 用户角色id
     */
    private Long roleId;
    /**
     * 用户角色名称
     */
    private String roleName;
    /**
     * 角色类型
     */
    private int roleType;

    public Long getMaitixUserId() {
        return maitixUserId;
    }

    public void setMaitixUserId(Long maitixUserId) {
        this.maitixUserId = maitixUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserOrganizationId() {
        return userOrganizationId;
    }

    public void setUserOrganizationId(Long userOrganizationId) {
        this.userOrganizationId = userOrganizationId;
    }

    public String getUserOrganization() {
        return userOrganization;
    }

    public void setUserOrganization(String userOrganization) {
        this.userOrganization = userOrganization;
    }

    public Short getUserStatusId() {
        return userStatusId;
    }

    public void setUserStatusId(Short userStatusId) {
        this.userStatusId = userStatusId;
    }

    public String getUserStatusName() {
        return userStatusName;
    }

    public void setUserStatusName(String userStatusName) {
        this.userStatusName = userStatusName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }
}
