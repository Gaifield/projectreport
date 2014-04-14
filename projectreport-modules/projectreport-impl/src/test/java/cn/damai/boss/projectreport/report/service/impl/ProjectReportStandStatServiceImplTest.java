package cn.damai.boss.projectreport.report.service.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.service.ProjectReportStandStatService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-report.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class ProjectReportStandStatServiceImplTest {

    @Resource
    private ProjectReportStandStatService qrojectReportStandStatService;

    @Test
    public void testFindProjectReportStandStatList() {
    	try {
    	
    		PageResultData pageData=qrojectReportStandStatService.findProjectReportStandStatList("1",4148, null, 0, 1);
		 System.out.println(pageData);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
    }
}
