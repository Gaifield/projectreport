package cn.damai.boss.projectreport.report.enums;

/**
 * 票价类型
 * @author Administrator
 *
 */
public enum PriceTypeEnum {
    Normal(1, "普通票"), Promotion(2, "套票票");

    //码
    private int code;

    //名称
    private String name;

    private PriceTypeEnum(int code, String name) {
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
    	PriceTypeEnum[] priceTypeEnumArr = PriceTypeEnum.values();
        for (PriceTypeEnum priceTypeEnum : priceTypeEnumArr) {
            if (name != null && name.equals(priceTypeEnum.name)) {
                return priceTypeEnum.code;
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
    	PriceTypeEnum[] priceTypeEnumArr = PriceTypeEnum.values();
        for (PriceTypeEnum priceTypeEnum : priceTypeEnumArr) {
            if (priceTypeEnum.getCode()==code) {
                return priceTypeEnum.name;
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
