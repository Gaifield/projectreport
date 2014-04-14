package cn.damai.boss.projectreport.report.service;

import java.util.List;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.domain.Upt01SearchTemplate;
import cn.damai.boss.projectreport.report.vo.SearchTemplateVo;
import cn.damai.boss.projectreport.report.vo.TemplateContentVo;

/**
 * 注释：模板编辑service
 * 作者：wenjunrong 【温俊荣】
 * 时间：14-2-27 下午3:48
 */
public interface SearchTemplateService {
	/**
	 * 保存模板
	 * @param templateContentVo
	 * @param templateName
	 * @return
	 */
	public SearchTemplateVo saveSearchTemplate(TemplateContentVo templateContentVo,String templateName) throws ApplicationException;
	
	/**
	 * 编辑保存模板
	 */
	public 	SearchTemplateVo modifySearchTemplate(TemplateContentVo templateContentVo,long templateId) throws ApplicationException;
	
	/**
	 * 根据模板Id回显模板内容
	 * @param templateId
	 * @return
	 */
	public TemplateContentVo findTemplateContentByTemplateId(Long templateId) throws ApplicationException;
	
	/**
	 * 加载模板列表
	 * @return
	 */
	public List<SearchTemplateVo> findSearchTemplate() throws ApplicationException;
	
	/**
	 * 查询模板是否饱和
	 * @return
	 */
	public boolean findTemplateSize() throws ApplicationException;
	
	/**
	 * 合并模板
	 * @return
	 */
	public boolean motifyTemplateName(Long templateId,String templateName) throws ApplicationException;
	
	/**
	 * 删除模板
	 * @return
	 */
	public boolean motifyTemplateStatus(Long templateId) throws ApplicationException;
	
}
