package cn.damai.boss.projectreport.report.vo;

import java.math.BigDecimal;

/**
 * 注释：折扣vo
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-6 下午5:27
 */
public class DisaccountVo {
    /**
     * 折扣名称
     */
    private String disaccountName;

    /**
     * 数量
     */
    private int quantity;

    /**
     * 金额
     */
    private BigDecimal amount = new BigDecimal("0");

    public String getDisaccountName() {
        return disaccountName;
    }

    public void setDisaccountName(String disaccountName) {
        this.disaccountName = disaccountName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
