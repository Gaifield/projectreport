package cn.damai.boss.projectreport.manager.service.test;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.service.RoleManagerService;
import cn.damai.boss.projectreport.manager.vo.ReportRoleVo;
import cn.damai.boss.projectreport.manager.vo.ReportVo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

/**
 * Created by 炜 on 14-2-20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
@TransactionConfiguration(transactionManager="projectReportTransactionManager",defaultRollback =false)
public class RoleManagerServiceImplTest {

    @Autowired
    private RoleManagerService roleManagerService;

    /**
     * 查询所有报表角色
     */
    @Test
    public void queryAllReportRoles(){
        List<ReportRoleVo> reportRoleVos = null;
        try {
            reportRoleVos = roleManagerService.queryAllReportRoles();
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testFindUserReportList(){
    	try {
    		List<ReportVo>  list = roleManagerService.findUserReportList(610L);
    		System.out.println(list);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
    }
}
