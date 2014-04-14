package cn.damai.boss.projectreport.report.vo;

import java.util.List;

/**
 * 分区出票汇总表列表
* @ClassName: ProjectReportStandStatVo
* @Description: 
* @author zhangbinghong
* @date 2014-2-27 下午7:23:28
 */
public class StandStatVo {	
	/**
	 * 场次信息列表
	 */
	private List<PerformVo> performs;

	public List<PerformVo> getPerforms() {
		return performs;
	}

	public void setPerforms(List<PerformVo> performs) {
		this.performs = performs;
	}
	
}
