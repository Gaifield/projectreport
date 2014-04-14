package cn.damai.boss.projectreport.manager.service.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.service.OperatorService;
import cn.damai.boss.projectreport.manager.vo.OperatorVo;

/**
 * Created by 温俊荣 on 14-2-25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
@TransactionConfiguration(transactionManager="projectReportTransactionManager",defaultRollback =false)
public class OperatorServiceImplTest {
	
	@Resource
	public OperatorService operatorService;
	
	@Test
	public void  findAllOperator(){
		try {
			List<OperatorVo> vo = operatorService.findAllOperator("wen",(short)1,(short)1);
			if(vo!=null&&vo.size()!=0){
				System.out.println(vo.size());
			}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
