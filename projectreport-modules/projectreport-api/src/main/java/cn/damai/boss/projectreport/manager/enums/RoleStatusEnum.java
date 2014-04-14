package cn.damai.boss.projectreport.manager.enums;

/**
 * 注释：角色状态枚举
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-25 上午9:35
 */
public enum RoleStatusEnum {

    Normal(1, "正常"), Delete(2, "删除");

    //码
    private int code;

    //名称
    private String name;

    private RoleStatusEnum(int code, String name) {
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
        RoleStatusEnum[] roleStatusEnumArr = RoleStatusEnum.values();
        for (RoleStatusEnum statusEnum : roleStatusEnumArr) {
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
    public static String getName(long code) {
        RoleStatusEnum[] roleStatusEnumArr = RoleStatusEnum.values();
        for (RoleStatusEnum statusEnum : roleStatusEnumArr) {
            if (code == statusEnum.code) {
                return statusEnum.name;
            }
        }

        return null;
    }

    public long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
