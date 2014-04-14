package cn.damai.boss.projectreport.manager.vo;

/**
 * 注释：报表VO
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-21 下午4:25
 */
public class ReportVo
{
    private Long reportId;
    private String reportName;
    private String reportUrl;

    public Long getReportId()
    {
        return reportId;
    }

    public void setReportId(Long reportId)
    {
        this.reportId = reportId;
    }

    public String getReportName()
    {
        return reportName;
    }

    public void setReportName(String reportName)
    {
        this.reportName = reportName;
    }

    public String getReportUrl()
    {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl)
    {
        this.reportUrl = reportUrl;
    }
}
