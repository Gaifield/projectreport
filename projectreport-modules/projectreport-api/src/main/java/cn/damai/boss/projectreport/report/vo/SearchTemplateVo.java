package cn.damai.boss.projectreport.report.vo;

import java.util.Date;

/**
 * 注释：模板VO
 * 作者：wenjunrong 【温俊荣】
 * 时间：14-2-27 下午6:53
 */
public class SearchTemplateVo {
	private int tag;
	
	private Long templateId;         //模板id
	
	private String templateName;     //模板名称
	
	public SearchTemplateVo(){
		
	}
	
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
	
	
}
