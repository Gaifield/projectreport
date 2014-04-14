package cn.damai.boss.projectreport.report.vo;

/**
 * 注释：项目类别VO
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-1 上午3:53
 */
public class ProjectClassVo {
    private Long projectClassID;
    private Long parentClassID;
    private String className;
    private String note;
    private Long categoryID;

    public Long getProjectClassID() {
        return projectClassID;
    }

    public void setProjectClassID(Long projectClassID) {
        this.projectClassID = projectClassID;
    }

    public Long getParentClassID() {
        return parentClassID;
    }

    public void setParentClassID(Long parentClassID) {
        this.parentClassID = parentClassID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }
}
