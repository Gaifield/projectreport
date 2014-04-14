package cn.damai.boss.projectreport.report.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.stereotype.Controller;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.commons.model.ReturnData;
import cn.damai.boss.projectreport.report.action.base.BaseAction;
import cn.damai.boss.projectreport.report.service.SearchTemplateService;
import cn.damai.boss.projectreport.report.vo.SearchTemplateVo;
import cn.damai.boss.projectreport.report.vo.TemplateContentVo;

/**
 * 注释：编辑模板Action 作者：wenjunrong 【温俊荣】 时间：14-2-27 下午3:12
 */
@Namespace("/")
@Controller
public class SearchTemplateAction extends BaseAction {
	private static final Log log = LogFactory.getFactory().getInstance(
			SearchTemplateAction.class);
	private TemplateContentVo templateContentVo;
	private String templateName;
	private List<SearchTemplateVo> searchTemplateVos;
	private long templateId;
	@Resource
	private SearchTemplateService searchTemplateService;
	
	/**
	 * 加载模板
	 * @return
	 */
	@Action(value = "showTemplate", results = { @Result(name = "success", location = "/projectreport/highlevelsearch.jsp") })
	public String showTemplate(){
		try {
			searchTemplateVos = searchTemplateService.findSearchTemplate();
			setSearchTemplateVos(searchTemplateVos);
		} catch (ApplicationException e) {
			log.error(e.getMessage(), e);
		}
			return SUCCESS;
	}
	
	/**
	 *保存模板 ,编辑更新模板
	 */
	@Action(value="saveTemplate",results={@Result(name="success",type="json", params = { "root", "returnData", "contentType", "text/html" })})
	public String  saveTemplate(){
		ReturnData<SearchTemplateVo> returnData = new ReturnData<SearchTemplateVo>();
		SearchTemplateVo searchTemplateVo = null;
		try {
			if(templateId!=0){
				searchTemplateVo =searchTemplateService.modifySearchTemplate(templateContentVo, templateId);
			}else{
				searchTemplateVo = searchTemplateService.saveSearchTemplate(templateContentVo, templateName);
			}
			if(searchTemplateVo.getTag()!=0){
				//数据中已存在模板内容相同的模板
				returnData.setStatus(100);
				returnData.setData(searchTemplateVo);
				setReturnData(returnData);
			}else{
				//正常情况
				returnData.setStatus(200);
				returnData.setData(searchTemplateVo);
				setReturnData(returnData);
			}
		} catch (ApplicationException e) {
				log.error(e.getMessage(), e);
				//300模板名称相同
				returnData.setStatus(e.getErrorCode());
				returnData.setData(null);
				setReturnData(returnData);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询模板数是否饱和（最多5条）
	 * @return
	 */
	@Action(value="checkTemplateSize",results={@Result(name="success",type="json", params = { "root", "returnData", "contentType", "text/html" })})
	public String checkTemplateSize(){		
		ReturnData<String> returnData = new ReturnData<String>();
		try {
			boolean tag = searchTemplateService.findTemplateSize();
			if(tag){
				returnData.setStatus(100);
				returnData.setData("无法保存！已达最大模板数！");
				setReturnData(returnData);
			}else{
				returnData.setStatus(101);
				returnData.setData("正常");
				setReturnData(returnData);
			}
		} catch (ApplicationException e) {
			log.error(e.getMessage(), e);
		}
		return SUCCESS;
	}
	
	/**
	 * 编辑模板回显数据
	 * @return
	 */
	@Action(value="updateTemplate",results={@Result(name="success",type="json", params = { "root", "templateContentVo", "contentType", "text/html" })})
	public String updateTemplate(){		
		try {
			templateContentVo = searchTemplateService.findTemplateContentByTemplateId(templateId);
			setTemplateContentVo(templateContentVo);
		} catch (ApplicationException e) {
			log.error(e.getMessage(), e);
		}
		return SUCCESS;
	}
	
	/**
	 * 合并模板
	 * @return
	 */
	@Action(value="mergeTemplate",results={@Result(name="success",type="json", params = { "root", "returnData", "contentType", "text/html" })})
	public String mergeTemplate(){		
		ReturnData<String> returnData = new ReturnData<String>();
		try {
			searchTemplateService.motifyTemplateName(templateId, templateName);
			returnData.setStatus(HttpStatusEnum.Success.getCode());
			returnData.setData(HttpStatusEnum.Success.getName());
			setReturnData(returnData);
		} catch (ApplicationException e) {
			log.error(e.getMessage(), e);
		}
		return SUCCESS;
	}
	
	/**
	 * 删除模板
	 * @return
	 */
	@Action(value="deleteTemplate",results={@Result(name="success",type="json", params = { "root", "returnData", "contentType", "text/html" })})
	public String deleteTemplate(){		
		ReturnData<String> returnData = new ReturnData<String>();
		try {
			searchTemplateService.motifyTemplateStatus(templateId);
			returnData.setStatus(HttpStatusEnum.Success.getCode());
			returnData.setData(HttpStatusEnum.Success.getName());
			setReturnData(returnData);
		} catch (ApplicationException e) {
			log.error(e.getMessage(), e);
		}
		return SUCCESS;
	}
	
	
	public TemplateContentVo getTemplateContentVo() {
		return templateContentVo;
	}

	public void setTemplateContentVo(TemplateContentVo templateContentVo) {
		this.templateContentVo = templateContentVo;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<SearchTemplateVo> getSearchTemplateVos() {
		return searchTemplateVos;
	}

	public void setSearchTemplateVos(List<SearchTemplateVo> searchTemplateVos) {
		this.searchTemplateVos = searchTemplateVos;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	
}
