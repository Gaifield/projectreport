package cn.damai.boss.projectreport.manager.dao.impl;

import java.util.Date;

import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import cn.damai.boss.projectreport.common.dao.BaseJpaDaoSupport;
import cn.damai.boss.projectreport.domain.Upt01OperatorLog;
import cn.damai.boss.projectreport.manager.dao.OperatorLogDAOCustom;

/**
 * 注释：日志扩展DAO实现类 作者：wenjunrong 【温俊荣】 时间：14-3-6 下午8:19
 */
@Repository
public class OperatorLogDAOCustomImpl extends
		BaseJpaDaoSupport<Upt01OperatorLog, Long> implements
		OperatorLogDAOCustom {

	@Override
	public void insertOperatorLog(Long operatorId, String content, int type) {
		// 添加新建操作员日志		
		Query query=entityManager.createNativeQuery("insert into upt01_operator_log(operator_id,log_type,content,create_time)values(?,?,?,?)");
		SQLQuery sqlQuery = query.unwrap(SQLQuery.class);
		query.setParameter(1, operatorId);
		query.setParameter(2, type);
		query.setParameter(3, content);
		query.setParameter(4, new Date());
		sqlQuery.executeUpdate();
	}
}