package cn.damai.boss.projectreport.report.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 注释：场次出票vo
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-6 下午3:53
 */
public class SaleVo {
    //项目名称
    private String projectName;
    //场次名称
    private String performName;
    //场次Id
    private Long performId;
    //演出时间
    private String performTime;
    //出票vo
    private List<SaleRowVo> saleRowVoList;
    //所有打折
    private List<String> allDisaccountVoList;
    //总计出票数量
    private int totalQuantity;
    //总计出票金额
    private BigDecimal totalAmount;
    //工作票数量
    private int staffQuantity;
    //防涨票数量
    private int protectQuantity;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getPerformId() {
        return performId;
    }

    public void setPerformId(Long performId) {
        this.performId = performId;
    }

    public String getPerformName() {
        return performName;
    }

    public void setPerformName(String performName) {
        this.performName = performName;
    }

    public String getPerformTime() {
        return performTime;
    }

    public void setPerformTime(String performTime) {
        this.performTime = performTime;
    }

    public List<SaleRowVo> getSaleRowVoList() {
        return saleRowVoList;
    }

    public void setSaleRowVoList(List<SaleRowVo> saleRowVoList) {
        this.saleRowVoList = saleRowVoList;
    }

    public List<String> getAllDisaccountVoList() {
        return allDisaccountVoList;
    }

    public void setAllDisaccountVoList(List<String> allDisaccountVoList) {
        this.allDisaccountVoList = allDisaccountVoList;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getStaffQuantity() {
        return staffQuantity;
    }

    public void setStaffQuantity(int staffQuantity) {
        this.staffQuantity = staffQuantity;
    }

    public int getProtectQuantity() {
        return protectQuantity;
    }

    public void setProtectQuantity(int protectQuantity) {
        this.protectQuantity = protectQuantity;
    }
}
