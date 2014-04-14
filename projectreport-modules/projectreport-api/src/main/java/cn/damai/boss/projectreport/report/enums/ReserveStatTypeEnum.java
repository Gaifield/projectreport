package cn.damai.boss.projectreport.report.enums;

/**
 * 座位预留统计查询类型
 * @author Administrator
 *
 */
public enum ReserveStatTypeEnum {
	ReserveAll((short)1, "总预留"), ReserveManagement((short)2, "管理端预留"),ReserveClient((short)3, "客户端预留");

    //码
    private short code;

    //名称
    private String name;

    private ReserveStatTypeEnum(short code, String name) {
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
        ReserveStatTypeEnum[] dataSourceEnumArr = ReserveStatTypeEnum.values();
        for (ReserveStatTypeEnum dataSourceEnum : dataSourceEnumArr) {
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
