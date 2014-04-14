package cn.damai.boss.projectreport.domain;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Upt01MaitixUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_maitix_user")
public class Upt01MaitixUser implements java.io.Serializable {

	// Fields

	private Long maitixUserId;
	private Upt01ReportRole upt01ReportRole;
	private Long userId;
	private Long createUserId;
	private Date createTime;
	private Long modifyUserId;
	private Date modifyTime;

	// Constructors

	/** default constructor */
	public Upt01MaitixUser() {
	}

	/** minimal constructor */
	public Upt01MaitixUser(Long userId, Long createUserId, Date createTime) {
		this.userId = userId;
		this.createUserId = createUserId;
		this.createTime = createTime;
	}

	/** full constructor */
	public Upt01MaitixUser(Upt01ReportRole upt01ReportRole, Long userId, Long createUserId, Date createTime,
			Long modifyUserId, Date modifyTime) {
		this.upt01ReportRole = upt01ReportRole;
		this.userId = userId;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.modifyUserId = modifyUserId;
		this.modifyTime = modifyTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "maitix_user_id", unique = true, nullable = false)
	public Long getMaitixUserId() {
		return this.maitixUserId;
	}

	public void setMaitixUserId(Long maitixUserId) {
		this.maitixUserId = maitixUserId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	public Upt01ReportRole getUpt01ReportRole() {
		return this.upt01ReportRole;
	}

	public void setUpt01ReportRole(Upt01ReportRole upt01ReportRole) {
		this.upt01ReportRole = upt01ReportRole;
	}

	@Column(name = "user_id", nullable = false)
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

}