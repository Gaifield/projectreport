package cn.damai.boss.projectreport.report.dao.impl;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.report.context.ReportUserContextUtil;
import cn.damai.boss.projectreport.report.dao.QueryProjectDAO;
import cn.damai.boss.projectreport.report.enums.BSGSiteEnum;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.enums.ProjectStatusEnum;
import cn.damai.boss.projectreport.report.vo.ProjectClassVo;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 注释：项目筛选DAO扩展实现类 作者：liutengfei 【刘腾飞】 时间：14-2-26 下午4:43
 */
@Repository
public class QueryProjectDAOImpl extends BaseDaoSupport implements QueryProjectDAO {

    /**
     * 读取配置文件中北京的数据库名称
     */
    @Value("${database.name.beijing}")
    private String databaseNameBj;
    /**
     * 读取配置文件中上海的数据库名称
     */
    @Value("${database.name.shanghai}")
    private String databaseNameSh;
    /**
     * 读取配置文件中广州的数据库名称
     */
    @Value("${database.name.guangzhou}")
    private String databaseNameGz;

    /**
     * 查询项目类别
     *
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public List<ProjectClassVo> findProjectClass() {
        String sql = "select * from PiaoCenterDB.dbo.ProjectClass";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.addScalar("projectClassID", Hibernate.LONG);
        sqlQuery.addScalar("parentClassID", Hibernate.LONG);
        sqlQuery.addScalar("className", Hibernate.STRING);
        sqlQuery.addScalar("note", Hibernate.STRING);
        sqlQuery.addScalar("categoryID", Hibernate.LONG);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(ProjectClassVo.class));
        List<ProjectClassVo> voList = sqlQuery.list();
        return voList;
    }

    /**
     * 根据项目名称查询项目
     *
     * @param projectName 项目名称
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public List<ReportProjectVo> findProjectNameByKeyWord(String projectName) {
        StringBuilder builder = new StringBuilder();
        builder.append("select Name as projectName from PiaoCenterDB.dbo.ProjectInfo where Name like :Name");
        builder.append(" union all");
        builder.append(" select Name as projectName from SH_PiaoCenterDB.dbo.ProjectInfo where Name like :Name");
        builder.append(" union all");
        builder.append(" select Name as projectName from GZ_PiaoCenterDB.dbo.ProjectInfo where Name like :Name");
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(builder.toString());
        sqlQuery.setParameter("Name", "%" + projectName + "%");
        sqlQuery.addScalar("projectName", Hibernate.STRING);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(ReportProjectVo.class));
        List<ReportProjectVo> voList = sqlQuery.list();
        return voList;
    }

    /**
     * 根据项目Id查询项目信息
     *
     * @param projectId 项目Id
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public ReportProjectVo queryProjectByProjectId(long projectId) {
        StringBuilder builder = new StringBuilder();
        builder.append("select '0' as dataResource, ProjectID as projectId,Name as projectName,ChooseSeatOn as chooseSeatOn");
        builder.append(" from ProjectInfo where ProjectID=:projectId");
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(builder.toString());
        sqlQuery.setParameter("projectId", projectId);

        sqlQuery.addScalar("dataResource", Hibernate.STRING);
        sqlQuery.addScalar("projectId", Hibernate.LONG);
        sqlQuery.addScalar("projectName", Hibernate.STRING);
        sqlQuery.addScalar("chooseSeatOn", Hibernate.INTEGER);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(ReportProjectVo.class));
        List<ReportProjectVo> voList = sqlQuery.list();
        if (voList != null && voList.size() > 0) {
            return voList.get(0);
        }
        return null;
    }

    private String replaceDataBaseName(String strSQL) {
        strSQL = Pattern.compile("BJ\\.dbo\\.").matcher(strSQL).replaceAll(databaseNameBj + ".dbo.");
        strSQL = Pattern.compile("SH\\.dbo\\.").matcher(strSQL).replaceAll(databaseNameSh + ".dbo.");
        strSQL = Pattern.compile("GZ\\.dbo\\.").matcher(strSQL).replaceAll(databaseNameGz + ".dbo.");
        return strSQL;
    }

    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public List<ReportProjectVo> queryOwnerProjectList(Map<String, Long> siteTradeIdMap) {
        StringBuilder builder = new StringBuilder();
        List<Long> tradeIds = new ArrayList<Long>();
        Iterator<String> iteratorKey = siteTradeIdMap.keySet().iterator();
        while (iteratorKey.hasNext()) {
            String key = iteratorKey.next();
            if (builder.length() > 0) {
                builder.append(" union all ");
            }
            if (key.equals(String.valueOf(BSGSiteEnum.BeiJing.getCode()))) {
                builder.append("select  distinct top 10 '1' as dataResource, p.ProjectID as projectId,p.Name as projectName,p.ChooseSeatOn as chooseSeatOn from BJ.dbo.ProjectInfo  p left join BJ.dbo.ProjectUnionTrader put on put.ProjectID=p.ProjectID left join BJ.dbo.PerformInfo pf WITH(NOLOCK) on pf.ProjectID =p.ProjectID where (p.TraderID=? or put.TraderID=?) and pf.PerformTime between DATEADD(MONTH,-1,GETDATE()) and DATEADD(MONTH,1,GETDATE())");
                tradeIds.add(siteTradeIdMap.get(key));
                tradeIds.add(siteTradeIdMap.get(key));
            } else if (key.equals(String.valueOf(BSGSiteEnum.ShangHai.getCode()))) {
                builder.append(" select distinct  top 10 '2' as dataResource, p.ProjectID as projectId,p.Name as projectName,p.ChooseSeatOn as chooseSeatOn from SH.dbo.ProjectInfo p left join SH.dbo.ProjectUnionTrader put on put.ProjectID=p.ProjectID left join SH.dbo.PerformInfo pf WITH(NOLOCK) on pf.ProjectID =p.ProjectID where (p.TraderID=? or put.TraderID=?) and pf.PerformTime between DATEADD(MONTH,-1,GETDATE()) and DATEADD(MONTH,1,GETDATE())");
                tradeIds.add(siteTradeIdMap.get(key));
                tradeIds.add(siteTradeIdMap.get(key));
            } else if (key.equals(String.valueOf(BSGSiteEnum.GuangZhou.getCode()))) {
                builder.append(" select distinct  top 10 '3' as dataResource, p.ProjectID as projectId,p.Name as projectName,p.ChooseSeatOn as chooseSeatOn from GZ.dbo.ProjectInfo p left join GZ.dbo.ProjectUnionTrader put on put.ProjectID=p.ProjectID left join GZ.dbo.PerformInfo pf WITH(NOLOCK) on pf.ProjectID =p.ProjectID where (p.TraderID=? or put.TraderID=?) and pf.PerformTime between DATEADD(MONTH,-1,GETDATE()) and DATEADD(MONTH,1,GETDATE())");
                tradeIds.add(siteTradeIdMap.get(key));
                tradeIds.add(siteTradeIdMap.get(key));
            }
        }

        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(replaceDataBaseName(builder.toString()));
        for (int i = 0; i < tradeIds.size(); i++) {
            sqlQuery.setParameter(i, tradeIds.get(i));
        }

        sqlQuery.addScalar("dataResource", Hibernate.STRING);
        sqlQuery.addScalar("projectId", Hibernate.LONG);
        sqlQuery.addScalar("projectName", Hibernate.STRING);
        sqlQuery.addScalar("chooseSeatOn", Hibernate.INTEGER);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(ReportProjectVo.class));
        return sqlQuery.list();
    }

    /**
     * 验证主办方是否被授于项目权限
     *
     * @param projectId 项目id
     * @param traderId  主办id
     * @return
     * @author：guwei 【顾炜】 2014-3-28 下午3:17:48
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public ReportProjectVo queryIsOwnerProject(Long projectId, Long traderId) {

        String sql = "select  distinct top 10 '1' as dataResource, p.ProjectID as projectId,p.Name as projectName,p.ChooseSeatOn as chooseSeatOn "
                + " from ProjectInfo  p left join ProjectUnionTrader put on put.ProjectID=p.ProjectID "
                + " left join PerformInfo pf WITH(NOLOCK) on pf.ProjectID =p.ProjectID where (p.TraderID=:traderId or put.TraderID=:traderId) and p.ProjectID=:projectId";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);

        sqlQuery.setParameter("traderId", traderId);
        sqlQuery.setParameter("projectId", projectId);

        sqlQuery.addScalar("dataResource", Hibernate.STRING);
        sqlQuery.addScalar("projectId", Hibernate.LONG);
        sqlQuery.addScalar("projectName", Hibernate.STRING);
        sqlQuery.addScalar("chooseSeatOn", Hibernate.INTEGER);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(ReportProjectVo.class));
        List list = sqlQuery.list();
        if (list != null && list.size() > 0) {
            return (ReportProjectVo) list.get(0);
        }
        return null;
    }

    /**
     * 根据过滤条件查询项目
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(value = "reportTransactionManager", readOnly = true)
    public PageResultData queryProjectByFilter(ReportProjectVo vo) {
        String querySQL = getBSGSQL(vo, "*", "order by endTime desc");
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(querySQL);
        setFilterValue(sqlQuery, vo);
        setAliasType(sqlQuery);
        PageResultData pageData = getPageData(vo, sqlQuery);
        return pageData;
    }

    /**
     * 得到北京、上海、广州SQL
     *
     * @param vo
     * @return
     */
    private String getBSGSQL(ReportProjectVo vo, String queryField, String orderField) {
        // 北京
        String bjQuerySQL = getQuerySQL(queryField, DataSourceEnum.BJMaitix.getCodeStr());
        String bjQueryAndFilterSQL = getFilterSQL(vo, bjQuerySQL);
        String bjSQL = bjQueryAndFilterSQL.replaceAll("%s%", "PiaoCenterDB.dbo");
        // 上海
        String shQuerySQL = getQuerySQL(queryField, DataSourceEnum.SHMaitix.getCodeStr());
        String shQueryAndFilterSQL = getFilterSQL(vo, shQuerySQL);
        String shSQL = shQueryAndFilterSQL.replaceAll("%s%", "SH_PiaoCenterDB.dbo");
        // 广州
        String gzQuerySQL = getQuerySQL(queryField, DataSourceEnum.GZMaitix.getCodeStr());
        String gzQueryAndFilterSQL = getFilterSQL(vo, gzQuerySQL);
        String gzSQL = gzQueryAndFilterSQL.replaceAll("%s%", "GZ_PiaoCenterDB.dbo");

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(bjSQL).append(" union all ");
        sqlBuilder.append(shSQL).append(" union all ").append(gzSQL);
        if (orderField != null && orderField.length() != 0) {
            sqlBuilder.append(" ").append(orderField);
        }
        return sqlBuilder.toString();
    }

