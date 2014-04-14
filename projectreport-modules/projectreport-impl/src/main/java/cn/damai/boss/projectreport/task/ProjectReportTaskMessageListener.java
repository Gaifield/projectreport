package cn.damai.boss.projectreport.task;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.manager.service.ProjectTaskExecuter;
import cn.damai.boss.projectreport.manager.service.ProjectTaskService;
import cn.damai.boss.projectreport.manager.vo.ProjectTaskMessageVo;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ProjectReportTaskMessageListener implements MessageListener {
	private static final Logger log = LoggerFactory
			.getLogger(ProjectReportTaskMessageListener.class);
	private static final String HEAD_DataType = "dataType";

	private static final ObjectMapper jsonMapper = new ObjectMapper();
	private ExecutorService pool = Executors.newFixedThreadPool(10);

	@Resource
	private ProjectTaskService projectTaskService;

	@Resource
	private Map<String, ProjectTaskExecuter> projectTaskExecuters;

	@Override
	public void onMessage(final Message message) {
		pool.execute(new Runnable() {
			public void run() {
				if (message instanceof BytesMessage) {
					BytesMessage bytesMessage = (BytesMessage) message;
					try {
						String synClassName = bytesMessage
								.getStringProperty(HEAD_DataType);
						Class<?> aClass = Class.forName(synClassName);

						byte[] data = new byte[(int) bytesMessage
								.getBodyLength()];
						bytesMessage.readBytes(data);

						String synData = new String(data, "UTF-8");
						if (log.isDebugEnabled()) {
							log.debug("\n\n");
							log.debug("synClassName=" + synClassName);
							log.debug("synData=\n" + synData);
						}

						Object obj = jsonMapper.readValue(synData.getBytes(),
								aClass);

						processMessage(obj);
					} catch (Exception e) {
						log.error("大麦项目报表消息类型错误, error： ", e);
						e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * 处理消息
	 * 
	 * @param obj
	 */
	private void processMessage(Object obj) {
		if (obj instanceof ProjectTaskMessageVo) {
			ProjectTaskMessageVo messageVo = ((ProjectTaskMessageVo) obj);
			log.info("开始 处理大麦项目报表项目统计任务消息，taskId:" + messageVo.getTaskId());

			// 执行项目报表统计任务
			try {
				// 1、获取项目任务执行表
				String strTaskType = String.valueOf(messageVo.getTaskType());
				if (projectTaskExecuters == null
						|| !projectTaskExecuters.containsKey(strTaskType)) {
					throw new ApplicationException(
							HttpStatusEnum.ServerError.getCode(),
							String.format("taskType为%s的任务不存在。", strTaskType));
				}

				ProjectTaskExecuter executer = projectTaskExecuters
						.get(strTaskType);
				try {
					executer.executeProjectTask(messageVo.getProjectId());
				} catch (ApplicationException ex) {
					throw ex;
				} catch (Exception ex) {
					throw new ApplicationException(
							HttpStatusEnum.ServerError.getCode(),
							String.format("执行%s项目任务时发生错误。",
									executer.getTaskExecuterName()));
				}

				// 2、更新任务执行状态
				projectTaskService.modifProjectTaskComplete(messageVo
						.getTaskId());

				log.info("完成 处理大麦项目报表项目统计任务消息，taskId:" + messageVo.getTaskId());
			} catch (ApplicationException e) {
				log.error(
						String.format("调用任务处理接口报错。errorCode:%s",
								e.getErrorCode()), e);
				e.printStackTrace();
			}
		} else {
			try {
				String msg = jsonMapper.writeValueAsString(obj);
				log.info("消息类型错误。message:" + msg);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public Map<String, ProjectTaskExecuter> getProjectTaskExecuters() {
		return projectTaskExecuters;
	}

	public void setProjectTaskExecuters(
			Map<String, ProjectTaskExecuter> projectTaskExecuters) {
		this.projectTaskExecuters = projectTaskExecuters;
	}
}