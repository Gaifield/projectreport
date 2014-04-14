package cn.damai.boss.projectreport.report.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.report.dao.SellerSaleDao;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-report.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class SellerSaleDaoImplTest {

	@Resource
	private SellerSaleDao ssd;
	
	@Test
	public void findSellerSaleInfo() {
		DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.BJMaitix.getCodeStr());
	}
	
	@Test
	public void findAgentByAgentId(){
		DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.BJMaitix.getCodeStr());
		
		
		//ssd.findAgentByAgentId(111L);
	}

	
	@Test
	public void singleSellerSale(){
		DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.BJMaitix.getCodeStr());
		Map<Integer,List<Long>> agentMap = new HashMap<Integer,List<Long>>();
		List<Long> list1 = new ArrayList<Long>();
		list1.add(157L);
		agentMap.put(1, list1);
		List<Long> list3 = new ArrayList<Long>();
		list3.add(226L);
		agentMap.put(3,list3);
		//ssd.findPromotionSellerSale(null, agentMap, null, null);
	}
	
	@Test
	public void findAgentListByAgentName(){
		
		DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.BJMaitix.getCodeStr());
		
		//ssd.findAgentListByAgentName("红马");
		//ssd.findSingleStaff(null, null);
		//ssd.findPromotionPresent(null, null);
		
	}
	
	
}
