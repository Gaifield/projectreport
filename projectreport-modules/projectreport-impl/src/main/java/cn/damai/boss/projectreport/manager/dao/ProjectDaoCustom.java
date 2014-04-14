package cn.damai.boss.projectreport.manager.dao;

import java.util.Date;


/**
 * 项目用户自定义DAO
 * @author Administrator
 *
 */
public interface ProjectDaoCustom {
	
	/**
	 * 查找导入记录项目的最大终止日期
	 * @return max date/null;
	 */
	public Date queryLatestProjectDeadline();
}