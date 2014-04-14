package cn.damai.boss.projectreport.report.vo;

/**
 * 注释：项目VO
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-26 下午4:25
 */
public class ReportProjectVo {
    //项目类别id，使用ProjectClass的CategoryID字段
    private Long categoryID;
    //项目类别
    private String className;
    //项目id
    private Long projectId;
    //项目id对应的商品中心id
    private Long piaoCnId;
    //项目名称
    private String projectName;
    //项目状态
    private int projectStatus;
    private String projectStatusName;
    //演出开始时间
    private String startTime;
    //演出结束时间
    private String endTime;
    //演出城市
    private String performCity;
    //演出场馆id
    private Long performFieldId;
    //演出场馆
    private String performField;
    //今日销售金额
    private String todayMoney;
    //总销售金额
    private String totalMoney;
    //剩余票房
    private String remainingBoxOffice;
    //数据来源，北京、上海、广州
    private String dataResource;
    /**
     * 是否选坐：1是；0否。
     */
    private int chooseSeatOn;
    //是否是首页，true：首页查询
    private boolean isIndex;

    //当前页
    private int page;
    //当前页显示条数据
    private int pageSize;

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getPiaoCnId() {
        return piaoCnId;
    }

    public void setPiaoCnId(Long piaoCnId) {
        this.piaoCnId = piaoCnId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(int projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getProjectStatusName() {
        return projectStatusName;
    }

    public void setProjectStatusName(String projectStatusName) {
        this.projectStatusName = projectStatusName;
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

    public String getPerformCity() {
        return performCity;
    }

    public void setPerformCity(String performCity) {
        this.performCity = performCity;
    }

    public Long getPerformFieldId() {
        return performFieldId;
    }

    public void setPerformFieldId(Long performFieldId) {
        this.performFieldId = performFieldId;
    }

    public String getPerformField() {
        return performField;
    }

    public void setPerformField(String performField) {
        this.performField = performField;
    }

    public String getTodayMoney() {
        return todayMoney;
    }

    public void setTodayMoney(String todayMoney) {
        this.todayMoney = todayMoney;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getRemainingBoxOffice() {
        return remainingBoxOffice;
    }

    public void setRemainingBoxOffice(String remainingBoxOffice) {
        this.remainingBoxOffice = remainingBoxOffice;
    }

    public String getDataResource() {
        return dataResource;
    }

    public void setDataResource(String dataResource) {
        this.dataResource = dataResource;
    }

    public int getChooseSeatOn() {
        return chooseSeatOn;
    }

    public void setChooseSeatOn(int chooseSeatOn) {
        this.chooseSeatOn = chooseSeatOn;
    }

    public boolean isIndex() {
        return isIndex;
    }

    public void setIndex(boolean isIndex) {
        this.isIndex = isIndex;
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