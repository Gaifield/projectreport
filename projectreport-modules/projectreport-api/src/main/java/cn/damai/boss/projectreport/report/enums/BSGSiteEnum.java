package cn.damai.boss.projectreport.report.enums;

/**
 * 
 * 注释：北京上海广州 站点枚举类 
 * 作者：guwei 【顾炜】 
 * 时间：2014-3-1 上午1:05:12
 * 
 */
public enum BSGSiteEnum {

	BeiJing((short) 1, "北京站"), ShangHai((short) 2, "上海站"), GuangZhou((short) 3,
			"广州站");

	// 码
	private short code;

	// 名称
	private String name;

	private BSGSiteEnum(short codeStr, String name) {
		this.code = codeStr;
		this.name = name;
	}

	/**
	 * 根据名称返回code
	 * 
	 * @param name
	 *            名称
	 * @return
	 */
	public static Short getCode(String name) {
		BSGSiteEnum[] dataSourceEnumArr = BSGSiteEnum.values();
		for (BSGSiteEnum dataSourceEnum : dataSourceEnumArr) {
			if (name != null && name.equals(dataSourceEnum.name)) {
				return dataSourceEnum.code;
			}
		}
		return null;
	}

	/**
	 * 根据code返回名称
	 * 
	 * @param code
	 * @return
	 */
	public static String getName(short code) {
		BSGSiteEnum[] dataSourceEnumArr = BSGSiteEnum.values();
		for (BSGSiteEnum dataSourceEnum : dataSourceEnumArr) {
			if (code == dataSourceEnum.code) {
				return dataSourceEnum.name;
			}
		}

		return null;
	}

	public short getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

}
