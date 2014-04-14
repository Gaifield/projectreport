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
 * Upt01SeatStat entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_seat_stat", uniqueConstraints = @UniqueConstraint(columnNames = { "project_id",
		"perform_id", "price_id" }))
public class Upt01SeatStat implements java.io.Serializable {

	// Fields

	private Long seatStatId;
	private Long projectId;
	private Long performId;
	private Long priceId;
	private Integer quantity;
	private Integer seatQuantity;
	private Double seatAmount;
	private Integer staffQuantity;
	private Double staffAmount;
	private Integer protectQuantity;
	private Double protectAmount;
	private Timestamp createTime;
	private Timestamp modifyTime;

	// Constructors

	/** default constructor */
	public Upt01SeatStat() {
	}

	/** minimal constructor */
	public Upt01SeatStat(Long projectId, Long performId, Long priceId, Integer quantity, Integer seatQuantity, Double seatAmount,
			Integer staffQuantity, Double staffAmount, Integer protectQuantity, Double protectAmount, Timestamp createTime) {
		this.projectId = projectId;
		this.performId = performId;
		this.priceId = priceId;
		this.quantity = quantity;
		this.seatQuantity = seatQuantity;
		this.seatAmount = seatAmount;
		this.staffQuantity = staffQuantity;
		this.staffAmount = staffAmount;
		this.protectQuantity = protectQuantity;
		this.protectAmount = protectAmount;
		this.createTime = createTime;
	}

	/** full constructor */
	public Upt01SeatStat(Long projectId, Long performId, Long priceId, Integer quantity, Integer seatQuantity, Double seatAmount,
			Integer staffQuantity, Double staffAmount, Integer protectQuantity, Double protectAmount, Timestamp createTime,
			Timestamp modifyTime) {
		this.projectId = projectId;
		this.performId = performId;
		this.priceId = priceId;
		this.quantity = quantity;
		this.seatQuantity = seatQuantity;
		this.seatAmount = seatAmount;
		this.staffQuantity = staffQuantity;
		this.staffAmount = staffAmount;
		this.protectQuantity = protectQuantity;
		this.protectAmount = protectAmount;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "seat_stat_id", unique = true, nullable = false)
	public Long getSeatStatId() {
		return this.seatStatId;
	}

	public void setSeatStatId(Long seatStatId) {
		this.seatStatId = seatStatId;
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

	@Column(name = "quantity", nullable = false)
	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(name = "seat_quantity", nullable = false)
	public Integer getSeatQuantity() {
		return this.seatQuantity;
	}

	public void setSeatQuantity(Integer seatQuantity) {
		this.seatQuantity = seatQuantity;
	}

	@Column(name = "seat_amount", nullable = false, precision = 12)
	public Double getSeatAmount() {
		return this.seatAmount;
	}

	public void setSeatAmount(Double seatAmount) {
		this.seatAmount = seatAmount;
	}

	@Column(name = "staff_quantity", nullable = false)
	public Integer getStaffQuantity() {
		return this.staffQuantity;
	}

	public void setStaffQuantity(Integer staffQuantity) {
		this.staffQuantity = staffQuantity;
	}

	@Column(name = "staff_amount", nullable = false, precision = 12)
	public Double getStaffAmount() {
		return this.staffAmount;
	}

	public void setStaffAmount(Double staffAmount) {
		this.staffAmount = staffAmount;
	}

	@Column(name = "protect_quantity", nullable = false)
	public Integer getProtectQuantity() {
		return this.protectQuantity;
	}

	public void setProtectQuantity(Integer protectQuantity) {
		this.protectQuantity = protectQuantity;
	}

	@Column(name = "protect_amount", nullable = false, precision = 12)
	public Double getProtectAmount() {
		return this.protectAmount;
	}

	public void setProtectAmount(Double protectAmount) {
		this.protectAmount = protectAmount;
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