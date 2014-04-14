package cn.damai.boss.projectreport.report.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.service.SellerSaleService;
import cn.damai.boss.projectreport.report.vo.SellerSaleReportVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-report.xml" })
@TransactionConfiguration(transactionManager = "maitixTransactionManager", defaultRollback = false)
public class SellerSaleServiceImplTest {

	@Resource
	private SellerSaleService sellerSaleService;

	@Test
	public void testFindSellerSaleStat() {
		DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.Maitix
				.getCodeStr());
		List<Long> projectIds = Arrays.asList(new Long[] { 1584L });
		String dataSource="1";
		long projectId = 3780L;
		try {
			SellerSaleReportVo result =sellerSaleService.findSellerSaleStat(dataSource, projectId, null, null, null, null, 5, 1,true);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
