package cn.damai.boss.projectreport.manager.vo;

import java.util.Date;

/**
 * 项目Vo类
 * @author Administrator
 *
 */
public class ProjectVo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5493955981816821119L;
	// Fields

	/**
	 * 项目id
	 */
	private Long projectId;	
	/**
	 * 项目最终截至时间
	 */
	private Date projectDeadline;		
	private Integer taskQuantity;
	private Integer completeTaskQuantity;
	private Short status;
	private Date createTime;
	private Date modifyTime;
	
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}	
	public Date getProjectDeadline() {
		return projectDeadline;
	}
	public void setProjectDeadline(Date projectDeadline) {
		this.projectDeadline = projectDeadline;
	}
	public Integer getTaskQuantity() {
		return taskQuantity;
	}
	public void setTaskQuantity(Integer taskQuantity) {
		this.taskQuantity = taskQuantity;
	}
	public Integer getCompleteTaskQuantity() {
		return completeTaskQuantity;
	}
	public void setCompleteTaskQuantity(Integer completeTaskQuantity) {
		this.completeTaskQuantity = completeTaskQuantity;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}