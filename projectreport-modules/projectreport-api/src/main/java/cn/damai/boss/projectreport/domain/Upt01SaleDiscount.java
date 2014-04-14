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
 * Upt01SaleDiscount entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_sale_discount")
public class Upt01SaleDiscount implements java.io.Serializable {

	// Fields

	private Long saleDiscountId;
	private Upt01Sale upt01Sale;
	private Double discount;
	private Integer quantity;
	private Double amount;

	// Constructors

	/** default constructor */
	public Upt01SaleDiscount() {
	}

	/** full constructor */
	public Upt01SaleDiscount(Upt01Sale upt01Sale, Double discount, Integer quantity, Double amount) {
		this.upt01Sale = upt01Sale;
		this.discount = discount;
		this.quantity = quantity;
		this.amount = amount;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "sale_discount_id", unique = true, nullable = false)
	public Long getSaleDiscountId() {
		return this.saleDiscountId;
	}

	public void setSaleDiscountId(Long saleDiscountId) {
		this.saleDiscountId = saleDiscountId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_id", nullable = false)
	public Upt01Sale getUpt01Sale() {
		return this.upt01Sale;
	}

	public void setUpt01Sale(Upt01Sale upt01Sale) {
		this.upt01Sale = upt01Sale;
	}

	@Column(name = "discount", nullable = false, precision = 12)
	public Double getDiscount() {
		return this.discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	@Column(name = "quantity", nullable = false)
	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(name = "amount", nullable = false, precision = 12)
	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}