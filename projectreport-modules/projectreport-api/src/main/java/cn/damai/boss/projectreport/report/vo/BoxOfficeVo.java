package cn.damai.boss.projectreport.report.vo;

/**
 * 票房vo
 * 
 * @author：guwei 【顾炜】 time：2014-3-6 下午4:18:51
 * 
 */
public class BoxOfficeVo {

	/**
	 * 场次Id
	 */
	private Long performId;
	/**
	 * 场次名称
	 */
	private String performName;

	/**
	 * 票价
	 */
	private PriceVo priceVo;

	/**
	 * 可售总票房
	 */
	private PriceCellVo validateSellSumBoxOffice;

	/**
	 * 剩余票房
	 */
	private PriceCellVo remainBoxOffice;

	/**
	 * 预留票房
	 */
	private PriceCellVo reservedBoxOffice;

	public PriceVo getPriceVo() {
		return priceVo;
	}

	public void setPriceVo(PriceVo priceVo) {
		this.priceVo = priceVo;
	}

	public PriceCellVo getValidateSellSumBoxOffice() {
		return validateSellSumBoxOffice;
	}

	public void setValidateSellSumBoxOffice(PriceCellVo validateSellSumBoxOffice) {
		this.validateSellSumBoxOffice = validateSellSumBoxOffice;
	}

	public PriceCellVo getRemainBoxOffice() {
		return remainBoxOffice;
	}

	public void setRemainBoxOffice(PriceCellVo remainBoxOffice) {
		this.remainBoxOffice = remainBoxOffice;
	}

	public PriceCellVo getReservedBoxOffice() {
		return reservedBoxOffice;
	}

	public void setReservedBoxOffice(PriceCellVo reservedBoxOffice) {
		this.reservedBoxOffice = reservedBoxOffice;
	}

	public Long getPerformId() {
		return performId;
	}

	public void setPerformId(Long performId) {
		this.performId = performId;
	}

	public String getPerformName() {
		return performName;
	}

	public void setPerformName(String performName) {
		this.performName = performName;
	}

}
