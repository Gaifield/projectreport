package cn.damai.boss.projectreport.report.enums;

/**
 * <p/>
 * null或0为普通打印 1=主办方打印为赠票 2=主办方打印为工作票 3=主办方打印为防涨票
 * <p/>
 * 注释：票类型枚举 作者：liutengfei 【刘腾飞】 时间：14-3-7 下午2:58
 */
public enum TicketTypeEnum {
	Normal(0, "普通打印"), PreSent(1, "赠票"), Staff(2, "工作票"), Protect(3, "防涨票");

	// 码
	private int code;

	// 名称
	private String name;

	private TicketTypeEnum(int code, String name) {
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
		TicketTypeEnum[] ticketTypeEnumArr = TicketTypeEnum.values();
		for (TicketTypeEnum ticketTypeEnum : ticketTypeEnumArr) {
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
		TicketTypeEnum[] ticketTypeEnumArr = TicketTypeEnum.values();
		for (TicketTypeEnum ticketTypeEnum : ticketTypeEnumArr) {
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
