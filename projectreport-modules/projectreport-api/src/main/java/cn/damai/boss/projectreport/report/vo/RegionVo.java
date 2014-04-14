package cn.damai.boss.projectreport.report.vo;

/**
 * 项目城市Vo
 * @author wenjunrong
 *
 */
public class RegionVo {
	//项目城市ID
	private Long areaId; 
	//项目城市名称
	private String areaName;
	
	public RegionVo(){
		
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}
