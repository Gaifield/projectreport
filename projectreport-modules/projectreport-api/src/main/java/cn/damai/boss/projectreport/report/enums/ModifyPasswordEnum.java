package cn.damai.boss.projectreport.report.enums;

/**
 * 用户修改状态
 * 1: "密码错误" 
 * 2: "密码修改成功"
 * 3: "密码修改失败"
 * 创建人：guwei【顾炜】   
 * 创建时间：2014-2-27 下午6:52:53
 */
public enum ModifyPasswordEnum {

    PasswordFasle(1, "密码错误"), ModifySuccess(2, "密码修改成功"),ModifyFailure(3, "密码修改失败");

    //码
    private int code;

    //名称
    private String name;

    private ModifyPasswordEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    

    /**
     * 根据code返回名称
     *
     * @param code
     * @return
     */
    public static String getName(int code) {
        ModifyPasswordEnum[] dataSourceEnumArr = ModifyPasswordEnum.values();
        for (ModifyPasswordEnum dataSourceEnum : dataSourceEnumArr) {
            if ( code == dataSourceEnum.getCode()) {
                return dataSourceEnum.name;
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
