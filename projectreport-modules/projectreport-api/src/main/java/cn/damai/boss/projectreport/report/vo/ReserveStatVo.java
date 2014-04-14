package cn.damai.boss.projectreport.report.vo;

import java.util.List;

/**
 * 预留统计报表VO
 * @author Administrator
 *
 */
public class ReserveStatVo {	
	/**
	 * 价格列表
	 */
	private List<PriceVo> prices;
	
	/**
	 * 预留级别列表
	 */
	private List<ReserveTyleVo> reserveTyles;
	
	/**
	 * 总预留、管理端预留、客户端预留列表
	 */
	private List<ReserveStatTypeVo> reserveStatTypes;

	public List<PriceVo> getPrices() {
		return prices;
	}

	public void setPrices(List<PriceVo> prices) {
		this.prices = prices;
	}

	public List<ReserveTyleVo> getReserveTyles() {
		return reserveTyles;
	}

	public void setReserveTyles(List<ReserveTyleVo> reserveTyles) {
		this.reserveTyles = reserveTyles;
	}

	public List<ReserveStatTypeVo> getReserveStatTypes() {
		return reserveStatTypes;
	}

	public void setReserveStatTypes(List<ReserveStatTypeVo> reserveStatTypes) {
		this.reserveStatTypes = reserveStatTypes;
	}
}