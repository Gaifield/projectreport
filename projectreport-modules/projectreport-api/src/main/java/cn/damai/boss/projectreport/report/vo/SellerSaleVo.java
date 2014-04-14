package cn.damai.boss.projectreport.report.vo;

import java.util.List;

/**
 * 注释：出票汇总VO 作者：liutengfei 【刘腾飞】 时间：14-3-6 下午2:04
 */
public class SellerSaleVo {
	// 价格vo
	private PriceVo priceVo;
	// 正常出票
	private PriceCellVo normalSale;
	// 折扣vo
	private List<DisaccountVo> disaccountVoList;
	// 赠品出票
	private PriceCellVo presentSale;
	// 工作票出票
	private PriceCellVo staffSale;
	// 小计
	private PriceCellVo totalSaleCell;

	public PriceVo getPriceVo() {
		return priceVo;
	}

	public void setPriceVo(PriceVo priceVo) {
		this.priceVo = priceVo;
	}

	public PriceCellVo getNormalSale() {
		return normalSale;
	}

	public void setNormalSale(PriceCellVo normalSale) {
		this.normalSale = normalSale;
	}

	public List<DisaccountVo> getDisaccountVoList() {
		return disaccountVoList;
	}

	public void setDisaccountVoList(List<DisaccountVo> disaccountVoList) {
		this.disaccountVoList = disaccountVoList;
	}

	public PriceCellVo getStaffSale() {
		return staffSale;
	}

	public void setStaffSale(PriceCellVo staffSale) {
		this.staffSale = staffSale;
	}

	public PriceCellVo getTotalSaleCell() {
		return totalSaleCell;
	}

	public void setTotalSaleCell(PriceCellVo totalSaleCell) {
		this.totalSaleCell = totalSaleCell;
	}

	public PriceCellVo getPresentSale() {
		return presentSale;
	}

	public void setPresentSale(PriceCellVo presentSale) {
		this.presentSale = presentSale;
	}

}
