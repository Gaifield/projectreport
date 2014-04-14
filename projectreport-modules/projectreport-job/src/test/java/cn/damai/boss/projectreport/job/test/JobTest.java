package cn.damai.boss.projectreport.job.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.job.api.JobService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
		"classpath:applicationContext-dubbo.xml",
		"classpath:applicationContext-jms.xml",
		"classpath:applicationContext-quartz.xml" })
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class JobTest {

	@Autowired
	private JobService jobService;
	
	@Test
	public void testSendMessage() {
		while (true) {
			jobService.execute();
			try {
				Thread.sleep(1000 * 30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}