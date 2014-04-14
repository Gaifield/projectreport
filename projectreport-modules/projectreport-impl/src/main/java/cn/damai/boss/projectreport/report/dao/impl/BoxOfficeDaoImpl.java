package cn.damai.boss.projectreport.report.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.report.dao.BoxOfficeDao;
import cn.damai.boss.projectreport.report.vo.BoxOfficeVo;

@Repository
public class BoxOfficeDaoImpl extends BaseDaoSupport implements BoxOfficeDao {

	/**
	 * 判断项目是否是选座项目
	 * 
	 * @param projectId
	 * @return
	 * @author：guwei 【顾炜】 time：2014-3-6 下午5:44:15
	 */
	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public boolean findProjectIsChooseSeatOn(Long projectId) {
		// TODO Auto-generated method stub

		String sql = "SELECT ChooseSeatOn FROM ProjectInfo WHERE ProjectID = :projectId";

		Session session = super.getCurrentSession();

		SQLQuery query = session.createSQLQuery(sql);

		query.setParameter("projectId", projectId);

		List list = query.list();

		if (list != null && list.size() == 1) {
			// 1=选座销售 2=不选座销售(和座位无关)
			return "1".equals(list.get(0) + "");
		}

		return false;
	}

	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<BoxOfficeVo> findBoxOfficeByProjectIdOrPerformIds(Long projectId, List<Long> performIds) {
		// TODO Auto-generated method stub

		// 判断项目是不是选作项目
		boolean bool = findProjectIsChooseSeatOn(projectId);
		if (bool) {
			findBoxOfficeChooseSeatOn(projectId, performIds);
		} else {
			findBoxOfficeChooseSeatOff(projectId, performIds);
		}
		return null;
	}

	/**
	 * 选座项目
	 * @param projectId
	 * @param performIds
	 * @author：guwei 【顾炜】
	 * time：2014-3-6 下午8:46:54
	 */
	private void findBoxOfficeChooseSeatOn(Long projectId, List<Long> performIds) {

		boolean bool = performIds != null && performIds.size() > 0;

		String sql = " SELECT COUNT(1) AS validateSellBoxOffice,pgp.Price, ";
		if (bool) {
			sql += " pgp.PerformInfoID, ";
		}
		sql += " (SELECT COUNT(1) FROM PSeatInfo psi1	WHERE psi1.PGradePriceID = psi.PGradePriceID AND psi1.Status IN (2,4)"
				+ " ) AS RemainCount "
				+ " FROM PSeatInfo psi	"
				+ " INNER JOIN PerformGradePrice pgp ON pgp.PGradePriceID = psi.PGradePriceID"
				+ " INNER JOIN PerformInfo per ON per.PerformInfoID = pgp.PerformInfoID "
				+ " WHERE  per.Status in(1,2) AND psi.SeatType < 1 AND per.ProjectID = :projectId";

		if (bool) {
			sql += " AND pgp.PerformInfoID IN (:performIds) ";
		}

		sql += " GROUP BY pgp.Price,psi.PGradePriceID,per.Status";
		if (bool) {
			sql += ",pgp.PerformInfoID ";
		}

		Session session = super.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("projectId", projectId);
		if (bool) {
			query.setParameter("performIds", performIds);
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		List<Map> resultList = query.list();
		
		for(Map	map :resultList){
			if(!bool){
				BoxOfficeVo boxOfficeVo = new BoxOfficeVo();
				Long performId = Long.valueOf(map.get("PerformInfoID").toString());//场次id
				Long validateSellBoxOffice = Long.valueOf(map.get("validateSellBoxOffice").toString());
				Long price = Long.valueOf(map.get("Price").toString());
				Long remainCount = Long.valueOf(map.get("RemainCount").toString());
				boxOfficeVo.setPerformId(performId);
			}
		}
	}

	/**
	 * 无座项目
	 * @param projectId
	 * @param performIds
	 * @author：guwei 【顾炜】
	 * time：2014-3-6 下午8:47:13
	 */
	private void findBoxOfficeChooseSeatOff(Long projectId, List<Long> performIds) {

		boolean bool = performIds != null && performIds.size() > 0;

		String sql = " SELECT SUM(pgp.RemainCount) AS RemainCount ,SUM(pgp.MaxSellCount) AS validateSellBoxOffice,pgp.Price  ";
		if (bool) {
			sql += ",per.PerformInfoID ";
		}
		sql += " FROM PerformGradePrice pgp" + " INNER JOIN PerformInfo per ON per.PerformInfoID = pgp.PerformInfoID "
				+ " WHERE per.Status in(1,2) AND per.ProjectID = :projectId ";
		if (bool) {

			sql += " AND per.PerformInfoID IN (:performIds) ";
		}
		sql += " GROUP BY pgp.Price ";
		if (bool) {
			sql += ",per.PerformInfoID";
		}

		Session session = super.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("projectId", projectId);
		if (bool) {
			query.setParameter("performIds", performIds);
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		List<Map> list = query.list();
	}
}
