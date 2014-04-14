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
 * Upt01ReserveStat entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_reserve_stat", uniqueConstraints = @UniqueConstraint(columnNames = { "project_id",
		"perform_id", "price_id" }))
public class Upt01ReserveStat implements java.io.Serializable {

	// Fields

	private Long reserveStatId;
	private Long projectId;
	private Long performId;
	private Long priceId;
	private Short reserveType;
	private Long reserveLvel;
	private Integer quantity;
	private Timestamp createTime;
	private Timestamp modifyTime;

	// Constructors

	/** default constructor */
	public Upt01ReserveStat() {
	}

	/** minimal constructor */
	public Upt01ReserveStat(Long projectId, Long performId, Long priceId, Short reserveType, Long reserveLvel, Integer quantity,
			Timestamp createTime) {
		this.projectId = projectId;
		this.performId = performId;
		this.priceId = priceId;
		this.reserveType = reserveType;
		this.reserveLvel = reserveLvel;
		this.quantity = quantity;
		this.createTime = createTime;
	}

	/** full constructor */
	public Upt01ReserveStat(Long projectId, Long performId, Long priceId, Short reserveType, Long reserveLvel, Integer quantity,
			Timestamp createTime, Timestamp modifyTime) {
		this.projectId = projectId;
		this.performId = performId;
		this.priceId = priceId;
		this.reserveType = reserveType;
		this.reserveLvel = reserveLvel;
		this.quantity = quantity;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "reserve_stat_id", unique = true, nullable = false)
	public Long getReserveStatId() {
		return this.reserveStatId;
	}

	public void setReserveStatId(Long reserveStatId) {
		this.reserveStatId = reserveStatId;
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

	@Column(name = "price_id", nullable = false)
	public Long getPriceId() {
		return this.priceId;
	}

	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}

	@Column(name = "reserve_type", nullable = false)
	public Short getReserveType() {
		return this.reserveType;
	}

	public void setReserveType(Short reserveType) {
		this.reserveType = reserveType;
	}

	@Column(name = "reserve_lvel", nullable = false)
	public Long getReserveLvel() {
		return this.reserveLvel;
	}

	public void setReserveLvel(Long reserveLvel) {
		this.reserveLvel = reserveLvel;
	}

	@Column(name = "quantity", nullable = false)
	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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