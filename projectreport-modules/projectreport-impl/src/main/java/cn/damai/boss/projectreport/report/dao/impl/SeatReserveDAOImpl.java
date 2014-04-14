package cn.damai.boss.projectreport.report.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.dao.SeatReserveDAO;
import cn.damai.boss.projectreport.report.enums.ReserveStatTypeEnum;
import cn.damai.boss.projectreport.report.vo.ReserveDetailVo;

@Repository
public class SeatReserveDAOImpl extends BaseDaoSupport implements
		SeatReserveDAO {

	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<ReserveDetailVo> querySeatReserveList(long projectId,
			List<Long> performInfoIds, short startType) {
		StringBuilder sb = new StringBuilder();
		sb.append("select "
				+ startType
				+ " as reserveStatType, reserveTyle,price,priceName,sum(quantity) as quantity,sum(amount) as amount,ticketType ");

		sb.append("from("
				+ "   Select "
				+ "    ISnull(PSO.ReverseType,0) as reverseType, "
				+ "    prt.ReserveTyle as reserveTyle, "
				+ "    PGP.Price as price,"
				+ "    psg.GradeName as priceName, "
				+ "    COUNT(PSO.PSeatID) as quantity,"
				+ "    SUM(PGP.Price) as amount,"
				+ "    1 as TicketType"
				+ "    from PSeatInfo PSO  WITH (NOLOCK)"
				+ "    Inner Join PerformGradePrice PGP WITH (NOLOCK) On PGP.PGradePriceID = PSO.PGradePriceID"
				+ "    Inner join ProjectSellGrade psg WITH (NOLOCK) on psg.PSellGradeID=PGP.PSellGradeID"
				+ "    left join PromotionCombineDetail pcd WITH (NOLOCK) on pcd.PSeatID=PSO.PSeatID"
				+ "    inner Join PerformReserveTyle prt WITH (NOLOCK) on prt.PerformReserveTyleID = PSO.PerformReserveTyleID"
				+ "    inner join PerformInfo pf WITH (NOLOCK) on pf.PerformInfoID=PGP.PerformInfoID"
				+ "  Where pcd.PCombineID is null and PSO.Status = 6"
				+ " AND (PSO.SeatType IS NULL OR PSO.SeatType = 0)"
				+ "  And ReverseType in(:reverseTypes) and pf.ProjectID=:projectId ");
		String strPerformIds = "0";
		if (performInfoIds != null) {
			strPerformIds = Utils.join(performInfoIds);
			sb.append(" and pf.PerformInfoID in(").append(strPerformIds)
					.append(")");
		}
		sb.append("  group by ReverseType,prt.ReserveTyle,PGP.Price,psg.GradeName ");
		sb.append("  Union all ");
		sb.append("  select cmb.reverseType,cmb.reserveTyle,cmb.Price,cmb.priceName,"
				+ "sum(cmb.quantity) as quantity,SUM(cmb.Price) as amount,2 as ticketType "
				+ " from( "
				+ " select ISNULL(ps.ReverseType,0) as reverseType, "
				+ " prt.ReserveTyle as reserveTyle, "
				+ " tp.Price as price,tp.PromotionName as priceName, "
				+ " COUNT(1) as quantity "
				+ " from TicketPromotion tp WITH (NOLOCK) "
				+ " inner join PromotionCombine pcb WITH (NOLOCK) on pcb.PromotionID=tp.PromotionID "
				+ " inner join PromotionCombineDetail pcbd WITH (NOLOCK) on pcbd.PCombineID=pcb.PCombineID "
				+ " inner join PSeatInfo ps WITH (NOLOCK) on ps.PSeatID=pcbd.PSeatID and ps.Status=6 "
				+ " inner Join PerformReserveTyle prt WITH (NOLOCK) on prt.PerformReserveTyleID = ps.PerformReserveTyleID "
				+ " inner join PerformInfo pf WITH (NOLOCK) on pf.PerformInfoID=tp.PerformInfoID "
				+ " where (ps.SeatType IS NULL OR ps.SeatType = 0)"
				+ " and pcb.Status=1 and pf.ProjectID=:projectId");
		if (performInfoIds != null) {
			// sb.append(" and pf.PerformInfoID in(:performInfoIds)");
			sb.append(" and pf.PerformInfoID in(").append(strPerformIds)
					.append(")");
		}
		sb.append("  group by ps.ReverseType,prt.reserveTyle,tp.PromotionName,tp.Price,pcb.PCombineID ");
		sb.append("  ) cmb group by cmb.reverseType,cmb.ReserveTyle,cmb.priceName,cmb.Price");

		sb.append(") tmp group by reserveTyle,price,priceName,ticketType");

		Session session = getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(String.valueOf(sb));
		sqlQuery.setParameter("projectId", projectId);

		// 统计类型
		List<Integer> reverseTypes = null;
		if (startType == ReserveStatTypeEnum.ReserveManagement.getCode()) {
			reverseTypes = Arrays.asList(new Integer[] { 0 });
		} else if (startType == ReserveStatTypeEnum.ReserveClient.getCode()) {
			reverseTypes = Arrays.asList(new Integer[] { 1, 2 });
		} else {
			reverseTypes = Arrays.asList(new Integer[] { 0, 1, 2 });
		}
		sqlQuery.setParameterList("reverseTypes", reverseTypes);

		if (performInfoIds != null) {
			// sqlQuery.setParameterList("performInfoIds", performInfoIds);
		}

		sqlQuery.addScalar("reserveStatType", new ShortType());
		sqlQuery.addScalar("reserveTyle", new IntegerType());
		sqlQuery.addScalar("price", new BigDecimalType());
		sqlQuery.addScalar("priceName", new StringType());
		sqlQuery.addScalar("quantity", new IntegerType());
		sqlQuery.addScalar("amount", new BigDecimalType());
		sqlQuery.addScalar("ticketType", new ShortType());
		sqlQuery.setResultTransformer(Transformers
				.aliasToBean(ReserveDetailVo.class));

		return sqlQuery.list();
	}

}
