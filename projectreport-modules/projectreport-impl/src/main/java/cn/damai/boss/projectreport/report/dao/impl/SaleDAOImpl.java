package cn.damai.boss.projectreport.report.dao.impl;

import cn.damai.boss.projectreport.report.dao.SaleDAO;
import cn.damai.boss.projectreport.report.enums.SaleModeEnum;
import cn.damai.boss.projectreport.report.enums.TicketTypeEnum;
import cn.damai.boss.projectreport.report.vo.*;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注释：出票汇总DAO实现类
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-6 下午2:06
 */
@Repository
public class SaleDAOImpl extends BaseDaoSupport implements SaleDAO {
    /**
     * 查询场次数据
     *
     * @param filterVo 过滤参数vo
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public Map<Long, SaleVo> findPerformData(SaleFilterVo filterVo) {
        Long projectId = filterVo.getProjectId();
        String performIds = filterVo.getPerformIds();
        String sql = getPerformSQL(performIds, false);
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter("projectId", projectId);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        //分页
        if (filterVo.isSplitPage()) {
            int page = filterVo.getPage();
            page = page <= 0 ? 1 : page;
            int pageSize = filterVo.getPageSize();
            sqlQuery.setFirstResult((page - 1) * pageSize);
            sqlQuery.setMaxResults(pageSize);
        }
        List<Map> list = sqlQuery.list();

        //整合数据
        Map<Long, SaleVo> saleDetailVoMap = new HashMap<Long, SaleVo>();
        for (Map map : list) {
            long performInfoID = Long.valueOf(map.get("performInfoID").toString());
            String projectName = map.get("projectName").toString();
            String performName = map.get("performName").toString();
            String performTime = map.get("performTime").toString();

            SaleVo detailVo = saleDetailVoMap.get(performInfoID);
            if (detailVo == null) {
                detailVo = new SaleVo();
                detailVo.setProjectName(projectName);
                detailVo.setPerformId(performInfoID);
                detailVo.setPerformName(performName);
                if (performTime != null) {
                    performTime = performTime.substring(0, performTime.indexOf("."));
                }
                detailVo.setPerformTime(performTime);
            }
            saleDetailVoMap.put(performInfoID, detailVo);
        }
        return saleDetailVoMap;
    }

    /**
     * 查询总场次
     *
     * @param filterVo 过滤参数vo
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public int findPerformDataTotal(SaleFilterVo filterVo) {
        Long projectId = filterVo.getProjectId();
        String performIds = filterVo.getPerformIds();
        String sql = getPerformSQL(performIds, true);
        String countSQL = "select count(*) from (" + sql + ") ss";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(countSQL);
        sqlQuery.setParameter("projectId", projectId);
        return (Integer) sqlQuery.uniqueResult();
    }

    /**
     * 查询场次数据SQL
     *
     * @param performIds
     * @return
     */
    private String getPerformSQL(String performIds, boolean isTotal) {
        String sql = "SELECT " +
                "pro.NAME AS projectName, " +
                "per.PerformInfoID AS performInfoID, " +
                "per.NAME AS performName, " +
                "per.PerformTime AS performTime " +
                "FROM " +
                "PerformInfo per  WITH (NOLOCK) " +
                "INNER JOIN ProjectInfo pro ON per.ProjectID = pro.ProjectID " +
                "WHERE per.STATUS IN (2, 3) AND pro.projectId=:projectId ";
        if (!isTotal && performIds != null && performIds.length() != 0) {
            sql += " AND per.PerformInfoID IN (" + performIds + ")";
        }
        return sql;
    }

