package cn.damai.boss.projectreport.report.dao.impl;

import cn.damai.boss.projectreport.common.dao.BaseJpaDaoSupport;
import cn.damai.boss.projectreport.domain.Upt01MaitixUser;
import cn.damai.boss.projectreport.manager.vo.MaitixUserVo;
import cn.damai.boss.projectreport.report.dao.ReportMaitixUserDAO;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * 注释：报表主办方用户DAO
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-9 下午2:21
 */
@Repository
public class ReportMaitixUserDAOImpl extends BaseJpaDaoSupport<Upt01MaitixUser, Long> implements ReportMaitixUserDAO {
    /**
     * 根据组织机构用户Id查询用户记录
     *
     * @param userId 组织机构用户Id
     * @return
     */

    @Override
    public MaitixUserVo findByUserId(Long userId) {
        String sql = "select * from upt01_maitix_user m,upt01_report_role r where m.role_id=r.role_id and user_id=:userId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", userId);
        SQLQuery sqlQuery = query.unwrap(SQLQuery.class);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> list = sqlQuery.list();
        if (list != null && list.size() != 0) {
            MaitixUserVo vo = new MaitixUserVo();
            Map map = list.get(0);
            vo.setMaitixUserId(Long.valueOf(map.get("maitix_user_id").toString()));
            vo.setUserId(Long.valueOf(map.get("user_id").toString()));
            vo.setRoleType(Integer.valueOf(map.get("report_user_type").toString()));
            return vo;
        }
        return null;
    }


}