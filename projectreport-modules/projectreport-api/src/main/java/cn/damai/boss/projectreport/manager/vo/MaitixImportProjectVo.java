package cn.damai.boss.projectreport.manager.vo;

import java.util.Date;

/**
 * Maitix 导入的项目Vo实体类
 * @author Administrator
 *
 */
public class MaitixImportProjectVo {
	private long projectId;
	private Date projectDeadline;
	
	public long getProjectId() {
		return projectId;
	}
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	public Date getProjectDeadline() {
		return projectDeadline;
	}
	public void setProjectDeadline(Date projectDeadline) {
		this.projectDeadline = projectDeadline;
	}
}