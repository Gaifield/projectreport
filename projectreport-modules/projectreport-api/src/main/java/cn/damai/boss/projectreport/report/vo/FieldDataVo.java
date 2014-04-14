package cn.damai.boss.projectreport.report.vo;

import java.util.List;

/**
 * 接口返回的场馆信息数据类
 * @author wenjunrong
 *
 */
public class FieldDataVo {
	/**
	 * 场馆vo列表
	 */
	public List<RmvenueVo> rmvenuelist;
	/**
	 *总记录数
	 */
	public Integer total;
	public List<RmvenueVo> getRmvenuelist() {
		return rmvenuelist;
	}
	public void setRmvenuelist(List<RmvenueVo> rmvenuelist) {
		this.rmvenuelist = rmvenuelist;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
}
