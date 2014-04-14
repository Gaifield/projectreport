package cn.damai.boss.projectreport.job.impl.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.job.api.JobTask;
import cn.damai.boss.projectreport.job.api.ProjectReportTask;
import cn.damai.boss.projectreport.manager.service.ProjectTaskService;
import cn.damai.boss.projectreport.manager.vo.MaitixImportProjectVo;
import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.manager.vo.ProjectVo;

/**
 * 同步项目任务接口实现类 说明：过期项目（maitix设置为业务终止的项目）每天一次定时同步任务
 * 
 * @author Administrator
 * 
 */
@Component
public class SynchronizeProjectJobTaskServiceImpl implements JobTask {

	private static final Log log = LogFactory.getFactory().getInstance(
			SynchronizeProjectJobTaskServiceImpl.class);

	/**
	 * 接口每次导入记录数
	 */
	private int pageSize = 100;

	@Resource
	public ProjectTaskService projectTaskService;

	/**
	 * 定时任务名称
	 */
	private String taskName = "同步过期项目定时任务";

	/**
	 * 项目报表统计任务列表
	 */
	private Set<ProjectReportTask> projectReportTasks = new HashSet<ProjectReportTask>();

	@Override
	public void performTask() {

		// 1、组织同步参数
		Date dateStart = null; // 开始日期
		Date dateEnd = Utils.addDate(-1);
		int pageIndex = 1;

		// 1、首次获取分页数据
		PageResultData<MaitixImportProjectVo> pageData = importMaitixProject(
				dateStart, dateEnd, pageSize, pageIndex);
		if (pageData != null && pageData.getRows() != null) {
			// 计算页数
			int pageCount = Utils.countPageSize(pageData.getTotal(), pageSize);
			
			do {
				if (pageData != null && pageData.getRows() != null) {
					for (MaitixImportProjectVo vo : pageData.getRows()) {
						saveImportProject(vo);
					}
				}
				// 循环获取后续页数据
				pageIndex++;
				pageData = importMaitixProject(dateStart, dateEnd, pageSize,
						pageIndex);

			} while (pageIndex <= pageCount);
		}
	}

	/**
	 * 调用Maitix接口查询项目分页数据
	 * 
	 * @param startDate 	开始日期
	 * @param endDate 		截止日期
	 * @param pageSize 		每页大小
	 * @param pageIndex 	页码
	 * @return
	 */
	private PageResultData<MaitixImportProjectVo> importMaitixProject(Date startDate,
			Date endDate, int pageSize, int pageIndex) {
		PageResultData<MaitixImportProjectVo> pageData = new PageResultData<MaitixImportProjectVo>();

		List<MaitixImportProjectVo> projectList = new ArrayList<MaitixImportProjectVo>();
		for (int i = 0; i < 10; i++) {
			MaitixImportProjectVo vo = new MaitixImportProjectVo();
			vo.setProjectId(i + 1 * pageSize);
			vo.setProjectDeadline(new Date());
			projectList.add(vo);
		}
		pageData.setRows(projectList);
		pageData.setTotal(100);
		return pageData;
	}

	private void saveImportProject(MaitixImportProjectVo vo) {
		try {
			// 2.1、检查同步记录
			ProjectVo projectVo = projectTaskService.findProject(vo
					.getProjectId());

			boolean isInsertend = false;
			if (projectVo == null || projectVo.getProjectId() == null) {
				try {
					// 2.2、添加记录
					projectVo = new ProjectVo();
					projectVo.setProjectId(vo.getProjectId());
					projectVo.setProjectDeadline(new Date());
					projectVo.setTaskQuantity(projectReportTasks.size());
					projectTaskService.saveProject(projectVo);
					isInsertend = true;
				} catch (ApplicationException e) {
					projectVo = null;
					log.error(
							String.format("插入项目id:%s 记录时发生错误.",
									vo.getProjectId()), e);
				}
			}

			if (isInsertend) {
				// 2.3、执行项目报表统计任务
				performProjectReportTask(vo.getProjectId());
			}
		} catch (ApplicationException e) {
			log.error(String.format("查询项目id:%s 记录时发生错误.", vo.getProjectId()), e);
		}
	}

	/**
	 * 执行项目报表统计任务
	 * 
	 * @param projectId
	 */
	private void performProjectReportTask(long projectId) {
		Iterator<ProjectReportTask> projectReportTaskIterator = projectReportTasks
				.iterator();
		int index = 0;
		while (projectReportTaskIterator.hasNext()) {
			ProjectReportTask projectReportTask = projectReportTaskIterator
					.next();
			log.info(String.format("开始 执行 第 %s个任务[%s]。", ++index,
					projectReportTask.getProjectReportTaskName()));
			try {

				projectReportTask.performProjectReportTask(projectId);

				log.info(String.format("完成 执行 第 %s个任务[%s]。", ++index,
						projectReportTask.getProjectReportTaskName()));
			} catch (Exception ex) {
				log.error(String.format(" 执行 第 %s个任务[%s]时发生错误。  ", ++index,
						projectReportTask.getProjectReportTaskName()), ex);
			}
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String getJobTaskName() {
		return taskName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setProjectReportTasks(Set<ProjectReportTask> projectReportTasks) {
		this.projectReportTasks = projectReportTasks;
	}
}
