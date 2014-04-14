package cn.damai.boss.projectreport.report.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 注释：座位汇总VO 作者：liutengfei 【刘腾飞】 时间：14-3-3 下午2:00
 */
public class SeatStatVo {
	// 座位ID（大麦报表）
	private Long seatStatId;
	// 项目ID
	private Long projectId;
	// 项目名称
	private String projectName;
	// 场次ID
	private Long performId;
	// 场次名称
	private String performName;
	// 场次时间
	private Date performTime;

	private String performTimeStr;
	// 价格ID
	private Long priceId;
	// 价格
	private BigDecimal price;
	// 总票数
	private int quantity;
	// 使用座位数量
	private Integer seatQuantity;
	// 使用座位金额
	private BigDecimal seatAmount;
	// 工作票数量
	private Integer staffQuantity;
	// 工作票金额
	private BigDecimal staffAmount;
	// 防涨票数量
	private Integer protectQuantity;
	// 防涨票金额
	private BigDecimal protectAmount;
	// 可售数量
	private int vendibilityQuantity;
	// 可售金额
	private BigDecimal vendibilityAmount;
	// 创建时间
	private String createTime;
	// 修改时间
	private String modifyTime;

	private String priceName;
	// 座位类型
	private int seatType;

	/**
	 * 价格显示名称
	 */
	private String priceShowName;

	private List<SeatStatVo> seatStatVoList;

	public Long getSeatStatId() {
		return seatStatId;
	}

	public void setSeatStatId(Long seatStatId) {
		this.seatStatId = seatStatId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getPerformId() {
		return performId;
	}

	public void setPerformId(Long performId) {
		this.performId = performId;
	}

	public Long getPriceId() {
		return priceId;
	}

	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Integer getSeatQuantity() {
		return seatQuantity = seatQuantity == null ? 0 : seatQuantity;
	}

	public void setSeatQuantity(Integer seatQuantity) {
		if (seatQuantity == null) {
			seatQuantity = 0;
		}
		this.seatQuantity = seatQuantity;
	}

	public BigDecimal getSeatAmount() {
		if (seatAmount == null) {
			seatAmount = price.multiply(BigDecimal.valueOf(this.getSeatQuantity()));
		}
		return seatAmount = seatAmount == null ? new BigDecimal(0) : seatAmount;
	}

	public void setSeatAmount(BigDecimal seatAmount) {
		if (seatAmount == null) {
			seatAmount = new BigDecimal(0);
		}
		this.seatAmount = seatAmount;
	}

	public Integer getStaffQuantity() {
		return staffQuantity = staffQuantity == null ? 0 : staffQuantity;
	}

	public void setStaffQuantity(Integer staffQuantity) {
		if (staffQuantity == null) {
			staffQuantity = 0;
		}
		this.staffQuantity = staffQuantity;
	}

	public BigDecimal getStaffAmount() {
		if (staffAmount == null) {
			staffAmount = price.multiply(BigDecimal.valueOf(this.getStaffQuantity()));
		}
		return staffAmount = staffAmount == null ? new BigDecimal(0) : staffAmount;
	}

	public void setStaffAmount(BigDecimal staffAmount) {
		if (staffAmount == null) {
			staffAmount = new BigDecimal(0);
		}
		this.staffAmount = staffAmount;
	}

	public Integer getProtectQuantity() {
		return protectQuantity = protectQuantity == null ? 0 : protectQuantity;
	}

	public void setProtectQuantity(Integer protectQuantity) {
		if (protectQuantity == null) {
			protectQuantity = 0;
		}
		this.protectQuantity = protectQuantity;
	}

	public BigDecimal getProtectAmount() {
		if (protectAmount == null) {
			protectAmount = price.multiply(BigDecimal.valueOf(this.getProtectQuantity()));
		}
		return protectAmount = protectAmount == null ? new BigDecimal(0) : protectAmount;
	}

	public void setProtectAmount(BigDecimal protectAmount) {
		if (protectAmount == null) {
			protectAmount = new BigDecimal(0);
		}
		this.protectAmount = protectAmount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPerformName() {
		return performName;
	}

	public void setPerformName(String performName) {
		this.performName = performName;
	}

	public int getVendibilityQuantity() {
		if (vendibilityQuantity == 0) {
			vendibilityQuantity = seatQuantity - staffQuantity - protectQuantity;
		}
		return vendibilityQuantity;
	}

	public void setVendibilityQuantity(int vendibilityQuantity) {
		this.vendibilityQuantity = vendibilityQuantity;
	}

	public BigDecimal getVendibilityAmount() {
		if (vendibilityAmount == null) {
			vendibilityAmount = seatAmount.subtract(staffAmount).subtract(protectAmount);
		}
		return vendibilityAmount = vendibilityAmount == null ? new BigDecimal(0) : vendibilityAmount;
	}

	public void setVendibilityAmount(BigDecimal vendibilityAmount) {
		this.vendibilityAmount = vendibilityAmount;
	}

	public List<SeatStatVo> getSeatStatVoList() {
		return seatStatVoList;
	}

	public void setSeatStatVoList(List<SeatStatVo> seatStatVoList) {
		this.seatStatVoList = seatStatVoList;
	}

	public Date getPerformTime() {
		return performTime;
	}

	public void setPerformTime(Date performTime) {
		this.performTime = performTime;
	}

	public String getPerformTimeStr() {
		if (performTimeStr == null) {
			performTimeStr = DateFormatUtils.format(performTime, "yyyy-MM-dd HH:mm");
		}
		return performTimeStr;
	}

	public void setPerformTimeStr(String performTimeStr) {
		this.performTimeStr = performTimeStr;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public int getSeatType() {
		return seatType;
	}

	public void setSeatType(int seatType) {
		this.seatType = seatType;
	}

	public String getPriceShowName() {
		return priceShowName;
	}

	public void setPriceShowName(String priceShowName) {
		this.priceShowName = priceShowName;
	}
}
