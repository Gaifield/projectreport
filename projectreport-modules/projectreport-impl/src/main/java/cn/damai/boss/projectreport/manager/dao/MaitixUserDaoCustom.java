package cn.damai.boss.projectreport.manager.dao;

import java.util.List;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.domain.Upt01MaitixUser;

/**
 * Created by 炜 on 14-2-21.
 */
public interface MaitixUserDaoCustom {

    /**
     * 根据角色id查询用户列表
     * @param roleId 角色id
     * @return
     * @throws ApplicationException
     * @author 顾炜
     */
    List<Upt01MaitixUser> queryUpt01MaitixUsersByRoleId(long roleId) throws ApplicationException;
    
    /**
     * 根据用户角色分页查询Maitix用户列表
     * @param roleId 角色Id
     * @param pageSize 每页大小
     * @param pageIndex 页码
     * @return
     */
    PageResultData<Upt01MaitixUser> queryMaitixUsers(Long roleId,int pageSize,int pageIndex);
}