package cn.damai.boss.projectreport.report.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.service.PerformInfoService;
import cn.damai.boss.projectreport.report.vo.PerformVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-report.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class PerformInfoServiceImplText {

	@Resource
	private PerformInfoService performInfoService;
	
	@Test
	public void queryPerformInfoByProjectId() {
		
		try {
			List<PerformVo> list = performInfoService.queryPerformInfoByProjectId(541L, (short) 1,null,null);
			System.out.println(list.size());
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
