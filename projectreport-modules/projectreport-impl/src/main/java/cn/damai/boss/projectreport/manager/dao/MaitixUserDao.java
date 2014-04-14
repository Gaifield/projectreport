package cn.damai.boss.projectreport.manager.dao;

import cn.damai.boss.projectreport.domain.Upt01MaitixUser;
import cn.damai.crius.core.dao.BaseDao;

/**
 * Created by 炜 on 14-2-21.
 */
public interface MaitixUserDao extends BaseDao<Upt01MaitixUser,Long> ,MaitixUserDaoCustom {
	
	/**
	 * 根据组织机构用户Id查询用户记录
	 * @param userId 组织机构用户Id
	 * @return
	 */
    public Upt01MaitixUser findByUserId(Long userId);
}