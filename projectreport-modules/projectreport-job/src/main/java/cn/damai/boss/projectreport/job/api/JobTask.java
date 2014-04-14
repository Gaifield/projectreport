package cn.damai.boss.projectreport.job.api;
/**
 * 大麦项目报表定时任务接口
 * @author Administrator
 *
 */
public interface JobTask {
	
	/**
	 * 定时任务执行方法
	 */
	public void performTask();
	
	/**
	 * 获取定时任务名称
	 * @return 定时任务名称
	 */
	public String getJobTaskName();
}
