package cn.damai.boss.projectreport.report.dao.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.domain.Upt01SearchTemplate;
import cn.damai.boss.projectreport.report.dao.SearchTemplateDAO;

/**
 * Created by 温俊荣 on 14-2-27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-report.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class SearchTemplateDAOCustomImplTest {
	
	@Autowired
	private SearchTemplateDAO searchTemplateDAO;
	
	@Test
	public void queryNormalSearchTemplate(){
		List<Upt01SearchTemplate> templates = searchTemplateDAO.queryNormalSearchTemplate((long)2);
		if(templates!=null&&templates.size()!=0){
			System.out.println(templates.size());
		}
	}
}
