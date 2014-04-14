package cn.damai.boss.projectreport.manager.dao.impl;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.domain.Upt01MaitixUser;
import cn.damai.boss.projectreport.manager.dao.MaitixUserDao;
import cn.damai.boss.projectreport.manager.vo.MaitixUserVo;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 炜 on 14-2-22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class MaitixUserDaoCustomTest extends TestCase {

    @Autowired
    private MaitixUserDao maitixUserDao;

    /**
     * 测试根据用户角色id查询用户信息
     */
    @Test
    public void queryUpt01MaitixUsersByRoleId() {

        try {
        	List<Upt01MaitixUser> list = maitixUserDao.queryUpt01MaitixUsersByRoleId(1L);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }

    }
}
