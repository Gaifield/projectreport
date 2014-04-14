package cn.damai.boss.projectreport.report.enums;

/**
 * 注释：数据源枚举类型
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-27 上午9:58
 */
public enum DataSourceEnum {

    Report("0", "报表数据源"), BJMaitix("1", "maitix北京只读数据源"), SHMaitix("2", "maitix上海只读数据源"),
    GZMaitix("3", "maitix广州只读数据源"), Maitix("4", "maitix只读数据源"), BUserCenter("5", "B平台只读数据源");

    //码
    private String codeStr;

    //名称
    private String name;

    private DataSourceEnum(String codeStr, String name) {
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
        DataSourceEnum[] dataSourceEnumArr = DataSourceEnum.values();
        for (DataSourceEnum dataSourceEnum : dataSourceEnumArr) {
            if (name != null && name.equals(dataSourceEnum.name)) {
                return dataSourceEnum.codeStr;
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
        DataSourceEnum[] dataSourceEnumArr = DataSourceEnum.values();
        for (DataSourceEnum dataSourceEnum : dataSourceEnumArr) {
            if (code != null && code.equals(dataSourceEnum.codeStr)) {
                return dataSourceEnum.name;
            }
        }

        return null;
    }

    public static DataSourceEnum GetDataSourceEnum(short code) {
    	String strCode=String.valueOf(code);
        DataSourceEnum[] dataSourceEnumArr = DataSourceEnum.values();
        for (DataSourceEnum dataSourceEnum : dataSourceEnumArr) {
            if (strCode != null && strCode.equals(dataSourceEnum.codeStr)) {
                return dataSourceEnum;
            }
        }

        throw new IllegalArgumentException("指定的数据库不存在。");
    }
    
    public static DataSourceEnum checkDataSourceEnum(String strCode) {
        DataSourceEnum[] dataSourceEnumArr = DataSourceEnum.values();
        for (DataSourceEnum dataSourceEnum : dataSourceEnumArr) {
            if (strCode != null && strCode.equals(dataSourceEnum.codeStr)) {
                return dataSourceEnum;
            }
        }
        throw new IllegalArgumentException("参数错误。");
    }
    
    public String getCodeStr() {
        return codeStr;
    }

    public String getName() {
        return name;
    }

}
