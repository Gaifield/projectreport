package cn.damai.boss.projectreport.report.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-5
 * Time: 下午12:07
 * To change this template use File | Settings | File Templates.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = DynamicDataSourceHolder.getDataSourceName();
        return dataSourceName;
    }
}
