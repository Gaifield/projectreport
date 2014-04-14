package cn.damai.boss.projectreport.report.service.impl;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.dao.QueryProjectDAO;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.service.QueryProjectService;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;
import cn.damai.crius.core.utils.Pager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注释：项目筛选单元测试 作者：liutengfei 【刘腾飞】 时间：14-2-26 下午4:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-report.xml" })
@TransactionConfiguration(transactionManager = "maitixTransactionManager", defaultRollback = false)
public class QueryProjectServiceImplTest {

	@Resource
	private QueryProjectDAO queryProjectDAO;

	@Resource
	private QueryProjectService queryProjectService;

	@Test
	public void queryProject() {
		DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.Maitix
				.getCodeStr());
		ReportProjectVo vo = new ReportProjectVo();
		vo.setPage(1);
		vo.setPageSize(10);
		queryProjectDAO.queryProjectByFilter(vo);
		System.out.println(new Date());
	}

	@Test
	public void queryProjectTest() {
		List<ReportProjectVo> voList = new ArrayList<ReportProjectVo>();
		for (int i = 0; i < 10; i++) {
			ReportProjectVo vo = new ReportProjectVo();
			vo.setProjectId(1L);
			vo.setPiaoCnId(1L);
			vo.setProjectName("test1");
			vo.setProjectStatusName("正在销售");
			vo.setStartTime("2014-2-28");
			vo.setEndTime("2014-3-1");
			vo.setPerformCity("北京");
			vo.setPerformField("五棵松");
			vo.setTodayMoney("12345");
			vo.setTotalMoney("1234567");
			voList.add(vo);
		}
		Pager pager = new Pager(1, 20);
		pager.setData(voList);
		pager.setTotalResults(voList.size());
	}

	@Test
	public void testFindOwnerProjectList() {
		DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.Maitix
				.getCodeStr());
		Map<String, Long> siteTradeIdMap = new HashMap<String, Long>();
		siteTradeIdMap.put("1", 1L);
		siteTradeIdMap.put("2", 17673L);
		siteTradeIdMap.put("3", 17752L);
		try {
			List<ReportProjectVo> list = queryProjectService
					.findOwnerProjectList(siteTradeIdMap);
			System.out.println(list);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void queryPerformFieldByKeyWord(){
		try {
			queryProjectService.queryPerformFieldByKeyWord("工人");
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
