package cn.damai.boss.projectreport.report.enums;

/**
 * 票品打印类型
 * 
 * @author Administrator
 * 
 */
public enum TicketPrintTypeEnum {
	/**
	 * 普通票
	 */
	Normal(0, "普通票"),
	/**
	 * 赠票"
	 */
	PreSent(1, "赠票"),
	/**
	 * 工作票
	 */
	Staff(2, "工作票"),
	/**
	 * 座位工作票
	 */
	SeatStaff(3, "座位工作票"),
	/**
	 * 划定防涨票
	 */
	SeatProtect(4, "座位防涨票");

	// 码
	private int code;

	// 名称
	private String name;

	private TicketPrintTypeEnum(int code, String name) {
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
		TicketPrintTypeEnum[] ticketTypeEnumArr = TicketPrintTypeEnum.values();
		for (TicketPrintTypeEnum ticketTypeEnum : ticketTypeEnumArr) {
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
		TicketPrintTypeEnum[] ticketTypeEnumArr = TicketPrintTypeEnum.values();
		for (TicketPrintTypeEnum ticketTypeEnum : ticketTypeEnumArr) {
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
