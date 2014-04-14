package cn.damai.boss.projectreport.manager.enums;

/**
 * 注释：用户类型枚举
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-21 下午2:30
 */
public enum UserTypeEnum
{

    ZhuBan(1, "主办"), YunYing(2, "运营"), CaiWu(3, "财务"), GongAn(4, "公安");

    //码
    private int code;

    //名称
    private String name;

    private UserTypeEnum(int code, String name)
    {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据名称返回code
     *
     * @param name 名称
     * @return
     */
    public static int getCode(String name)
    {
        UserTypeEnum[] userTypeEnumArr = UserTypeEnum.values();
        for (UserTypeEnum statusEnum : userTypeEnumArr)
        {
            if (name != null && name.equals(statusEnum.name))
            {
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
    public static String getName(int code)
    {
        UserTypeEnum[] userTypeEnumArr = UserTypeEnum.values();
        for (UserTypeEnum statusEnum : userTypeEnumArr)
        {
            if (code == statusEnum.code)
            {
                return statusEnum.name;
            }
        }

        return null;
    }

    public int getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

}
