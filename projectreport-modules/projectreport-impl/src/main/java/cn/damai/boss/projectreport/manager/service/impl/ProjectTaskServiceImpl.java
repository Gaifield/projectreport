package cn.damai.boss.projectreport.manager.service.impl;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.commons.utils.ModelUtils;
import cn.damai.boss.projectreport.domain.Upt01Project;
import cn.damai.boss.projectreport.domain.Upt01ProjectTask;
import cn.damai.boss.projectreport.manager.dao.ProjectDao;
import cn.damai.boss.projectreport.manager.dao.ProjectTaskDao;
import cn.damai.boss.projectreport.manager.enums.ProjectStatusEnum;
import cn.damai.boss.projectreport.manager.enums.ProjectTaskStatusEnum;
import cn.damai.boss.projectreport.manager.service.ProjectTaskService;
import cn.damai.boss.projectreport.manager.vo.ProjectTaskVo;
import cn.damai.boss.projectreport.manager.vo.ProjectVo;

@Service
public class ProjectTaskServiceImpl implements ProjectTaskService {

	@Resource
	private ProjectDao projectDao;

	@Resource
	private ProjectTaskDao projectTaskDao;

	@Override
	public Date findLatestProjectDeadline() throws ApplicationException {
		try {
			return projectDao.queryLatestProjectDeadline();			
		} catch (Exception ex) {
			throw new ApplicationException(
					HttpStatusEnum.ServerError.getCode(), ex.getMessage());
		}
	}

	@Override
	public void saveProject(ProjectVo projectVo) throws ApplicationException {
		try {
			// 设置创建时间、状态、任务完成数量
			projectVo.setCreateTime(new Date());
			projectVo.setStatus(ProjectStatusEnum.New.getCode());
			projectVo.setCompleteTaskQuantity(0);

			Upt01Project po = new Upt01Project();
			po.setProjectId(projectVo.getProjectId());
			po.setProjectDeadline(new Timestamp(projectVo.getProjectDeadline()
					.getTime()));
			po.setTaskQuantity(projectVo.getTaskQuantity());
			po.setCompleteTaskQuantity(projectVo.getCompleteTaskQuantity());
			po.setStatus(projectVo.getStatus());
			po.setCreateTime(new Timestamp(projectVo.getCreateTime().getTime()));

			projectDao.save(po);
		} catch (Exception ex) {
			throw new ApplicationException(
					HttpStatusEnum.ServerError.getCode(), ex.getMessage());
		}
	}

	@Override
	public void saveProjectTask(ProjectTaskVo projectTaskVo)
			throws ApplicationException {
		try {
			// 设置创建时间、状态
			projectTaskVo.setCreateTime(new Date());
			projectTaskVo.setTaskStatus(ProjectTaskStatusEnum.New.getCode());

			Upt01ProjectTask po = new Upt01ProjectTask();
			Upt01Project upt01Project = new Upt01Project();
			upt01Project.setProjectId(projectTaskVo.getProjectId());
			po.setUpt01Project(upt01Project);
			po.setTaskType(projectTaskVo.getTaskType());
			po.setTaskStatus(projectTaskVo.getTaskStatus());
			po.setCreateTime(new Timestamp(projectTaskVo.getCreateTime()
					.getTime()));

			projectTaskDao.save(po);

			projectTaskVo.setTaskId(po.getTaskId());
		} catch (Exception ex) {
			throw new ApplicationException(
					HttpStatusEnum.ServerError.getCode(), ex.getMessage());
		}
	}

	@Override
	public ProjectVo findProject(long projectId) throws ApplicationException {
		try {
			Upt01Project upt01Project = projectDao.findOne(projectId);
			if (upt01Project != null) {
				return ModelUtils.fromDomainObjectToVo(ProjectVo.class,
						upt01Project);
			}
		} catch (Exception ex) {
			throw new ApplicationException(
					HttpStatusEnum.ServerError.getCode(), ex.getMessage());
		}
		return null;
	}

	@Override
	public ProjectTaskVo findProjectTaskByTaskId(long taskId)
			throws ApplicationException {
		try {
			Upt01ProjectTask upt01ProjectTask = projectTaskDao.findOne(taskId);
			if (upt01ProjectTask != null) {
				ProjectTaskVo vo = ModelUtils.fromDomainObjectToVo(
						ProjectTaskVo.class, upt01ProjectTask);
				vo.setProjectId(upt01ProjectTask.getUpt01Project()
						.getProjectId());
				return vo;
			}
		} catch (Exception ex) {
			throw new ApplicationException(
					HttpStatusEnum.ServerError.getCode(), ex.getMessage());
		}
		return null;
	}

	@Override
	public void modifProjectTaskComplete(long taskId)
			throws ApplicationException {
		try {
			// 1、更新任务状态为完成
			Upt01ProjectTask upt01ProjectTask = projectTaskDao.findOne(taskId);
			Timestamp modifyTimestamp = new Timestamp(new Date().getTime());

			upt01ProjectTask.setCompleteTime(modifyTimestamp);
			upt01ProjectTask.setTaskStatus(ProjectTaskStatusEnum.StatCompleted
					.getCode());
			projectTaskDao.save(upt01ProjectTask);

			// 2、更新项目统计状态
			Upt01Project upt01Project = projectDao.findOne(upt01ProjectTask
					.getUpt01Project().getProjectId());
			upt01Project.setModifyTime(modifyTimestamp);
			int completeTaskQuantity = upt01Project.getCompleteTaskQuantity();
			// 累计统计完成数
			if (completeTaskQuantity < upt01Project.getTaskQuantity()
					.intValue()) {
				upt01Project.setCompleteTaskQuantity(completeTaskQuantity + 1);
			}
			// 如果累计任务数和总任务数一致，则更改状态为完成。
			if (upt01Project.getCompleteTaskQuantity().equals(
					upt01Project.getTaskQuantity())) {
				upt01Project.setStatus(ProjectStatusEnum.StatCompleted
						.getCode());
			}
			projectDao.save(upt01Project);

		} catch (Exception ex) {
			throw new ApplicationException(
					HttpStatusEnum.ServerError.getCode(), ex.getMessage());
		}
	}
}