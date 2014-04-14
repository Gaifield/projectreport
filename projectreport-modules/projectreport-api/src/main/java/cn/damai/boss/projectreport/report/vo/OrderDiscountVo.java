package cn.damai.boss.projectreport.report.vo;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * 订单折扣VO
 * 
 * @author Administrator
 * 
 */
public class OrderDiscountVo {

	private static final BigDecimal FULLPRICE = new BigDecimal("100.00");
	
	private String guid = UUID.randomUUID().toString();
	

	/**
	 * 折扣
	 */
	private BigDecimal discount = new BigDecimal("-100.00");

	/**
	 * 折扣名称
	 */
	private String disaccountName;

	/**
	 * 数量
	 */
	private int quantity;

	/**
	 * 金额
	 */
	private BigDecimal amount = new BigDecimal("0.00");

	public OrderDiscountVo() {
		super();
	}

	public OrderDiscountVo(String disaccountName) {
		super();
		this.disaccountName = disaccountName;
	}

	public OrderDiscountVo(BigDecimal discount) {
		super();
		this.discount = discount;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getDisaccountName() {
		if (disaccountName == null && !discount.equals(FULLPRICE)) {
			return discount.intValue() + "折销售";
		} else if (discount.equals(FULLPRICE)) {
			return "全价销售";
		} else {
			return disaccountName;
		}
	}

	public void setDisaccountName(String disaccountName) {
		this.disaccountName = disaccountName;
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

	/**
	 * 累计折扣数量、金额
	 * 
	 * @param sumDiscountList
	 * @param orderDiscountVo
	 */
	public static void sumDiscountList(List<OrderDiscountVo> sumDiscountList, OrderDiscountVo orderDiscountVo) {
		if (sumDiscountList != null) {
			int index = addToDiscountList(sumDiscountList, orderDiscountVo.getDiscount());
			if(index>=0){
				sumDiscountList.get(index).add(orderDiscountVo);
			}
		}
	}

	/**
	 * 添加折扣到列表
	 * @param targetList 目标列表
	 * @param discount 折扣值
	 * @return 返回索引
	 */
	public static int addToDiscountList(List<OrderDiscountVo> targetList, BigDecimal discount) {
		int index = -1;
		if (targetList != null && discount != null) {
			for (int i = 0; i < targetList.size(); i++) {
				OrderDiscountVo vo = targetList.get(i);
				if (vo.getDiscount() != null) {
					if (vo.getDiscount().compareTo(discount) == 0) {
						index = i;
						break;
					}
				}
			}
			if (index < 0) {
				BigDecimal discountValue = BigDecimal.ZERO;
				discountValue=discountValue.add(discount);
				
				OrderDiscountVo orderDiscountVo= new OrderDiscountVo();
				orderDiscountVo.setDiscount(discount);
				targetList.add(orderDiscountVo);
				index = targetList.size() - 1;
			}
		}
		return index;
	}
	
	public void add(OrderDiscountVo orderDiscountVo) {
		this.quantity += orderDiscountVo.getQuantity();
		this.amount = this.amount.add(orderDiscountVo.getAmount());
	}

	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}

	public void addAmount(BigDecimal amount) {
		this.amount = this.amount.add(amount);
	}

	public void add(int quantity, BigDecimal amount) {
		this.quantity += quantity;
		this.amount = this.amount.add(amount);
	}
	
	public static Comparator<OrderDiscountVo> comparator(){
		return new Comparator<OrderDiscountVo>() {
			@Override
			public int compare(OrderDiscountVo o1, OrderDiscountVo o2) {
				return -o1.getDiscount().compareTo(o2.getDiscount());
			}
		};
	}
}