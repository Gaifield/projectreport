package cn.damai.boss.projectreport.report.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.service.MultiProjectStatService;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-report.xml" })
@TransactionConfiguration(transactionManager = "maitixTransactionManager", defaultRollback = false)
public class MultiProjectStatServiceImplTest {

	@Resource
	private MultiProjectStatService multiProjectStatService;

	@Test
	public void testFindMultiProjectStatList() {
		DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.Maitix
				.getCodeStr());
		List<Long> projectIds = Arrays.asList(new Long[] { 1584L });
		try {
			List<ReportProjectVo> list = null;// multiProjectStatService.findMultiProjectStatList(projectIds);
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