    /**
     * 得到项目查询SQL
     *
     * @return
     */
    private String getQuerySQL(String queryField, String dataSource) {
        if (DataSourceEnum.BJMaitix.getCodeStr().equals(dataSource)) {
            queryField += ",'1' as dataResource";
        } else if (DataSourceEnum.SHMaitix.getCodeStr().equals(dataSource)) {
            queryField += ",'2' as dataResource";
        } else if (DataSourceEnum.GZMaitix.getCodeStr().equals(dataSource)) {
            queryField += ",'3' as dataResource";
        }
        // 主办方ID
        Map<String, Long> bsgBusinessId = ReportUserContextUtil.getBSGBusinessId();
        Long businessId = bsgBusinessId.get(dataSource);
        String sql = "SELECT " + queryField + " FROM ("
                + "SELECT pjpcn.PiaoCnInfoID AS piaoCnId,pj.ProjectID AS projectId, "
                + "pc.CategoryID as categoryID,pc.ClassName as className, "
                + "pj.NAME AS projectName,pj.STATUS AS projectStatus, f.FieldName AS performField, "
                + "f.CityName AS performCity,f.FieldID AS performFieldId,  "
                + "( SELECT MIN (PerformTime) FROM %s%.PerformInfo mpf WITH (NOLOCK)  "
                + "WHERE mpf.ProjectID = pj.ProjectID "
                + ") AS startTime, "
                + "( SELECT MAX (PerformTime) FROM %s%.PerformInfo mpf WITH (NOLOCK)  "
                + "WHERE mpf.ProjectID = pj.ProjectID "
                + ") AS endTime "
                + "FROM %s%.ProjectInfo pj WITH (NOLOCK)  "
                + "INNER JOIN %s%.PerformJoinPiaoCn pjpcn  WITH (NOLOCK)  "
                + "ON pjpcn.TicketSystemInfoID = pj.ProjectID AND pjpcn.InfoType = 1 "
                + "INNER JOIN %s%.ProjectClass pc WITH (NOLOCK) on pj.ProjectClassID=pc.ProjectClassID "
                + "INNER JOIN  "
                + "( "
                + "SELECT pf.ProjectID, f.FieldID, f.NAME FieldName, c.NAME AS CityName " + "FROM %s%.FieldInfo f WITH (NOLOCK)  "
                + "INNER JOIN %s%.City c ON f.CityID = c.CityID "
                + "INNER JOIN %s%.PerformInfo pf ON pf.FieldID = f.FieldID "
                + "WHERE pf.PerformInfoID IN  "
                + "( "
                + "SELECT MIN (PerformInfoID) FROM %s%.PerformInfo WITH (NOLOCK)  "
                + "WHERE STATUS IN (2, 3) GROUP BY ProjectID "
                + ") "
                + ") f ON f.ProjectID = pj.ProjectID "
                + "where pj.Status in(4,5) ";
        if (businessId != null && businessId != 0) {
            sql += " and (pj.TraderID=" + businessId
                    + " or exists(select 1 from %s%.ProjectUnionTrader WITH (NOLOCK) "
                    + "where projectId=pj.ProjectID and TraderID=" + businessId
                    + "))";
        }
        sql += ") pinfo where 1=1";
        return sql;
    }

