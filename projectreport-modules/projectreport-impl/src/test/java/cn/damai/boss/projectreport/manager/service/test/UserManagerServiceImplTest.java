package cn.damai.boss.projectreport.manager.service.test;

import java.util.List;

import cn.damai.boss.maitix.vo.BusinessUserVo;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.service.UserManagerService;
import cn.damai.boss.projectreport.manager.vo.MaitixUserVo;
import cn.damai.soa.maitix.user.service.OrgUserManagerService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.annotation.Resource;

/**
 * Created by 炜 on 14-2-21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
@TransactionConfiguration(transactionManager="projectReportTransactionManager",defaultRollback =false)
public class UserManagerServiceImplTest  {

    @Resource
    public UserManagerService userManagerService;
    
    @Resource(name="soaOrgUserManagerService")
    private OrgUserManagerService orgUserManagerService;

    
    @Test
    public void findOrgBusinessUserList(){
    	//List<BusinessUserVo> buList = orgUserManagerService.findOrgBusinessUserList("zysj_report_1_1", null, null, null, null);
    	//System.out.println(buList.size());
    }
}
