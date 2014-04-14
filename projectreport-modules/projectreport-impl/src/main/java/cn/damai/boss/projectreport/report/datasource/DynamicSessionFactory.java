package cn.damai.boss.projectreport.report.datasource;

import org.hibernate.SessionFactory;

public interface DynamicSessionFactory extends SessionFactory {
    
    public SessionFactory getHibernateSessionFactory();
}
