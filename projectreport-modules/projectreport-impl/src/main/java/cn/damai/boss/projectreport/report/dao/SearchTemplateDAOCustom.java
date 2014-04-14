package cn.damai.boss.projectreport.report.dao;

import java.util.List;

import cn.damai.boss.projectreport.domain.Upt01SearchTemplate;

/**
 * 注释：编辑模板DAO扩展接口
 * 作者：wenjunrong 【温俊荣】
 * 时间：14-2-27 下午5:24
 */
public interface SearchTemplateDAOCustom {
	
	/**
	 * 查询所有状态为正常的模板对象
	 * @return
	 */
	public List<Upt01SearchTemplate> queryNormalSearchTemplate(Long userId);
	
	/**
	 * 查询模板内容是否已存在
	 * @param templateContent
	 * @return
	 */
	public List<Upt01SearchTemplate> findByTemplateContent(String templateContent,Long userId);
	
	/**
	 * 根据模板名称查询模板
	 * @return
	 */
	public List<Upt01SearchTemplate> findByTemplateName(String templateName,Long userId);
}
