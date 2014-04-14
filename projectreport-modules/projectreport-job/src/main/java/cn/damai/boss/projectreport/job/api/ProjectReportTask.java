package cn.damai.boss.projectreport.job.api;

/**
 * 项目报表统计任务
 * @author Administrator
 *
 */
public interface ProjectReportTask {
	
	/**
	 * 执行项目统计任务
	 * @param projectId 项目Id
	 */
	public void performProjectReportTask(long projectId);
	
	/**
	 * 获取项目报表任务名称
	 * @return 任务名称
	 */
	public String getProjectReportTaskName();		
}