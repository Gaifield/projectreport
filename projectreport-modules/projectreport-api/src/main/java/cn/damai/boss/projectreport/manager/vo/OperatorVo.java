package cn.damai.boss.projectreport.manager.vo;

import java.util.Date;

/**
 * 注释：操作员管理与前台交互
 * 作者：wenjunrong 【温俊荣】
 * 时间：14-2-24 下午1:33
 */
public class OperatorVo {
	private Long operatorId;       //操作员ID
	private Long userId;           //OA用户ID
	private Short permissionLevel; //操作员权限级别
	private String permissionLevelName;//操作员权限级别
	private Short status;          //操作员状态
	private String statusName;     //操作员状态名称
	private Date createTime;       //操作员创建时间
	private String createTimeFormat;//操作员创建时间格式化成：2014-02-27 10:28:00
	private String operatorName;   //操作员名称
	private String operatorDept;   //操作员组织机构
	private Short reverseStatus;   //相反状态
	private String reverseStatusName; //相反状态名称
	
	public OperatorVo(){
		
	}
	
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Short getPermissionLevel() {
		return permissionLevel;
	}
	public void setPermissionLevel(Short permissionLevel) {
		this.permissionLevel = permissionLevel;
	}
	public String getPermissionLevelName() {
		return permissionLevelName;
	}
	public void setPermissionLevelName(String permissionLevelName) {
		this.permissionLevelName = permissionLevelName;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getOperatorDept() {
		return operatorDept;
	}
	public void setOperatorDept(String operatorDept) {
		this.operatorDept = operatorDept;
	}

	public Short getReverseStatus() {
		return reverseStatus;
	}

	public void setReverseStatus(Short reverseStatus) {
		this.reverseStatus = reverseStatus;
	}

	public String getReverseStatusName() {
		return reverseStatusName;
	}

	public void setReverseStatusName(String reverseStatusName) {
		this.reverseStatusName = reverseStatusName;
	}

	public String getCreateTimeFormat() {
		return createTimeFormat;
	}

	public void setCreateTimeFormat(String createTimeFormat) {
		this.createTimeFormat = createTimeFormat;
	}
	
	
	
}