    /**
     * 设置过滤条件
     *
     * @param vo
     * @param sql
     * @return
     */
    private String getFilterSQL(ReportProjectVo vo, String sql) {
        Long projectClassId = vo.getCategoryID();
        if (projectClassId != null && projectClassId != 0) {
            sql += " and pinfo.categoryID=:categoryID";
        }
        Long performJoinPiaoCnId = vo.getPiaoCnId();
        if (performJoinPiaoCnId != null && performJoinPiaoCnId != 0) {
            sql += " and pinfo.piaoCnId=:piaoCnId";
        }
        int projectStatus = vo.getProjectStatus();
        if (projectStatus != 0) {
            sql += " and pinfo.projectStatus=:projectStatus";
        }
        String projectName = vo.getProjectName();
        if (projectName != null && projectName.trim().length() != 0) {
            sql += " and pinfo.projectName like :projectName";
        }
        String startTime = vo.getStartTime();
        if (startTime != null && startTime.trim().length() != 0) {
            sql += " and pinfo.startTime >= :startTime";
        }
        String endTime = vo.getEndTime();
        if (endTime != null && endTime.trim().length() != 0) {
            sql += " and pinfo.endTime <= :endTime";
        }
        String performField = vo.getPerformField();
        if (performField != null && performField.trim().length() != 0) {
            sql += " and pinfo.performField like :performField";
        }
        String performCity = vo.getPerformCity();
        if (performCity != null && performCity.trim().length() != 0) {
            sql += " and pinfo.performCity like :performCity";
        }
        return sql;
    }

