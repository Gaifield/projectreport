package cn.damai.boss.projectreport.report.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.dao.SeatStatDAO;
import cn.damai.boss.projectreport.report.vo.SeatStatVo;

/**
 * 注释：座位汇总DAO实现类 作者：liutengfei 【刘腾飞】 时间：14-3-3 下午1:59
 */
@Repository
public class SeatStatDAOImpl extends BaseDaoSupport implements SeatStatDAO {

	/**
	 * 座位汇总查询 单票
	 * 
	 * @param performIds
	 * @return
	 * @author：guwei 【顾炜】 time：2014-3-3 下午4:07:21
	 */
	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<SeatStatVo> findSeatStatInfo(Long projectId, List<Long> performIds) {
		// TODO Auto-generated method stub

		StringBuffer sql = new StringBuffer(
				" SELECT pgp.Price as price ,psg.GradeName as priceName,  COUNT(1) AS seatQuantity,ISNULL(psi.SeatType, 0) AS seatType "
						+ " FROM PerformGradePrice pgp WITH (NOLOCK) "
						+ " Inner join ProjectSellGrade psg WITH (NOLOCK) on psg.PSellGradeID=pgp.PSellGradeID  "
						+ " INNER JOIN PSeatInfo psi WITH (NOLOCK) ON psi.PGradePriceID = pgp.PGradePriceID "
						+ " LEFT JOIN PromotionCombineDetail pcd WITH (NOLOCK) ON pcd.PSeatID = psi.PSeatID  "
						+ " inner join PerformInfo per on per.PerformInfoID = pgp.PerformInfoID ");

		sql.append(" WHERE pgp.Status<>3  AND pcd.PCombineID IS NULL and per.ProjectID = :projectId ");
		if (performIds != null) {
			sql.append(" and pgp.PerformInfoID IN ( ");
			sql.append(Utils.join(performIds) + " )");

		}
		sql.append(" GROUP BY pgp.Price,psg.GradeName,ISNULL(psi.SeatType, 0)");

		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql.toString());
		query.setParameter("projectId", projectId);

		query.addScalar("price", new BigDecimalType());
		query.addScalar("priceName", new StringType());
		query.addScalar("seatQuantity", new IntegerType());
		query.addScalar("seatType", new IntegerType());

		query.setResultTransformer(Transformers.aliasToBean(SeatStatVo.class));

