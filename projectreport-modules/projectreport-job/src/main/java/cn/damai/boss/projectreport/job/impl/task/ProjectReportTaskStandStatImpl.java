package cn.damai.boss.projectreport.job.impl.task;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.job.api.ProjectReportTask;
import cn.damai.boss.projectreport.manager.enums.ProjectTaskTypeEnum;
import cn.damai.boss.projectreport.manager.service.ProjectTaskService;
import cn.damai.boss.projectreport.manager.vo.ProjectTaskMessageVo;
import cn.damai.boss.projectreport.manager.vo.ProjectTaskVo;

/**
 * 项目出票统计任务
 * 
 * @author Administrator
 * 
 */
@Component
public class ProjectReportTaskStandStatImpl implements ProjectReportTask {

	private static final Log log = LogFactory.getFactory().getInstance(
			ProjectReportTaskStandStatImpl.class);
	private String taskName = "项目出票统计任务";

	@Resource
	public ProjectTaskService projectTaskService;

	@Resource
	public MessageSender messageSender;
	
	@Override
	public void performProjectReportTask(long projectId) {

		// 1、新增项目统计任务表记录
		ProjectTaskVo projectTaskVo = new ProjectTaskVo();
		projectTaskVo.setProjectId(projectId);
		projectTaskVo.setTaskType(ProjectTaskTypeEnum.StandStat.getCode());
		try {
			projectTaskService.saveProjectTask(projectTaskVo);
		} catch (ApplicationException e) {
			log.error(String.format("插入项目ID： %s %s  记录时发错误,code:%s", projectId,
					taskName, e.getErrorCode()), e);
		}

		Long taskId = projectTaskVo.getTaskId();
		if (taskId != null && taskId > 0) {

			log.info(String.format("新增项目ID： %s %s 记录完成,taskId:%s", projectId,
					taskName, taskId));

			// 2、发送任务消息
			ProjectTaskMessageVo vo = new ProjectTaskMessageVo(
					projectTaskVo.getTaskId(), projectTaskVo.getProjectId(), projectTaskVo.getTaskType());
			messageSender.send(vo);
		}
	}

	@Override
	public String getProjectReportTaskName() {
		return getTaskName();
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
}