    /**
     * 单票折扣
     *
     * @param filterVo
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public Map<Long, Map<String, SaleRowVo>> findDisaccount(SaleFilterVo filterVo) {
        String performIds = filterVo.getPerformIds();
        String startTime = filterVo.getStartTime();
        String endTime = filterVo.getEndTime();
        String sql = "select PerformInfoID,Price,psp.GradeName,Discount," +
                "SUM(TicketQuantity) as TicketQuantity,SUM(SumMoney) as SumMoney  " +
                "from ( " +
                "select aod.PGradePriceID,aod.Discount,COUNT(1) as TicketQuantity,  " +
                "sum(aod.RealPrice) as SumMoney from AgentOrder ao  WITH (NOLOCK) " +
                "inner join AgentOrderDetail aod WITH (NOLOCK) on ao.AgentOrderID=aod.AgentOrderID   " +
                " left join PSeatInfo ps WITH (NOLOCK) on ps.PSeatID=aod.PSeatID " +
                " where (ps.SeatType is null or ps.SeatType=0) AND aod.PCombineID is null and aod.Status<>6 " +
                "and (ao.IsChiefFreePrint is null or ao.IsChiefFreePrint=0) " +
                "and ao.PerformInfoID in(" + performIds + ") ";
        if (startTime != null && startTime.length() != 0) {
            sql += "and ao.CreateDate >= :startTime ";
        }
        if (endTime != null && endTime.length() != 0) {
            sql += "and ao.CreateDate <= :endTime ";
        }
        sql += "group by aod.PGradePriceID,aod.Discount   " +
                "union all " +
                "select od.PGradePriceID,100 Discount,COUNT(1) as TicketQuantity,SUM(od.RealPrice) as SumMoney " +
                "from OrderForm ofm  WITH (NOLOCK) " +
                "inner join OrderDetail od WITH (NOLOCK) on ofm.OrderID=od.OrderID " +
                "inner join OrderForm orm WITH (NOLOCK) on od.OrderID=orm.OrderID " +
                " left join PSeatInfo ps WITH (NOLOCK) on ps.PSeatID=od.PSeatID " +
                "where (ps.SeatType is null or ps.SeatType=0) AND od.PCombineID is null and od.Status=6  and od.PerformInfoID in(" + performIds + ") ";

        if (startTime != null && startTime.length() != 0) {
            sql += "and orm.CreateDate >= :startTime ";
        }
        if (endTime != null && endTime.length() != 0) {
            sql += "and orm.CreateDate <= :endTime ";
        }
        sql += "group by od.PGradePriceID " +
                "union all " +
                "select pgrade.TicketSystemInfoID,od.Discount,SUM(od.TicketQuantity) asTicketQuantity ," +
                "SUM(od.RealPrice*od.Quantity) as SumMoney " +
                "from B_OrderDetail od  WITH (NOLOCK) " +
                "inner join PerformJoinPiaoCn pgrade WITH (NOLOCK) on od.ProductID=pgrade.PiaoCnInfoID and pgrade.InfoType=3  " +
                "inner join PerformJoinPiaoCn cnPf WITH (NOLOCK) ON cnPf.PiaoCnInfoID = od.PerformID AND cnPf.InfoType = 2  " +
                "inner join B_OrderTicketPrintInfo btp WITH (NOLOCK) on btp.OrderID=od.OrderID  and btp.IsChiefFreePrint=0   " +
                "inner join B_OrderForm bof WITH (NOLOCK) on od.OrderID=bof.OrderID " +
                "where od.OrderDetailStatus=2 and od.IsCombine=0  " +
                "and cnPf.TicketSystemInfoID in(" + performIds + ") ";
        if (startTime != null && startTime.length() != 0) {
            sql += "and bof.CreateDate >= :startTime ";
        }
        if (endTime != null && endTime.length() != 0) {
            sql += "and bof.CreateDate <= :endTime ";
        }
        sql += "group by pgrade.TicketSystemInfoID,od.Discount " +
                ")  tsingleSell  " +
                "inner join PerformGradePrice pfgp WITH (NOLOCK) on tsingleSell.PGradePriceID=pfgp.PGradePriceID " +
                "inner join ProjectSellGrade psp WITH (NOLOCK) on psp.PSellGradeID=pfgp.PSellGradeID  " +
                "group by PerformInfoID,Price,psp.GradeName,Discount;";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        if (startTime != null && startTime.length() != 0) {
            sqlQuery.setParameter("startTime", startTime);
        }
        if (endTime != null && endTime.length() != 0) {
            sqlQuery.setParameter("endTime", endTime);
        }
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> resultList = sqlQuery.list();
        return getDisaccountMap(resultList, SaleModeEnum.Single.getCode());
    }


    /**
     * 单票总票房
     *
     * @param filterVo 场次id
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public Map<Long, Map<String, SaleRowVo>> findTotal(SaleFilterVo filterVo) {
        int seatType = filterVo.getSeatType();
        String performIds = filterVo.getPerformIds();
        String sql;
        if (seatType == 1) {
            sql = getBoxOfficeBySeat(performIds);
        } else {
            sql = getBoxOfficeByNoSeat(performIds);
        }
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);

        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> list = sqlQuery.list();
        Map<Long, Map<String, SaleRowVo>> performPriceMap = getTotalBoxOffice(list, SaleModeEnum.Single.getCode());
        return performPriceMap;
    }


    /**
     * 有座单票总票房
     *
     * @return
     */

    private String getBoxOfficeBySeat(String performIds) {
        String sql = "SELECT pgp.PerformInfoID,psg.GradeName,pgp.Price,COUNT(1) AS totalQuantity " +
                "FROM  PSeatInfo psi  WITH (NOLOCK) " +
                "INNER JOIN PerformGradePrice pgp  WITH (NOLOCK) ON pgp.PGradePriceID = psi.PGradePriceID " +
                "INNER JOIN ProjectSellGrade psg  WITH (NOLOCK) on psg.PSellGradeID= pgp.PSellGradeID " +
                "LEFT JOIN PromotionCombineDetail pcd WITH (NOLOCK) ON pcd.PSeatID = psi.PSeatID " +
                "WHERE pcd.PCombineID IS NULL AND (psi.SeatType is null or psi.SeatType=0) " +
                "and pgp.PerformInfoID in(" + performIds + ") " +
                "GROUP BY psg.GradeName,pgp.Price,psi.PGradePriceID,pgp.PerformInfoID";
        return sql;
    }

    /**
     * 无座单票总票房
     *
     * @return
     */
    private String getBoxOfficeByNoSeat(String performIds) {
        String sql = "SELECT pgp.PerformInfoID,psg.GradeName, pgp.Price," +
                "SUM(pgp.MaxSellCount) AS totalQuantity " +
                "FROM PerformGradePrice pgp  WITH (NOLOCK) " +
                "inner join ProjectSellGrade psg  WITH (NOLOCK) on psg.PSellGradeID= pgp.PSellGradeID " +
                "WHERE pgp.PerformInfoID in(" + performIds + ") " +
                "GROUP BY psg.GradeName,pgp.Price,pgp.PerformInfoID ";
        return sql;
    }

