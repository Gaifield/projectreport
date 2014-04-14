package cn.damai.boss.projectreport.job.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import cn.damai.boss.projectreport.job.api.JobService;
import cn.damai.boss.projectreport.job.api.JobTask;

/**
 * 定时任务接口实现类
 * 
 * @author Administrator
 * 
 */
@Component
public class JobServiceImpl implements JobService, org.quartz.Job {

	private static final Log log = LogFactory.getFactory().getInstance(
			JobServiceImpl.class);

	/**
	 * 任务列表
	 */
	private Set<JobTask> jobTasks = new HashSet<JobTask>();

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		log.info("执行大麦项目报表定时任务.");
		execute();
	}

	@Override
	public void execute() {
		Iterator<JobTask> reportTaskIterator = jobTasks.iterator();
		int index = 0;
		while (reportTaskIterator.hasNext()) {
			JobTask reportTask = reportTaskIterator.next();
			log.info(String.format("开始 执行 第 %s个任务[%s]。", ++index,
					reportTask.getJobTaskName()));
			try {
				reportTask.performTask();

				log.info(String.format("完成 执行 第 %s个任务[%s]。", ++index,
						reportTask.getJobTaskName()));
			} catch (Exception ex) {
				log.error(String.format(" 执行 第 %s个任务[%s]时发生错误。  ", ++index,
						reportTask.getJobTaskName()), ex);
			}
		}
	}

	public Set<JobTask> getJobTasks() {
		return jobTasks;
	}

	public void setJobTasks(Set<JobTask> jobTasks) {
		this.jobTasks = jobTasks;
	}

}