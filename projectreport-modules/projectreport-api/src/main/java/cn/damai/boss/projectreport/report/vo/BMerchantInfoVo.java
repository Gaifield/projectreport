package cn.damai.boss.projectreport.report.vo;

/**
 * B平台商家信息VO
 * @author Administrator
 *
 */
public class BMerchantInfoVo {
	/**
	 * 商家Id
	 */
	private Long merchantID;
	/**
	 * 商家名称
	 */
	private String companyName;
	public Long getMerchantID() {
		return merchantID;
	}
	public void setMerchantID(Long merchantID) {
		this.merchantID = merchantID;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}	
}