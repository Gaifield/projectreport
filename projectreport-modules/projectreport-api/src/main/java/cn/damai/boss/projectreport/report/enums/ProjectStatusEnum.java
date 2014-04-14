package cn.damai.boss.projectreport.report.enums;

/**
 * 注释：项目状态枚举
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-1 上午3:38
 * <p/>
 * 项目状态:
 * 开始=1
 * 审核中=2
 * 审核拒绝=3
 * 审核通过=4
 * 结束=5
 * 作废=6
 */
public enum ProjectStatusEnum {


    Start("1", "开始"), Auditing("2", "审核中"), Refuse("3", "审核拒绝"),
    Approve("4", "正在销售"), Over("5", "已结束"), Invalid("6", "作废");

    //码
    private String codeStr;

    //名称
    private String name;

    private ProjectStatusEnum(String codeStr, String name) {
        this.codeStr = codeStr;
        this.name = name;
    }

    /**
     * 根据名称返回code
     *
     * @param name 名称
     * @return
     */
    public static String getCode(String name) {
        ProjectStatusEnum[] projectStatusEnum = ProjectStatusEnum.values();
        for (ProjectStatusEnum dataSourceEnum : projectStatusEnum) {
            if (name != null && name.equals(dataSourceEnum.name)) {
                return dataSourceEnum.codeStr;
            }
        }

        return "-1";
    }

    /**
     * 根据code返回名称
     *
     * @param code
     * @return
     */
    public static String getName(String code) {
        ProjectStatusEnum[] dataSourceEnumArr = ProjectStatusEnum.values();
        for (ProjectStatusEnum projectStatusEnum : dataSourceEnumArr) {
            if (code != null && code.equals(projectStatusEnum.codeStr)) {
                return projectStatusEnum.name;
            }
        }

        return null;
    }

    public String getCodeStr() {
        return codeStr;
    }

    public String getName() {
        return name;
    }


}
