package cn.damai.boss.projectreport.manager.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.manager.dao.OperatorLogDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = true)
public class OperatorLogDAOCustomTest {
	
	@Autowired
	private OperatorLogDAO operatorLogDAO;
	
	@Test
	public void testSave(){
		operatorLogDAO.insertOperatorLog(2L, "测试", 1);		
	}
}