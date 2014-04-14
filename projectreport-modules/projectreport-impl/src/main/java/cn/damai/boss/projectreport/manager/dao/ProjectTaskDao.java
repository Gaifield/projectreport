package cn.damai.boss.projectreport.manager.dao;

import cn.damai.boss.projectreport.domain.Upt01ProjectTask;
import cn.damai.crius.core.dao.BaseDao;

/**
 * 项目统计任务表DAO
 * @author Administrator
 *
 */
public interface ProjectTaskDao extends BaseDao<Upt01ProjectTask,Long>,ProjectTaskDaoCustom {
	
}