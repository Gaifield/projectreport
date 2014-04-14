package cn.damai.boss.projectreport.manager.vo;

/**
 * 项目报表统计任务消息消息
 * 
 * @author Administrator
 * 
 */
public class ProjectTaskMessageVo implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2884471160601415540L;

	/**
	 * 统计任务Id
	 */
	private long taskId;

	/**
	 * 项目Id
	 */
	private long projectId;
	
	/**
	 * 任务类型
	 */
	private short taskType;
	
	public ProjectTaskMessageVo() {

	}

	public ProjectTaskMessageVo(long taskId, long projectId, short taskType) {
		super();
		this.taskId = taskId;
		this.projectId = projectId;
		this.taskType = taskType;
	}


	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public short getTaskType() {
		return taskType;
	}

	public void setTaskType(short taskType) {
		this.taskType = taskType;
	}
}