    /**
     * 设置字段类型，使查询结果返回vo
     *
     * @param sqlQuery
     * @param vo
     */
    private void setFilterValue(SQLQuery sqlQuery, ReportProjectVo vo) {
        Long projectClassId = vo.getCategoryID();
        if (projectClassId != null && projectClassId != 0) {
            sqlQuery.setParameter("categoryID", projectClassId);
        }
        Long performJoinPiaoCnId = vo.getPiaoCnId();
        if (performJoinPiaoCnId != null && performJoinPiaoCnId != 0) {
            sqlQuery.setParameter("piaoCnId", performJoinPiaoCnId);
        }
        int projectStatus = vo.getProjectStatus();
        if (projectStatus != 0) {
            sqlQuery.setParameter("projectStatus", projectStatus);
        }
        String projectName = vo.getProjectName();
        if (projectName != null && projectName.trim().length() != 0) {
            sqlQuery.setParameter("projectName", "%" + projectName + "%");
        }
        String startTime = vo.getStartTime();
        if (startTime != null && startTime.trim().length() != 0) {
            sqlQuery.setParameter("startTime", startTime);
        }
        String endTime = vo.getEndTime();
        if (endTime != null && endTime.trim().length() != 0) {
            sqlQuery.setParameter("endTime", endTime);
        }
        String performField = vo.getPerformField();
        if (performField != null && performField.trim().length() != 0) {
            sqlQuery.setParameter("performField", "%" + performField + "%");
        }
        String performCity = vo.getPerformCity();
        if (performCity != null && performCity.trim().length() != 0) {
            sqlQuery.setParameter("performCity", "%" + performCity + "%");
        }
    }

