package cn.damai.boss.projectreport.manager.enums;

/**
 * 项目任务状态
 *
 */
public enum ProjectStatusEnum {

    New((short)1, "新建"), StatCompleted((short)2, "统计完成");

    //码
    private short code;

    //名称
    private String name;

    private ProjectStatusEnum(short code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据名称返回code
     *
     * @param name 名称
     * @return
     */
    public static short getCode(String name) {
        ProjectStatusEnum[] statusEnumArr = ProjectStatusEnum.values();
        for (ProjectStatusEnum statusEnum : statusEnumArr) {
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
        ProjectStatusEnum[] statusEnumArr = ProjectStatusEnum.values();
        for (ProjectStatusEnum statusEnum : statusEnumArr) {
            if (code == statusEnum.code) {
                return statusEnum.name;
            }
        }
        return null;
    }

    public short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}