package cn.damai.boss.projectreport.report.dao.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.report.dao.BusinessPlatformDAO;
import cn.damai.boss.projectreport.report.vo.BMerchantInfoVo;

@Repository
public class BusinessPlatformDAOImpl extends BaseDaoSupport implements BusinessPlatformDAO {

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<BMerchantInfoVo> queryBMerchantInfoList(List<Long> merchantIDs,String companyName) {
		Session session = sessionFactory.getCurrentSession();
		StringBuilder sbSql = new StringBuilder("select merchantID,companyName from MerchantInfo WITH(NOLOCK) where 1=1 ");
		 
		if(merchantIDs!=null){
			sbSql.append(" AND merchantID in(:merchantIDs)");
		}
		if(companyName!=null){
			sbSql.append(" AND companyName like :companyName");
		}
		
		SQLQuery query = session.createSQLQuery(String.valueOf(sbSql));
		if(merchantIDs!=null){
			query.setParameterList("merchantIDs", merchantIDs);
		}
		if(companyName!=null){
			query.setParameter("companyName","%"+companyName+"%");
		}

		query.addScalar("merchantID", new LongType());
		query.addScalar("companyName", new StringType());
		query.setResultTransformer(Transformers
				.aliasToBean(BMerchantInfoVo.class));

		return query.list();
	}
}
