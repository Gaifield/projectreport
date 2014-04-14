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
 * Upt01SellerSaleDiscount entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_seller_sale_discount")
public class Upt01SellerSaleDiscount implements java.io.Serializable {

	// Fields

	private Long sellerSaleDiscountId;
	private Upt01SellerSale upt01SellerSale;
	private Double discount;
	private Integer quantity;
	private Double amount;

	// Constructors

	/** default constructor */
	public Upt01SellerSaleDiscount() {
	}

	/** full constructor */
	public Upt01SellerSaleDiscount(Upt01SellerSale upt01SellerSale, Double discount, Integer quantity, Double amount) {
		this.upt01SellerSale = upt01SellerSale;
		this.discount = discount;
		this.quantity = quantity;
		this.amount = amount;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "seller_sale_discount_id", unique = true, nullable = false)
	public Long getSellerSaleDiscountId() {
		return this.sellerSaleDiscountId;
	}

	public void setSellerSaleDiscountId(Long sellerSaleDiscountId) {
		this.sellerSaleDiscountId = sellerSaleDiscountId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_sale_id", nullable = false)
	public Upt01SellerSale getUpt01SellerSale() {
		return this.upt01SellerSale;
	}

	public void setUpt01SellerSale(Upt01SellerSale upt01SellerSale) {
		this.upt01SellerSale = upt01SellerSale;
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