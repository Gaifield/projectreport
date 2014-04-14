package cn.damai.boss.projectreport.manager.enums;

/**
 * 注释：bool 转数字枚举
 * 作者：顾炜    guwei
 * 时间：2014-2-24
 */
public enum BooleanEnum
{

    TRUE(1, true),FALSE(0, false);

    //码
    private int code;

    //名称
    private boolean name;

    private BooleanEnum(int code, boolean name)
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
    public static int getCode(boolean name)
    {
        BooleanEnum[] userTypeEnumArr = BooleanEnum.values();
        for (BooleanEnum statusEnum : userTypeEnumArr)
        {
            if (name == statusEnum.name)
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
    public static Boolean getName(int code)
    {
        BooleanEnum[] userTypeEnumArr = BooleanEnum.values();
        for (BooleanEnum statusEnum : userTypeEnumArr)
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

    public Boolean getName()
    {
        return name;
    }

}
