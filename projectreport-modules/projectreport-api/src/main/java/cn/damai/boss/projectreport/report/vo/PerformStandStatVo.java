package cn.damai.boss.projectreport.report.vo;

/**
 * 场次看台出票统计Vo
 * 
 * @ClassName: PerformStandStatVo
 * @Description: TODO
 * @author zhangbinghong
 * @date 2014-2-27 下午7:29:58
 */
public class PerformStandStatVo {
	private long performInfoId;

	/**
	 * 看台Id
	 */
	private long standId;
	/*
	 * 看台名称
	 */
	private String standName;
	/**
	 * 总座位数量
	 */
	private Integer seatQuantity = 0;

	/*
	 * 防涨票数量
	 */
	private Integer protectQuantity = 0;

	/*
	 * 出票数量
	 */
	private Integer saleQuantity = 0;

	public long getPerformInfoId() {
		return performInfoId;
	}

	public void setPerformInfoId(long performInfoId) {
		this.performInfoId = performInfoId;
	}

	public long getStandId() {
		return standId;
	}

	public void setStandId(long standId) {
		this.standId = standId;
	}

	public String getStandName() {
		return standName;
	}

	public void setStandName(String standName) {
		this.standName = standName;
	}

	public Integer getSeatQuantity() {
		return seatQuantity;
	}

	public void setSeatQuantity(Integer seatQuantity) {
		if (seatQuantity != null) {
			this.seatQuantity = seatQuantity;
		}
	}

	public Integer getProtectQuantity() {
		return protectQuantity;
	}

	public void setProtectQuantity(Integer protectQuantity) {
		if (protectQuantity != null) {
			this.protectQuantity = protectQuantity;
		}
	}

	public Integer getSaleQuantity() {
		return saleQuantity;
	}

	public void setSaleQuantity(Integer saleQuantity) {
		if (saleQuantity != null) {
			this.saleQuantity = saleQuantity;
		}
	}

}