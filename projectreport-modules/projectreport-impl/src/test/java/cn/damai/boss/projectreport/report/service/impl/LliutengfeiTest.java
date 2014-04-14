package cn.damai.boss.projectreport.report.service.impl;

import java.math.BigDecimal;

/**
 * 注释：
 * 作者：liutengfei 【刘腾飞】
 * 时间：14-3-5 下午3:23
 */
public class LliutengfeiTest {
    public static void main(String[] args) {
        //testSQL();
        BigDecimal src = new BigDecimal("99.012");
        BigDecimal tgt = new BigDecimal("80.9900");
        System.out.println(src.compareTo(tgt));
    }


    private static void testSQL() {
        String str = "declare @StartDate datetime;declare @EndDate datetime;set @StartDate=GETDATE()set @EndDate =convert(varchar,@StartDate,10)+' 23:59:59.998'set @StartDate=convert(varchar,@StartDate,10)+' 00:00'; " +
                " " +
                "select pjpcn.PiaoCnInfoID,pj.Name as ProjectName,pj.Status,f.FieldName,f.CityName, " +
                "(select MIN(PerformTime) from %s%.PerformInfo mpf where mpf.ProjectID=pj.ProjectID ) as StartTime " +
                ",(select max(PerformTime) from %s%.PerformInfo mpf where mpf.ProjectID=pj.ProjectID ) as EndTime,f.FieldID " +
                ",tds.TodayMoney,ts.TotalMoney " +
                "from %s%.ProjectInfo pj " +
                "inner join %s%.PerformJoinPiaoCn pjpcn on pjpcn.TicketSystemInfoID=pj.ProjectID and pjpcn.InfoType=1 " +
                "inner join ( " +
                "select pf.ProjectID,f.FieldID,f.Name FieldName,c.Name as CityName from %s%.FieldInfo f inner join %s%.City c on f.CityID=c.CityID " +
                "inner join %s%.PerformInfo pf on pf.FieldID=f.FieldID " +
                "where pf.PerformInfoID in(select MIN(PerformInfoID) from %s%.PerformInfo where Status in(2,3) group by ProjectID) " +
                ") f on f.ProjectID=pj.ProjectID " +
                "left join (select ProjectID,SUM(TotalMoney) AS TotalMoney from (select pf.ProjectID,SUM(ao.RealAmount) TotalMoney from %s%.PerformInfo pf inner join  " +
                "%s%.AgentOrder ao on pf.PerformInfoID=ao.PerformInfoID " +
                "where ao.Status<>6 " +
                "group by pf.ProjectID " +
                "union all " +
                "select pf.ProjectID,SUM(ofm.RealAmount) TotalMoney from  " +
                "%s%.OrderForm ofm inner join %s%.OrderDetail od on od.OrderID=ofm.OrderID " +
                "inner join %s%.PerformInfo pf on pf.PerformInfoID=od.PerformInfoID " +
                "where od.Status=6 and od.PromotionID is null " +
                "group by pf.ProjectID " +
                "union all " +
                "select pct.ProjectID,SUM(pct.RealPrice) as TotalMoney from ( " +
                "select pf.ProjectID,od.PCombineID,od.RealPrice from  " +
                "%s%.OrderForm ofm inner join %s%.OrderDetail od on od.OrderID=ofm.OrderID " +
                "inner join %s%.PerformInfo pf on pf.PerformInfoID=od.PerformInfoID " +
                "where od.Status=6 and od.PromotionID>0 " +
                "group by pf.ProjectID,od.PCombineID,od.RealPrice) pct group by pct.ProjectID " +
                "union all " +
                "select pjcn.TicketSystemInfoID,SUM(bod.RealPrice*bod.Quantity) as TotalMoney from %s%.B_OrderForm bo  " +
                "inner join %s%.B_OrderDetail bod on bo.OrderID=bod.OrderID " +
                "inner join %s%.PerformJoinPiaoCn pjcn on pjcn.PiaoCnInfoID=bod.ProjectID and pjcn.InfoType=1 " +
                "where bod.OrderDetailStatus=2  " +
                "group by pjcn.TicketSystemInfoID) t group by ProjectID) ts on ts.ProjectID=pj.ProjectID " +
                "left join ( select ts.ProjectID,SUM(ts.TodayMoney) as TodayMoney from ( " +
                "select pf.ProjectID,SUM(ao.RealAmount) TodayMoney from %s%.PerformInfo pf inner join  " +
                "%s%.AgentOrder ao on pf.PerformInfoID=ao.PerformInfoID " +
                "where ao.CreateDate between @StartDate and @EndDate and ao.Status<>6 " +
                "group by pf.ProjectID " +
                "union all " +
                "select pf.ProjectID,SUM(ofm.RealAmount) TodayMoney from  " +
                "OrderForm ofm inner join %s%.OrderDetail od on od.OrderID=ofm.OrderID " +
                "inner join %s%.PerformInfo pf on pf.PerformInfoID=od.PerformInfoID " +
                "where ofm.CreateDate between @StartDate and @EndDate and od.Status=6 and od.PromotionID is null " +
                "group by pf.ProjectID " +
                "union all " +
                "select pct.ProjectID,SUM(pct.RealPrice) as TodayMoney from ( " +
                "select pf.ProjectID,od.PCombineID,od.RealPrice from  " +
                "%s%.OrderForm ofm inner join %s%.OrderDetail od on od.OrderID=ofm.OrderID " +
                "inner join %s%.PerformInfo pf on pf.PerformInfoID=od.PerformInfoID " +
                "where ofm.CreateDate between @StartDate and @EndDate and od.Status=6 and od.PromotionID>0 " +
                "group by pf.ProjectID,od.PCombineID,od.RealPrice) pct group by pct.ProjectID " +
                "union all " +
                "select pjcn.TicketSystemInfoID,SUM(bod.RealPrice*bod.Quantity) as TodayMoney from %s%.B_OrderForm bo  " +
                "inner join %s%.B_OrderDetail bod on bo.OrderID=bod.OrderID " +
                "inner join %s%.PerformJoinPiaoCn pjcn on pjcn.PiaoCnInfoID=bod.ProjectID and pjcn.InfoType=1 " +
                "where bo.CreateDate between @StartDate and @EndDate and bod.OrderDetailStatus=2 " +
                "group by pjcn.TicketSystemInfoID) ts group by ts.ProjectID) tds on tds.ProjectID=pj.ProjectID";
        String sql = "select  from (" +
                "select pjpcn.PiaoCnInfoID,pj.Name as ProjectName,pj.Status,f.FieldName,f.CityName, " +
                "(select MIN(PerformTime) from %s%.PerformInfo mpf where mpf.ProjectID=pj.ProjectID ) as StartTime " +
                ",(select max(PerformTime) from %s%.PerformInfo mpf where mpf.ProjectID=pj.ProjectID ) as EndTime,f.FieldID " +
                ",tds.TodayMoney,ts.TotalMoney " +
                "from %s%.ProjectInfo pj " +
                "inner join %s%.PerformJoinPiaoCn pjpcn on pjpcn.TicketSystemInfoID=pj.ProjectID and pjpcn.InfoType=1 " +
                "inner join ( " +
                "select pf.ProjectID,f.FieldID,f.Name FieldName,c.Name as CityName from %s%.FieldInfo f inner join %s%.City c on f.CityID=c.CityID " +
                "inner join %s%.PerformInfo pf on pf.FieldID=f.FieldID " +
                "where pf.PerformInfoID in(select MIN(PerformInfoID) from %s%.PerformInfo where Status in(2,3) group by ProjectID) " +
                ") f on f.ProjectID=pj.ProjectID " +
                "left join (select ProjectID,SUM(TotalMoney) AS TotalMoney from (select pf.ProjectID,SUM(ao.RealAmount) TotalMoney from %s%.PerformInfo pf inner join  " +
                "%s%.AgentOrder ao on pf.PerformInfoID=ao.PerformInfoID " +
                "where ao.Status<>6 " +
                "group by pf.ProjectID " +
                "union all " +
                "select pf.ProjectID,SUM(ofm.RealAmount) TotalMoney from  " +
                "%s%.OrderForm ofm inner join %s%.OrderDetail od on od.OrderID=ofm.OrderID " +
                "inner join %s%.PerformInfo pf on pf.PerformInfoID=od.PerformInfoID " +
                "where od.Status=6 and od.PromotionID is null " +
                "group by pf.ProjectID " +
                "union all " +
                "select pct.ProjectID,SUM(pct.RealPrice) as TotalMoney from ( " +
                "select pf.ProjectID,od.PCombineID,od.RealPrice from  " +
                "%s%.OrderForm ofm inner join %s%.OrderDetail od on od.OrderID=ofm.OrderID " +
                "inner join %s%.PerformInfo pf on pf.PerformInfoID=od.PerformInfoID " +
                "where od.Status=6 and od.PromotionID>0 " +
                "group by pf.ProjectID,od.PCombineID,od.RealPrice) pct group by pct.ProjectID " +
                "union all " +
                "select pjcn.TicketSystemInfoID,SUM(bod.RealPrice*bod.Quantity) as TotalMoney from %s%.B_OrderForm bo  " +
                "inner join %s%.B_OrderDetail bod on bo.OrderID=bod.OrderID " +
                "inner join %s%.PerformJoinPiaoCn pjcn on pjcn.PiaoCnInfoID=bod.ProjectID and pjcn.InfoType=1 " +
                "where bod.OrderDetailStatus=2  " +
                "group by pjcn.TicketSystemInfoID) t group by ProjectID) ts on ts.ProjectID=pj.ProjectID " +
                "left join ( select ts.ProjectID,SUM(ts.TodayMoney) as TodayMoney from ( " +
                "select pf.ProjectID,SUM(ao.RealAmount) TodayMoney from %s%.PerformInfo pf inner join  " +
                "%s%.AgentOrder ao on pf.PerformInfoID=ao.PerformInfoID " +
                "where ao.CreateDate between @StartDate and @EndDate and ao.Status<>6 " +
                "group by pf.ProjectID " +
                "union all " +
                "select pf.ProjectID,SUM(ofm.RealAmount) TodayMoney from  " +
                "%s%.OrderForm ofm inner join %s%.OrderDetail od on od.OrderID=ofm.OrderID " +
                "inner join %s%.PerformInfo pf on pf.PerformInfoID=od.PerformInfoID " +
                "where ofm.CreateDate between @StartDate and @EndDate and od.Status=6 and od.PromotionID is null " +
                "group by pf.ProjectID " +
                "union all " +
                "select pct.ProjectID,SUM(pct.RealPrice) as TodayMoney from ( " +
                "select pf.ProjectID,od.PCombineID,od.RealPrice from  " +
                "%s%.OrderForm ofm inner join %s%.OrderDetail od on od.OrderID=ofm.OrderID " +
                "inner join %s%.PerformInfo pf on pf.PerformInfoID=od.PerformInfoID " +
                "where ofm.CreateDate between @StartDate and @EndDate and od.Status=6 and od.PromotionID>0 " +
                "group by pf.ProjectID,od.PCombineID,od.RealPrice) pct group by pct.ProjectID " +
                "union all " +
                "select pjcn.TicketSystemInfoID,SUM(bod.RealPrice*bod.Quantity) as TodayMoney from %s%.B_OrderForm bo  " +
                "inner join %s%.B_OrderDetail bod on bo.OrderID=bod.OrderID " +
                "inner join %s%.PerformJoinPiaoCn pjcn on pjcn.PiaoCnInfoID=bod.ProjectID and pjcn.InfoType=1 " +
                "where bo.CreateDate between @StartDate and @EndDate and bod.OrderDetailStatus=2 " +
                "group by pjcn.TicketSystemInfoID) ts group by ts.ProjectID) tds on tds.ProjectID=pj.ProjectID" +
                ") pinfo where 1=1 ";
        System.out.println(str.replaceAll("%s%", "PiaoCenterDB.dbo"));
    }
}
