package cn.damai.boss.projectreport.report.enums;

/**
 * 代理商 信息来源
 * 1：maitix 系统 
 * 2：大麦网
 * 3：M+平台
 * @author：guwei 【顾炜】
 * time：2014-3-8 下午7:56:03
 *
 */
public enum AgentFromEnum {
    Maitix(1, "maitix系统"), Damai(2, "大麦网"),MPlus(3,"M+平台");

    //码
    private int code;

    //名称
    private String name;

    private AgentFromEnum(int code, String name) {
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
        AgentFromEnum[] ticketTypeEnumArr = AgentFromEnum.values();
        for (AgentFromEnum ticketTypeEnum : ticketTypeEnumArr) {
            if (name != null && name.equals(ticketTypeEnum.name)) {
                return ticketTypeEnum.code;
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
    public static String getName(Integer code) {
        AgentFromEnum[] ticketTypeEnumArr = AgentFromEnum.values();
        for (AgentFromEnum ticketTypeEnum : ticketTypeEnumArr) {
            if (code != null && code.equals(ticketTypeEnum.code)) {
                return ticketTypeEnum.name;
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
