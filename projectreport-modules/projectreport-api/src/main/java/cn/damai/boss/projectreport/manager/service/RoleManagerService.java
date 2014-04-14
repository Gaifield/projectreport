package cn.damai.boss.projectreport.manager.service;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.manager.vo.ReportRoleVo;
import cn.damai.boss.projectreport.manager.vo.ReportVo;

import java.util.List;

/**
 * 角色管理
 * Created by 炜 on 14-2-20.
 */
public interface RoleManagerService {

    /**
     * 查询所有报表角色
     *
     * @return
     */
    List<ReportRoleVo> queryAllReportRoles() throws ApplicationException;

    /**
     * 根据角色id查询角色信息
     *
     * @param reportRoleId 角色id
     * @return
     * @throws ApplicationException
     */
    ReportRoleVo queryReportRoleById(long reportRoleId) throws ApplicationException;

    /**
     * 新建角色
     *
     * @return
     */
    boolean saveRole(ReportRoleVo vo) throws ApplicationException;

    /**
     * 编辑角色
     *
     * @return
     */
    boolean modifyRole(ReportRoleVo vo) throws ApplicationException;
    
    /**
     * 根据用户查询授权的报表
     * @param userId 用户Id
     * @return List<ReportRoleVo> 
     * @throws ApplicationException
     */
    public List<ReportVo> findUserReportList(long userId) throws ApplicationException;
}
