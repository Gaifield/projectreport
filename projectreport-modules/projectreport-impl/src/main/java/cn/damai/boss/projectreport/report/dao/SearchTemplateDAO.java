package cn.damai.boss.projectreport.report.dao;

import cn.damai.boss.projectreport.domain.Upt01SearchTemplate;
import cn.damai.crius.core.dao.BaseDao;

/**
 * 注释：模板编辑DAO
 * 作者：wenjunrong 【温俊荣】
 * 时间：14-2-27 下午3:54
 */
public interface SearchTemplateDAO extends BaseDao<Upt01SearchTemplate, Long>, SearchTemplateDAOCustom {
	
	/**
	 * 根据模板Id查找模板
	 * @param templateId
	 * @return
	 */
	public Upt01SearchTemplate findBytemplateId(Long templateId);
	
	
	
	
}
