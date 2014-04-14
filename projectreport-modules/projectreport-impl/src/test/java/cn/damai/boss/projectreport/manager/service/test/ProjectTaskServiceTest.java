package cn.damai.boss.projectreport.manager.service.test;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.enums.ProjectTaskTypeEnum;
import cn.damai.boss.projectreport.manager.service.ProjectTaskService;
import cn.damai.boss.projectreport.manager.vo.ProjectTaskVo;
import cn.damai.boss.projectreport.manager.vo.ProjectVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
@TransactionConfiguration(transactionManager="projectReportTransactionManager",defaultRollback =false)
public class ProjectTaskServiceTest {
    
	@Resource
    public ProjectTaskService projectTaskService;
    
    @Test
    public void testSaveProject(){
    	ProjectVo projectVo= new ProjectVo();
    	projectVo.setProjectId(1L);
    	projectVo.setTaskQuantity(5);    	
    	try {
			projectTaskService.saveProject(projectVo);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void testSaveProjectTask(){
    	ProjectTaskVo projectTaskVo= new ProjectTaskVo();
    	projectTaskVo.setProjectId(1L);
    	projectTaskVo.setTaskType(ProjectTaskTypeEnum.SaleStat.getCode());
    	try {
			projectTaskService.saveProjectTask(projectTaskVo);
			Assert.assertNotNull(projectTaskVo.getTaskId());
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
    }
        
    @Test
    public void testFindProject(){
    	long projectId=1l;    	
    	try {
    		ProjectVo projectVo= projectTaskService.findProject(projectId);
    		System.out.println(projectVo.getProjectId());
    		Assert.assertNotNull(projectVo);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
    }
    
    @Test
    public void testFindProjectTaskByTaskId(){
    	try {
    		ProjectTaskVo projectTaskVo= projectTaskService.findProjectTaskByTaskId(1);
    		System.out.println(projectTaskVo.getTaskId());
			Assert.assertNotNull(projectTaskVo);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
    }
}