package cn.damai.boss.projectreport.report.service.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.service.ProjectReportReserveStatService;
import cn.damai.boss.projectreport.report.vo.ReserveStatVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-report.xml" })
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class ProjectReportReserveStatServiceImplTest {

	@Resource
	private ProjectReportReserveStatService projectReportReserveStatService;

	@Test
	public void testFindProjectReportStandStatList() {
		DynamicDataSourceHolder.putDataSourceName("1");
		try {
			ReserveStatVo vo = projectReportReserveStatService
					.findProjectReportReserveStatList("1",9718, null);
			System.out.println(vo);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
}
