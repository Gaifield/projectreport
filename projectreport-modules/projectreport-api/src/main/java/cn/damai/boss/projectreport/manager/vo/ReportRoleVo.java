package cn.damai.boss.projectreport.manager.vo;

import java.util.Date;
import java.util.List;

/**
 * 角色vo
 * Created by 炜 on 14-2-20.
 */
public class ReportRoleVo {

    //角色id
    private Long roleId;
    //角色名称
    private String roleName;
    //角色类型  1:主办、2：运营、3：财务、4：公安
    private Short reportUserType;
    private String reportUserTypeName;
    //状态    1:正常；2：删除
    private Short status;
    private Long createUserId;
    private String createUserName;
    private Date createTime;
    private Long modifyUserId;
    private String modifyUserName;
    private Date modifyTime;
    //所有报表数据
    private List<ReportVo> reportVoList;
    //用户勾选的报表
    private String reportVoIds;

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

    public Short getReportUserType() {
        return reportUserType;
    }

    public void setReportUserType(Short reportUserType) {
        this.reportUserType = reportUserType;
    }

    public String getReportUserTypeName() {
        return reportUserTypeName;
    }

    public void setReportUserTypeName(String reportUserTypeName) {
        this.reportUserTypeName = reportUserTypeName;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Long modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public List<ReportVo> getReportVoList() {
        return reportVoList;
    }

    public void setReportVoList(List<ReportVo> reportVoList) {
        this.reportVoList = reportVoList;
    }

    public String getReportVoIds() {
        return reportVoIds;
    }

    public void setReportVoIds(String reportVoIds) {
        this.reportVoIds = reportVoIds;
    }
}