    /**
     * 单张票（工作&赠票）
     *
     * @param filterVo
     * @return
     */
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public Map<Long, Map<String, SaleRowVo>> findPresentOrStaff(SaleFilterVo filterVo) {
        String performIds = filterVo.getPerformIds();
        int tickeType = filterVo.getTicketType();
        String startTime = filterVo.getStartTime();
        String endTime = filterVo.getEndTime();
        String sql = "select ao.PerformInfoID,pfgp.Price,psp.GradeName,COUNT(1) as TicketQuantity " +
                "from AgentOrderDetail aod  WITH (NOLOCK) " +
                "inner join AgentOrder ao WITH (NOLOCK) on aod.AgentOrderID=ao.AgentOrderID  " +
                "inner join PerformGradePrice pfgp WITH (NOLOCK) on aod.PGradePriceID=pfgp.PGradePriceID " +
                "inner join ProjectSellGrade psp WITH (NOLOCK) on psp.PSellGradeID=pfgp.PSellGradeID " +
                " LEFT JOIN PSeatInfo ps WITH (NOLOCK) ON ps.PSeatID = aod.PSeatID "+
                "where (ps.SeatType IS NULL OR ps.SeatType = 0 ) AND aod.PCombineID is null and aod.Status<>6  " +
                "and aod.IsChiefFreePrint=:isChiefFreePrint and ps.SeatType not in(2,3) " +
                "and ao.PerformInfoID in(" + performIds + ") ";
        if (startTime != null && startTime.length() != 0) {
            sql += "and ao.CreateDate >= :startTime ";
        }
        if (endTime != null && endTime.length() != 0) {
            sql += "and ao.CreateDate <= :endTime ";
        }
        sql += "group by ao.PerformInfoID,pfgp.Price,psp.GradeName ";
        sql += "UNION ALL ";
        sql += "SELECT pgrade.TicketSystemInfoID as PerformInfoID, pfgp.Price, psp.GradeName,SUM(od.TicketQuantity) asTicketQuantity " +
                "FROM  B_OrderDetail od WITH (NOLOCK) " +
                "INNER JOIN PerformJoinPiaoCn pgrade WITH (NOLOCK) ON od.ProductID = pgrade.PiaoCnInfoID " +
                "AND pgrade.InfoType = 3 " +
                "INNER JOIN PerformJoinPiaoCn cnPf WITH (NOLOCK) ON cnPf.PerformJoinPiaoCnID = od.PerformID " +
                "AND cnPf.InfoType = 2 " +
                "INNER JOIN B_OrderTicketPrintInfo btp WITH (NOLOCK) ON btp.OrderID = od.OrderID " +
                "INNER JOIN B_OrderForm bof WITH (NOLOCK) ON od.OrderID = bof.OrderID " +
                "INNER JOIN PerformGradePrice pfgp WITH (NOLOCK) ON cnPf.TicketSystemInfoID=pfgp.PerformInfoID " +
                "INNER JOIN ProjectSellGrade psp WITH (NOLOCK) ON psp.PSellGradeID = pfgp.PSellGradeID " +
                "WHERE od.OrderDetailStatus = 2 AND od.IsCombine = 0  " +
                "AND btp.IsChiefFreePrint=:isChiefFreePrint " +
                "AND cnPf.TicketSystemInfoID IN (" + performIds + ") ";
        if (startTime != null && startTime.length() != 0) {
            sql += "AND bof.CreateDate >= :startTime ";
        }
        if (endTime != null && endTime.length() != 0) {
            sql += "AND bof.CreateDate <= :endTime ";
        }
        sql += "GROUP BY pgrade.TicketSystemInfoID, pfgp.Price, psp.GradeName ";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter("isChiefFreePrint", tickeType);

        if (startTime != null && startTime.length() != 0) {
            sqlQuery.setParameter("startTime", startTime);
        }
        if (endTime != null && endTime.length() != 0) {
            sqlQuery.setParameter("endTime", endTime);
        }
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> resultList = sqlQuery.list();
        return getStaffOrPresentMap(resultList, tickeType, SaleModeEnum.Single.getCode());
    }


