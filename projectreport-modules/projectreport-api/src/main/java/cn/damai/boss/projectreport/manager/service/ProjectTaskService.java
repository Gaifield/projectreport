package cn.damai.boss.projectreport.manager.service;

import java.util.Date;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.vo.ProjectTaskVo;
import cn.damai.boss.projectreport.manager.vo.ProjectVo;

/**
 * 大麦项目报表项目任务服务类
 * @author Administrator
 *
 */
public interface ProjectTaskService {

	/**
	 * 查找导入记录项目的最大终止日期
	 * @return 无记录则返回null
	 * @throws ApplicationException
	 */
	public Date findLatestProjectDeadline() throws ApplicationException;
	
	/**
	 * 保存同步项目
	 * @param projectVo 项目Vo
	 * @throws ApplicationException
	 */
	public void saveProject(ProjectVo projectVo) throws ApplicationException;
	
	/**
	 * 查询项目
	 * @param projectId 项目Id
	 * @return
	 * @throws ApplicationException
	 */
	public ProjectVo findProject(long projectId) throws ApplicationException;
	
	/**
	 * 保存项目统计任务
	 * @param projectTaskVo 项目统计任务Vo
	 * @throws ApplicationException
	 */
	public void saveProjectTask(ProjectTaskVo projectTaskVo) throws ApplicationException;
	
	/**
	 * 查询项目任务
	 * @param taskId 任务Id
	 * @return
	 * @throws ApplicationException
	 */
	public ProjectTaskVo findProjectTaskByTaskId(long taskId) throws ApplicationException;
	
	/**
	 * 更新项目任务状态
	 * @param taskId 任务Id
	 * @throws ApplicationException
	 */
	public void modifProjectTaskComplete(long taskId) throws ApplicationException;	
}