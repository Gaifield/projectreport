package cn.damai.boss.projectreport.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Upt01ReportRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_report_role")
public class Upt01ReportRole implements java.io.Serializable {

	// Fields

	private Long roleId;
	private String roleName;
	private Short reportUserType;
	private Short status;
	private Long createUserId;
	private Date createTime;
	private Long modifyUserId;
	private Date modifyTime;
	private List<Upt01MaitixUser> upt01MaitixUsers = new ArrayList<Upt01MaitixUser>();
	private List<Upt01RoleReport> upt01RoleReports = new ArrayList<Upt01RoleReport>();

	// Constructors

	/** default constructor */
	public Upt01ReportRole() {
	}

	/** minimal constructor */
	public Upt01ReportRole(String roleName, Short reportUserType, Short status, Long createUserId, Date createTime) {
		this.roleName = roleName;
		this.reportUserType = reportUserType;
		this.status = status;
		this.createUserId = createUserId;
		this.createTime = createTime;
	}

	/** full constructor */
	public Upt01ReportRole(String roleName, Short reportUserType, Short status, Long createUserId, Date createTime,
			Long modifyUserId, Date modifyTime, List<Upt01MaitixUser> upt01MaitixUsers, List<Upt01RoleReport> upt01RoleReports) {
		this.roleName = roleName;
		this.reportUserType = reportUserType;
		this.status = status;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.modifyUserId = modifyUserId;
		this.modifyTime = modifyTime;
		this.upt01MaitixUsers = upt01MaitixUsers;
		this.upt01RoleReports = upt01RoleReports;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "role_id", unique = true, nullable = false)
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "role_name", nullable = false, length = 50)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "report_user_type", nullable = false)
	public Short getReportUserType() {
		return this.reportUserType;
	}

	public void setReportUserType(Short reportUserType) {
		this.reportUserType = reportUserType;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upt01ReportRole")
	public List<Upt01MaitixUser> getUpt01MaitixUsers() {
		return this.upt01MaitixUsers;
	}

	public void setUpt01MaitixUsers(List<Upt01MaitixUser> upt01MaitixUsers) {
		this.upt01MaitixUsers = upt01MaitixUsers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upt01ReportRole")
	public List<Upt01RoleReport> getUpt01RoleReports() {
		return this.upt01RoleReports;
	}

	public void setUpt01RoleReports(List<Upt01RoleReport> upt01RoleReports) {
		this.upt01RoleReports = upt01RoleReports;
	}

}