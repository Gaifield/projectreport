package cn.damai.boss.projectreport.report.service.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.utils.ModelUtils;
import cn.damai.boss.projectreport.domain.Upt01SearchTemplate;
import cn.damai.boss.projectreport.report.context.ReportUserContextUtil;
import cn.damai.boss.projectreport.report.dao.SearchTemplateDAO;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.enums.SearchTemplateStatusEnum;
import cn.damai.boss.projectreport.report.service.SearchTemplateService;
import cn.damai.boss.projectreport.report.vo.SearchTemplateVo;
import cn.damai.boss.projectreport.report.vo.TemplateContentVo;

/**
 * 注释：模板编辑service实现类
 * 作者：wenjunrong 【温俊荣】
 * 时间：14-2-27 下午3:51
 */
@Service
public class SearchTemplateServiceImpl implements SearchTemplateService {
	
	@Resource
	private SearchTemplateDAO searchTemplateDAO;
	
	
	
	
	
	/**
	 * 保存模板
	 * @param templateContentVo
	 * @param templateName
	 * @return
	 */
	@Override
	@Transactional(value = "projectReportTransactionManager")
	public SearchTemplateVo saveSearchTemplate(TemplateContentVo templateContentVo,
			String templateName) throws ApplicationException {
		
		//Long userId = ReportUserContextUtil.getUserId();
		ObjectMapper objectMapper = new ObjectMapper();
		Upt01SearchTemplate searchTemplete = new Upt01SearchTemplate();
		SearchTemplateVo searchTemplateVo = null;
		if(findByTemplateName(templateName,ReportUserContextUtil.getBusinessUserId())){
			throw new ApplicationException(300,"模板名称已存在！");
		}
		//待修改
		searchTemplete.setUserId(ReportUserContextUtil.getBusinessUserId());
		searchTemplete.setTemplateName(templateName);
		searchTemplete.setCreateTime(new Date());
		searchTemplete.setModifyTime(new Date());
		searchTemplete.setStatus((short)SearchTemplateStatusEnum.Normal.getCode());
		try {
			//将vo对象转换成json字符串
			String templateContent =objectMapper.writeValueAsString(templateContentVo);
			searchTemplete.setTemplateContent(templateContent);
			//查询模板内容是否已存在
			Upt01SearchTemplate searchTemplate = findByTemplateContent(templateContent,ReportUserContextUtil.getBusinessUserId());
			if(searchTemplate!=null){
				searchTemplateVo = ModelUtils.fromDomainObjectToVo(SearchTemplateVo.class, searchTemplate);
				searchTemplateVo.setTag(1);   //在控制层判断是否为异常VO，正常VO中的tag属性为0。
				return searchTemplateVo;
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		searchTemplateVo = ModelUtils.fromDomainObjectToVo(SearchTemplateVo.class,searchTemplateDAO.save(searchTemplete));
		return searchTemplateVo;
	}
	
	/**
	 * 编辑保存模板
	 */
	@Override
	@Transactional(value = "projectReportTransactionManager")
	public SearchTemplateVo modifySearchTemplate(TemplateContentVo templateContentVo,long templateId) throws ApplicationException {
		SearchTemplateVo searchTemplateVo = null;
		Upt01SearchTemplate searchTemplate = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			//将vo对象转换成json字符串
			String templateContent =objectMapper.writeValueAsString(templateContentVo);
			searchTemplate = findByTemplateContent(templateContent, ReportUserContextUtil.getBusinessUserId());
			if(searchTemplate!=null){
				searchTemplateVo = ModelUtils.fromDomainObjectToVo(SearchTemplateVo.class, searchTemplate);
				searchTemplateVo.setTag(1);   //在控制层判断是否为异常VO，正常VO中的tag属性为0。
			}else{
				searchTemplate = searchTemplateDAO.findBytemplateId(templateId);
				if(searchTemplate!=null){
					searchTemplate.setTemplateContent(templateContent);
					searchTemplate.setModifyTime(new Date());
					searchTemplateDAO.save(searchTemplate);
					searchTemplateVo = ModelUtils.fromDomainObjectToVo(SearchTemplateVo.class, searchTemplate);
					searchTemplateVo.setTag(0);
				}
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchTemplateVo;
	}
	
	/**
	 * 根据模板Id回显模板内容
	 * @param templateId
	 * @return
	 */
	@Override
	@Transactional(value = "projectReportTransactionManager")
	public TemplateContentVo findTemplateContentByTemplateId(Long templateId) throws ApplicationException{
		ObjectMapper objectMapper = new ObjectMapper();
		Upt01SearchTemplate template = searchTemplateDAO.findBytemplateId(templateId);
		String contentJson = template.getTemplateContent();
		try {
			return objectMapper.readValue(contentJson,TemplateContentVo.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加载模板列表
	 * @return
	 */
	@Override
	@Transactional(value = "projectReportTransactionManager")
	public List<SearchTemplateVo> findSearchTemplate() throws ApplicationException{
		//ReportUserContextUtil.getUserId()
		List<Upt01SearchTemplate> searchTemplates = searchTemplateDAO.queryNormalSearchTemplate(ReportUserContextUtil.getBusinessUserId());
		List<SearchTemplateVo> searchTemplateVo = new ArrayList<SearchTemplateVo>();
		if(searchTemplates!=null&&searchTemplates.size()!=0){
			for(int i=0;i<searchTemplates.size();i++){
				searchTemplateVo.add(ModelUtils.fromDomainObjectToVo(SearchTemplateVo.class, searchTemplates.get(i)));
			}
		}
		return searchTemplateVo;
	}
	
	/**
	 * 查询是否模板名称已存在
	 * @param templateName
	 * @return
	 */
	public boolean findByTemplateName(String templateName,Long userId) {
		List<Upt01SearchTemplate> searchTemplate = searchTemplateDAO.findByTemplateName(templateName, userId);
		if(searchTemplate!=null&&searchTemplate.size()!=0){
			return true;
		}
		return false;
	}
	
	/**
	 * 查询模板内容是否已存在
	 * @param templateContent
	 * @return
	 */
	public Upt01SearchTemplate findByTemplateContent(String templateContent,Long userId){
		List<Upt01SearchTemplate> searchTemplate = searchTemplateDAO.findByTemplateContent(templateContent,userId);
		if(searchTemplate!=null&&searchTemplate.size()!=0){
			return searchTemplate.get(0);
		}
		return null;
	}
	
	/**
	 * 合并模板
	 * @param templateName
	 * @return
	 */
	@Override
	@Transactional(value = "projectReportTransactionManager")
	public boolean motifyTemplateName(Long templateId,String templateName) throws ApplicationException{
		Upt01SearchTemplate searchTemplate = searchTemplateDAO.findBytemplateId(templateId);
		searchTemplate.setTemplateName(templateName);
		searchTemplate.setModifyTime(new Date());
		return searchTemplateDAO.save(searchTemplate)!=null;
	}
	
	/**
	 * 查询模板是否饱和
	 * @return
	 */
	@Override
	@Transactional(value = "projectReportTransactionManager")
	public boolean findTemplateSize() throws ApplicationException {
		//验证数据库中非删除状态的模板数是否超过五条
		//ReportUserContextUtil.getUserId()
		DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.Report.getCodeStr());
		List<Upt01SearchTemplate> currentTemplate = searchTemplateDAO.queryNormalSearchTemplate(ReportUserContextUtil.getBusinessUserId());
		if(currentTemplate!=null&&currentTemplate.size()==5){
			return true;
		}
		return false;
	}
	
	/**
	 * 删除模板
	 * @return
	 */
	@Override
	@Transactional(value = "projectReportTransactionManager")
	public boolean motifyTemplateStatus(Long templateId)
			throws ApplicationException {
		Upt01SearchTemplate searchTemplate = searchTemplateDAO.findBytemplateId(templateId);
		if(searchTemplate!=null){
			searchTemplate.setStatus((short)SearchTemplateStatusEnum.Delete.getCode());
			searchTemplate.setModifyTime(new Date());
			searchTemplateDAO.save(searchTemplate);
			return true;
		}
		return false;
	}
	
	
	
	

}
