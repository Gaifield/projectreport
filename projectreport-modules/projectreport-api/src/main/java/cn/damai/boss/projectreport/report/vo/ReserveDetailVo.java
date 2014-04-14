package cn.damai.boss.projectreport.report.vo;

import java.math.BigDecimal;

/**
 * 预留统计明细VO
 * @author Administrator
 *
 */
public class ReserveDetailVo {
	
	/**
	 * 预留统计类型： 1 总预留，2 管理端预留，3 客户端预留
	 */
	private short reserveStatType;
	
	/**
	 * 预留商家类型 0管理端，1主办方，2新邮票通
	 */
	private short reverseType;
	
	/**
	 * 预留级别
	 */
	private int reserveTyle;
	
	/**
	 * 价格
	 */
	private BigDecimal price;
	
	/**
	 * 价格名称
	 */
	private String priceName;
	
	/**
	 * 数量
	 */
	private int quantity;
	
	/**
	 * 金额
	 */
	private BigDecimal amount;
	
	/**
	 * 票类型 1：普通票，2：套票
	 */
	private short ticketType;

	public short getReserveStatType() {
		return reserveStatType;
	}

	public void setReserveStatType(short reserveStatType) {
		this.reserveStatType = reserveStatType;
	}

	public short getReverseType() {
		return reverseType;
	}

	public void setReverseType(short reverseType) {
		this.reverseType = reverseType;
	}

	public int getReserveTyle() {
		return reserveTyle;
	}

	public void setReserveTyle(int reserveTyle) {
		this.reserveTyle = reserveTyle;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public short getTicketType() {
		return ticketType;
	}

	public void setTicketType(short ticketType) {
		this.ticketType = ticketType;
	}	
}