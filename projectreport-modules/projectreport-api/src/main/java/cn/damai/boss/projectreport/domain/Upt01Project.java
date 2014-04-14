package cn.damai.boss.projectreport.domain;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Upt01Project entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_project")
public class Upt01Project implements java.io.Serializable {

	// Fields

	private Long projectId;
	private Timestamp projectDeadline;
	private Integer taskQuantity;
	private Integer completeTaskQuantity;
	private Short status;
	private Timestamp createTime;
	private Timestamp modifyTime;
	private Set<Upt01ProjectTask> upt01ProjectTasks = new HashSet<Upt01ProjectTask>(0);

	// Constructors

	/** default constructor */
	public Upt01Project() {
	}

	/** minimal constructor */
	public Upt01Project(Long projectId, Integer taskQuantity, Integer completeTaskQuantity, Short status, Timestamp createTime) {
		this.projectId = projectId;
		this.taskQuantity = taskQuantity;
		this.completeTaskQuantity = completeTaskQuantity;
		this.status = status;
		this.createTime = createTime;
	}

	/** full constructor */
	public Upt01Project(Long projectId, Integer taskQuantity, Integer completeTaskQuantity, Short status, Timestamp createTime,
			Timestamp modifyTime, Set<Upt01ProjectTask> upt01ProjectTasks) {
		this.projectId = projectId;
		this.taskQuantity = taskQuantity;
		this.completeTaskQuantity = completeTaskQuantity;
		this.status = status;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.upt01ProjectTasks = upt01ProjectTasks;
	}

	// Property accessors
	@Id
	@Column(name = "project_id", unique = true, nullable = false)
	public Long getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}	
	
	@Column(name = "project_deadline", nullable = false, length = 19)
	public Timestamp getProjectDeadline() {
		return projectDeadline;
	}

	public void setProjectDeadline(Timestamp projectDeadline) {
		this.projectDeadline = projectDeadline;
	}

	@Column(name = "task_quantity", nullable = false)
	public Integer getTaskQuantity() {
		return this.taskQuantity;
	}

	public void setTaskQuantity(Integer taskQuantity) {
		this.taskQuantity = taskQuantity;
	}

	@Column(name = "complete_task_quantity", nullable = false)
	public Integer getCompleteTaskQuantity() {
		return this.completeTaskQuantity;
	}

	public void setCompleteTaskQuantity(Integer completeTaskQuantity) {
		this.completeTaskQuantity = completeTaskQuantity;
	}

	@Column(name = "status", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "modify_time", length = 19)
	public Timestamp getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upt01Project")
	public Set<Upt01ProjectTask> getUpt01ProjectTasks() {
		return this.upt01ProjectTasks;
	}

	public void setUpt01ProjectTasks(Set<Upt01ProjectTask> upt01ProjectTasks) {
		this.upt01ProjectTasks = upt01ProjectTasks;
	}

}