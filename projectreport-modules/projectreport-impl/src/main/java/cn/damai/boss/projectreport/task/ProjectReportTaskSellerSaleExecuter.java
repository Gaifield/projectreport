package cn.damai.boss.projectreport.task;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.service.ProjectTaskExecuter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * 渠道销售统计任务
 * 
 * @author Administrator
 * 
 */
@Component
public class ProjectReportTaskSellerSaleExecuter implements ProjectTaskExecuter {

	private static final Log log = LogFactory.getFactory().getInstance(
			ProjectReportTaskSellerSaleExecuter.class);
	private String name = "渠道销售统计任务";

	@Override
	public void executeProjectTask(long projectId) throws ApplicationException {
		log.info(String.format("执行项目统计任务：%s ", name));
		
	}

	@Override
	public String getTaskExecuterName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}