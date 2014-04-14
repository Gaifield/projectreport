package cn.damai.boss.projectreport.report.enums;

/**
 * 座位类型
 * 
 * @author：guwei 【顾炜】 time：2014-3-21 下午3:24:14
 */
public enum SeatTypeEnum {
	Normal(0, "普通票"), Staff(1, "工作票"), Protect(2, "防涨票");

	// 码
	private int code;

	// 名称
	private String name;

	private SeatTypeEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * 根据名称返回code
	 * 
	 * @param name
	 *            名称
	 * @return
	 */
	public static int getCode(String name) {
		SeatTypeEnum[] ticketTypeEnumArr = SeatTypeEnum.values();
		for (SeatTypeEnum ticketTypeEnum : ticketTypeEnumArr) {
			if (name != null && name.equals(ticketTypeEnum.name)) {
				return ticketTypeEnum.code;
			}
		}

		return -1;
	}

	/**
	 * 根据code返回名称
	 * 
	 * @param code
	 * @return
	 */
	public static String getName(int code) {
		SeatTypeEnum[] ticketTypeEnumArr = SeatTypeEnum.values();
		for (SeatTypeEnum ticketTypeEnum : ticketTypeEnumArr) {
			if (code == ticketTypeEnum.code) {
				return ticketTypeEnum.name;
			}
		}

		return null;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
