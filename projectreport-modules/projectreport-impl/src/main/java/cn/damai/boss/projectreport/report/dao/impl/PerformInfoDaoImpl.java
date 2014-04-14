package cn.damai.boss.projectreport.report.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.common.dao.BaseJpaDaoSupport;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.report.dao.PerformInfoDao;
import cn.damai.boss.projectreport.report.vo.PerformVo;
/**
 * 场次dao
 * @author：guwei 【顾炜】
 * time：2014-3-1 上午2:22:08
 *
 */
@Repository
public class PerformInfoDaoImpl extends BaseDaoSupport implements PerformInfoDao {
	

	/**
	 * 根据项目查询场次
	 * @param projectId
	 * @return
	 * @author：guwei 【顾炜】
	 * time：2014-3-1 上午2:22:18
	 */
	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<PerformVo> findPerformInfoByProjectId(Long projectId,String startTime,String endTime) throws ApplicationException {
		/*
		// TODO Auto-generated method stub
		try{
			//项目id不为空时查询
			if(projectId != null){
				StringBuffer sql = new StringBuffer(" select  per.performinfoid as performInfoID,per.FieldID as filedId,per.performTime as performTimeString,fi.Name as fieldName from performinfo  per ");
				sql.append(" inner join FieldInfo fi on per.FieldID = fi.FieldID where per.projectId = ");
				sql.append(projectId);
				//判断 按照时间筛选时
				if((startTime!=null && !"".equals(startTime.trim())) || (endTime !=null && !"".equals(endTime.trim()))){
					sql.append(" and per.performTime > '");
					sql.append(startTime);
					sql.append(" ' and per.performTime <'");
					sql.append(endTime);
					sql.append("' ");
				}
				
				Query query = entityManager.createNativeQuery(sql.toString()); 
				SQLQuery sqlQuery = query.unwrap(SQLQuery.class);
				
				sqlQuery.addScalar("performInfoID", Hibernate.LONG);
				sqlQuery.addScalar("filedId", Hibernate.LONG);
				sqlQuery.addScalar("fieldName", Hibernate.STRING);
				sqlQuery.addScalar("performTimeString", Hibernate.STRING);

				sqlQuery.setResultTransformer(Transformers
						.aliasToBean(PerformVo.class));

				List<PerformVo> voList = query.getResultList();
				return voList;
			}			
		}catch(Exception e){
			e.printStackTrace();
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
			
		}
		
		*/
		return null;
	}

}
