package cn.damai.boss.projectreport.manager.dao;

import cn.damai.boss.projectreport.domain.Upt01Operator;
import cn.damai.crius.core.dao.BaseDao;

import java.util.List;

/**
 * 注释：操作员DAO
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-2-20 下午2:15
 */
public interface OperatorDAO extends BaseDao<Upt01Operator, Long>, OperatorDAOCustom {
    /**
     * 查询操作员是否已存在
     *
     * @param userId
     * @return
     */
    public Upt01Operator findByUserId(Long userId);

    /**
     * 查询操作员表中启用状态下的管理员
     *
     * @param level
     * @param status
     * @return
     */
    public List<Upt01Operator> findByPermissionLevelAndStatus(Short level, Short status);

    /**
     * 根据操作员id查询操作员
     *
     * @param operatorId
     * @return
     */
    public Upt01Operator findByOperatorId(Long operatorId);
}
