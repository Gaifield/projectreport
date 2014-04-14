package cn.damai.boss.projectreport.report.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.dao.MaitixPerformDAO;
import cn.damai.boss.projectreport.report.vo.PerformStandStatVo;
import cn.damai.boss.projectreport.report.vo.PerformVo;

@Repository
public class MaitixPerformDAOImpl extends BaseDaoSupport implements
		MaitixPerformDAO {

	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public PageResultData queryPerformList(long projectId,
			List<Long> performInfoIds, Date startTime, Date endTime,
			int pageSize, int pageIndex) {
		PageResultData pageData = new PageResultData();

		String strSQL = " from PerformInfo pf WITH (NOLOCK) "
				+ " inner join FieldInfo fd WITH (NOLOCK) on fd.FieldID=pf.FieldID"
				+ " where pf.[Status] in(2,3) and pf.ProjectID=:projectId";
		StringBuilder builder = new StringBuilder(strSQL);
		if (performInfoIds != null) {
			builder.append(" and pf.PerformInfoID in(").append(Utils.join(performInfoIds)).append(")");
		}
		if (startTime != null) {
			builder.append(" and pf.PerformTime >=:startTime");
		}
		if (endTime != null) {
			builder.append(" and pf.PerformTime <=:endTime");
		}

		Session session = getCurrentSession();

		// 分页
		if (pageSize > 0) {
			StringBuilder sbSqlCount = new StringBuilder("select count(*) ")
					.append(builder);
			SQLQuery sqlQueryCount = session.createSQLQuery(String
					.valueOf(sbSqlCount));
			sqlQueryCount.setParameter("projectId", projectId);
			if (performInfoIds != null) {
				//sqlQueryCount.setParameterList("performIds", performInfoIds);
			}
			if (startTime != null) {
				sqlQueryCount.setParameter("startTime", startTime);
			}
			if (endTime != null) {
				sqlQueryCount.setParameter("endTime", endTime);
			}

			// 总记录数
			int size = ((Integer) sqlQueryCount.uniqueResult()).intValue();
			pageData.setTotal(size);
		}

		// 列表
		StringBuilder sbSql = new StringBuilder(
				"select pf.PerformInfoID as performId,pf.PerformInfoID as performInfoID, pf.Name as performName"
						+ ",pf.PerformTime as performTime,fd.Name as fieldName")
				.append(builder).append(" order by pf.PerformTime desc");
		SQLQuery sqlQuery = session.createSQLQuery(String.valueOf(sbSql));
		sqlQuery.setParameter("projectId", projectId);
		if (performInfoIds != null) {
			//sqlQuery.setParameterList("performIds", performInfoIds);
		}
		if (startTime != null) {
			sqlQuery.setParameter("startTime", startTime);
		}
		if (endTime != null) {
			sqlQuery.setParameter("endTime", endTime);
		}

		sqlQuery.addScalar("performId", new LongType());
		sqlQuery.addScalar("performInfoID", new LongType());
		sqlQuery.addScalar("performName", new StringType());
		sqlQuery.addScalar("performTime", new TimestampType());
		sqlQuery.addScalar("fieldName", new StringType());
		sqlQuery.setResultTransformer(Transformers.aliasToBean(PerformVo.class));

		if (pageSize > 0) {
			sqlQuery.setFirstResult((pageIndex - 1) * pageSize);
			sqlQuery.setMaxResults(pageSize);
		}

		List<PerformVo> voList = sqlQuery.list();
		pageData.setRows(voList);
		return pageData;
	}

	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<PerformStandStatVo> queryPerformStandStat(long projectInfoId,
			List<Long> performInfoIds) {
		StringBuilder builder = new StringBuilder("");

		builder.append("select sd.PerformInfoId,sd.StandName as standName, "
				+ " temp1.seatQuantity,temp2.protectQuantity,temp3.saleQuantity "
				+ " from PStandInfo as sd WITH (NOLOCK)  "
				+ " inner join PerformInfo pf WITH (NOLOCK) on pf.PerformInfoID=sd.PerformInfoID  "
				+ " left join ( "
				+ "  select COUNT(st1.PSeatID) as seatQuantity,st1.PStandID "
				+ "  from PSeatInfo st1 WITH (NOLOCK) "
				+ "  inner join PStandInfo ps1 WITH (NOLOCK) on ps1.PStandID=st1.PStandID "
				+ "  inner join PerformInfo pf1 WITH (NOLOCK) on pf1.PerformInfoID=ps1.PerformInfoID  "
				+ "  where ps1.Status<>3 AND st1.PGradePriceID IS NOT NULL "
				+ "  and pf1.ProjectID=:projectID ");
		String strPerformIds="0";
		if (performInfoIds != null && performInfoIds.size() > 0) {
			strPerformIds = Utils.join(performInfoIds);
			builder.append(" AND pf1.PerformInfoID IN(").append(strPerformIds).append(")");
		}
		builder.append("  group by st1.PStandID "
				+ " ) temp1 on temp1.PStandID=sd.PStandID "
				+ "  left join ( "
				+ "  select COUNT(st2.PSeatID) as protectQuantity,st2.PStandID "
				+ "  from PSeatInfo st2 WITH (NOLOCK) "
				+ "  inner join PStandInfo ps2 WITH (NOLOCK) on ps2.PStandID=st2.PStandID "
				+ "  inner join PerformInfo pf2 WITH (NOLOCK) on pf2.PerformInfoID=ps2.PerformInfoID  "
				+ "  where  ps2.Status<>3 AND st2.PGradePriceID IS NOT NULL  and st2.SeatType=2 "
				+ "  and pf2.ProjectID=:projectID ");
		if (performInfoIds != null && performInfoIds.size() > 0) {
			//builder.append(" AND pf2.PerformInfoID IN(:performInfoIds)");
			builder.append(" AND pf2.PerformInfoID IN(").append(strPerformIds).append(")");
		}
		builder.append("  group by st2.PStandID "
				+ " ) temp2 on temp2.PStandID=sd.PStandID  "
				+ "  left join ( "
				+ "  select COUNT(st3.PSeatID) as saleQuantity,st3.PStandID "
				+ "  from PSeatInfo st3 WITH (NOLOCK) "
				+ "  inner join PStandInfo ps3 WITH (NOLOCK) on ps3.PStandID=st3.PStandID "
				+ "  inner join PerformInfo pf3 WITH (NOLOCK) on pf3.PerformInfoID=ps3.PerformInfoID  "
				+ "  where ps3.Status<>3 AND st3.PGradePriceID IS NOT NULL and st3.SeatType<>2 and st3.Status in(8,10,12) "
				+ "  and pf3.ProjectID=:projectID ");
		if (performInfoIds != null && performInfoIds.size() > 0) {
			//builder.append(" AND pf3.PerformInfoID IN(:performInfoIds)");
			builder.append(" AND pf3.PerformInfoID IN(").append(strPerformIds).append(")");
		}
		builder.append("  group by st3.PStandID "
				+ " ) temp3 on temp3.PStandID=sd.PStandID   "
				+ "  where sd.Status<>3 AND pf.ProjectID=:projectID ");		
		if (performInfoIds != null && performInfoIds.size() > 0) {
			builder.append(" AND pf.PerformInfoID IN(:performInfoIds)");
		}

		Session session = getCurrentSession();

		SQLQuery sqlQuery = session.createSQLQuery(String.valueOf(builder));
		sqlQuery.setParameter("projectID", projectInfoId);
		if (performInfoIds != null && performInfoIds.size() > 0) {
			sqlQuery.setParameterList("performInfoIds", performInfoIds);
		}

		sqlQuery.addScalar("performInfoId", Hibernate.LONG);
		sqlQuery.addScalar("standName", Hibernate.STRING);
		sqlQuery.addScalar("seatQuantity", Hibernate.INTEGER);
		sqlQuery.addScalar("protectQuantity", Hibernate.INTEGER);
		sqlQuery.addScalar("saleQuantity", Hibernate.INTEGER);
		sqlQuery.setResultTransformer(Transformers
				.aliasToBean(PerformStandStatVo.class));
		return sqlQuery.list();
	}
}
