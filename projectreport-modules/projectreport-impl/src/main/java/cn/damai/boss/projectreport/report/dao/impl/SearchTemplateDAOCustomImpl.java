package cn.damai.boss.projectreport.report.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.damai.boss.projectreport.common.dao.BaseJpaDaoSupport;
import cn.damai.boss.projectreport.domain.Upt01SearchTemplate;
import cn.damai.boss.projectreport.report.dao.SearchTemplateDAOCustom;
import cn.damai.boss.projectreport.report.enums.SearchTemplateStatusEnum;
/**
 * 注释：编辑模板DAO实现类
 * 作者：wenjunrong 【温俊荣】
 * 时间：14-2-27 下午7:08
 */
@Repository
public class SearchTemplateDAOCustomImpl extends BaseJpaDaoSupport<Upt01SearchTemplate, Long> implements SearchTemplateDAOCustom {
	/**
	 * 查询所有状态为正常的模板对象
	 * @return
	 */
	@Override
	public List<Upt01SearchTemplate> queryNormalSearchTemplate(Long userId) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" from Upt01SearchTemplate obj WHERE obj.status <> ? ");
		paramList.add((short)SearchTemplateStatusEnum.Delete.getCode());
		sql.append(" AND obj.userId = ?");
		paramList.add(userId);
		sql.append(" order by obj.modifyTime desc");
		return super.executeQuery(sql.toString(), paramList);
	}
	
	/**
	 * 查询模板内容是否已存在
	 * @param templateContent
	 * @return
	 */
	@Override
	public List<Upt01SearchTemplate> findByTemplateContent(String templateContent,Long userId) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" from Upt01SearchTemplate obj WHERE obj.status <> ? ");
		paramList.add((short)SearchTemplateStatusEnum.Delete.getCode());
		sql.append(" AND obj.userId = ?");
		paramList.add(userId);
		sql.append(" AND obj.templateContent = ?");
		paramList.add(templateContent);
		sql.append(" order by obj.modifyTime desc");
		return super.executeQuery(sql.toString(), paramList);
	}
	
	/**
	 * 根据模板名称查询模板
	 * @return
	 */
	@Override
	public List<Upt01SearchTemplate> findByTemplateName(String templateName,
			Long userId) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" from Upt01SearchTemplate obj WHERE obj.status <> ? ");
		paramList.add((short)SearchTemplateStatusEnum.Delete.getCode());
		sql.append(" AND obj.userId = ?");
		paramList.add(userId);
		sql.append(" AND obj.templateName = ?");
		paramList.add(templateName);
		sql.append(" order by obj.modifyTime desc");
		return super.executeQuery(sql.toString(), paramList);
	}

}
