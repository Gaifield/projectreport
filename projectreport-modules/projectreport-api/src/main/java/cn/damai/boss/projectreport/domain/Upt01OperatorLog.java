package cn.damai.boss.projectreport.domain;

import java.sql.Timestamp;
import java.util.Date;

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
 * Upt01OperatorLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_operator_log")
public class Upt01OperatorLog implements java.io.Serializable {

	// Fields

	private Long logId;
	private Upt01Operator upt01Operator;
	private Integer logType;
	private String content;
	private Date createTime;

	// Constructors

	/** default constructor */
	public Upt01OperatorLog() {
	}

	/** minimal constructor */
	public Upt01OperatorLog(Upt01Operator upt01Operator, Integer logType, Timestamp createTime) {
		this.upt01Operator = upt01Operator;
		this.logType = logType;
		this.createTime = createTime;
	}

	/** full constructor */
	public Upt01OperatorLog(Upt01Operator upt01Operator, Integer logType, String content, Timestamp createTime) {
		this.upt01Operator = upt01Operator;
		this.logType = logType;
		this.content = content;
		this.createTime = createTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "log_id", unique = true, nullable = false)
	public Long getLogId() {
		return this.logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operator_id", nullable = false)
	public Upt01Operator getUpt01Operator() {
		return this.upt01Operator;
	}

	public void setUpt01Operator(Upt01Operator upt01Operator) {
		this.upt01Operator = upt01Operator;
	}

	@Column(name = "log_type", nullable = false)
	public Integer getLogType() {
		return this.logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	@Column(name = "content", length = 500)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}