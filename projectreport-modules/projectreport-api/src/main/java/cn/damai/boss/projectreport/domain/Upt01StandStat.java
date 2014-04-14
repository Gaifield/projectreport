package cn.damai.boss.projectreport.domain;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Upt01StandStat entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_stand_stat", uniqueConstraints = @UniqueConstraint(columnNames = { "project_id",
		"perform_id", "stand_id" }))
public class Upt01StandStat implements java.io.Serializable {

	// Fields

	private Long standStatId;
	private Long projectId;
	private Long performId;
	private Long standId;
	private Integer seatQuantity;
	private Integer protectQuantity;
	private Integer saleQuantity;
	private Timestamp createTime;
	private Timestamp modifyTime;

	// Constructors

	/** default constructor */
	public Upt01StandStat() {
	}

	/** minimal constructor */
	public Upt01StandStat(Long projectId, Long performId, Long standId, Integer seatQuantity, Integer protectQuantity,
			Integer saleQuantity, Timestamp createTime) {
		this.projectId = projectId;
		this.performId = performId;
		this.standId = standId;
		this.seatQuantity = seatQuantity;
		this.protectQuantity = protectQuantity;
		this.saleQuantity = saleQuantity;
		this.createTime = createTime;
	}

	/** full constructor */
	public Upt01StandStat(Long projectId, Long performId, Long standId, Integer seatQuantity, Integer protectQuantity,
			Integer saleQuantity, Timestamp createTime, Timestamp modifyTime) {
		this.projectId = projectId;
		this.performId = performId;
		this.standId = standId;
		this.seatQuantity = seatQuantity;
		this.protectQuantity = protectQuantity;
		this.saleQuantity = saleQuantity;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "stand_stat_id", unique = true, nullable = false)
	public Long getStandStatId() {
		return this.standStatId;
	}

	public void setStandStatId(Long standStatId) {
		this.standStatId = standStatId;
	}

	@Column(name = "project_id", nullable = false)
	public Long getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	@Column(name = "perform_id", nullable = false)
	public Long getPerformId() {
		return this.performId;
	}

	public void setPerformId(Long performId) {
		this.performId = performId;
	}

	@Column(name = "stand_id", nullable = false)
	public Long getStandId() {
		return this.standId;
	}

	public void setStandId(Long standId) {
		this.standId = standId;
	}

	@Column(name = "seat_quantity", nullable = false)
	public Integer getSeatQuantity() {
		return this.seatQuantity;
	}

	public void setSeatQuantity(Integer seatQuantity) {
		this.seatQuantity = seatQuantity;
	}

	@Column(name = "protect_quantity", nullable = false)
	public Integer getProtectQuantity() {
		return this.protectQuantity;
	}

	public void setProtectQuantity(Integer protectQuantity) {
		this.protectQuantity = protectQuantity;
	}

	@Column(name = "sale_quantity", nullable = false)
	public Integer getSaleQuantity() {
		return this.saleQuantity;
	}

	public void setSaleQuantity(Integer saleQuantity) {
		this.saleQuantity = saleQuantity;
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

}