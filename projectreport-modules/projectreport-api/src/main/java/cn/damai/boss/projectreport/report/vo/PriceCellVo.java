package cn.damai.boss.projectreport.report.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 价格统计值VO
 * @author Administrator
 *
 */
public class PriceCellVo {
	/**
	 * 数量
	 */
	private int quantity;
	
	/**
	 * 金额
	 */
	private BigDecimal amount= new BigDecimal("0");
	
	public PriceCellVo(){
		
	}
	
	public PriceCellVo(int quantity, BigDecimal amount) {
		super();
		this.quantity = quantity;
		this.amount = amount;
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
	
	public void addQuantity(int quantity){
		this.quantity+=quantity;
	}
	public void addAmount(BigDecimal amount){
		this.amount= this.amount.add(amount);
	}
	public void add(int quantity,BigDecimal amount){
		this.quantity+=quantity;
		this.amount= this.amount.add(amount);
	}
	
	public void add(PriceCellVo priceCellVo){
		if(priceCellVo!=null){
			this.quantity+=priceCellVo.getQuantity();
			this.amount= this.amount.add(priceCellVo.getAmount());
		}
	}
	
	public static List<PriceCellVo> initCells(int count){
		List<PriceCellVo> cells=new ArrayList<PriceCellVo>(count);
		for(int i=0;i<count;i++){
			cells.add(new PriceCellVo());
		}
		return cells;
	}
}
