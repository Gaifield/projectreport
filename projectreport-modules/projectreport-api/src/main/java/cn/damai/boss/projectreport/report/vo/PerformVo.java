package cn.damai.boss.projectreport.report.vo;

import java.util.Date;
import java.util.List;

/**
 * 场次VO类
 *
 * @author zhangbinghong
 * @ClassName: PerformVo
 * @Description: Maitix场次信息
 * @date 2014-2-27 下午7:22:27
 */
public class PerformVo<T> {

    /**
     * 商品中心场次Id
     */
    private long performId;

    /**
     * Maitix场次Id
     */
    private long performInfoID;
    /**
     * 场次名称
     */
    private String performName;

    /**
     * 场次时间
     */
    private Date performTime;
    /**
     * 演出时间 字符串
     */
    private String performTimeString;
    /**
     * 场馆Id
     */
    private long filedId;
    /**
     * 场馆名称
     */
    private String fieldName;

    /**
     * 场次看台统计列表
     */
    private List<T> list;

    /**
     * 工作票数量
     */
    private int staffQuantity;
    /**
     * 防涨票数量
     */
    private int protectQuantity;

    public long getPerformId() {
        return performId;
    }

    public void setPerformId(long performId) {
        this.performId = performId;
    }

    public long getPerformInfoID() {
        return performInfoID;
    }

    public void setPerformInfoID(long performInfoID) {
        this.performInfoID = performInfoID;
    }

    public String getPerformName() {
        return performName;
    }

    public void setPerformName(String performName) {
        this.performName = performName;
    }

    public Date getPerformTime() {
        return performTime;
    }

    public void setPerformTime(Date performTime) {
        this.performTime = performTime;
    }

    public String getPerformTimeString() {
        return performTimeString;
    }

    public void setPerformTimeString(String performTimeString) {
        this.performTimeString = performTimeString;
    }

    public long getFiledId() {
        return filedId;
    }

    public void setFiledId(long filedId) {
        this.filedId = filedId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
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