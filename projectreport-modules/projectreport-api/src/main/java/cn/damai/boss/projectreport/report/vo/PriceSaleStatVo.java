package cn.damai.boss.projectreport.report.vo;

import java.math.BigDecimal;

/**
 * 票价销售折扣统计Vo
 * @author Administrator
 *
 */
public class PriceSaleStatVo {
	private short ticketType; //普通票：1；2套票
	private BigDecimal price;
	private String priceName;
	private BigDecimal discount=new BigDecimal("0");
	private Long performId;
	private Long agentID;
	private Integer agentFrom;
	private int ticketQuantity;
	private BigDecimal sumMoney=new BigDecimal("0");
	
	public short getTicketType() {
		return ticketType;
	}
	public void setTicketType(short ticketType) {
		this.ticketType = ticketType;
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
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public Long getPerformId() {
		return performId;
	}
	public void setPerformId(Long performId) {
		this.performId = performId;
	}
	public Long getAgentID() {
		return agentID;
	}
	public void setAgentID(Long agentID) {
		this.agentID = agentID;
	}
	public Integer getAgentFrom() {
		return agentFrom;
	}
	public void setAgentFrom(Integer agentFrom) {
		this.agentFrom = agentFrom;
	}
	public int getTicketQuantity() {
		return ticketQuantity;
	}
	public void setTicketQuantity(int ticketQuantity) {
		this.ticketQuantity = ticketQuantity;
	}
	public BigDecimal getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(BigDecimal sumMoney) {
		this.sumMoney = sumMoney;
	}
}