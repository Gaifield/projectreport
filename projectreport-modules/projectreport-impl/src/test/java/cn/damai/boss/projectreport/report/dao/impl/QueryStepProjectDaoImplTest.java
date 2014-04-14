package cn.damai.boss.projectreport.report.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.dao.MultiProjectStatDao;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.BSGSiteEnum;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-report.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class QueryStepProjectDaoImplTest {
	
	@Autowired
	private MultiProjectStatDao queryStepProjectDao;

	/**
	 * 跨项目查询 
	 * 
	 * @author：guwei 【顾炜】
	 * time：2014-3-1 上午1:48:34
	 */
	@Test
	public void findQueryStepProjectInfo() {
		//DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.Maitix.getCode());
		try {
			List<Long> list = new ArrayList<Long>();
			list.add(18716L);
			list.add(18717L);
			list.add(18718L);
			List<ReportProjectVo> voList = queryStepProjectDao.queryMulitProjectInfoList(null,list);
			System.out.println(voList.size());
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
