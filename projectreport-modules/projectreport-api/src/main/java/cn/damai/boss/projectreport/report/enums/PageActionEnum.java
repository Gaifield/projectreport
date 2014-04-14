package cn.damai.boss.projectreport.report.enums;

/**
 * 页面操作类型
 * @author Administrator
 *
 */
public enum PageActionEnum {
	/*
	 * 默认搜索
	 */
	Default((short)0, "默认"),
	/**
	 * 导出Excel
	 */
	Excel((short)1, "Excel"),
	/**
	 * 导出Pdf
	 */
	Pdf((short)2, "Pdf");

    //码
    private short code;

    //名称
    private String name;

    private PageActionEnum(short code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code返回名称
     *
     * @param code
     * @return
     */
    public static String getName(short code) {
        PageActionEnum[] dataSourceEnumArr = PageActionEnum.values();
        for (PageActionEnum dataSourceEnum : dataSourceEnumArr) {
            if ( code == dataSourceEnum.getCode()) {
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