    /**
     * 单票预留票房
     *
     * @param filterVo
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public Map<Long, Map<String, SaleRowVo>> findReserveBoxOffice(SaleFilterVo filterVo) {
        String sql = "Select  PGP.PerformInfoID,PGP.Price, psg.GradeName,COUNT(PSO.PSeatID) as quantity " +
                "from PSeatInfo PSO  WITH (NOLOCK) " +
                "Inner Join PerformGradePrice PGP WITH (NOLOCK) On PGP.PGradePriceID = PSO.PGradePriceID " +
                "Inner join ProjectSellGrade psg WITH (NOLOCK) on psg.PSellGradeID=PGP.PSellGradeID " +
                "left join PromotionCombineDetail pcd WITH (NOLOCK) on pcd.PSeatID=PSO.PSeatID " +
                "Where pcd.PCombineID is null and PSO.Status = 6 " +
                "and PGP.PerformInfoID in(" + filterVo.getPerformIds() + ") " +
                "group by PGP.PerformInfoID,PGP.Price,psg.GradeName";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> resultList = sqlQuery.list();
        return getReserveBoxOffice(resultList, SaleModeEnum.Single.getCode());
    }

    /**
     * 套票（普通）
     *
     * @param filterVo
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public Map<Long, Map<String, SaleRowVo>> findDisaccountPromotion(SaleFilterVo filterVo) {
        String performIds = filterVo.getPerformIds();
        String startTime = filterVo.getStartTime();
        String endTime = filterVo.getEndTime();
        String sql = "select sale.PerformInfoID,sale.PromotionName,sale.Price,sale.Discount," +
                "SUM(sale.TicketQuantity) as TicketQuantity,SUM(sale.SumMoney) as SumMoney " +
                "from( " +
                "select tp.PerformInfoID, tp.PromotionName,tp.Price,t.Discount," +
                "sum(t.TicketQuantity) as TicketQuantity,SUM(t.RealPrice) as SumMoney " +
                "from TicketPromotion tp  WITH (NOLOCK) " +
                "inner join  " +
                "( " +
                "select aod.Discount,aod.RealPrice,COUNT(1)as TicketQuantity " +
                ",aod.PromotionID,aod.PCombineID " +
                "from AgentOrder ao  WITH (NOLOCK) " +
                "inner join AgentOrderDetail aod WITH (NOLOCK) on aod.AgentOrderID=ao.AgentOrderID " +
                "left join PSeatInfo ps WITH (NOLOCK) on ps.PSeatID=aod.PSeatID " +
                "where (ps.SeatType is null or ps.SeatType=0) AND ao.Status<>6 and ao.PerformInfoID in(" + performIds + ") " +
                "and (aod.IsChiefFreePrint is null or aod.IsChiefFreePrint=0) ";
        if (startTime != null && startTime.length() != 0) {
            sql += "and ao.CreateDate >= :startTime ";
        }
        if (endTime != null && endTime.length() != 0) {
            sql += "and ao.CreateDate <= :endTime ";
        }
        sql += "group by aod.Discount,aod.RealPrice,aod.PromotionID,aod.PCombineID " +
                ") t on t.PromotionID=tp.PromotionID " +
                "group by tp.PerformInfoID,tp.Price,tp.PromotionName, t.Discount " +
                "union all " +
                "select tp.PerformInfoID,tp.PromotionName,tp.Price,100 as Discount," +
                "SUM(t.TicketQuantity) as TicketQuantity,SUM(t.RealPrice) as SumMoney " +
                "from TicketPromotion tp inner join ( " +
                "select od.PromotionID,od.RealPrice,COUNT(1) as TicketQuantity " +
                "from OrderDetail od " +
                "inner join TicketPromotion tp WITH (NOLOCK) on tp.PromotionID=od.PromotionID " +
                "inner join OrderForm orm WITH (NOLOCK) on od.OrderID=orm.OrderID " +
                " LEFT JOIN PSeatInfo ps WITH (NOLOCK) ON ps.PSeatID = od.PSeatID "+
                "where (ps.SeatType IS NULL OR ps.SeatType = 0 ) AND od.Status<>6 and od.PerformInfoID in(" + performIds + ") ";
        if (startTime != null && startTime.length() != 0) {
            sql += "and orm.CreateDate >= :startTime ";
        }
        if (endTime != null && endTime.length() != 0) {
            sql += "and orm.CreateDate <= :endTime ";
        }
        sql += "group by od.PromotionID,od.RealPrice,od.PCombineID " +
                ")t on t.PromotionID=tp.PromotionID " +
                "group by tp.PerformInfoID,tp.PromotionName,tp.Price  " +
                "union all " +
                "select tp.PerformInfoID,tp.PromotionName,tp.Price," +
                "od.Discount,SUM(od.TicketQuantity) as TicketQuantity, " +
                "SUM(od.RealPrice*od.Quantity) as SumMoney    " +
                "from B_OrderDetail od  WITH (NOLOCK) " +
                "inner join PerformJoinPiaoCn pgrade WITH (NOLOCK) on od.ProductID=pgrade.PiaoCnInfoID and pgrade.InfoType=5 " +
                "inner join PerformJoinPiaoCn cnPf WITH (NOLOCK) on cnPf.PiaoCnInfoID = od.PerformID AND cnPf.InfoType = 2 " +
                "inner join TicketPromotion tp WITH (NOLOCK) on tp.PromotionID=pgrade.TicketSystemInfoID " +
                "inner join B_OrderTicketPrintInfo otp WITH (NOLOCK) " +
                "on otp.OrderID=od.OrderID and (otp.IsChiefFreePrint is null or otp.IsChiefFreePrint=0) " +
                "inner join B_OrderForm bof WITH (NOLOCK) on od.OrderID=bof.OrderID " +
                "where od.OrderDetailStatus=2 and od.IsCombine=1 and cnPf.TicketSystemInfoID in(" + performIds + ") ";
        if (startTime != null && startTime.length() != 0) {
            sql += "and bof.CreateDate >= :startTime ";
        }
        if (endTime != null && endTime.length() != 0) {
            sql += "and bof.CreateDate <= :endTime ";
        }
        sql += "group by tp.PerformInfoID,tp.PromotionName,tp.Price,od.Discount " +
                ")sale " +
                "group by sale.PerformInfoID,sale.PromotionName,sale.Price,sale.Discount";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);

        if (startTime != null && startTime.length() != 0) {
            sql += "and bof.CreateDate >= :startTime ";
            sqlQuery.setParameter("startTime", startTime);
        }
        if (endTime != null && endTime.length() != 0) {
            sqlQuery.setParameter("endTime", endTime);
        }
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> resultList = sqlQuery.list();
        return getDisaccountMap(resultList, SaleModeEnum.Promotion.getCode());
    }

    /**
     * 套票（工作&赠票）
     *
     * @param filterVo
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public Map<Long, Map<String, SaleRowVo>> findPresentOrStaffPromotion(SaleFilterVo filterVo) {
        String performIds = filterVo.getPerformIds();
        String startTime = filterVo.getStartTime();
        String endTime = filterVo.getEndTime();
        int ticketType = filterVo.getTicketType();

        String sql = "select sale.PerformInfoID,sale.PromotionName,sale.Price," +
                "SUM(sale.TicketQuantity) as TicketQuantity,SUM(sale.SumMoney) as SumMoney " +
                "from( " +
                "select tp.PerformInfoID, tp.PromotionName,tp.Price," +
                "sum(t.TicketQuantity) as TicketQuantity,SUM(t.RealPrice) as SumMoney " +
                "from TicketPromotion tp  WITH (NOLOCK) " +
                "inner join  " +
                "( " +
                " select aod.RealPrice,COUNT(1)as TicketQuantity " +
                " ,aod.PromotionID,aod.PCombineID " +
                " from AgentOrder ao  WITH (NOLOCK) " +
                " inner join AgentOrderDetail aod WITH (NOLOCK) on aod.AgentOrderID=ao.AgentOrderID " +
                " left join PSeatInfo ps WITH (NOLOCK) on ps.PSeatID=aod.PSeatID " +
                " and (ps.SeatType is null or ps.SeatType=0)  " +
                " where ao.Status<>6 and ao.PerformInfoID in(" + performIds + ")and aod.IsChiefFreePrint =:isChiefFreePrint ";
        if (startTime != null && startTime.length() != 0) {
            sql += "and ao.CreateDate >= :startTime ";
        }
        if (endTime != null && endTime.length() != 0) {
            sql += "and ao.CreateDate <= :endTime ";
        }
        sql += "group by aod.RealPrice,aod.PromotionID,aod.PCombineID " +
                ") t on t.PromotionID=tp.PromotionID " +
                "group by tp.PerformInfoID,tp.Price,tp.PromotionName " +
                "union all   " +
                "select tp.PerformInfoID,tp.PromotionName,tp.Price,SUM(od.TicketQuantity) as TicketQuantity , " +
                "SUM(od.RealPrice*od.Quantity) as SumMoney    " +
                "from B_OrderDetail od  WITH (NOLOCK) " +
                "inner join PerformJoinPiaoCn pgrade WITH (NOLOCK) on od.ProductID=pgrade.PiaoCnInfoID and pgrade.InfoType=5 " +
                "inner join PerformJoinPiaoCn cnPf WITH (NOLOCK) ON cnPf.PerformJoinPiaoCnID = od.PerformID AND cnPf.InfoType = 2   " +
                "inner join TicketPromotion tp WITH (NOLOCK) on tp.PromotionID=pgrade.TicketSystemInfoID " +
                "inner join B_OrderTicketPrintInfo otp WITH (NOLOCK) on otp.OrderID=od.OrderID and otp.IsChiefFreePrint=:isChiefFreePrint  " +
                "inner join B_OrderForm bof WITH (NOLOCK) on od.OrderID=bof.OrderID " +
                "where od.OrderDetailStatus=2 and od.IsCombine=1 and cnPf.TicketSystemInfoID in(" + performIds + ") ";
        if (startTime != null && startTime.length() != 0) {
            sql += "and bof.CreateDate >= :startTime ";
        }
        if (endTime != null && endTime.length() != 0) {
            sql += "and bof.CreateDate <= :endTime ";
        }
        sql += "group by tp.PerformInfoID,tp.PromotionName,tp.Price " +
                ")sale " +
                "group by sale.PerformInfoID,sale.PromotionName,sale.Price";

        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);

        sqlQuery.setParameter("isChiefFreePrint", ticketType);
        if (startTime != null && startTime.length() != 0) {
            sqlQuery.setParameter("startTime", startTime);
        }
        if (endTime != null && endTime.length() != 0) {
            sqlQuery.setParameter("endTime", endTime);
        }
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> resultList = sqlQuery.list();
        return getStaffOrPresentMap(resultList, ticketType, SaleModeEnum.Promotion.getCode());
    }

    /**
     * 套票总票房
     *
     * @param filterVo 场次id
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public Map<Long, Map<String, SaleRowVo>> findTotalPromotion(SaleFilterVo filterVo) {
        String performIds = filterVo.getPerformIds();
        String sql = "SELECT PerformInfoID, PromotionName, Price, " +
                "SUM(cmb.TicketQuantity) AS totalQuantity, SUM(cmb.Price) AS sumMoney " +
                "FROM ( " +
                "SELECT tp.PerformInfoID, tp.PromotionName, tp.Price, " +
                "COUNT(1) AS TicketQuantity " +
                "FROM TicketPromotion tp WITH (NOLOCK)  " +
                "INNER JOIN PromotionCombine pcb WITH (NOLOCK) ON pcb.PromotionID = tp.PromotionID " +
                "INNER JOIN PromotionCombineDetail pcbd WITH (NOLOCK) ON pcbd.PCombineID = pcb.PCombineID " +
                "WHERE tp.PerformInfoID IN (" + performIds + ") " +
                "GROUP BY tp.PerformInfoID, tp.PromotionName, tp.Price, pcb.PCombineID " +
                ") cmb " +
                "GROUP BY cmb.PerformInfoID, cmb.PromotionName, cmb.Price";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> list = sqlQuery.list();
        Map<Long, Map<String, SaleRowVo>> performPriceMap = getTotalBoxOffice(list, SaleModeEnum.Promotion.getCode());
        return performPriceMap;
    }


    /**
     * 套票预留票房
     *
     * @param filterVo
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public Map<Long, Map<String, SaleRowVo>> findReservePromotion(SaleFilterVo filterVo) {
        String performIds = filterVo.getPerformIds();
        String sql = "select cmb.PerformInfoID,cmb.PromotionName,cmb.Price," +
                "sum(cmb.TicketQuantity) as quantity,SUM(cmb.Price) as sumMoney " +
                "from( " +
                "select tp.PerformInfoID,tp.PromotionName,tp.Price,COUNT(1) as TicketQuantity " +
                "from TicketPromotion tp WITH (NOLOCK) " +
                "inner join PromotionCombine pcb WITH (NOLOCK) on pcb.PromotionID=tp.PromotionID " +
                "inner join PromotionCombineDetail pcbd WITH (NOLOCK) on pcbd.PCombineID=pcb.PCombineID " +
                "inner join PSeatInfo ps WITH (NOLOCK) on ps.PSeatID=pcbd.PSeatID and ps.Status=6 " +
                "where pcb.Status=1 and tp.PerformInfoID in(" + performIds + ")" +
                "group by tp.PerformInfoID,tp.PromotionName,tp.Price,pcb.PCombineID " +
                ") cmb " +
                "group by cmb.PerformInfoID,cmb.PromotionName,cmb.Price";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> resultList = sqlQuery.list();
        return getReserveBoxOffice(resultList, SaleModeEnum.Promotion.getCode());
    }


    /**
     * 场次工作票和防涨票
     *
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public Map<Long, SaleVo> findPerformStaffOrProtect(SaleFilterVo filterVo) {
        String performIds = filterVo.getPerformIds();
        String sql = "SELECT " +
                "per.PerformInfoID AS performInfoID, " +
                "SeatType AS seatType, " +
                "count(1) AS quantity " +
                "FROM " +
                "PerformInfo per  WITH (NOLOCK) " +
                "LEFT JOIN PstandInfo psi  WITH (NOLOCK) ON per.PerformInfoID = psi.PerformInfoID " +
                "LEFT JOIN PSeatInfo si  WITH (NOLOCK) ON psi.PStandID = si.PStandID " +
                "WHERE SeatType <> 0 AND " +
                "per.PerformInfoID IN (" + performIds + ") " +
                "GROUP BY per.PerformInfoID,SeatType";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> list = sqlQuery.list();
        Map<Long, SaleVo> saleDetailVoMap = new HashMap<Long, SaleVo>();
        if ((list == null || list.size() == 0) && performIds != null && performIds.length() != 0) {
            String[] perfomrIdArr = performIds.split(",");
            for (String performInfo : perfomrIdArr) {
                Long performInfoID = Long.valueOf(performInfo);
                SaleVo saleVo = new SaleVo();
                saleVo.setProtectQuantity(0);
                saleVo.setStaffQuantity(0);
                saleDetailVoMap.put(performInfoID, saleVo);
            }
            return saleDetailVoMap;
        }
        for (Map map : list) {

            Object seatTypeObj = map.get("seatType");
            int seatType = 0;
            if (seatTypeObj != null) {
                seatType = Integer.valueOf(seatTypeObj.toString());
            }

            int quantity = 0;
            Object quantityObj = map.get("quantity");
            if (quantityObj != null) {
                quantity = Integer.valueOf(quantityObj.toString());
            }

            long performInfoID = Long.valueOf(map.get("performInfoID").toString());
            SaleVo detailVo = saleDetailVoMap.get(performInfoID);
            if (detailVo == null) {
                detailVo = new SaleVo();
                detailVo.setPerformId(performInfoID);
            }

            if (seatType == 1) {
                detailVo.setStaffQuantity(quantity);
            } else if (seatType == 2) {
                detailVo.setProtectQuantity(quantity);
            }

            saleDetailVoMap.put(performInfoID, detailVo);
        }
        return saleDetailVoMap;
    }

    /**
     * 把单票折扣【正常出售】数据封装成map
     * <p>
     * key：场次ID  value：价格map
     * </p>
     *
     * @param resultList
     * @param saleMode   销售方式，1：单票，2：套票
     * @return
     */
    private Map<Long, Map<String, SaleRowVo>> getDisaccountMap(List<Map> resultList, int saleMode) {
        Map<Long, Map<String, SaleRowVo>> performMapMap = new HashMap<Long, Map<String, SaleRowVo>>();
        for (Map resultMap : resultList) {
            //获取数据
            Long performInfoID = Long.valueOf(resultMap.get("PerformInfoID").toString());
            BigDecimal price = new BigDecimal(resultMap.get("Price").toString());
            String disaccountName = getNameByProcessZero(resultMap.get("Discount").toString());
            String sumMoneyStr = getNameByProcessZero(resultMap.get("SumMoney").toString());
            BigDecimal sumMoney = new BigDecimal(sumMoneyStr);
            Integer quantity = Integer.valueOf(resultMap.get("TicketQuantity").toString());

            String priceName;
            if (saleMode == SaleModeEnum.Single.getCode()) {
                //如果是单票，显示格式：A级（100）
                String gradeName = resultMap.get("GradeName").toString();
                String name = String.valueOf(price.doubleValue());
                priceName = gradeName == null ? name : gradeName + "(" + name + ")";
            } else {
                //如果是套票，显示套票名称，格式：套票 58元 或 240（80X3）
                priceName = resultMap.get("PromotionName").toString();
            }

            Map<String, SaleRowVo> saleVoMap = performMapMap.get(performInfoID);
            if (saleVoMap == null || saleVoMap.size() == 0) {
                saleVoMap = new HashMap<String, SaleRowVo>();
            }
            SaleRowVo saleRowVo = saleVoMap.get(priceName);
            if (saleRowVo == null) {
                saleRowVo = new SaleRowVo();
            }
            List<DisaccountVo> disaccountVoList = saleRowVo.getDisaccountVoList();
            if (disaccountVoList == null || disaccountVoList.size() == 0) {
                disaccountVoList = new ArrayList<DisaccountVo>();
                DisaccountVo disaccountVo = new DisaccountVo();
                disaccountVo.setDisaccountName(disaccountName);
                disaccountVo.setQuantity(quantity);
                disaccountVo.setAmount(sumMoney);
                disaccountVoList.add(disaccountVo);
            } else {
                boolean isContain = false;
                for (DisaccountVo vo : disaccountVoList) {
                    String name = vo.getDisaccountName();
                    if (disaccountName.equals(name)) {
                        isContain = true;
                        vo.setQuantity(vo.getQuantity() + quantity);
                        BigDecimal amount = vo.getAmount();
                        amount = amount.add(sumMoney);
                        vo.setAmount(amount);
                    }
                }
                if (!isContain) {
                    DisaccountVo disaccountVo = new DisaccountVo();
                    disaccountVo.setDisaccountName(disaccountName);
                    disaccountVo.setQuantity(quantity);
                    disaccountVo.setAmount(sumMoney);
                    disaccountVoList.add(disaccountVo);
                }
            }

            //设置折扣
            saleRowVo.setDisaccountVoList(disaccountVoList);
            //设置价格
            PriceVo priceVo = new PriceVo();
            priceVo.setPriceName(priceName);
            priceVo.setPrice(price);
            priceVo.setTicketType((short) saleMode);
            saleRowVo.setPriceVo(priceVo);

            saleVoMap.put(priceName, saleRowVo);
            performMapMap.put(performInfoID, saleVoMap);
        }
        return performMapMap;
    }

