package cn.damai.boss.projectreport.report.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.dao.OrderAgentDAO;
import cn.damai.boss.projectreport.report.vo.AgentVo;

import com.alibaba.dubbo.common.utils.StringUtils;

@Repository
public class OrderAgentDAOImpl extends BaseDaoSupport implements OrderAgentDAO {

	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public PageResultData<AgentVo> queryProjectAgentList(long projectId,
			List<Long> performIds, List<Long> bPlatformMerhantIds, Date saleTimeStart,
			Date saleTimeEnd, String agentName, int pageSize, int pageIndex) {
		PageResultData<AgentVo> pageData = new PageResultData<AgentVo>();
		
		StringBuilder sbFrom= new StringBuilder("select -1 as  agentId,'大麦网' as agentName,2 as agentFrom ");
		sbFrom.append(" UNION ALL ");
		sbFrom.append("select distinct ao.AgentID as agentId,ag.CompanyName as agentName,1 asagentFrom " +
              " from AgentOrder ao WITH(NOLOCK) " +
              "    inner join Agent ag WITH(NOLOCK) on ag.AgentID=ao.AgentID " +
              "    inner join PerformInfo pf WITH(NOLOCK) on pf.PerformInfoID=ao.PerformInfoID " +
              "    where ao.Status<>6 and pf.ProjectID=:projectId ");
		String strPerformIds = "0";
		if(performIds!=null){
			//sbFrom.append(" AND pf.PerformInfoID in(:performIds)");
			strPerformIds = Utils.join(performIds);
			sbFrom.append(" AND pf.PerformInfoID in(").append(strPerformIds).append(") ");
		}
		if(saleTimeStart!=null && saleTimeEnd!=null){
			sbFrom.append(" AND ao.CreateDate BETWEEN :saleTimeStart AND :saleTimeEnd");
		}
		
		sbFrom.append(" UNION ALL ");
		sbFrom.append("select distinct bo.MerchantID as agentId,'' as agentName, 3 as agentFrom from B_OrderForm bo WITH(NOLOCK) " +
              "    inner join B_OrderDetail bod WITH(NOLOCK) on bod.OrderID= bo.OrderID " +
              "    inner join PerformJoinPiaoCn pjp WITH(NOLOCK) on bod.PerformID=pjp.PiaoCnInfoID and pjp.InfoType=2 " +
              "    inner join PerformInfo pf WITH(NOLOCK) on pf.PerformInfoID = pjp.TicketSystemInfoID " +
              "    where bod.OrderDetailStatus=2 and pf.ProjectID=:projectId ");
		if(performIds!=null){
			//sbFrom.append(" AND pf.PerformInfoID in(:performIds)");
			sbFrom.append(" AND pf.PerformInfoID in(").append(strPerformIds).append(") ");
		}
		if(saleTimeStart!=null && saleTimeEnd!=null){
			sbFrom.append(" AND bo.CreateDate BETWEEN :saleTimeStart AND :saleTimeEnd");
		}
		if(bPlatformMerhantIds!=null){
//			sbFrom.append(" AND bo.MerchantID in(:bPlatformMerhantIds)");
			sbFrom.append(" AND bo.MerchantID in(").append(Utils.join(bPlatformMerhantIds)).append(")");
		}
		
		Session session = super.getCurrentSession();
		
		// 分页
		if (pageSize > 0) {
			StringBuilder sbSqlCount = new StringBuilder("select count(*) from(")
					.append(sbFrom).append(") temp where 1=1 ");
			if(!StringUtils.isBlank(agentName)){
				sbSqlCount.append(" AND (temp.agentName='' or temp.agentName like :agentName)");
			}
			
			SQLQuery sqlQueryCount = session.createSQLQuery(String
					.valueOf(sbSqlCount));
			sqlQueryCount.setParameter("projectId", projectId);
			if(!StringUtils.isBlank(agentName)){
				sqlQueryCount.setParameter("agentName", "%"+agentName+"%");
			}
			if(bPlatformMerhantIds!=null){
				//sqlQueryCount.setParameterList("bPlatformMerhantIds", bPlatformMerhantIds);
			}
			if(performIds!=null){
				//sqlQueryCount.setParameterList("performIds", performIds);
			}
			if(saleTimeStart!=null && saleTimeEnd!=null){
				sqlQueryCount.setParameter("saleTimeStart", saleTimeStart);
				sqlQueryCount.setParameter("saleTimeEnd", saleTimeEnd);
			}

			// 总记录数
			int size = ((Integer) sqlQueryCount.uniqueResult()).intValue();
			pageData.setTotal(size);
		}
		
		
		StringBuilder sbSql = new StringBuilder("select agentId,agentName,agentFrom from( ");
		sbSql.append(sbFrom);
		sbSql.append(")temp where 1=1");
		if(!StringUtils.isBlank(agentName)){
			sbSql.append(" AND (temp.agentName='' or temp.agentName like :agentName)");
		}
		SQLQuery query = session.createSQLQuery(String
				.valueOf(sbSql));
		
		query.setParameter("projectId", projectId);
		if(!StringUtils.isBlank(agentName)){
			query.setParameter("agentName", "%"+agentName+"%");
		}
		if(bPlatformMerhantIds!=null){
			//query.setParameterList("bPlatformMerhantIds", bPlatformMerhantIds);
		}
		if(performIds!=null){
			//query.setParameterList("performIds", performIds);
		}
		if(saleTimeStart!=null && saleTimeEnd!=null){
			query.setParameter("saleTimeStart", saleTimeStart);
			query.setParameter("saleTimeEnd", saleTimeEnd);
		}		

		if (pageSize > 0) {
			query.setFirstResult((pageIndex - 1) * pageSize);
			query.setMaxResults(pageSize);
		}
		
		query.addScalar("agentFrom", new IntegerType());
		query.addScalar("agentId", new LongType());
		query.addScalar("agentName", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(AgentVo.class));
		
		List<AgentVo> voList = query.list();
		pageData.setRows(voList);
		return pageData;
	}
}