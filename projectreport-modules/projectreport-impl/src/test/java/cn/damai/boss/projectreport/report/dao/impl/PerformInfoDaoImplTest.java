package cn.damai.boss.projectreport.report.dao.impl;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.dao.PerformInfoDao;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-report.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class PerformInfoDaoImplTest {

	@Autowired
	private PerformInfoDao performInfoDao;
	
	
	@Test
	public void findPerformInfoByProjectId() {
		DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.BJMaitix.getCodeStr());
		try {
			performInfoDao.findPerformInfoByProjectId(541L,null,null);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
