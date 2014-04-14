package cn.damai.boss.projectreport.manager.enums;

/**
 * 组织机构用户状态
 * 状态：1 = 可用；2 = 禁用。
 * @author：guwei 【顾炜】
 * time：2014-3-6 下午1:37:19
 *
 */
public enum BusinessUserStatusEnum {

    Normal(1, "可用"), Delete(2, "禁用");

    //码
    private int code;

    //名称
    private String name;

    private BusinessUserStatusEnum(int code, String name) {
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
        BusinessUserStatusEnum[] roleStatusEnumArr = BusinessUserStatusEnum.values();
        for (BusinessUserStatusEnum statusEnum : roleStatusEnumArr) {
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
        BusinessUserStatusEnum[] roleStatusEnumArr = BusinessUserStatusEnum.values();
        for (BusinessUserStatusEnum statusEnum : roleStatusEnumArr) {
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
