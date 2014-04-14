package cn.damai.boss.projectreport.report.vo;

import java.util.List;

/**
 * 注释：出票汇总过滤条件vo
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-21 上午11:44
 */
public class SaleFilterVo {
    //数据来源
    private String source;
    //项目id
    private Long projectId;
    //场次id，格式：id1,id2,id3
    private String performIds;
    //场次id list
    private List<Long> performIdList;
    //页面选择radio，0：全部，1：上月，2：本月，3：自定义
    private String dateRadio = "0";
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //票类型，1, "赠票",2, "工作票", 3, "防涨票"
    private int ticketType;
    //销售方式，1：单票，2：套票
    private int saleMode;
    //是否有座，1：有座
    private int seatType;
    //是否分页
    private boolean isSplitPage;
    //当前页
    private int page;
    //每页显示条数
    private int pageSize;

    public Long getProjectId() {
        return projectId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getPerformIds() {
        return performIds;
    }

    public void setPerformIds(String performIds) {
        this.performIds = performIds;
    }

    public List<Long> getPerformIdList() {
        return performIdList;
    }

    public void setPerformIdList(List<Long> performIdList) {
        this.performIdList = performIdList;
    }

    public String getDateRadio() {
        return dateRadio;
    }

    public void setDateRadio(String dateRadio) {
        this.dateRadio = dateRadio;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTicketType() {
        return ticketType;
    }

    public void setTicketType(int ticketType) {
        this.ticketType = ticketType;
    }

    public int getSaleMode() {
        return saleMode;
    }

    public void setSaleMode(int saleMode) {
        this.saleMode = saleMode;
    }

    public int getSeatType() {
        return seatType;
    }

    public void setSeatType(int seatType) {
        this.seatType = seatType;
    }

    public boolean isSplitPage() {
        return isSplitPage;
    }

    public void setSplitPage(boolean isSplitPage) {
        this.isSplitPage = isSplitPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
