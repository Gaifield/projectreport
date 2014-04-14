package cn.damai.boss.projectreport.manager.enums;

/**
 * 项目统计任务枚举
 */
public enum ProjectTaskTypeEnum {
	//1：出票统计；2：座位统计；3：渠道销售统计；4：预留统计；5：分区出票统计
    SaleStat((short)1, "出票统计"),
    SeatStat((short)2, "座位统计"),
    SellerSaleStat((short)3, "渠道销售统计"),
    ReserveStat((short)4, "预留统计"),
    StandStat((short)5, "分区出票统计");

    //码
    private short code;

    //名称
    private String name;

    private ProjectTaskTypeEnum(short code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据名称返回code
     *
     * @param name 名称
     * @return
     */
    public static short getCode(String name) {
        ProjectTaskTypeEnum[] statusEnumArr = ProjectTaskTypeEnum.values();
        for (ProjectTaskTypeEnum statusEnum : statusEnumArr) {
            if (name != null && name.equals(statusEnum.name)) {
                return statusEnum.code;
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
        ProjectTaskTypeEnum[] statusEnumArr = ProjectTaskTypeEnum.values();
        for (ProjectTaskTypeEnum statusEnum : statusEnumArr) {
            if (code == statusEnum.code) {
                return statusEnum.name;
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