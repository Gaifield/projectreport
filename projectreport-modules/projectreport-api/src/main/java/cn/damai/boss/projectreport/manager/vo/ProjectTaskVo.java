package cn.damai.boss.projectreport.manager.vo;

import java.util.Date;

/**
 * 项目统计任务Vo类
 * @author Administrator
 *
 */
public class ProjectTaskVo implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long projectId;
	private Long taskId;
	private Short taskType;
	private Short taskStatus;
	private Date createTime;
	private Date completeTime;
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Short getTaskType() {
		return taskType;
	}
	public void setTaskType(Short taskType) {
		this.taskType = taskType;
	}
	public Short getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(Short taskStatus) {
		this.taskStatus = taskStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	
	
}