    /**
     * 得到工作、防涨票map【单票或套票】
     *
     * @param list
     * @param ticketType 票类型，1：赠票，2：工作票
     * @param saleMode   销售方式，1：单票，2：套票
     * @return
     */
    private Map<Long, Map<String, SaleRowVo>> getStaffOrPresentMap(List<Map> list, int ticketType, int saleMode) {
        Map<Long, Map<String, SaleRowVo>> performMap = new HashMap<Long, Map<String, SaleRowVo>>();
        for (Map resultMap : list) {
            //获取数据
            Long performInfoID = Long.valueOf(resultMap.get("PerformInfoID").toString());
            Integer quantity = Integer.valueOf(resultMap.get("TicketQuantity").toString());
            //注意：赠票、工作票金额都是为0
            BigDecimal sumMoney = BigDecimal.ZERO;

            String priceName;
            if (saleMode == SaleModeEnum.Single.getCode()) {
                String gradeName = resultMap.get("GradeName").toString();
                BigDecimal price = new BigDecimal(resultMap.get("Price").toString());
                String name = String.valueOf(price.doubleValue());
                priceName = gradeName == null ? name : gradeName + "(" + name + ")";
            } else {
                priceName = resultMap.get("PromotionName").toString();
            }

            Map<String, SaleRowVo> saleVoMap = performMap.get(performInfoID);
            if (saleVoMap == null) {
                saleVoMap = new HashMap<String, SaleRowVo>();
            }
            SaleRowVo saleRowVo = saleVoMap.get(priceName);
            if (saleRowVo == null) {
                saleRowVo = new SaleRowVo();
            }

            PriceCellVo cellVo = new PriceCellVo();
            cellVo.setQuantity(quantity);
            cellVo.setAmount(sumMoney);
            if (ticketType == TicketTypeEnum.Staff.getCode()) {
                saleRowVo.setStaffSale(cellVo);
            } else {
                saleRowVo.setPresentSale(cellVo);
            }
            saleVoMap.put(priceName, saleRowVo);
            performMap.put(performInfoID, saleVoMap);
        }
        return performMap;
    }

