package cn.damai.boss.projectreport.report.dao.impl;

import java.util.List;
import java.util.regex.Pattern;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.dao.MultiProjectStatDao;
import cn.damai.boss.projectreport.report.enums.BSGSiteEnum;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;

/**
 * 跨项目查询
 * 
 * @author：guwei 【顾炜】 time：2014-3-1 上午1:15:58
 * 
 */
@Repository
public class MultiProjectStatDaoImpl extends BaseDaoSupport implements MultiProjectStatDao {

	/**
	 * 读取配置文件中北京的数据库名称
	 */
	@Value( "${database.name.beijing}")
	private String databaseNameBj;
	/**
	 * 读取配置文件中上海的数据库名称
	 */
	@Value( "${database.name.shanghai}")
	private String databaseNameSh;
	/**
	 * 读取配置文件中广州的数据库名称
	 */
	@Value( "${database.name.guangzhou}")
	private String databaseNameGz;

	/**
	 * 根据项目id list 快项目查询
	 * 
	 * 创建人：guwei【顾炜】 创建时间：2014-2-27 上午11:46:25
	 */
	@Override
	@Transactional(value = "reportTransactionManager", readOnly = true)
	public List<ReportProjectVo> queryMulitProjectInfoList(String site,List<Long> projectIds) throws ApplicationException {		
		String sql="SELECT  " +
	              "  pjpcn.PiaoCnInfoID AS piaoCnId,pj.ProjectID AS projectId,  " +
	              "  pj.NAME AS projectName,pj.STATUS AS projectStatus,  " +
	              "  f.Name AS performField,c.NAME AS performCity,                " +
	              "  pf.startTime,pf.endTime,  " +
	              "  f.FieldID AS performFieldId,  " +
	              "  ISNULL(tds.TodayMoney,0) AS todayMoney,  " +
	              "  ISNULL(ts.TotalMoney,0) AS totalMoney,  " +
	              "CASE WHEN pj.ChooseSeatOn = 1 THEN(  " +
	              "  select ISNULL(SUM(tmp.remainingBoxOffice),0)from(  " +
	              "    SELECT Sum(pgp.Price) as remainingBoxOffice  " +
	              "    FROM dbo.PSeatInfo psi WITH(NOLOCK)     " +
	              "    INNER JOIN dbo.PerformGradePrice pgp WITH(NOLOCK) ON pgp.PGradePriceID = psi.PGradePriceID  " +
	              "    inner join dbo.ProjectSellGrade psg WITH(NOLOCK) on psg.PSellGradeID= pgp.PSellGradeID  " +
	              "    inner join dbo.PerformInfo pf WITH(NOLOCK) on pf.PerformInfoID=pgp.PerformInfoID  " +
	              "    WHERE (psi.SeatType is null or psi.SeatType=0)  " +
	              "    AND pf.ProjectID=pj.ProjectID  " +
	              "    GROUP BY psg.GradeName,pgp.Price,psi.PGradePriceID,pgp.PerformInfoID      " +
	              "    UNION ALL  " +
	              "    select SUM(tp.Price) AS remainingBoxOffice   " +
	              "    from dbo.TicketPromotion tp WITH(NOLOCK)  " +
	              "    inner join dbo.PromotionCombine pcb WITH(NOLOCK) on pcb.PromotionID=tp.PromotionID  " +
	              "    inner join dbo.PromotionCombineDetail pcbd WITH(NOLOCK) on pcbd.PCombineID=pcb.PCombineID  " +
	              "    inner join dbo.PerformInfo pf WITH(NOLOCK) on pf.PerformInfoID=tp.PerformInfoID  " +
	              "    where pcb.Status=1  " +
	              "    AND pf.ProjectID = pj.ProjectID  " +
	              "  group by pf.ProjectID)tmp  " +
	              ")ELSE(  " +
	              "  SELECT SUM(pgp.RemainCount*pgp.Price) AS remainingBoxOffice   " +
	              "  FROM dbo.PerformGradePrice pgp WITH(NOLOCK) " +
	              "  inner join dbo.ProjectSellGrade psg WITH(NOLOCK) on psg.PSellGradeID= pgp.PSellGradeID  " +
	              "  WHERE Status<>3  " +
	              "  AND psg.ProjectID= pj.ProjectID  " +
	              "  GROUP BY psg.ProjectID  " +
	              ")END AS remainingBoxOffice  " +
	              "FROM dbo.ProjectInfo pj WITH(NOLOCK)  " +
	              "Left JOIN dbo.PerformJoinPiaoCn pjpcn WITH(NOLOCK) ON pjpcn.TicketSystemInfoID = pj.ProjectID  " +
	              "AND pjpcn.InfoType = 1  " +
	              "inner join (  " +
	              "  SELECT ProjectID,MIN(PerformInfoID) as PerformInfoID,  " +
	              "    MIN(PerformTime) as startTime,MAX(PerformTime) as endTime  " +
	              "  FROM dbo.PerformInfo WITH(NOLOCK) WHERE Status<>4  " +
	              "  and ProjectID in(:projectIds)  " +
	              "  group by ProjectID  " +
	              ") pf on pf.ProjectID=pj.ProjectID  " +
	              " INNER JOIN dbo.PerformInfo pff WITH(NOLOCK) on pff.PerformInfoID=pf.PerformInfoID    " +
	              " LEFT JOIN dbo.FieldInfo f WITH(NOLOCK) on f.FieldID=pff.FieldID  " +
	              "INNER JOIN City c WITH(NOLOCK) on c.CityID = f.CityID  " +
	              "LEFT JOIN (  " +
	              "  select ProjectID,SUM(TotalMoney) AS totalMoney from (  " +
	              "    select pf.ProjectID,SUM(ao.RealAmount) TotalMoney  " +
	              "    from dbo.PerformInfo pf WITH(NOLOCK)" +
	              "    inner join dbo.AgentOrder ao WITH(NOLOCK) on pf.PerformInfoID=ao.PerformInfoID   " +
	              "    where ao.Status<>6   " +
	              "    AND pf.ProjectID in(:projectIds)  " +
	              "    group by pf.ProjectID    " +
	              "    union all    " +
	              "    select pf.ProjectID,SUM(ofm.RealAmount) TotalMoney" +
	              "    from dbo.OrderForm ofm WITH(NOLOCK) inner join OrderDetail od WITH(NOLOCK) on od.OrderID=ofm.OrderID  " +
	              "    inner join dbo.PerformInfo pf WITH(NOLOCK) on pf.PerformInfoID=od.PerformInfoID    " +
	              "    where od.Status=6  " +
	              "    AND pf.ProjectID in(:projectIds)  " +
	              "    group by pf.ProjectID  " +
	              "    union all    " +
	              "    select pjcn.TicketSystemInfoID,SUM(bod.RealPrice*bod.Quantity) as TotalMoney" +
	              "    from dbo.B_OrderForm bo WITH(NOLOCK) " +
	              "    inner join dbo.B_OrderDetail bod WITH(NOLOCK) on bo.OrderID=bod.OrderID    " +
	              "    inner join dbo.PerformJoinPiaoCn pjcn WITH(NOLOCK) on pjcn.PiaoCnInfoID=bod.ProjectID and pjcn.InfoType=1    " +
	              "    where bod.OrderDetailStatus=2  " +
	              "    AND pjcn.TicketSystemInfoID in(:projectIds) " +
	              "    group by pjcn.TicketSystemInfoID  " +
	              "  ) t group by ProjectID  " +
	              ") ts ON ts.ProjectID = pj.ProjectID  " +
	              "LEFT JOIN (            " +
	              "  select tmp.ProjectID,SUM(tmp.TodayMoney) as TodayMoney from (    " +
	              "    select pf.ProjectID,SUM(ao.RealAmount) TodayMoney  " +
	              "    from dbo.PerformInfo pf WITH(NOLOCK)" +
	              "    inner join dbo.AgentOrder ao WITH(NOLOCK) on pf.PerformInfoID=ao.PerformInfoID    " +
	              "    where ao.Status<>6      " +
	              "    AND DATEDIFF(DAY,ao.CreateDate,GETDATE())=0  " +
	              "    AND pf.ProjectID in(:projectIds)  " +
	              "    group by pf.ProjectID    " +
	              "    union all    " +
	              "    select t1.ProjectID,SUM(t1.SumMoney)  " +
	              "    from (  " +
	              "      select pf.ProjectID,SUM(od.RealPrice) as SumMoney  " +
	              "      from dbo.OrderForm ofm WITH(NOLOCK)  " +
	              "      inner join dbo.OrderDetail od WITH(NOLOCK) on ofm.OrderID=od.OrderID  " +
	              "      inner join dbo.PerformInfo pf WITH(NOLOCK) on pf.PerformInfoID=od.PerformInfoID  " +
	              "      where od.Status=6 and od.PCombineID is null  " +
	              "      AND DATEDIFF(DAY,ofm.CreateDate,GETDATE())=0  " +
	              "      AND pf.ProjectID in(:projectIds)  " +
	              "      group by pf.ProjectID  " +
	              "    union all  " +
	              "      select t.ProjectID,SUM(t.RealPrice)  " +
	              "      from (  " +
	              "        select pf.ProjectID,od.PCombineID,od.RealPrice  " +
	              "        from dbo.OrderForm ofm WITH(NOLOCK)   " +
	              "        inner join dbo.OrderDetail od WITH(NOLOCK) on ofm.OrderID=od.OrderID  " +
	              "        inner join dbo.PerformInfo pf WITH(NOLOCK) on pf.PerformInfoID=od.PerformInfoID  " +
	              "        where od.Status=6 and od.PCombineID>0  " +
	              "        AND DATEDIFF(DAY,ofm.CreateDate,GETDATE())=0  " +
	              "        AND pf.ProjectID in(:projectIds)  " +
	              "        group by pf.ProjectID,od.PCombineID,od.RealPrice  " +
	              "      )t group by ProjectID  " +
	              "    ) t1 group by t1.ProjectID      " +
	              "  UNION ALL    " +
	              "  select pjcn.TicketSystemInfoID,SUM(bod.RealPrice*bod.Quantity) as TodayMoney  " +
	              "  from dbo.B_OrderForm bo WITH(NOLOCK)    " +
	              "  inner join dbo.B_OrderDetail bod WITH(NOLOCK) on bo.OrderID=bod.OrderID    " +
	              "  inner join dbo.PerformJoinPiaoCn pjcn WITH(NOLOCK) on pjcn.PiaoCnInfoID=bod.ProjectID and pjcn.InfoType=1    " +
	              "  where bod.OrderDetailStatus=2    " +
	              "  and DATEDIFF(DAY,bo.CreateDate,GETDATE())=0  " +
	              "  and pjcn.TicketSystemInfoID in(:projectIds)  " +
	              "  group by pjcn.TicketSystemInfoID  " +
	              "  ) tmp group by tmp.ProjectID    " +
	              ")tds ON tds.ProjectID=pj.ProjectID  " +
	              "where pj.ProjectID in(:projectIds)";
		
		String dataSite="";
		if(site.equals(String.valueOf(BSGSiteEnum.BeiJing.getCode()))){
			dataSite = databaseNameBj;
		}else if(site.equals(String.valueOf(BSGSiteEnum.ShangHai.getCode()))){
			dataSite = databaseNameSh;
		}else if(site.equals(String.valueOf(BSGSiteEnum.GuangZhou.getCode()))){
			dataSite = databaseNameGz;
		}
		StringBuilder sbSql = new StringBuilder(Pattern.compile("dbo\\.").matcher(sql).replaceAll(dataSite+".dbo."));

		Session session = super.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(String.valueOf(sbSql));
		sqlQuery.setParameterList("projectIds", projectIds);		
		
		sqlQuery.addScalar( "piaoCnId", new LongType());
		sqlQuery.addScalar( "projectId", new LongType());
		sqlQuery.addScalar( "projectName", new StringType());
		sqlQuery.addScalar( "projectStatus", new IntegerType());
		sqlQuery.addScalar( "startTime", new StringType());
		sqlQuery.addScalar( "endTime", new StringType());		
		sqlQuery.addScalar( "performFieldId", new LongType());
		sqlQuery.addScalar( "performField", new StringType());
		sqlQuery.addScalar( "performCity", new StringType());
		sqlQuery.addScalar( "totalMoney", new StringType());
		sqlQuery.addScalar( "todayMoney", new StringType());
		sqlQuery.addScalar( "remainingBoxOffice", new StringType());
		sqlQuery.setResultTransformer(Transformers.aliasToBean(ReportProjectVo.class));

		return sqlQuery.list();
	}


}
