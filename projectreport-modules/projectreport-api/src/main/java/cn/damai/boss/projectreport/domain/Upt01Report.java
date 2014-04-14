package cn.damai.boss.projectreport.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Upt01Report entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_report")
public class Upt01Report implements java.io.Serializable
{

    // Fields

    private Long reportId;
    private String reportName;
    private String reportUrl;
    private List<Upt01RoleReport> upt01RoleReports = new ArrayList<Upt01RoleReport>();

    // Constructors

    /**
     * default constructor
     */
    public Upt01Report()
    {
    }

    /**
     * minimal constructor
     */
    public Upt01Report(String reportName, String reportUrl)
    {
        this.reportName = reportName;
        this.reportUrl = reportUrl;
    }

    /**
     * full constructor
     */
    public Upt01Report(String reportName, String reportUrl, List<Upt01RoleReport> upt01RoleReports)
    {
        this.reportName = reportName;
        this.reportUrl = reportUrl;
        this.upt01RoleReports = upt01RoleReports;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "report_id", unique = true, nullable = false)
    public Long getReportId()
    {
        return this.reportId;
    }

    public void setReportId(Long reportId)
    {
        this.reportId = reportId;
    }

    @Column(name = "report_name", nullable = false, length = 50)
    public String getReportName()
    {
        return this.reportName;
    }

    public void setReportName(String reportName)
    {
        this.reportName = reportName;
    }

    @Column(name = "report_url", nullable = false, length = 100)
    public String getReportUrl()
    {
        return this.reportUrl;
    }

    public void setReportUrl(String reportUrl)
    {
        this.reportUrl = reportUrl;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upt01Report")
    public List<Upt01RoleReport> getUpt01RoleReports()
    {
        return upt01RoleReports;
    }

    public void setUpt01RoleReports(List<Upt01RoleReport> upt01RoleReports)
    {
        this.upt01RoleReports = upt01RoleReports;
    }
}