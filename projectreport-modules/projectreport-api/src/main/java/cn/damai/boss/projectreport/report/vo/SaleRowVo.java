package cn.damai.boss.projectreport.report.vo;

import java.util.List;

/**
 * 注释：出票汇总VO
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-6 下午2:04
 */
public class SaleRowVo {
    //价格vo
    private PriceVo priceVo;
    //可售总票房
    private PriceCellVo priceTotalSale;
    //折扣vo
    private List<DisaccountVo> disaccountVoList;
    //赠品出票
    private PriceCellVo presentSale;
    //工作票出票
    private PriceCellVo staffSale;
    //小计
    private PriceCellVo totalSaleCell;
    //剩余票房
    private PriceCellVo leftSale;
    //预留票房
    private PriceCellVo reserveSale;
    //当前可售票房
    private PriceCellVo availableSale;
    //工作票数量
    private int staffQuantity;
    //防涨票数量
    private int protectQuantity;


    public PriceVo getPriceVo() {
        return priceVo;
    }

    public void setPriceVo(PriceVo priceVo) {
        this.priceVo = priceVo;
    }

    public PriceCellVo getPriceTotalSale() {
        return priceTotalSale;
    }

    public void setPriceTotalSale(PriceCellVo priceTotalSale) {
        this.priceTotalSale = priceTotalSale;
    }

    public List<DisaccountVo> getDisaccountVoList() {
        return disaccountVoList;
    }

    public void setDisaccountVoList(List<DisaccountVo> disaccountVoList) {
        this.disaccountVoList = disaccountVoList;
    }

    public PriceCellVo getPresentSale() {
        return presentSale;
    }

    public void setPresentSale(PriceCellVo presentSale) {
        this.presentSale = presentSale;
    }

    public PriceCellVo getStaffSale() {
        return staffSale;
    }

    public void setStaffSale(PriceCellVo staffSale) {
        this.staffSale = staffSale;
    }

    public PriceCellVo getTotalSaleCell() {
        return totalSaleCell;
    }

    public void setTotalSaleCell(PriceCellVo totalSaleCell) {
        this.totalSaleCell = totalSaleCell;
    }

    public PriceCellVo getLeftSale() {
        return leftSale;
    }

    public void setLeftSale(PriceCellVo leftSale) {
        this.leftSale = leftSale;
    }

    public PriceCellVo getReserveSale() {
        return reserveSale;
    }

    public void setReserveSale(PriceCellVo reserveSale) {
        this.reserveSale = reserveSale;
    }

    public PriceCellVo getAvailableSale() {
        return availableSale;
    }

    public void setAvailableSale(PriceCellVo availableSale) {
        this.availableSale = availableSale;
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
