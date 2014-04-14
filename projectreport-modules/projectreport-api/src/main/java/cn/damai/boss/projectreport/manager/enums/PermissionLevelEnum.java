package cn.damai.boss.projectreport.manager.enums;

/**
 * 注释：操作员权限登记枚举类型
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-20 上午10:44
 */
public enum PermissionLevelEnum {
    Operator(1, "操作员权限"), Admin(2, "管理员权限");

    private int code;
    private String name;

    private PermissionLevelEnum(int code, String name) {
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
        PermissionLevelEnum[] levelEnumArr = PermissionLevelEnum.values();
        for (PermissionLevelEnum levelEnum : levelEnumArr) {
            if (name != null && name.equals(levelEnum.name)) {
                return levelEnum.code;
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
        PermissionLevelEnum[] levelEnumArr = PermissionLevelEnum.values();
        for (PermissionLevelEnum levelEnum : levelEnumArr) {
            if (code == levelEnum.code) {
                return levelEnum.name;
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
