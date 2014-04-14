package cn.damai.boss.projectreport.manager.enums;

/**
 * 用户状态
 *
 * @author guwei
 */
public enum UserStatusEnum {

    Enable(1, "启用"), Disable(2, "禁用"), All(0, "全部"),Delete(3,"删除");

    //码
    private int code;

    //名称
    private String name;

    private UserStatusEnum(int code, String name) {
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
        UserStatusEnum[] statusEnumArr = UserStatusEnum.values();
        for (UserStatusEnum statusEnum : statusEnumArr) {
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
        UserStatusEnum[] statusEnumArr = UserStatusEnum.values();
        for (UserStatusEnum statusEnum : statusEnumArr) {
            if (code == statusEnum.code) {
                return statusEnum.name;
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