    /**
     * 得到总票房
     *
     * @param list
     * @param saleMode 1：单票，2：套票
     * @return
     */
    private Map<Long, Map<String, SaleRowVo>> getTotalBoxOffice(List<Map> list, int saleMode) {
        Map<Long, Map<String, SaleRowVo>> performPriceMap = new HashMap<Long, Map<String, SaleRowVo>>();
        for (Map resultMap : list) {
            Long performInfoID = Long.valueOf(resultMap.get("PerformInfoID").toString());
            Integer totalQuantity = Integer.valueOf(resultMap.get("totalQuantity").toString());
            BigDecimal price = new BigDecimal(resultMap.get("Price").toString());
            BigDecimal totalAmount = BigDecimal.ZERO;
            if (saleMode == SaleModeEnum.Single.getCode()) {
                totalAmount = price.multiply(new BigDecimal(totalQuantity));
            } else {
                totalAmount = new BigDecimal(resultMap.get("sumMoney").toString());
            }

            String priceName;
            if (saleMode == SaleModeEnum.Single.getCode()) {
                String gradeName = resultMap.get("GradeName").toString();
                String name = String.valueOf(price.doubleValue());
                priceName = gradeName == null ? name : gradeName + "(" + name + ")";
            } else {
                priceName = resultMap.get("PromotionName").toString();
            }

            Map<String, SaleRowVo> saleVoMap = performPriceMap.get(performInfoID);
            if (saleVoMap == null || saleVoMap.size() == 0) {
                saleVoMap = new HashMap<String, SaleRowVo>();
            }
            SaleRowVo saleRowVo = saleVoMap.get(priceName);
            if (saleRowVo == null) {
                saleRowVo = new SaleRowVo();
            }

            PriceCellVo totalCellVo = new PriceCellVo();
            totalCellVo.setQuantity(totalQuantity);
            totalCellVo.setAmount(totalAmount);

            saleRowVo.setPriceTotalSale(totalCellVo);
            saleVoMap.put(priceName, saleRowVo);
            performPriceMap.put(performInfoID, saleVoMap);
        }
        return performPriceMap;
    }


