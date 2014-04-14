package cn.damai.boss.projectreport.report.enums;

/**
 * 注释：查询日期类型枚举
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-25 上午3:38
 * <p/>
 */
public enum QueryDateTypeEnum {


    All("0", "全部"), ThisMonth("1", "本月"), UpMonth("2", "上月"), custom("3", "自定义");

    //码
    private String codeStr;

    //名称
    private String name;

    private QueryDateTypeEnum(String codeStr, String name) {
        this.codeStr = codeStr;
        this.name = name;
    }

    /**
     * 根据名称返回code
     *
     * @param name 名称
     * @return
     */
    public static String getCode(String name) {
        QueryDateTypeEnum[] dateTypeEnumArr = QueryDateTypeEnum.values();
        for (QueryDateTypeEnum dateTypeEnum : dateTypeEnumArr) {
            if (name != null && name.equals(dateTypeEnum.name)) {
                return dateTypeEnum.codeStr;
            }
        }

        return "-1";
    }

    /**
     * 根据code返回名称
     *
     * @param code
     * @return
     */
    public static String getName(String code) {
        QueryDateTypeEnum[] dateTypeEnumArr = QueryDateTypeEnum.values();
        for (QueryDateTypeEnum dateTypeEnum : dateTypeEnumArr) {
            if (code != null && code.equals(dateTypeEnum.codeStr)) {
                return dateTypeEnum.name;
            }
        }

        return null;
    }

    public String getCodeStr() {
        return codeStr;
    }

    public String getName() {
        return name;
    }


}
