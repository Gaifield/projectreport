package cn.damai.boss.projectreport.report.dao.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.report.dao.BoxOfficeDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-report.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class BoxOfficeDaoImplTest {

	@Resource
	private BoxOfficeDao bod;
	
	@Test
	public void findProjectIsChooseSeatOn() {
		boolean bool = bod.findProjectIsChooseSeatOn(987L);
		System.out.println(bool);
	}

	@Test
	public void findBoxOfficeByProjectIdOrPerformIds(){
		bod.findBoxOfficeByProjectIdOrPerformIds(9718L, null);
	}
	
}
