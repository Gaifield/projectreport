package cn.damai.boss.projectreport.report.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.damai.boss.projectreport.report.vo.PriceSaleStatVo;


/**
 * 销售渠道统计
 * 
 * @author：guwei 【顾炜】 time：2014-3-2 下午1:45:36
 * 
 */
public interface SellerSaleDao {
	/**
	 * 普通票价渠道销售统计
	 * @param projectId
	 * @param performIds
	 * @param agentMap
	 * @param saleTimeStart
	 * @param saleTimeEnd
	 * @param isGroupAll
	 * @return
	 */
	public List<PriceSaleStatVo> queryNormalPriceDiscountSale(long projectId,List<Long> performIds,
			Map<Integer, List<Long>> agentMap, Date saleTimeStart, Date saleTimeEnd,boolean isGroupAll);

	/**
	 *  普通票价（工作票、赠票）渠道销售统计
	 * @param projectId
	 * @param performIds
	 * @param agentMap
	 * @param saleTimeStart
	 * @param saleTimeEnd
	 * @param isGroupAll
	 * @param isChiefFreePrint
	 * @return
	 */
	public List<PriceSaleStatVo> queryNormalPriceZeroDiscountSale(long projectId, List<Long> performIds,
			Map<Integer, List<Long>> agentMap, Date saleTimeStart,
			Date saleTimeEnd, boolean isGroupAll, int isChiefFreePrint);

	/**
	 * 套票票价渠道销售统计
	 * @param projectId
	 * @param performIds
	 * @param agentMap
	 * @param saleTimeStart
	 * @param saleTimeEnd
	 * @param isGroupAll
	 * @return
	 */
	public List<PriceSaleStatVo> queryPromotionPriceDiscountSale(long projectId,List<Long> performIds,
			Map<Integer, List<Long>> agentMap, Date saleTimeStart, Date saleTimeEnd,boolean isGroupAll);

	/**
	 * 套票票价（工作票、赠票）渠道销售统计
	 * @param projectId
	 * @param performIds
	 * @param agentMap
	 * @param saleTimeStart
	 * @param saleTimeEnd
	 * @param isGroupAll
	 * @param isChiefFreePrint
	 * @return
	 */
	public List<PriceSaleStatVo> queryPromotionPriceZeroDiscountSale(long projectId, List<Long> performIds,
			Map<Integer, List<Long>> agentMap, Date saleTimeStart,
			Date saleTimeEnd, boolean isGroupAll, int isChiefFreePrint);
}
