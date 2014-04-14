package cn.damai.boss.projectreport.report.vo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.damai.boss.projectreport.common.vo.PageResultData;

/**
 * 渠道销售报表Vo对象
 * @author Administrator
 *
 */
public class SellerSaleReportVo {	
	/**
	 * 渠道商分页数据
	 */
	private PageResultData<AgentVo> pageData;
	
	/**
	 * 价格列表
	 */
	private List<PriceVo> prices;
	
	/**
	 * 销售折扣列表
	 */
	private List<OrderDiscountVo> orderDiscountList;
	
	private List<OrderDiscountVo> orderDiscountSortList;

	public PageResultData<AgentVo> getPageData() {
		return pageData;
	}

	public void setPageData(PageResultData<AgentVo> pageData) {
		this.pageData = pageData;
	}

	public List<PriceVo> getPrices() {
		return prices;
	}

	public void setPrices(List<PriceVo> prices) {
		this.prices = prices;
	}

	public List<OrderDiscountVo> getOrderDiscountList() {
		return orderDiscountList;
	}

	public void setOrderDiscountList(List<OrderDiscountVo> orderDiscountList) {
		this.orderDiscountList = orderDiscountList;
	}

	public List<OrderDiscountVo> getOrderDiscountSortList() {
		if(orderDiscountList!=null){
			Collections.sort(orderDiscountList, OrderDiscountVo.comparator());
		}
		return orderDiscountList;
	}	
}