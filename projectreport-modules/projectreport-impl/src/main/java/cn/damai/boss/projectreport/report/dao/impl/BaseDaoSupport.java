package cn.damai.boss.projectreport.report.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class BaseDaoSupport {	
	
    @Resource(name = "reportSessionFactory")
	protected SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
}