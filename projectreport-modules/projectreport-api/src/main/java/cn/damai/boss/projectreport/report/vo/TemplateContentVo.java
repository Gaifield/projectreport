package cn.damai.boss.projectreport.report.vo;


/**
 * 注释：模板内容VO
 * 作者：wenjunrong 【温俊荣】
 * 时间：14-2-27 下午3:18
 */
public class TemplateContentVo {
	//项目类别
    private int projectType;
    //项目状态
    private int projectStatus;
    //项目id
    private int projectId;
    //演出城市
    private String performCity;
    //项目名称
    private String projectName;
    //演出开始时间
    private String startTime;
    //演出结束时间
    private String endTime;
    //演出场馆
    private String performField;
    
    public TemplateContentVo(){
    	
    }
    
	public int getProjectType() {
		return projectType;
	}
	public void setProjectType(int projectType) {
		this.projectType = projectType;
	}
	public int getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(int projectStatus) {
		this.projectStatus = projectStatus;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getPerformCity() {
		return performCity;
	}
	public void setPerformCity(String performCity) {
		this.performCity = performCity;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
	public String getPerformField() {
		return performField;
	}
	public void setPerformField(String performField) {
		this.performField = performField;
	}

	
}
