package cn.damai.boss.projectreport.report.service.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.domain.Upt01SearchTemplate;
import cn.damai.boss.projectreport.report.service.SearchTemplateService;
import cn.damai.boss.projectreport.report.vo.SearchTemplateVo;
import cn.damai.boss.projectreport.report.vo.TemplateContentVo;

/**
 * Created by 温俊荣 on 14-2-28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-report.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class SearchTemplateServiceImplTest {
	
	@Autowired
	private SearchTemplateService searchTemplateService;
	
	@Test
	public void saveSearchTemplate(){
		TemplateContentVo templateVo = new TemplateContentVo();
		templateVo.setProjectType(23);
		templateVo.setProjectId(31);
		templateVo.setEndTime("2014-04-27");
		templateVo.setStartTime("2014-06-27");
		templateVo.setProjectStatus(1);
		templateVo.setPerformCity("北京");
		templateVo.setPerformField("鸟巢");
		templateVo.setProjectName("艾薇儿北京演唱会");
		try {
			SearchTemplateVo searchTemplateVo = searchTemplateService.saveSearchTemplate(templateVo, "模板五");
			System.out.println(searchTemplateVo.toString());
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void searchTemplateVo(){
		List<SearchTemplateVo> templateVo;
		try {
			templateVo = searchTemplateService.findSearchTemplate();
			if(templateVo!=null&&templateVo.size()!=0){
				System.out.println(templateVo.size());
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		
	}
}
