package cn.damai.boss.projectreport.report.enums;

/**
 * 注释：销售方式枚举
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-7 下午2:58
 */
public enum SaleModeEnum {
    Single(1, "单票"), Promotion(2, "套票");

    //码
    private int code;

    //名称
    private String name;

    private SaleModeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据名称返回code
     *
     * @param name 名称
     * @return
     */
    public static int getCode(String name) {
        SaleModeEnum[] ticketTypeEnumArr = SaleModeEnum.values();
        for (SaleModeEnum ticketTypeEnum : ticketTypeEnumArr) {
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
        SaleModeEnum[] ticketTypeEnumArr = SaleModeEnum.values();
        for (SaleModeEnum ticketTypeEnum : ticketTypeEnumArr) {
            if (code ==ticketTypeEnum.code) {
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
