package cn.damai.boss.projectreport.job.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
		"classpath:applicationContext-dubbo.xml",
		"classpath:applicationContext-jms-listener.xml",
		"classpath:applicationContext-quartz.xml" })
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class ListenerTest {

	@Test
	public void testMessageListener() {
		while (true) {
			System.out.println(new Date());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}