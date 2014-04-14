package cn.damai.boss.projectreport.manager.service;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.vo.MaitixUserVo;

import java.util.List;

/**
 * 主办方用户管理
 * Created by 炜 on 14-2-21.
 */
public interface UserManagerService {

    /**
     * 查询用户信息
     *
     * @param userName     用户名称
     * @param userRoleId   用户角色id
     * @param userStatusId 用户状态id
     * @param pageSize 分页大小
     * @param pageIndex 页码
     * @return
     */
    PageResultData<MaitixUserVo> findMaitixUserList(String userName, long userRoleId, Short userStatus,int pageSize,int pageIndex) throws ApplicationException;


    /**
     * 根据用户id，用户角色id 修改用户角色
     *
     * @param userIdList 用户id
     * @param userRoleId 用户角色id
     * @return
     * @throws ApplicationException
     */
    boolean modifyUserRole(List<Long> userIdList, long userRoleId) throws ApplicationException;
}
