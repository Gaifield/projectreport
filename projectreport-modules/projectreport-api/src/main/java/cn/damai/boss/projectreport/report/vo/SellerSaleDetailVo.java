package cn.damai.boss.projectreport.report.vo;

import java.util.List;

/**
 * 渠道商汇总明细
 * 
 * @author：guwei 【顾炜】 time：2014-3-8 下午5:11:05
 * 
 */
public class SellerSaleDetailVo {
	// 渠道商id
	private Long agentId;
	// 渠道商名称
	private String agentName;
	// 出票vo
	private List<SellerSaleVo> sellerSaleVo;
	// 所有打折
	private List<String> allDisaccountVoList;

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public List<SellerSaleVo> getSellerSaleVo() {
		return sellerSaleVo;
	}

	public void setSellerSaleVo(List<SellerSaleVo> sellerSaleVo) {
		this.sellerSaleVo = sellerSaleVo;
	}

	public List<String> getAllDisaccountVoList() {
		return allDisaccountVoList;
	}

	public void setAllDisaccountVoList(List<String> allDisaccountVoList) {
		this.allDisaccountVoList = allDisaccountVoList;
	}

}
