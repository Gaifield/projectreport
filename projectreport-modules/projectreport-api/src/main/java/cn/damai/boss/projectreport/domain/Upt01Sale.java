package cn.damai.boss.projectreport.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Upt01Sale entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_sale", uniqueConstraints = @UniqueConstraint(columnNames = { "project_id",
		"perform_id", "price_id", "sale_date" }))
public class Upt01Sale implements java.io.Serializable {

	// Fields

	private Long saleId;
	private Long projectId;
	private Long performId;
	private Long priceId;
	private Date saleDate;
	private Integer normalSaleQuantity;
	private Double normalSaleAmount;
	private Integer presentQuantity;
	private Double presentAmount;
	private Integer staffQuantity;
	private Double staffAmount;
	private Integer protectQuantity;
	private Double protectAmount;
	private Integer seatStaffQuantity;
	private Integer seatProtectQuantity;
	private Timestamp createTime;
	private Timestamp modifyTime;
	private Set<Upt01SaleDiscount> upt01SaleDiscounts = new HashSet<Upt01SaleDiscount>(0);

	// Constructors

	/** default constructor */
	public Upt01Sale() {
	}

	/** minimal constructor */
	public Upt01Sale(Long projectId, Long performId, Long priceId, Date saleDate, Integer normalSaleQuantity,
			Double normalSaleAmount, Integer presentQuantity, Double presentAmount, Integer staffQuantity, Double staffAmount,
			Integer protectQuantity, Double protectAmount, Integer seatStaffQuantity, Integer seatProtectQuantity,
			Timestamp createTime) {
		this.projectId = projectId;
		this.performId = performId;
		this.priceId = priceId;
		this.saleDate = saleDate;
		this.normalSaleQuantity = normalSaleQuantity;
		this.normalSaleAmount = normalSaleAmount;
		this.presentQuantity = presentQuantity;
		this.presentAmount = presentAmount;
		this.staffQuantity = staffQuantity;
		this.staffAmount = staffAmount;
		this.protectQuantity = protectQuantity;
		this.protectAmount = protectAmount;
		this.seatStaffQuantity = seatStaffQuantity;
		this.seatProtectQuantity = seatProtectQuantity;
		this.createTime = createTime;
	}

	/** full constructor */
	public Upt01Sale(Long projectId, Long performId, Long priceId, Date saleDate, Integer normalSaleQuantity,
			Double normalSaleAmount, Integer presentQuantity, Double presentAmount, Integer staffQuantity, Double staffAmount,
			Integer protectQuantity, Double protectAmount, Integer seatStaffQuantity, Integer seatProtectQuantity,
			Timestamp createTime, Timestamp modifyTime, Set<Upt01SaleDiscount> upt01SaleDiscounts) {
		this.projectId = projectId;
		this.performId = performId;
		this.priceId = priceId;
		this.saleDate = saleDate;
		this.normalSaleQuantity = normalSaleQuantity;
		this.normalSaleAmount = normalSaleAmount;
		this.presentQuantity = presentQuantity;
		this.presentAmount = presentAmount;
		this.staffQuantity = staffQuantity;
		this.staffAmount = staffAmount;
		this.protectQuantity = protectQuantity;
		this.protectAmount = protectAmount;
		this.seatStaffQuantity = seatStaffQuantity;
		this.seatProtectQuantity = seatProtectQuantity;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.upt01SaleDiscounts = upt01SaleDiscounts;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "sale_id", unique = true, nullable = false)
	public Long getSaleId() {
		return this.saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "sale_date", nullable = false, length = 10)
	public Date getSaleDate() {
		return this.saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	@Column(name = "normal_sale_quantity", nullable = false)
	public Integer getNormalSaleQuantity() {
		return this.normalSaleQuantity;
	}

	public void setNormalSaleQuantity(Integer normalSaleQuantity) {
		this.normalSaleQuantity = normalSaleQuantity;
	}

	@Column(name = "normal_sale_amount", nullable = false, precision = 12)
	public Double getNormalSaleAmount() {
		return this.normalSaleAmount;
	}

	public void setNormalSaleAmount(Double normalSaleAmount) {
		this.normalSaleAmount = normalSaleAmount;
	}

	@Column(name = "present_quantity", nullable = false)
	public Integer getPresentQuantity() {
		return this.presentQuantity;
	}

	public void setPresentQuantity(Integer presentQuantity) {
		this.presentQuantity = presentQuantity;
	}

	@Column(name = "present_amount", nullable = false, precision = 12)
	public Double getPresentAmount() {
		return this.presentAmount;
	}

	public void setPresentAmount(Double presentAmount) {
		this.presentAmount = presentAmount;
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

	@Column(name = "seat_staff_quantity", nullable = false)
	public Integer getSeatStaffQuantity() {
		return this.seatStaffQuantity;
	}

	public void setSeatStaffQuantity(Integer seatStaffQuantity) {
		this.seatStaffQuantity = seatStaffQuantity;
	}

	@Column(name = "seat_protect_quantity", nullable = false)
	public Integer getSeatProtectQuantity() {
		return this.seatProtectQuantity;
	}

	public void setSeatProtectQuantity(Integer seatProtectQuantity) {
		this.seatProtectQuantity = seatProtectQuantity;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upt01Sale")
	public Set<Upt01SaleDiscount> getUpt01SaleDiscounts() {
		return this.upt01SaleDiscounts;
	}

	public void setUpt01SaleDiscounts(Set<Upt01SaleDiscount> upt01SaleDiscounts) {
		this.upt01SaleDiscounts = upt01SaleDiscounts;
	}

}