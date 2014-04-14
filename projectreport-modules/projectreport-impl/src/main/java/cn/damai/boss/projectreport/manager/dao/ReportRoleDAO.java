package cn.damai.boss.projectreport.manager.dao;

import cn.damai.boss.projectreport.domain.Upt01ReportRole;
import cn.damai.crius.core.dao.BaseDao;

/**
 * 角色Dao
 * Created by 炜 on 14-2-20.
 */
public interface ReportRoleDAO extends BaseDao<Upt01ReportRole,Long> {
	/**
	 * 通过角色id查找用户角色
	 */
	public Upt01ReportRole findByRoleId(Long roleId);

}
