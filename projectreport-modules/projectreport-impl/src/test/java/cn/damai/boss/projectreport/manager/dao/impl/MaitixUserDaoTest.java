package cn.damai.boss.projectreport.manager.dao.impl;

import cn.damai.boss.projectreport.manager.dao.MaitixUserDao;
import cn.damai.boss.projectreport.manager.dao.ReportRoleDAO;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by 炜 on 14-2-22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class MaitixUserDaoTest extends TestCase {

    @Autowired
    private MaitixUserDao maitixUserDao;
    @Autowired
    private ReportRoleDAO reportRoleDAO;

    @Test
    public void findUpt01MaitixUserByUpt01ReportRoleRoleId() {


    }
}
