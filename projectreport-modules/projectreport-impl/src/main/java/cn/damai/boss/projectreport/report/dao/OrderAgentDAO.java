package cn.damai.boss.projectreport.report.dao;

import java.util.Date;
import java.util.List;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.report.vo.AgentVo;

public interface OrderAgentDAO {

	/**
	 * 分页查询项目订单对应的代理商列表
	 * @param projectId 项目Id
	 * @param performIds 场次Id列表
	 * @param bPlatformMerhantIds B平台代理商列表
	 * @param saleTimeStart 订单销售开始时间
	 * @param saleTimeEnd 订单销售截至时间
	 * @param agentName 代理商名称
	 * @param pageSize 每页大小
	 * @param pageIndex 页码
	 * @return PageResultData<AgentVo>
	 */
	public PageResultData<AgentVo> queryProjectAgentList(long projectId, List<Long> performIds,List<Long> bPlatformMerhantIds,
			Date saleTimeStart, Date saleTimeEnd, String agentName,int pageSize,int pageIndex);
}