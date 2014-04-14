package cn.damai.boss.projectreport.report.vo;

import java.util.List;

/**
 *  价格行对应的单元格列表
 * @author Administrator
 *
 */
public class PriceStatRowVo {
	
	/**
	 * 价格
	 */
	private PriceVo price;
	
	/**
	 * 价格行对应的单元格对象
	 */
	private List<PriceCellVo> rowCells;

	public PriceVo getPrice() {
		return price;
	}

	public void setPrice(PriceVo price) {
		this.price = price;
	}

	public List<PriceCellVo> getRowCells() {
		return rowCells;
	}

	public void setRowCells(List<PriceCellVo> rowCells) {
		this.rowCells = rowCells;
	}	
}