		List list = query.list();
		if (list != null && list.size() > 0) {

			return list;
		}
		return null;
	}

	/**
	 * 座位汇总查询 套票
	 * 
	 * @param performIds
	 * @return
	 * @author：guwei 【顾炜】 2014-3-25 下午9:24:50
	 */
	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<SeatStatVo> queryPromotionSeatInfo(Long projectId, List<Long> performIds) {
		StringBuffer sql = new StringBuffer(
				"select t1.PromotionName as priceName ,t1.Price as price, "
						+ " sum(t1.TicketQuantity) as seatQuantity,SUM(t1.Price) as seatAmount,t1.seatType as seatType "
						+ " from( select tp.PerformInfoID,tp.PromotionName,tp.Price,COUNT(1) as TicketQuantity,ISNULL(ps.SeatType,0) as seatType  from TicketPromotion tp WITH (NOLOCK) "
						+ " inner join PromotionCombine pcb WITH (NOLOCK) on pcb.PromotionID=tp.PromotionID "
						+ " inner join PromotionCombineDetail pcbd WITH (NOLOCK) on pcbd.PCombineID=pcb.PCombineID  inner join PSeatInfo ps on ps.PSeatID=pcbd.PSeatID "
						+ " inner join PerformInfo per WITH (NOLOCK)  on per.PerformInfoID = tp.PerformInfoID " 
						+ " where per.projectId = :projectId");
		if (performIds != null) {
			sql.append("  and tp.PerformInfoID in( ");
			sql.append(Utils.join(performIds) + " )");

		}
		sql.append(" group by tp.PerformInfoID,tp.PromotionName,tp.Price,pcb.PCombineID,ISNULL(ps.SeatType,0)  ) t1 "
				+ " group by t1.PromotionName,t1.Price,t1.seatType ");

		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql.toString());
		query.setParameter("projectId", projectId);

		query.addScalar("price", new BigDecimalType());
		query.addScalar("priceName", new StringType());
		query.addScalar("seatQuantity", new IntegerType());
		query.addScalar("seatAmount", new BigDecimalType());
		query.addScalar("seatType", new IntegerType());

		query.setResultTransformer(Transformers.aliasToBean(SeatStatVo.class));

		List list = query.list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	/**
	 * 座位明细查询 单票
	 * 
	 * @param performIds
	 * @return
	 * @author：guwei 【顾炜】 2014-3-26 上午9:08:50
	 */
	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<SeatStatVo> findSeatStatDetail(List<Long> performIds) {
		// TODO Auto-generated method stub
		if (performIds == null || performIds.size() < 1) {
			return null;
		}
		StringBuffer sql = new StringBuffer(
				" select per.PerformTime as performTime,  per.Name as performName,t1.priceName as priceName, t1.PerformInfoID as performId,t1.Price as price, "
						+ " t1.seatQuantity,t1.seatType as seatType "
						+ " from  ( select pgp.PerformInfoID,pgp.Price,psg.GradeName as priceName,COUNT(1) as seatQuantity,ISNULL(psi.SeatType,0) as seatType from PerformGradePrice pgp  WITH (NOLOCK) "
						+ " Inner join ProjectSellGrade psg WITH (NOLOCK) on psg.PSellGradeID=pgp.PSellGradeID "
						+ " inner join PSeatInfo psi WITH (NOLOCK)  on psi.PGradePriceID = pgp.PGradePriceID "
						+ " left join PromotionCombineDetail pcd WITH (NOLOCK)  on pcd.PSeatID = psi.PSeatID "
						+ " where pgp.Status<>3 and pcd.PCombineID is null  and pgp.PerformInfoID in(");
		sql.append(Utils.join(performIds));
		sql.append(" )  group by pgp.Price,psg.GradeName ,pgp.PerformInfoID,ISNULL(psi.SeatType,0)  ) t1 "
				+ " inner join PerformInfo per WITH (NOLOCK)  on per.PerformInfoID = t1.PerformInfoID ");

		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql.toString());

		query.addScalar("price", new BigDecimalType());
		query.addScalar("priceName", new StringType());
		query.addScalar("performId", new LongType());
		query.addScalar("performName", new StringType());
		query.addScalar("seatQuantity", new IntegerType());
		query.addScalar("performTime", new DateType());
		query.addScalar("seatType", new IntegerType());

		query.setResultTransformer(Transformers.aliasToBean(SeatStatVo.class));

		List list = query.list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	/**
	 * 座位明细查询 套票
	 * 
	 * @param performIds
	 * @param seatType
	 * @author：guwei 【顾炜】 2014-3-18 下午7:56:10
	 */
	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<SeatStatVo> queryPromotionSeatDetail(List<Long> performIds) {

		StringBuffer sql = new StringBuffer(
				"select per.PerformTime as performTime,  "
						+ " per.Name as performName, t1.PerformInfoID as performId,t1.PromotionName as priceName ,t1.Price as price, "
						+ " sum(t1.TicketQuantity) as seatQuantity,SUM(t1.Price) as seatAmount,t1.seatType as seatType "
						+ " from( select tp.PerformInfoID,tp.PromotionName,tp.Price,COUNT(1) as TicketQuantity,ISNULL(ps.SeatType,0) as seatType  from TicketPromotion tp WITH (NOLOCK) "
						+ " inner join PromotionCombine pcb WITH (NOLOCK) on pcb.PromotionID=tp.PromotionID "
						+ " inner join PromotionCombineDetail pcbd WITH (NOLOCK) on pcbd.PCombineID=pcb.PCombineID  inner join PSeatInfo ps on ps.PSeatID=pcbd.PSeatID "
						+ " where tp.PerformInfoID in(");
		sql.append(Utils.join(performIds));
		sql.append(" )group by tp.PerformInfoID,tp.PromotionName,tp.Price,pcb.PCombineID,ISNULL(ps.SeatType,0)  ) t1 "
				+ " inner join PerformInfo per WITH (NOLOCK)  on per.PerformInfoID = t1.PerformInfoID "
				+ " group by t1.PromotionName,t1.Price,per.PerformTime ,per.Name , t1.PerformInfoID,t1.seatType ");

		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql.toString());

		query.addScalar("price", new BigDecimalType());
		query.addScalar("priceName", new StringType());
		query.addScalar("performId", new LongType());
		query.addScalar("performName", new StringType());
		query.addScalar("seatQuantity", new IntegerType());
		query.addScalar("seatAmount", new BigDecimalType());
		query.addScalar("seatType", new IntegerType());

		query.setResultTransformer(Transformers.aliasToBean(SeatStatVo.class));

		List list = query.list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

}
