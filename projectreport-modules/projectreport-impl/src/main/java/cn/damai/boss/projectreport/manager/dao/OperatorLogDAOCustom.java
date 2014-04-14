package cn.damai.boss.projectreport.manager.dao;

/**
 * 注释：日志扩展DAO
 * 作者：wenjunrong 【温俊荣】
 * 时间：14-3-6 下午8:13
 */
public interface OperatorLogDAOCustom {
	
	/**
	 * 插入操作日志
	 * @param operatorId 操作员Id
	 * @param content 正文
	 * @param type 类型
	 */
	public void insertOperatorLog(Long operatorId,String content,int type);
}
