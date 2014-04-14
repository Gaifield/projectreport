package cn.damai.boss.projectreport.report.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.report.dao.SeatStatDAO;
import cn.damai.boss.projectreport.report.vo.SeatStatVo;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-report.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class SeatStatDAOImplTest {

	@Autowired
	private SeatStatDAO ssd;
	
	@Test
	public void findSeatStatInfo() {
		List<SeatStatVo> ssv;
		//ssv = ssd.findSeatStatInfo(new ArrayList<Long>(9724));
		//System.out.println(ssv);
	}

	
	@Test
	public void findSeatStatDetail(){
		
		List<Long> list = new ArrayList<Long>();
		list.add(45715L);
		list.add(45716L);
		ssd.findSeatStatDetail(list);
	}
}
