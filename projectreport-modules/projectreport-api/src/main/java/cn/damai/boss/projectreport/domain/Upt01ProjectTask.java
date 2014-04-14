package cn.damai.boss.projectreport.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Upt01ProjectTask entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_project_task")
public class Upt01ProjectTask implements java.io.Serializable {

	// Fields

	private Long taskId;
	private Upt01Project upt01Project;
	private Short taskType;
	private Short taskStatus;
	private Timestamp createTime;
	private Timestamp completeTime;

	// Constructors

	/** default constructor */
	public Upt01ProjectTask() {
	}

	/** minimal constructor */
	public Upt01ProjectTask(Long taskId, Upt01Project upt01Project, Short taskType, Short taskStatus, Timestamp createTime) {
		this.taskId = taskId;
		this.upt01Project = upt01Project;
		this.taskType = taskType;
		this.taskStatus = taskStatus;
		this.createTime = createTime;
	}

	/** full constructor */
	public Upt01ProjectTask(Long taskId, Upt01Project upt01Project, Short taskType, Short taskStatus, Timestamp createTime,
			Timestamp completeTime) {
		this.taskId = taskId;
		this.upt01Project = upt01Project;
		this.taskType = taskType;
		this.taskStatus = taskStatus;
		this.createTime = createTime;
		this.completeTime = completeTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "task_id", unique = true, nullable = false)
	public Long getTaskId() {
		return this.taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	public Upt01Project getUpt01Project() {
		return this.upt01Project;
	}

	public void setUpt01Project(Upt01Project upt01Project) {
		this.upt01Project = upt01Project;
	}

	@Column(name = "task_type", nullable = false)
	public Short getTaskType() {
		return this.taskType;
	}

	public void setTaskType(Short taskType) {
		this.taskType = taskType;
	}

	@Column(name = "task_status", nullable = false)
	public Short getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(Short taskStatus) {
		this.taskStatus = taskStatus;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "complete_time", length = 19)
	public Timestamp getCompleteTime() {
		return this.completeTime;
	}

	public void setCompleteTime(Timestamp completeTime) {
		this.completeTime = completeTime;
	}

}