package cn.damai.boss.projectreport.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Upt01RoleReport entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_role_report")
public class Upt01RoleReport implements java.io.Serializable {

	// Fields

	private Long roleReportId;
	private Upt01ReportRole upt01ReportRole;
	private Upt01Report upt01Report;

	// Constructors

	/** default constructor */
	public Upt01RoleReport() {
	}

	/** full constructor */
	public Upt01RoleReport(Upt01ReportRole upt01ReportRole, Upt01Report upt01Report) {
		this.upt01ReportRole = upt01ReportRole;
		this.upt01Report = upt01Report;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "role_report_id", unique = true, nullable = false)
	public Long getRoleReportId() {
		return this.roleReportId;
	}

	public void setRoleReportId(Long roleReportId) {
		this.roleReportId = roleReportId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Upt01ReportRole getUpt01ReportRole() {
		return this.upt01ReportRole;
	}

	public void setUpt01ReportRole(Upt01ReportRole upt01ReportRole) {
		this.upt01ReportRole = upt01ReportRole;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id", nullable = false)
	public Upt01Report getUpt01Report() {
		return this.upt01Report;
	}

	public void setUpt01Report(Upt01Report upt01Report) {
		this.upt01Report = upt01Report;
	}

}