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
 * Upt01SellerSale entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_seller_sale", uniqueConstraints = @UniqueConstraint(columnNames = { "project_id",
		"perform_id", "price_id", "seller_id", "sale_date" }))
public class Upt01SellerSale implements java.io.Serializable {

	// Fields

	private Long sellerSaleId;
	private Long projectId;
	private Long performId;
	private Long priceId;
	private Long sellerId;
	private Date saleDate;
	private Integer fullPriceQuantity;
	private Double fullPriceAmount;
	private Integer presentQuantity;
	private Double presentAmount;
	private Integer staffQuantity;
	private Double staffAmount;
	private Timestamp createTime;
	private Timestamp modifyTime;
	private Set<Upt01SellerSaleDiscount> upt01SellerSaleDiscounts = new HashSet<Upt01SellerSaleDiscount>(0);

	// Constructors

	/** default constructor */
	public Upt01SellerSale() {
	}

	/** minimal constructor */
	public Upt01SellerSale(Long projectId, Long performId, Long priceId, Long sellerId, Date saleDate, Integer fullPriceQuantity,
			Double fullPriceAmount, Integer presentQuantity, Double presentAmount, Integer staffQuantity, Double staffAmount,
			Timestamp createTime) {
		this.projectId = projectId;
		this.performId = performId;
		this.priceId = priceId;
		this.sellerId = sellerId;
		this.saleDate = saleDate;
		this.fullPriceQuantity = fullPriceQuantity;
		this.fullPriceAmount = fullPriceAmount;
		this.presentQuantity = presentQuantity;
		this.presentAmount = presentAmount;
		this.staffQuantity = staffQuantity;
		this.staffAmount = staffAmount;
		this.createTime = createTime;
	}

	/** full constructor */
	public Upt01SellerSale(Long projectId, Long performId, Long priceId, Long sellerId, Date saleDate, Integer fullPriceQuantity,
			Double fullPriceAmount, Integer presentQuantity, Double presentAmount, Integer staffQuantity, Double staffAmount,
			Timestamp createTime, Timestamp modifyTime, Set<Upt01SellerSaleDiscount> upt01SellerSaleDiscounts) {
		this.projectId = projectId;
		this.performId = performId;
		this.priceId = priceId;
		this.sellerId = sellerId;
		this.saleDate = saleDate;
		this.fullPriceQuantity = fullPriceQuantity;
		this.fullPriceAmount = fullPriceAmount;
		this.presentQuantity = presentQuantity;
		this.presentAmount = presentAmount;
		this.staffQuantity = staffQuantity;
		this.staffAmount = staffAmount;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.upt01SellerSaleDiscounts = upt01SellerSaleDiscounts;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "seller_sale_id", unique = true, nullable = false)
	public Long getSellerSaleId() {
		return this.sellerSaleId;
	}

	public void setSellerSaleId(Long sellerSaleId) {
		this.sellerSaleId = sellerSaleId;
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

	@Column(name = "seller_id", nullable = false)
	public Long getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "sale_date", nullable = false, length = 10)
	public Date getSaleDate() {
		return this.saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	@Column(name = "full_price_quantity", nullable = false)
	public Integer getFullPriceQuantity() {
		return this.fullPriceQuantity;
	}

	public void setFullPriceQuantity(Integer fullPriceQuantity) {
		this.fullPriceQuantity = fullPriceQuantity;
	}

	@Column(name = "full_price_amount", nullable = false, precision = 12)
	public Double getFullPriceAmount() {
		return this.fullPriceAmount;
	}

	public void setFullPriceAmount(Double fullPriceAmount) {
		this.fullPriceAmount = fullPriceAmount;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upt01SellerSale")
	public Set<Upt01SellerSaleDiscount> getUpt01SellerSaleDiscounts() {
		return this.upt01SellerSaleDiscounts;
	}

	public void setUpt01SellerSaleDiscounts(Set<Upt01SellerSaleDiscount> upt01SellerSaleDiscounts) {
		this.upt01SellerSaleDiscounts = upt01SellerSaleDiscounts;
	}

}