    /**
     * 得到预留票房
     *
     * @param resultList
     * @return
     */
    private Map<Long, Map<String, SaleRowVo>> getReserveBoxOffice(List<Map> resultList, int saleMode) {
        Map<Long, Map<String, SaleRowVo>> performPriceMap = new HashMap<Long, Map<String, SaleRowVo>>();
        for (Map resultMap : resultList) {
            Long performInfoID = Long.valueOf(resultMap.get("PerformInfoID").toString());
            Integer quantity = Integer.valueOf(resultMap.get("quantity").toString());
            BigDecimal price = new BigDecimal(resultMap.get("Price").toString());
            BigDecimal amount = BigDecimal.ZERO;
            if (saleMode == SaleModeEnum.Single.getCode()) {
                amount = price.multiply(new BigDecimal(quantity));
            } else {
                amount = new BigDecimal(resultMap.get("sumMoney").toString());
            }

            String priceName;
            if (saleMode == SaleModeEnum.Single.getCode()) {
                String gradeName = resultMap.get("GradeName").toString();
                String name = String.valueOf(price.doubleValue());
                priceName = gradeName == null ? name : gradeName + "(" + name + ")";
            } else {
                priceName = resultMap.get("PromotionName").toString();
            }

            Map<String, SaleRowVo> saleVoMap = performPriceMap.get(performInfoID);
            if (saleVoMap == null || saleVoMap.size() == 0) {
                saleVoMap = new HashMap<String, SaleRowVo>();
            }
            SaleRowVo saleRowVo = saleVoMap.get(priceName);
            if (saleRowVo == null) {
                saleRowVo = new SaleRowVo();
            }

            PriceCellVo cellVo = new PriceCellVo();
            cellVo.setQuantity(quantity);
            cellVo.setAmount(amount);

            saleRowVo.setReserveSale(cellVo);
            saleVoMap.put(priceName, saleRowVo);
            performPriceMap.put(performInfoID, saleVoMap);
        }
        return performPriceMap;
    }

    /**
     * 得到价格名称，去除0，例如：源名称：90.100，处理后：90.1
     *
     * @param name
     * @return
     */
    private String getNameByProcessZero(String name) {
        if (name.indexOf(".") > 0) {
            while (name.lastIndexOf("0") == name.length() - 1) {
                name = name.substring(0, name.lastIndexOf("0"));
            }
            if (name.indexOf(".") == name.length() - 1) {
                name = name.substring(0, name.length() - 1);
            }
        }
        return name;
    }

}
