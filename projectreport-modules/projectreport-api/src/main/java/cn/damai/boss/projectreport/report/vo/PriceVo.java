package cn.damai.boss.projectreport.report.vo;

import java.math.BigDecimal;

import cn.damai.boss.projectreport.report.enums.PriceTypeEnum;
import cn.damai.boss.projectreport.report.enums.TicketTypeEnum;

/**
 * 预留统计价格分组Vo
 *
 * @author Administrator
 */
public class PriceVo {

    /**
     * 票品类型 1：普通票，2：套票
     */
    private short ticketType;

    /**
     * 价格名称
     */
    private String priceName;

    /**
     * 价格显示名称
     */
    private String priceShowName;
    /**
     * 价格
     */
    private BigDecimal price;

    public PriceVo() {}

    public PriceVo(String priceName) {
        this.priceName = priceName;
    }

    public short getTicketType() {
        return ticketType;
    }

    public void setTicketType(short ticketType) {
        this.ticketType = ticketType;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public String getPriceShowName() {
        if(ticketType==PriceTypeEnum.Normal.getCode()){
        	return priceName+"("+ price.doubleValue() +")";
        }else{
        	return priceName;
        }
    }

    public void setPriceShowName(String priceShowName) {
    	this.priceShowName = priceShowName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 如过价格名称相同，则认为两个对象相等
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof PriceVo) {
            PriceVo priceVo = (PriceVo) obj;
            String name = priceVo.getPriceName();
            return priceName != null && priceName.equals(name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((priceName == null) ? 0 : priceName.hashCode());
        result = prime * result + ticketType;
        return result;
    }
}