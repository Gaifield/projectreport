package cn.damai.boss.projectreport.manager.dao.impl;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.domain.Upt01MaitixUser;
import cn.damai.boss.projectreport.common.dao.BaseJpaDaoSupport;
import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.manager.dao.MaitixUserDaoCustom;
import cn.damai.boss.projectreport.manager.enums.ReportRoleEnum;
import cn.damai.boss.projectreport.manager.vo.MaitixUserVo;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 炜 on 14-2-22.
 */
@Repository
public class MaitixUserDaoCustomImpl extends BaseJpaDaoSupport<Upt01MaitixUser, Long> implements MaitixUserDaoCustom {

    /**
     * 根据角色id查询用户信息
     *
     * @param roleId 角色id
     * @return
     * @throws ApplicationException
     */
    @Override
    public List<Upt01MaitixUser> queryUpt01MaitixUsersByRoleId(long roleId) throws ApplicationException {
        StringBuilder sb=new StringBuilder(" from Upt01MaitixUser as u  WHERE 1=1 ");
        //判断用户角色是否为全部
        if (roleId != ReportRoleEnum.All.getCode()) {
        	sb.append(" AND u.upt01ReportRole.roleId =?");
        }
        Query query = entityManager.createQuery(String.valueOf(sb),Upt01MaitixUser.class);
        if (roleId != ReportRoleEnum.All.getCode()) {
        	query.setParameter(1, roleId);
        }
        return query.getResultList();
    }
    
    @Override
    public PageResultData<Upt01MaitixUser> queryMaitixUsers(Long roleId,int pageSize,int pageIndex){
    	PageResultData<Upt01MaitixUser> pageResult=new PageResultData<Upt01MaitixUser>(); 
    	
        StringBuilder sb=new StringBuilder(" from Upt01MaitixUser as u  WHERE 1=1 ");
        //判断用户角色是否为全部
        if (roleId != null) {
        	sb.append(" AND u.upt01ReportRole.roleId =?");
        }
        Query queryCount = entityManager.createQuery("select count(*) "+String.valueOf(sb));
    	Query query = entityManager.createQuery(String.valueOf(sb),Upt01MaitixUser.class);
    	
        if (roleId != null) {
        	queryCount.setParameter(1, roleId);
        	query.setParameter(1, roleId);
        }
        pageResult.setTotal((Long) queryCount.getSingleResult());
        
        query.setFirstResult((pageIndex-1)*pageSize);
        query.setMaxResults(pageSize);
        pageResult.setRows(query.getResultList());
        return pageResult;
    }
}