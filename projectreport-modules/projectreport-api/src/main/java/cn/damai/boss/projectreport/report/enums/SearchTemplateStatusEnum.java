package cn.damai.boss.projectreport.report.enums;

public enum SearchTemplateStatusEnum {
	Normal(1,"正常"),Delete(2,"删除");
	
	private int code;

    //名称
    private String name;
    
    private SearchTemplateStatusEnum(int code, String name)
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
    	SearchTemplateStatusEnum[] logTypeEnumArr = SearchTemplateStatusEnum.values();
        for (SearchTemplateStatusEnum logTypeEnum : logTypeEnumArr)
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
    	SearchTemplateStatusEnum[] logTypeEnumArr = SearchTemplateStatusEnum.values();
        for (SearchTemplateStatusEnum logTypeEnum : logTypeEnumArr)
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
