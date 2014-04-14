package cn.damai.boss.projectreport.report.dao.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.report.dao.ReserveDAO;
import cn.damai.boss.projectreport.report.vo.ProjectClassVo;
import cn.damai.boss.projectreport.report.vo.ReserveTyleVo;

@Repository
public class ReserveDAOImpl extends BaseDaoSupport implements ReserveDAO {

	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<ReserveTyleVo> queryProjectReserveTyleList(long projectId) {
		String sql = "select performReserveTyleID,reserveTyle,tyleName from PerformReserveTyle WITH(NOLOCK) where ProjectID =:projectId order by reserveTyle";

		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setParameter("projectId", projectId);

		sqlQuery.addScalar("performReserveTyleID", new LongType());
		sqlQuery.addScalar("reserveTyle",  new IntegerType());
		sqlQuery.addScalar("tyleName", new StringType());
		sqlQuery.setResultTransformer(Transformers
				.aliasToBean(ReserveTyleVo.class));

		return sqlQuery.list();
	}

}
