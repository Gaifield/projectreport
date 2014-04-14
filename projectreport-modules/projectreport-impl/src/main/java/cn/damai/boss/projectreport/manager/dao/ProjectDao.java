package cn.damai.boss.projectreport.manager.dao;

import cn.damai.boss.projectreport.domain.Upt01Project;
import cn.damai.crius.core.dao.BaseDao;

/**
 * 项目同步表DAO
 * @author Administrator
 *
 */
public interface ProjectDao extends BaseDao<Upt01Project,Long>,ProjectDaoCustom  {
	
}