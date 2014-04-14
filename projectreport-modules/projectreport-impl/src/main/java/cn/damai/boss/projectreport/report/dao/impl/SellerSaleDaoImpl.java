package cn.damai.boss.projectreport.report.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.dao.SellerSaleDao;
import cn.damai.boss.projectreport.report.enums.AgentFromEnum;
import cn.damai.boss.projectreport.report.vo.PriceSaleStatVo;

/**
 * 销售渠道统计
 * 
 * @author：guwei 【顾炜】 time：2014-3-2 下午1:46:31
 * 
 */
@Repository
public class SellerSaleDaoImpl extends BaseDaoSupport implements SellerSaleDao {

	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<PriceSaleStatVo> queryNormalPriceDiscountSale(long projectId,
			List<Long> performIds, Map<Integer, List<Long>> agentMap,
			Date saleTimeStart, Date saleTimeEnd, boolean isGroupAll) {
		// 控制场次查询条件
		boolean perBool = performIds != null && performIds.size() > 0;
		// 控制时间范围查询
		boolean dateBool = (saleTimeStart != null && saleTimeEnd != null);

		StringBuilder sbFrom = new StringBuilder();
		// Maitix订单
		sbFrom.append("SELECT aod.PGradePriceID, ao.Agentid AS agentID, 1 agentFrom, aod.Discount,COUNT(1) as TicketQuantity,sum(aod.RealPrice) as sumMoney "
				+ " FROM AgentOrder ao WITH (NOLOCK)   "
				+ "   INNER JOIN AgentOrderDetail aod WITH (NOLOCK) ON ao.AgentOrderID=aod.AgentOrderID "
				+ "   INNER JOIN PerformInfo pf WITH (NOLOCK) ON pf.PerformInfoID=ao.PerformInfoID   "
				+ "   LEFT JOIN PSeatInfo ps WITH (NOLOCK) ON ps.PSeatID=aod.PSeatID "
				+ " WHERE (ps.SeatType IS NULL OR ps.SeatType=0) AND aod.PCombineID is null "
				+ "   AND aod.Status<>6 "
				+ "   AND (ao.IsChiefFreePrint IS NULL OR ao.IsChiefFreePrint=0) "
				+ "   AND pf.ProjectID=:projectId ");
		if (dateBool) {
			sbFrom.append(" AND ao.CreateDate BETWEEN :saleTimeStart AND :saleTimeEnd ");
		}
		String strPerformIds = "0";
		if (performIds != null) {
			// sbFrom.append(" AND pf.PerformInfoID in(:performIds) ");
			strPerformIds = Utils.join(performIds);
			sbFrom.append(" AND pf.PerformInfoID in(").append(strPerformIds)
					.append(") ");
		}
		List<Long> maitixAgents = getAgentList(agentMap,
				AgentFromEnum.Maitix.getCode());
		if (maitixAgents != null && maitixAgents.size() > 0) {
			sbFrom.append(" AND ao.Agentid in(:maitixAgents) ");
		}
		sbFrom.append(" GROUP BY aod.PGradePriceID,aod.Discount,ao.Agentid ");

		sbFrom.append(" UNION ALL ");

		sbFrom.append("SELECT od.PGradePriceID,-1 AgentID ,2 agentFrom,100 Discount,COUNT(1) as ticketQuantity,SUM(od.RealPrice) as sumMoney "
				+ "    FROM OrderForm ofm WITH (NOLOCK)   "
				+ "   INNER JOIN OrderDetail od WITH (NOLOCK) ON ofm.OrderID=od.OrderID  "
				+ "   INNER JOIN PerformInfo pf WITH (NOLOCK) on pf.PerformInfoID=od.PerformInfoID  "
				+ "  left join PSeatInfo ps WITH(NOLOCK) on ps.PSeatID=od.PSeatID "
				+ " WHERE (ps.SeatType IS NULL OR ps.SeatType = 0 ) AND od.PCombineID is null and od.Status=6 "
				+ "  AND pf.ProjectID=:projectId ");
		if (dateBool) {
			sbFrom.append(" AND ofm.CreateDate BETWEEN :saleTimeStart AND :saleTimeEnd ");
		}
		if (performIds != null) {
			// sbFrom.append(" AND pf.PerformInfoID in(:performIds) ");
			sbFrom.append(" AND pf.PerformInfoID in(").append(strPerformIds)
					.append(") ");
		}
		List<Long> damaiAgents = getAgentList(agentMap,
				AgentFromEnum.Damai.getCode());
		if (damaiAgents != null && damaiAgents.size() > 0) {
			sbFrom.append(" AND -1 in(:damaiAgents) ");
		}
		sbFrom.append(" GROUP BY od.PGradePriceID ");

		sbFrom.append(" UNION ALL ");

		sbFrom.append(" SELECT pgrade.TicketSystemInfoID as PGradePriceID,bof.MerchantID AS agentID ,3 agentFrom,od.Discount,SUM(od.TicketQuantity) AS TicketQuantity ,  "
				+ " SUM(od.RealPrice*od.Quantity) AS sumMoney "
				+ " FROM  B_OrderForm bof WITH (NOLOCK) "
				+ "   INNER JOIN B_OrderDetail od WITH (NOLOCK) ON od.OrderID=bof.OrderID   "
				+ "   INNER JOIN PerformJoinPiaoCn pgrade WITH (NOLOCK) "
				+ "   on od.ProductID=pgrade.PiaoCnInfoID AND pgrade.InfoType=3 "
				+ "   INNER JOIN PerformJoinPiaoCn cnPf WITH (NOLOCK)  "
				+ "   ON cnPf.PiaoCnInfoID = od.PerformID AND cnPf.InfoType = 2 "
				+ "   INNER JOIN PerformInfo pf WITH (NOLOCK) on pf.PerformInfoID=cnPf.TicketSystemInfoID  "
				+ "   INNER JOIN B_OrderTicketPrintInfo btp WITH (NOLOCK) "
				+ "   ON btp.OrderID=od.OrderID AND (btp.IsChiefFreePrint is null or btp.IsChiefFreePrint=0)  "
				+ " WHERE od.OrderDetailStatus=2 AND od.IsCombine=0  "
				+ "  AND pf.ProjectID=:projectId ");
		if (dateBool) {
			sbFrom.append(" AND bof.CreateDate BETWEEN :saleTimeStart AND :saleTimeEnd ");
		}
		if (performIds != null) {
			// sbFrom.append(" AND pf.PerformInfoID in(:performIds) ");
			sbFrom.append(" AND pf.PerformInfoID in(").append(strPerformIds)
					.append(") ");
		}
		List<Long> bPlatformAgents = getAgentList(agentMap,
				AgentFromEnum.MPlus.getCode());
		if (bPlatformAgents != null && bPlatformAgents.size() > 0) {
			sbFrom.append(" AND bof.MerchantID in(:bPlatformAgents) ");
		}
		sbFrom.append(" GROUP BY pgrade.TicketSystemInfoID,od.Discount,bof.MerchantID");

		StringBuilder sbSql = new StringBuilder();
		if (isGroupAll) {
			sbSql.append("select 1 as ticketType, Null as agentID,NULL as agentFrom,pfgp.price,psg.GradeName as priceName,temp.discount,SUM(temp.TicketQuantity) as ticketQuantity,SUM(temp.sumMoney) as sumMoney from (");
		} else {
			sbSql.append("SELECT 1 as ticketType, temp.agentFrom,temp.agentID,pfgp.price,psg.GradeName as priceName,temp.discount,SUM(temp.TicketQuantity) as ticketQuantity,SUM(temp.sumMoney) as sumMoney from (");
		}
		sbSql.append(sbFrom);
		sbSql.append(")temp INNER JOIN PerformGradePrice pfgp WITH (NOLOCK) on temp.PGradePriceID=pfgp.PGradePriceID ");
		sbSql.append(" INNER JOIN ProjectSellGrade psg WITH (NOLOCK) on psg.PSellGradeID=pfgp.PSellGradeID ");
		if (isGroupAll) {
			sbSql.append(" group by pfgp.price,psg.GradeName,temp.discount ");
		} else {
			sbSql.append(" group by temp.agentFrom,temp.agentID,pfgp.price,psg.GradeName,temp.discount ");
		}

		Session session = super.getCurrentSession();
		SQLQuery query = session.createSQLQuery(String.valueOf(sbSql));
		query.setParameter("projectId", projectId);
		if (perBool) {
			// query.setParameterList("performIds", performIds);
		}
		if (dateBool) {
			query.setParameter("saleTimeStart", saleTimeStart);
			query.setParameter("saleTimeEnd", saleTimeEnd);
		}
		if (maitixAgents != null && maitixAgents.size() > 0) {
			query.setParameterList("maitixAgents", maitixAgents);
		}
		if (damaiAgents != null && damaiAgents.size() > 0) {
			query.setParameterList("damaiAgents", damaiAgents);
		}
		if (bPlatformAgents != null && bPlatformAgents.size() > 0) {
			query.setParameterList("bPlatformAgents", bPlatformAgents);
		}
		addScalar(query);
		return query.list();
	}

	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<PriceSaleStatVo> queryNormalPriceZeroDiscountSale(
			long projectId, List<Long> performIds,
			Map<Integer, List<Long>> agentMap, Date saleTimeStart,
			Date saleTimeEnd, boolean isGroupAll, int isChiefFreePrint) {
		// 控制场次查询条件
		boolean perBool = performIds != null && performIds.size() > 0;
		// 控制时间范围查询
		boolean dateBool = (saleTimeStart != null && saleTimeEnd != null);

		StringBuilder sbFrom = new StringBuilder();
		// Maitix订单
		sbFrom.append("SELECT aod.PGradePriceID, ao.Agentid AS agentID, 1 agentFrom, aod.Discount,COUNT(1) as TicketQuantity,sum(aod.RealPrice) as sumMoney "
				+ " FROM AgentOrder ao WITH (NOLOCK)   "
				+ "   INNER JOIN AgentOrderDetail aod WITH(NOLOCK) ON ao.AgentOrderID=aod.AgentOrderID "
				+ "   INNER JOIN PerformInfo pf WITH (NOLOCK) ON pf.PerformInfoID=ao.PerformInfoID   "
				+ "   LEFT JOIN PSeatInfo ps WITH (NOLOCK) ON ps.PSeatID=aod.PSeatID  "
				+ " WHERE (ps.SeatType IS NULL OR ps.SeatType=0) AND aod.PCombineID is null "
				+ "   AND aod.Status<>6 "
				+ "   AND ao.IsChiefFreePrint=:isChiefFreePrint "
				+ "   AND pf.ProjectID=:projectId ");
		if (dateBool) {
			sbFrom.append(" AND ao.CreateDate BETWEEN :saleTimeStart AND :saleTimeEnd ");
		}
		String strPerformIds = "0";
		if (performIds != null) {
			// sbFrom.append(" AND pf.PerformInfoID in(:performIds) ");
			strPerformIds = Utils.join(performIds);
			sbFrom.append(" AND pf.PerformInfoID in(").append(strPerformIds)
					.append(") ");
		}
		List<Long> maitixAgents = getAgentList(agentMap,
				AgentFromEnum.Maitix.getCode());
		if (maitixAgents != null && maitixAgents.size() > 0) {
			sbFrom.append(" AND ao.Agentid in(:maitixAgents) ");
		}
		sbFrom.append(" GROUP BY aod.PGradePriceID,aod.Discount,ao.Agentid ");

		sbFrom.append(" UNION ALL ");

		sbFrom.append(" SELECT pgrade.TicketSystemInfoID as PGradePriceID,bof.MerchantID AS agentID ,3 agentFrom,od.Discount,SUM(od.TicketQuantity) AS TicketQuantity ,  "
				+ " SUM(od.RealPrice*od.Quantity) AS sumMoney "
				+ " FROM  B_OrderForm bof WITH (NOLOCK) "
				+ "   INNER JOIN B_OrderDetail od WITH (NOLOCK) ON od.OrderID=bof.OrderID   "
				+ "   INNER JOIN PerformJoinPiaoCn pgrade WITH (NOLOCK) "
				+ "   on od.ProductID=pgrade.PiaoCnInfoID AND pgrade.InfoType=3 "
				+ "   INNER JOIN PerformJoinPiaoCn cnPf WITH (NOLOCK)  "
				+ "   ON cnPf.PiaoCnInfoID = od.PerformID AND cnPf.InfoType = 2 "
				+ "   INNER JOIN PerformInfo pf WITH (NOLOCK) on pf.PerformInfoID=cnPf.TicketSystemInfoID  "
				+ "   INNER JOIN B_OrderTicketPrintInfo btp WITH (NOLOCK) "
				+ "   ON btp.OrderID=od.OrderID AND btp.IsChiefFreePrint=:isChiefFreePrint "
				+ " WHERE od.OrderDetailStatus=2 AND od.IsCombine=0  "
				+ "  AND pf.ProjectID=:projectId ");
		if (dateBool) {
			sbFrom.append(" AND bof.CreateDate BETWEEN :saleTimeStart AND :saleTimeEnd ");
		}
		if (performIds != null) {
			// sbFrom.append(" AND pf.PerformInfoID in(:performIds) ");
			sbFrom.append(" AND pf.PerformInfoID in(").append(strPerformIds)
					.append(") ");
		}
		List<Long> bPlatformAgents = getAgentList(agentMap,
				AgentFromEnum.MPlus.getCode());
		if (bPlatformAgents != null && bPlatformAgents.size() > 0) {
			sbFrom.append(" AND bof.MerchantID in(:bPlatformAgents) ");
		}
		sbFrom.append(" GROUP BY pgrade.TicketSystemInfoID,od.Discount,bof.MerchantID");

		StringBuilder sbSql = new StringBuilder();
		if (isGroupAll) {
			sbSql.append("select 1 as ticketType, Null as agentID,NULL as agentFrom,pfgp.price,psg.GradeName as priceName,temp.discount,SUM(temp.TicketQuantity) as ticketQuantity,SUM(temp.sumMoney) as sumMoney from (");
		} else {
			sbSql.append("SELECT 1 as ticketType, temp.agentFrom,temp.agentID,pfgp.price,psg.GradeName as priceName,temp.discount,SUM(temp.TicketQuantity) as ticketQuantity,SUM(temp.sumMoney) as sumMoney from (");
		}
		sbSql.append(sbFrom);
		sbSql.append(")temp INNER JOIN PerformGradePrice pfgp WITH(NOLOCK) on temp.PGradePriceID=pfgp.PGradePriceID ");
		sbSql.append(" INNER JOIN ProjectSellGrade psg WITH(NOLOCK) on psg.PSellGradeID=pfgp.PSellGradeID ");
		if (isGroupAll) {
			sbSql.append(" group by pfgp.price,psg.GradeName,temp.discount ");
		} else {
			sbSql.append(" group by temp.agentFrom,temp.agentID,pfgp.price,psg.GradeName,temp.discount ");
		}

		Session session = super.getCurrentSession();
		SQLQuery query = session.createSQLQuery(String.valueOf(sbSql));
		query.setParameter("projectId", projectId);
		query.setParameter("isChiefFreePrint", isChiefFreePrint);
		if (perBool) {
			// query.setParameterList("performIds", performIds);
		}
		if (dateBool) {
			query.setParameter("saleTimeStart", saleTimeStart);
			query.setParameter("saleTimeEnd", saleTimeEnd);
		}
		if (maitixAgents != null && maitixAgents.size() > 0) {
			query.setParameterList("maitixAgents", maitixAgents);
		}
		if (bPlatformAgents != null && bPlatformAgents.size() > 0) {
			query.setParameterList("bPlatformAgents", bPlatformAgents);
		}
		addScalar(query);
		return query.list();
	}

	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<PriceSaleStatVo> queryPromotionPriceDiscountSale(
			long projectId, List<Long> performIds,
			Map<Integer, List<Long>> agentMap, Date saleTimeStart,
			Date saleTimeEnd, boolean isGroupAll) {

		// 控制场次查询条件
		boolean perBool = performIds != null && performIds.size() > 0;
		// 控制时间范围查询
		boolean dateBool = (saleTimeStart != null && saleTimeEnd != null);

		StringBuilder sbFrom = new StringBuilder();
		// Maitix订单
		sbFrom.append("select tp.PromotionName as priceName,tp.Price as price ,t.agentID,t.agentFrom,  "
				+ "  t.Discount as discount,sum(t.TicketQuantity) as ticketQuantity,SUM(t.RealPrice) as sumMoney "
				+ " from TicketPromotion tp WITH(NOLOCK) "
				+ " inner join  "
				+ " ( "
				+ "  select aod.PromotionID,ao.AgentId AS agentID, 1 agentFrom "
				+ "  ,aod.Discount,aod.RealPrice,COUNT(1)as TicketQuantity,aod.PCombineID "
				+ "  from AgentOrder ao WITH(NOLOCK) "
				+ "  INNER JOIN PerformInfo pf WITH (NOLOCK) ON pf.PerformInfoID=ao.PerformInfoID "
				+ "  inner join AgentOrderDetail aod WITH(NOLOCK) on aod.AgentOrderID=ao.AgentOrderID "
				+ "  left join PSeatInfo ps WITH(NOLOCK) on ps.PSeatID=aod.PSeatID "
				+ "  where (ps.SeatType is null or ps.SeatType=0) AND ao.Status<>6 "
				+ "  AND (aod.IsChiefFreePrint is null or aod.IsChiefFreePrint=0) "
				+ "  and pf.ProjectID=:projectId ");
		if (dateBool) {
			sbFrom.append(" AND ao.CreateDate BETWEEN :saleTimeStart AND :saleTimeEnd ");
		}
		String strPerformIds = "0";
		if (performIds != null) {
			// sbFrom.append(" AND pf.PerformInfoID in(:performIds) ");
			strPerformIds = Utils.join(performIds);
			sbFrom.append(" AND pf.PerformInfoID in(").append(strPerformIds)
					.append(") ");
		}
		List<Long> maitixAgents = getAgentList(agentMap,
				AgentFromEnum.Maitix.getCode());
		if (maitixAgents != null && maitixAgents.size() > 0) {
			sbFrom.append(" AND ao.Agentid in(:maitixAgents) ");
		}
		sbFrom.append("  group by ao.Agentid,aod.Discount,aod.RealPrice ,aod.PromotionID,aod.PCombineID "
				+ " ) t on t.PromotionID=tp.PromotionID ");
		sbFrom.append(" group by tp.PromotionName,tp.Price,t.agentID,t.agentFrom,t.Discount ");
		sbFrom.append(" UNION ALL ");

		// 大麦网订单
		sbFrom.append("select tp.PromotionName as priceName,tp.Price as price,-1 agentID ,2 agentFrom,100 as discount,SUM(t.TicketQuantity) as ticketQuantity,SUM(t.RealPrice) as sumMoney "
				+ "from TicketPromotion tp WITH(NOLOCK) inner join ( "
				+ " select od.PromotionID, od.RealPrice, COUNT(1) as TicketQuantity "
				+ " from OrderForm odf WITH(NOLOCK)    "
				+ " inner join OrderDetail od WITH(NOLOCK) on od.OrderID=odf.OrderID "
				+ " INNER JOIN PerformInfo pf WITH (NOLOCK) ON pf.PerformInfoID=od.PerformInfoID "
				+ " inner join TicketPromotion tp WITH(NOLOCK) on tp.PromotionID=od.PromotionID "
				+ " LEFT JOIN PSeatInfo ps WITH (NOLOCK) ON ps.PSeatID = od.PSeatID "
				+ " where (ps.SeatType IS NULL OR ps.SeatType = 0 ) AND od.Status<>6 " + " and pf.ProjectID=:projectId ");
		if (dateBool) {
			sbFrom.append(" AND odf.CreateDate BETWEEN :saleTimeStart AND :saleTimeEnd ");
		}
		if (performIds != null) {
			// sbFrom.append(" AND pf.PerformInfoID in(:performIds) ");
			sbFrom.append(" AND pf.PerformInfoID in(").append(strPerformIds)
					.append(") ");
		}
		List<Long> damaiAgents = getAgentList(agentMap,
				AgentFromEnum.Damai.getCode());
		if (damaiAgents != null && damaiAgents.size() > 0) {
			sbFrom.append(" AND -1 in(:damaiAgents) ");
		}
		sbFrom.append(" group by od.PromotionID,od.RealPrice,od.PCombineID "
				+ ")t on t.PromotionID=tp.PromotionID ");
		sbFrom.append("group by tp.PromotionName,tp.Price");

		// 游票通订单
		sbFrom.append(" UNION ALL ");
		sbFrom.append("select tp.PromotionName as priceName,tp.Price as price,bof.MerchantID AS agentID ,3 agentFrom,od.Discount as discount,SUM(od.TicketQuantity) as ticketQuantity , "
				+ " SUM(od.RealPrice*od.Quantity) as sumMoney    "
				+ " from B_OrderForm bof WITH (NOLOCK) "
				+ "  inner join B_OrderDetail od  WITH(NOLOCK)  on od.OrderID=bof.OrderID  "
				+ "  inner join PerformJoinPiaoCn pgrade WITH(NOLOCK) on od.ProductID=pgrade.PiaoCnInfoID and pgrade.InfoType=5 "
				+ "  inner join PerformJoinPiaoCn cnPf WITH(NOLOCK) ON cnPf.PiaoCnInfoID = od.PerformID AND cnPf.InfoType = 2   "
				+ "  INNER JOIN PerformInfo pf WITH (NOLOCK) ON pf.PerformInfoID=pgrade.TicketSystemInfoID "
				+ "  inner join TicketPromotion tp WITH(NOLOCK) on tp.PromotionID=pgrade.TicketSystemInfoID "
				+ "  inner join B_OrderTicketPrintInfo otp WITH(NOLOCK) on otp.OrderID=od.OrderID and (otp.IsChiefFreePrint is null or otp.IsChiefFreePrint=0)  "
				+ " where od.OrderDetailStatus=2 and od.IsCombine=1 "
				+ " and pf.ProjectID=:projectId ");
		if (dateBool) {
			sbFrom.append(" AND bof.CreateDate BETWEEN :saleTimeStart AND :saleTimeEnd ");
		}
		if (performIds != null) {
			// sbFrom.append(" AND pf.PerformInfoID in(:performIds) ");
			sbFrom.append(" AND pf.PerformInfoID in(").append(strPerformIds)
					.append(") ");
		}
		List<Long> bPlatformAgents = getAgentList(agentMap,
				AgentFromEnum.MPlus.getCode());
		if (bPlatformAgents != null && bPlatformAgents.size() > 0) {
			sbFrom.append(" AND bof.MerchantID in(:bPlatformAgents) ");
		}
		sbFrom.append(" group by bof.MerchantID,tp.PromotionName,tp.Price,od.Discount ");

		StringBuilder sbSql = new StringBuilder();
		if (isGroupAll) {
			sbSql.append("select  2 as ticketType, sale.price,sale.priceName,sale.discount,NULL as agentFrom,NULL as agentID,SUM(sale.ticketQuantity) as ticketQuantity,SUM(sale.sumMoney) as sumMoney from(");
			sbSql.append(sbFrom);
			sbSql.append(")sale group by sale.price,sale.priceName,sale.discount ");
		} else {
			sbSql.append("select  2 as ticketType, sale.price,sale.priceName,sale.discount,sale.agentFrom,sale.agentID,SUM(sale.ticketQuantity) as ticketQuantity,SUM(sale.sumMoney) as sumMoney from(");
			sbSql.append(sbFrom);
			sbSql.append(")sale group by sale.price,sale.priceName,sale.discount,sale.agentFrom,sale.agentID ");
		}

		Session session = super.getCurrentSession();
		SQLQuery query = session.createSQLQuery(String.valueOf(sbSql));
		query.setParameter("projectId", projectId);
		if (perBool) {
			// query.setParameterList("performIds", performIds);
		}
		if (dateBool) {
			query.setParameter("saleTimeStart", saleTimeStart);
			query.setParameter("saleTimeEnd", saleTimeEnd);
		}
		if (maitixAgents != null && maitixAgents.size() > 0) {
			query.setParameterList("maitixAgents", maitixAgents);
		}
		if (damaiAgents != null && damaiAgents.size() > 0) {
			query.setParameterList("damaiAgents", damaiAgents);
		}
		if (bPlatformAgents != null && bPlatformAgents.size() > 0) {
			query.setParameterList("bPlatformAgents", bPlatformAgents);
		}
		query.setResultTransformer(Transformers
				.aliasToBean(PriceSaleStatVo.class));
		addScalar(query);
		return query.list();
	}

	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<PriceSaleStatVo> queryPromotionPriceZeroDiscountSale(
			long projectId, List<Long> performIds,
			Map<Integer, List<Long>> agentMap, Date saleTimeStart,
			Date saleTimeEnd, boolean isGroupAll, int isChiefFreePrint) {

		// 控制场次查询条件
		boolean perBool = performIds != null && performIds.size() > 0;
		// 控制时间范围查询
		boolean dateBool = (saleTimeStart != null && saleTimeEnd != null);

		StringBuilder sbFrom = new StringBuilder();
		// Maitix订单
		sbFrom.append("select tp.PromotionName as priceName,tp.Price as price ,t.agentID,t.agentFrom,  "
				+ "  t.Discount as discount,sum(t.TicketQuantity) as ticketQuantity,SUM(t.RealPrice) as sumMoney "
				+ " from TicketPromotion tp WITH(NOLOCK) "
				+ " inner join  "
				+ " ( "
				+ "  select aod.PromotionID,ao.AgentId AS agentID, 1 agentFrom "
				+ "  ,aod.Discount,aod.RealPrice,COUNT(1)as TicketQuantity,aod.PCombineID "
				+ "  from AgentOrder ao WITH(NOLOCK) "
				+ "  INNER JOIN PerformInfo pf WITH (NOLOCK) ON pf.PerformInfoID=ao.PerformInfoID "
				+ "  inner join AgentOrderDetail aod WITH(NOLOCK)  on aod.AgentOrderID=ao.AgentOrderID "
				+ "  left join PSeatInfo ps WITH(NOLOCK) on ps.PSeatID=aod.PSeatID "
				+ "  where (ps.SeatType IS NULL OR ps.SeatType = 0 ) AND ao.Status<>6 "
				+ "  AND (aod.IsChiefFreePrint is null or aod.IsChiefFreePrint=0) "
				+ "  and pf.ProjectID=:projectId ");
		if (dateBool) {
			sbFrom.append(" AND ao.CreateDate BETWEEN :saleTimeStart AND :saleTimeEnd ");
		}
		String strPerformIds = "0";
		if (performIds != null) {
			// sbFrom.append(" AND pf.PerformInfoID in(:performIds) ");
			strPerformIds = Utils.join(performIds);
			sbFrom.append(" AND pf.PerformInfoID in(").append(strPerformIds)
					.append(") ");
		}
		List<Long> maitixAgents = getAgentList(agentMap,
				AgentFromEnum.Maitix.getCode());
		if (maitixAgents != null && maitixAgents.size() > 0) {
			sbFrom.append(" AND ao.Agentid in(:maitixAgents) ");
		}
		sbFrom.append("  group by ao.Agentid,aod.Discount,aod.RealPrice ,aod.PromotionID,aod.PCombineID "
				+ " ) t on t.PromotionID=tp.PromotionID ");
		sbFrom.append(" group by tp.PromotionName,tp.Price,t.agentID,t.agentFrom,t.Discount ");

		// 大麦网不出工作票、赠票

		// 游票通订单
		sbFrom.append(" UNION ALL ");
		sbFrom.append("select tp.PromotionName as priceName,tp.Price as price,bof.MerchantID AS agentID ,3 agentFrom,od.Discount as discount,SUM(od.TicketQuantity) as ticketQuantity , "
				+ " SUM(od.RealPrice*od.Quantity) as sumMoney    "
				+ " from B_OrderForm bof  WITH (NOLOCK) "
				+ "  inner join B_OrderDetail od  WITH(NOLOCK)  on od.OrderID=bof.OrderID  "
				+ "  inner join PerformJoinPiaoCn pgrade WITH(NOLOCK) on od.ProductID=pgrade.PiaoCnInfoID and pgrade.InfoType=5 "
				+ "  inner join PerformJoinPiaoCn cnPf WITH(NOLOCK) ON cnPf.PiaoCnInfoID = od.PerformID AND cnPf.InfoType = 2   "
				+ "  INNER JOIN PerformInfo pf WITH (NOLOCK) ON pf.PerformInfoID=pgrade.TicketSystemInfoID "
				+ "  inner join TicketPromotion tp WITH(NOLOCK) on tp.PromotionID=pgrade.TicketSystemInfoID "
				+ "  inner join B_OrderTicketPrintInfo otp WITH(NOLOCK) on otp.OrderID=od.OrderID"
				+ "	 AND otp.IsChiefFreePrint=:isChiefFreePrint"
				+ " where od.OrderDetailStatus=2 and od.IsCombine=1 "
				+ " and pf.ProjectID=:projectId ");
		if (dateBool) {
			sbFrom.append(" AND bof.CreateDate BETWEEN :saleTimeStart AND :saleTimeEnd ");
		}
		if (performIds != null) {
			// sbFrom.append(" AND pf.PerformInfoID in(:performIds) ");
			sbFrom.append(" AND pf.PerformInfoID in(").append(strPerformIds)
					.append(") ");
		}
		List<Long> bPlatformAgents = getAgentList(agentMap,
				AgentFromEnum.MPlus.getCode());
		if (bPlatformAgents != null && bPlatformAgents.size() > 0) {
			sbFrom.append(" AND bof.MerchantID in(:bPlatformAgents) ");
		}
		sbFrom.append(" group by bof.MerchantID,tp.PromotionName,tp.Price,od.Discount ");

		StringBuilder sbSql = new StringBuilder();
		if (isGroupAll) {
			sbSql.append("select  2 as ticketType, sale.price,sale.priceName,sale.discount,NULL as agentFrom,NULL as agentID,SUM(sale.ticketQuantity) as ticketQuantity,SUM(sale.sumMoney) as sumMoney from(");
			sbSql.append(sbFrom);
			sbSql.append(")sale group by sale.price,sale.priceName,sale.discount ");
		} else {
			sbSql.append("select  2 as ticketType, sale.price,sale.priceName,sale.discount,sale.agentFrom,sale.agentID,SUM(sale.ticketQuantity) as ticketQuantity,SUM(sale.sumMoney) as sumMoney from(");
			sbSql.append(sbFrom);
			sbSql.append(")sale group by sale.price,sale.priceName,sale.discount,sale.agentFrom,sale.agentID ");
		}

		Session session = super.getCurrentSession();
		SQLQuery query = session.createSQLQuery(String.valueOf(sbSql));
		query.setParameter("projectId", projectId);
		query.setParameter("isChiefFreePrint", isChiefFreePrint);
		if (perBool) {
			// query.setParameterList("performIds", performIds);
		}
		if (dateBool) {
			query.setParameter("saleTimeStart", saleTimeStart);
			query.setParameter("saleTimeEnd", saleTimeEnd);
		}
		if (maitixAgents != null && maitixAgents.size() > 0) {
			query.setParameterList("maitixAgents", maitixAgents);
		}
		if (bPlatformAgents != null && bPlatformAgents.size() > 0) {
			query.setParameterList("bPlatformAgents", bPlatformAgents);
		}
		query.setResultTransformer(Transformers
				.aliasToBean(PriceSaleStatVo.class));
		addScalar(query);
		return query.list();
	}

	@SuppressWarnings("deprecation")
	private void addScalar(SQLQuery query) {
		if (query == null)
			return;
		query.setResultTransformer(Transformers
				.aliasToBean(PriceSaleStatVo.class));
		query.addScalar("ticketType", new ShortType());
		query.addScalar("price", new BigDecimalType());
		query.addScalar("priceName", new StringType());
		query.addScalar("discount", new BigDecimalType());
		query.addScalar("agentID", new LongType());
		query.addScalar("agentFrom", new IntegerType());
		query.addScalar("ticketQuantity", new IntegerType());
		query.addScalar("sumMoney", new BigDecimalType());
	}

	/**
	 * 根据代理商来源返回对应的代理商列表
	 * 
	 * @param agentMap
	 *            代理商字典
	 * @param agentFrom
	 *            来源
	 * @return
	 */
	private List<Long> getAgentList(Map<Integer, List<Long>> agentMap,
			Integer agentFrom) {
		List<Long> agentList = null;
		if (agentMap != null && agentMap.containsKey(agentFrom)) {
			agentList = agentMap.get(agentFrom);
		}
		return agentList;
	}
}