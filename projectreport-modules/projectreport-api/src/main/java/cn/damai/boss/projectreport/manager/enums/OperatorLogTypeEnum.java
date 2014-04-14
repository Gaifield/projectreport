package cn.damai.boss.projectreport.manager.enums;

public enum OperatorLogTypeEnum {

	NewOperator(1,"新建操作员"),MotifyOperatorLevel(2,"修改操作员权限"),MotifyOperatorStatus(3,"修改操作员状态"),DeleteOperator(4,"删除操作员"),NewRole(5,"新建角色"),MotifyRole(6,"修改角色"),MotifyUserRole(7,"修改用户角色");

	//码
    private int code;

    //名称
    private String name;
    
    private OperatorLogTypeEnum(int code, String name)
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
        OperatorLogTypeEnum[] logTypeEnumArr = OperatorLogTypeEnum.values();
        for (OperatorLogTypeEnum logTypeEnum : logTypeEnumArr)
        {
            if (name != null && name.equals(logTypeEnum.name))
            {
                return logTypeEnum.code;
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
        OperatorLogTypeEnum[] logTypeEnumArr = OperatorLogTypeEnum.values();
        for (OperatorLogTypeEnum logTypeEnum : logTypeEnumArr)
        {
            if (code == logTypeEnum.code)
            {
                return logTypeEnum.name;
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
