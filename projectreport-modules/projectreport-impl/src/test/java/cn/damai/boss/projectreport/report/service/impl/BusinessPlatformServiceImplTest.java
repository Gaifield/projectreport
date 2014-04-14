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
import cn.damai.boss.projectreport.report.service.BusinessPlatformService;
import cn.damai.boss.projectreport.report.vo.BMerchantInfoVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-report.xml" })
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class BusinessPlatformServiceImplTest {

	@Resource
	private BusinessPlatformService businessPlatformService;

	@Test
	public void testFindBMerchantInfoList() {
		DynamicDataSourceHolder.putDataSourceName("5");
		
		List<Long> merchantIDs=null;//Arrays.asList(new Long[]{1L,2L,3L,4L,5L,6L,7L,8L,9L,10L});
		try {			
			List<BMerchantInfoVo> list=businessPlatformService.findBMerchantInfoList(merchantIDs,"红马传媒");
			System.out.println(list);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
}
