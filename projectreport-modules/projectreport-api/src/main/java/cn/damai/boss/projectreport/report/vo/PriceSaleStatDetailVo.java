package cn.damai.boss.projectreport.report.vo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 渠道销售列表
 * 
 * @author Administrator
 * 
 */
public class PriceSaleStatDetailVo {
	/**
	 * 价格
	 */
	private PriceVo priceVo;

	// 折扣列表（包括小计）
	private List<OrderDiscountVo> disccountList;

	// 赠品出票
	private PriceCellVo presentSale = new PriceCellVo();

	// 工作票出票
	private PriceCellVo staffSale = new PriceCellVo();

	// 出票总计
	private PriceCellVo totalSale = new PriceCellVo();

	private List<OrderDiscountVo> disccountSortList;

	public PriceVo getPriceVo() {
		return priceVo;
	}

	public void setPriceVo(PriceVo priceVo) {
		this.priceVo = priceVo;
	}

	public List<OrderDiscountVo> getDisccountList() {
		return disccountList;
	}

	public void setDisccountList(List<OrderDiscountVo> disccountList) {
		this.disccountList = disccountList;
	}

	public PriceCellVo getPresentSale() {
		return presentSale;
	}

	public void setPresentSale(PriceCellVo presentSale) {
		this.presentSale = presentSale;
	}

	public PriceCellVo getStaffSale() {
		return staffSale;
	}

	public void setStaffSale(PriceCellVo staffSale) {
		this.staffSale = staffSale;
	}

	public PriceCellVo getTotalSale() {
		return totalSale;
	}

	public void setTotalSale(PriceCellVo totalSale) {
		this.totalSale = totalSale;
	}

	public List<OrderDiscountVo> getDisccountSortList() {
		if (this.disccountList != null) {
			Collections.sort(disccountList, OrderDiscountVo.comparator());
		}
		return disccountList;
	}
}