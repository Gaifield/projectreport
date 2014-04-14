package cn.damai.boss.projectreport.manager.enums;

/**
 * 报表角色
 * @author 顾炜
 */
public enum ReportRoleEnum
{

    All(0, "全部");

    //码
    private long code;

    //名称
    private String name;

    private ReportRoleEnum(long code, String name)
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
    public static long getCode(String name)
    {
        ReportRoleEnum[] userTypeEnumArr = ReportRoleEnum.values();
        for (ReportRoleEnum statusEnum : userTypeEnumArr)
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
    public static String getName(long code)
    {
        ReportRoleEnum[] userTypeEnumArr = ReportRoleEnum.values();
        for (ReportRoleEnum statusEnum : userTypeEnumArr)
        {
            if (code == statusEnum.code)
            {
                return statusEnum.name;
            }
        }

        return null;
    }

    public long getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

}
