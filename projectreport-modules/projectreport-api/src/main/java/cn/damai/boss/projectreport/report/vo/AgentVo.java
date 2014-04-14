package cn.damai.boss.projectreport.report.vo;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 代理商Vo
 * @author Administrator
 *
 */
public class AgentVo {
	/**
	 * 代理商来源
	 */
	private Integer agentFrom;
	/**
	 * 代理商Id
	 */
	private Long agentId;
	/**
	 * 代理商名称
	 */
	private String agentName;
		
	/**
	 * 代理报表明细列表
	 */
	private List<PriceSaleStatDetailVo> priceDetails;

	public Integer getAgentFrom() {
		return agentFrom;
	}

	public void setAgentFrom(Integer agentFrom) {
		this.agentFrom = agentFrom;
	}

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

	public List<PriceSaleStatDetailVo> getPriceDetails() {
		return priceDetails;
	}

	public void setPriceDetails(List<PriceSaleStatDetailVo> priceDetails) {
		this.priceDetails = priceDetails;
	}

	
	
}