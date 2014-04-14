package cn.damai.boss.projectreport.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Upt01Operator entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_operator")
public class Upt01Operator implements java.io.Serializable {

	// Fields

	private Long operatorId;
	private Long userId;
	private Short permissionLevel;
	private Short status;
	private Long createUserId;
	private Date createTime;
	private Long modifyUserId;
	private Date modifyTime;
	private Set<Upt01OperatorLog> upt01OperatorLogs = new HashSet<Upt01OperatorLog>(0);

	// Constructors

	/** default constructor */
	public Upt01Operator() {
	}

	/** minimal constructor */
	public Upt01Operator(Long userId, Short permissionLevel, Short status, Long createUserId, Date createTime) {
		this.userId = userId;
		this.permissionLevel = permissionLevel;
		this.status = status;
		this.createUserId = createUserId;
		this.createTime = createTime;
	}

	/** full constructor */
	public Upt01Operator(Long userId, Short permissionLevel, Short status, Long createUserId, Date createTime,
			Long modifyUserId, Date modifyTime, Set<Upt01OperatorLog> upt01OperatorLogs) {
		this.userId = userId;
		this.permissionLevel = permissionLevel;
		this.status = status;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.modifyUserId = modifyUserId;
		this.modifyTime = modifyTime;
		this.upt01OperatorLogs = upt01OperatorLogs;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "operator_id", unique = true, nullable = false)
	public Long getOperatorId() {
		return this.operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	@Column(name = "user_id", nullable = false)
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "permission_level", nullable = false)
	public Short getPermissionLevel() {
		return this.permissionLevel;
	}

	public void setPermissionLevel(Short permissionLevel) {
		this.permissionLevel = permissionLevel;
	}

	@Column(name = "status", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "create_user_id", nullable = false)
	public Long getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "modify_user_id")
	public Long getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(Long modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "modify_time", length = 19)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upt01Operator")
	public Set<Upt01OperatorLog> getUpt01OperatorLogs() {
		return this.upt01OperatorLogs;
	}

	public void setUpt01OperatorLogs(Set<Upt01OperatorLog> upt01OperatorLogs) {
		this.upt01OperatorLogs = upt01OperatorLogs;
	}

}