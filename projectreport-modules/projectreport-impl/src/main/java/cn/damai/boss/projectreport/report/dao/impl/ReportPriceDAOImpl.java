package cn.damai.boss.projectreport.report.dao.impl;

import cn.damai.boss.projectreport.commons.utils.Utils;
import cn.damai.boss.projectreport.report.dao.ReportPriceDAO;
import cn.damai.boss.projectreport.report.enums.SaleModeEnum;
import cn.damai.boss.projectreport.report.vo.PriceVo;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReportPriceDAOImpl extends BaseDaoSupport implements ReportPriceDAO {

    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public List<PriceVo> queryProjectReportPrice(long projectId, List<Long> performInfoIds) {
        StringBuilder sb = new StringBuilder();

        sb.append("select ticketType,priceName,price from ( " +
                "select 1 as ticketType,  psp.GradeName as priceName, pgp.Price " +
                "  from PerformGradePrice pgp WITH (NOLOCK) " +
                "  inner join PerformInfo pf WITH (NOLOCK) on pf.PerformInfoID=pgp.PerformInfoID " +
                "  Inner join ProjectSellGrade psp WITH (NOLOCK) on psp.PSellGradeID=PGP.PSellGradeID " +
                " where pgp.Status<>3 and  pf.ProjectID=:projectId ");
        String strPerformIds = "0";
        if (performInfoIds != null) {
            //sb.append(" and pf.PerformInfoID in(:performInfoIds)");
            strPerformIds = Utils.join(performInfoIds);
            sb.append(" AND pf.PerformInfoID in(").append(strPerformIds).append(") ");
        }
        sb.append(" group by pgp.Price,psp.GradeName ");

        sb.append(" union all  " +
                " select 2 as ticketType,tp.PromotionName as priceName,tp.Price " +
                "  from TicketPromotion tp WITH (NOLOCK) " +
                "  inner join PerformInfo pf WITH (NOLOCK) on pf.PerformInfoID=tp.PerformInfoID " +
                " where tp.PromotionStatus<>0 and  pf.ProjectID=:projectId ");
        if (performInfoIds != null) {
            //sb.append(" and pf.PerformInfoID in(:performInfoIds)");
            sb.append(" AND pf.PerformInfoID in(").append(strPerformIds).append(") ");
        }
        sb.append(" group by tp.Price,PromotionName " +
                ")tmp " +
                " order by ticketType,price desc,priceName");

        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(String.valueOf(sb));

        sqlQuery.setParameter("projectId", projectId);
        if (performInfoIds != null) {
            //sqlQuery.setParameterList("performInfoIds", performInfoIds);
        }

        sqlQuery.addScalar("ticketType", new ShortType());
        sqlQuery.addScalar("priceName", new StringType());
        sqlQuery.addScalar("price", new BigDecimalType());
        sqlQuery.setResultTransformer(Transformers.aliasToBean(PriceVo.class));

        return sqlQuery.list();
    }

    /**
     * 查询场次价格
     *
     * @param projectId  项目id
     * @param performIds 场次id
     * @return
     * @author 刘腾飞 2014年3月27日14:31:01增加
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public Map<Long, List<PriceVo>> queryPerformPrice(long projectId, String performIds) {
        String sql = "SELECT performInfoID,ticketType, priceName, price " +
                "FROM " +
                "( " +
                "SELECT pf.PerformInfoID as performInfoID,1 AS ticketType, " +
                "psp.GradeName AS priceName, pgp.Price " +
                "FROM PerformGradePrice pgp WITH (NOLOCK) " +
                "INNER JOIN PerformInfo pf WITH (NOLOCK) ON pf.PerformInfoID = pgp.PerformInfoID " +
                "INNER JOIN ProjectSellGrade psp WITH (NOLOCK) ON psp.PSellGradeID = PGP.PSellGradeID " +
                "WHERE pgp.Status <> 3 " +
                "AND pf.ProjectID = :projectId " +
                "AND pf.PerformInfoID IN (" + performIds + ") " +
                "GROUP BY pf.PerformInfoID,pgp.Price,psp.GradeName " +
                "UNION ALL " +
                "SELECT tp.PerformInfoID as performInfoID,2 AS ticketType, " +
                "PromotionName AS priceName, Price " +
                "FROM TicketPromotion tp WITH (NOLOCK) " +
                "INNER JOIN PerformInfo pf WITH (NOLOCK) ON pf.PerformInfoID = tp.PerformInfoID " +
                "WHERE PromotionStatus <> 0 " +
                "AND pf.ProjectID = :projectId  " +
                "AND tp.PerformInfoID IN (" + performIds + ") " +
                "GROUP BY  tp.PerformInfoID,Price,PromotionName " +
                ") tmp " +
                "ORDER BY ticketType, price DESC, priceName";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter("projectId", projectId);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> list = sqlQuery.list();
        Map<Long, List<PriceVo>> priceVoMap = new HashMap<Long, List<PriceVo>>();
        if (list != null && list.size() != 0) {
            for (Map resultMap : list) {
                Long performInfoID = Long.valueOf(resultMap.get("performInfoID").toString());
                List<PriceVo> priceVoList = priceVoMap.get(performInfoID);
                if (priceVoList == null) {
                    priceVoList = new ArrayList<PriceVo>();
                }
                short ticketType = Short.valueOf(resultMap.get("ticketType").toString());
                String priceName = resultMap.get("priceName").toString();
                BigDecimal price = new BigDecimal(resultMap.get("price").toString());
                String priceShowName = ticketType == SaleModeEnum.Single.getCode() ? priceName + "(" + price + ")" : priceName;

                PriceVo priceVo = new PriceVo();
                priceVo.setTicketType(ticketType);
                priceVo.setPriceName(priceName);
                priceVo.setPriceShowName(priceShowName);
                priceVo.setPrice(price);

                priceVoList.add(priceVo);
                priceVoMap.put(performInfoID, priceVoList);
            }
        }
        return priceVoMap;
    }
}
