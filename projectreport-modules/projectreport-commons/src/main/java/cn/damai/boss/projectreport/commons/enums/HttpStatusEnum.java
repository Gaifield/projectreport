package cn.damai.boss.projectreport.commons.enums;

/**
 * 用户状态
 *
 * @author Administrator
 */
public enum HttpStatusEnum
{

    Success(200, "成功"), Warn(300, "警告"), ClientError(400, "客户端错误"), ServerError(500, "服务器错误");

    //码
    private int code;

    //名称
    private String name;

    private HttpStatusEnum(int code, String name)
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
        HttpStatusEnum[] statusEnumArr = HttpStatusEnum.values();
        for (HttpStatusEnum statusEnum : statusEnumArr)
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
        HttpStatusEnum[] statusEnumArr = HttpStatusEnum.values();
        for (HttpStatusEnum statusEnum : statusEnumArr)
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