    /**
     * 设置别名类型
     *
     * @param sqlQuery
     */
    private void setAliasType(SQLQuery sqlQuery) {
        sqlQuery.addScalar("dataResource", Hibernate.STRING);
        sqlQuery.addScalar("piaoCnId", Hibernate.LONG);
        sqlQuery.addScalar("categoryID", Hibernate.LONG);
        sqlQuery.addScalar("className", Hibernate.STRING);
        sqlQuery.addScalar("projectId", Hibernate.LONG);
        sqlQuery.addScalar("projectName", Hibernate.STRING);
        sqlQuery.addScalar("performCity", Hibernate.STRING);
        sqlQuery.addScalar("performField", Hibernate.STRING);
        sqlQuery.addScalar("projectStatus", Hibernate.INTEGER);
        sqlQuery.addScalar("startTime", Hibernate.STRING);
        sqlQuery.addScalar("endTime", Hibernate.STRING);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(ReportProjectVo.class));
    }

    /**
     * 得到分页对象
     *
     * @param vo       项目VO
     * @param sqlQuery
     * @return
     */
    private PageResultData getPageData(ReportProjectVo vo, SQLQuery sqlQuery) {
        int page = vo.getPage();
        int pageSize = vo.getPageSize();
        sqlQuery.setFirstResult((page - 1) * pageSize);
        sqlQuery.setMaxResults(pageSize);
        List<ReportProjectVo> voList = sqlQuery.list();

        // 设置当日销售金额和总销售金额
        List<Long> projectIdList = getProjectId(voList);
        List<ReportProjectVo> totalVoList = findTotalMoney(projectIdList);
        List<ReportProjectVo> todayVoList = findTodayMoney(projectIdList);
        setMoney(voList, totalVoList, todayVoList);

        setProjectVo(voList);

        PageResultData pageData = new PageResultData();
        pageData.setRows(voList);

        // 如果是首页查询，不需要查询总计
        if (!vo.isIndex()) {
            pageData.setTotal(getRowsTotal(vo));
        }
        return pageData;
    }

    /**
     * 设置今日销售金额和总销售金额
     *
     * @param voList      项目VO
     * @param totalVoList 总销售金额
     * @param todayVoList 金额销售金额
     */
    private void setMoney(List<ReportProjectVo> voList, List<ReportProjectVo> totalVoList, List<ReportProjectVo> todayVoList) {
        if (voList == null || voList.size() == 0) {
            return;
        }
        for (ReportProjectVo projectVo : voList) {
            Long projectId = projectVo.getProjectId();
            for (ReportProjectVo totalVo : totalVoList) {
                Long id = totalVo.getProjectId();
                if (projectId.toString().equals(id.toString())) {
                    projectVo.setTotalMoney(totalVo.getTotalMoney());
                }
            }
            for (ReportProjectVo todayVo : todayVoList) {
                Long id = todayVo.getProjectId();
                if (projectId.toString().equals(id.toString())) {
                    projectVo.setTodayMoney(todayVo.getTodayMoney());
                }
            }
        }
    }

    /**
     * 查询总销售金额
     *
     * @param projectIdList
     * @return
     */
    private List<ReportProjectVo> findTotalMoney(List<Long> projectIdList) {
        List<ReportProjectVo> totalMoneyList = new ArrayList<ReportProjectVo>();
        if (projectIdList == null || projectIdList.size() == 0) {
            return totalMoneyList;
        }
        String sql = "SELECT ProjectID as projectId,SUM(TotalMoney) AS totalMoney " + "FROM " + "( "
                + "SELECT pf.ProjectID, SUM(ao.RealAmount) TotalMoney " + "FROM %s%.PerformInfo pf WITH (NOLOCK)  "
                + "INNER JOIN %s%.AgentOrder ao WITH (NOLOCK) ON pf.PerformInfoID = ao.PerformInfoID "
                + "WHERE ao. STATUS <> 6  AND pf.ProjectID IN(:projectIdList) " + "GROUP BY pf.ProjectID " + "UNION ALL "
                + "SELECT pf.ProjectID,SUM(ofm.RealAmount) TotalMoney " + "FROM %s%.OrderForm ofm WITH (NOLOCK)  "
                + "INNER JOIN %s%.OrderDetail od WITH (NOLOCK) ON od.OrderID = ofm.OrderID "
                + "INNER JOIN %s%.PerformInfo pf WITH (NOLOCK) ON pf.PerformInfoID = od.PerformInfoID "
                + "WHERE od. STATUS = 6  AND pf.ProjectID IN(:projectIdList) " + "GROUP BY pf.ProjectID " + "UNION ALL "
                + "SELECT pjcn.TicketSystemInfoID,SUM(bod.RealPrice * bod.Quantity) AS TotalMoney " + "FROM %s%.B_OrderForm bo WITH (NOLOCK)  "
                + "INNER JOIN %s%.B_OrderDetail bod WITH (NOLOCK) ON bo.OrderID = bod.OrderID "
                + "INNER JOIN %s%.PerformJoinPiaoCn pjcn WITH (NOLOCK) ON pjcn.PiaoCnInfoID = bod.ProjectID " + "AND pjcn.InfoType = 1 "
                + "WHERE bod.OrderDetailStatus = 2 " + " AND pjcn.TicketSystemInfoID IN(:projectIdList) " + "GROUP BY pjcn.TicketSystemInfoID " + ") t "
                + "GROUP BY ProjectID";
        // 北京
        String bjSQL = sql.replaceAll("%s%", "PiaoCenterDB.dbo");
        // 上海
        String shSQL = sql.replaceAll("%s%", "SH_PiaoCenterDB.dbo");
        // 广州
        String gzSQL = sql.replaceAll("%s%", "GZ_PiaoCenterDB.dbo");
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(bjSQL).append(" union all ");
        sqlBuilder.append(shSQL).append(" union all ").append(gzSQL);

        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlBuilder.toString());
        sqlQuery.setParameterList("projectIdList", projectIdList);
        sqlQuery.addScalar("projectId", Hibernate.LONG);
        sqlQuery.addScalar("totalMoney", Hibernate.STRING);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(ReportProjectVo.class));
        totalMoneyList = sqlQuery.list();
        return totalMoneyList;
    }

    /**
     * 查询总销售金额
     *
     * @param projectIdList
     * @return
     */
    private List<ReportProjectVo> findTodayMoney(List<Long> projectIdList) {
        List<ReportProjectVo> todayMoneyList = new ArrayList<ReportProjectVo>();
        if (projectIdList == null || projectIdList.size() == 0) {
            return todayMoneyList;
        }
        String sql = "SELECT ts.ProjectID,SUM(ts.TodayMoney) AS TodayMoney "
                + "FROM( "
                + "SELECT pf.ProjectID,SUM(ao.RealAmount) TodayMoney "
                + "FROM %s%.PerformINfo pf "
                + "INNER JOIN %s%.AgentOrder ao ON pf.PerformINfoID = ao.PerformINfoID "
                + "WHERE ao.CreateDate BETWEEN :today AND :tomorrow AND ao. STATUS <> 6 "
                + "AND pf.ProjectID IN(:projectIdList) "
                + "GROUP BY pf.ProjectID "
                + "UNION ALL "
                + "SELECT pf.ProjectID, SUM(ofm.RealAmount) TodayMoney "
                + "FROM %s%.OrderForm ofm "
                + "INNER JOIN %s%.OrderDetail od ON od.OrderID = ofm.OrderID "
                + "INNER JOIN %s%.PerformINfo pf ON pf.PerformINfoID = od.PerformINfoID "
                + "WHERE od. STATUS = 6 AND od.PCombINeID IS NULL  "
                + "AND ofm.CreateDate BETWEEN :today AND :tomorrow  "
                + "AND pf.ProjectID IN(:projectIdList) "
                + "GROUP BY pf.ProjectID "
                + "UNION ALL "
                + "SELECT pct.ProjectID, SUM(pct.RealPrice) AS TodayMoney "
                + "FROM( "
                + "SELECT pf.ProjectID, od.PCombINeID,od.RealPrice "
                + "FROM %s%.OrderForm ofm "
                + "INNER JOIN %s%.OrderDetail od ON od.OrderID = ofm.OrderID "
                + "INNER JOIN %s%.PerformINfo pf ON pf.PerformINfoID = od.PerformINfoID "
                + "WHERE od. STATUS = 6 AND od.PromotionID > 0 "
                + "AND ofm.CreateDate BETWEEN :today AND :tomorrow "
                + "AND pf.ProjectID IN(:projectIdList) "
                + "GROUP BY pf.ProjectID,od.PCombINeID,od.RealPrice "
                + ") pct GROUP BY pct.ProjectID "
                + "UNION ALL "
                + "SELECT pjcn.TicketSystemINfoID,SUM(bod.RealPrice * bod.Quantity) AS TodayMoney "
                + "FROM %s%.B_OrderForm bo "
                + "INNER JOIN %s%.B_OrderDetail bod ON bo.OrderID = bod.OrderID "
                + "INNER JOIN %s%.PerformJoINPiaoCn pjcn ON pjcn.PiaoCnINfoID = bod.ProjectID "
                + "AND pjcn.INfoType = 1 "
                + "WHERE bod.OrderDetailStatus = 2 "
                + "AND bo.CreateDate BETWEEN :today AND :tomorrow  "
                + "AND  pjcn.TicketSystemInfoID IN(:projectIdList) "
                + "GROUP BY pjcn.TicketSystemINfoID "
                + ") ts "
                + "GROUP BY ts.ProjectID";
        // 北京
        String bjSQL = sql.replaceAll("%s%", "PiaoCenterDB.dbo");
        // 上海
        String shSQL = sql.replaceAll("%s%", "SH_PiaoCenterDB.dbo");
        // 广州
        String gzSQL = sql.replaceAll("%s%", "GZ_PiaoCenterDB.dbo");
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(bjSQL).append(" union all ");
        sqlBuilder.append(shSQL).append(" union all ").append(gzSQL);

        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlBuilder.toString());
        sqlQuery.setParameterList("projectIdList", projectIdList);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(date);
        String tomorrow = format.format(DateUtils.addDays(date, 1));
        sqlQuery.setParameter("today", today);
        sqlQuery.setParameter("tomorrow", tomorrow);
        sqlQuery.addScalar("projectId", Hibernate.LONG);
        sqlQuery.addScalar("todayMoney", Hibernate.STRING);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(ReportProjectVo.class));
        todayMoneyList = sqlQuery.list();
        return todayMoneyList;
    }

    /**
     * 获取项目ID
     *
     * @param voList
     * @return
     */
    private List<Long> getProjectId(List<ReportProjectVo> voList) {
        List<Long> projectIdList = new ArrayList<Long>();
        if (voList != null && voList.size() != 0) {
            for (ReportProjectVo vo : voList) {
                projectIdList.add(vo.getProjectId());
            }
        }
        return projectIdList;
    }

    /**
     * 得到全部数据行数
     *
     * @param vo
     * @return
     */
    private int getRowsTotal(ReportProjectVo vo) {
        StringBuilder totalSQL = new StringBuilder();
        String bsgSQL = getBSGSQL(vo, "*", null);
        totalSQL.append("select count(*) from (").append(bsgSQL).append(") ss");
        SQLQuery countQuery = getCurrentSession().createSQLQuery(totalSQL.toString());
        setFilterValue(countQuery, vo);
        return (Integer) countQuery.uniqueResult();
    }

    /**
     * 设置项目Vo
     *
     * @param voList
     */
    private void setProjectVo(List<ReportProjectVo> voList) {
        if (voList == null || voList.size() == 0) {
            return;
        }
        for (ReportProjectVo vo : voList) {
            // 项目状态
            String projectStatus = String.valueOf(vo.getProjectStatus());
            vo.setProjectStatusName(ProjectStatusEnum.getName(projectStatus));
            // 当日销售金额
            String todayMoney = vo.getTodayMoney();
            if (todayMoney == null || todayMoney.trim().length() == 0) {
                vo.setTodayMoney("0");
            }
            // 总销售金额
            String totalMoney = vo.getTotalMoney();
            if (totalMoney == null || totalMoney.trim().length() == 0) {
                vo.setTotalMoney("0");
            }

            // 演出开始时间
            String startTime = vo.getStartTime();
            int startIndex = startTime.indexOf(".");
            if (startIndex > 0) {
                startTime = startTime.substring(0, startIndex);
                vo.setStartTime(startTime);
            }
            // 演出结束时间
            String endTime = vo.getEndTime();
            int endIndex = endTime.indexOf(".");
            if (endIndex > 0) {
                endTime = endTime.substring(0, endIndex);
                vo.setEndTime(endTime);
            }
        }
    }
}