package cn.damai.boss.projectreport.manager.service;

import cn.damai.boss.projectreport.commons.ApplicationException;

/**
 * 项目任务执行器
 * @author Administrator
 */
public interface ProjectTaskExecuter {
	
	/**
	 * 项目任务执行
	 * @param projectId 项目
	 */
	public void executeProjectTask(long projectId) throws ApplicationException;
	
	/**
	 * 获取项目任务名称
	 */
	public String getTaskExecuterName();
}