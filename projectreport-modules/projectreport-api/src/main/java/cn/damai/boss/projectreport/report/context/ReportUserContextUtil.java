package cn.damai.boss.projectreport.report.context;

import cn.damai.boss.projectreport.report.enums.DataSourceEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 注释：用户上下文信息工具类 作者：liutengfei 【刘腾飞】 时间：14-2-21 下午2:21
 */
public class ReportUserContextUtil {
	/**
	 * 得到操作员ID，即当前登录人ID
	 * 
	 * @return
	 */
	public static long getBusinessUserId() {
		ReportUserContext reportUserContext = ReportThreadLocalContext.getUserContext();
		if (reportUserContext != null) {
			return reportUserContext.getBusinessUserId();
		}
		return -1;
	}

	/**
	 * 得到主办方用户名称
	 * 
	 * @return
	 */
	public static String getBusinessUserName() {
		ReportUserContext reportUserContext = ReportThreadLocalContext.getUserContext();
		if (reportUserContext != null) {
			return reportUserContext.getBusinessUserName();
		}
		return null;
	}

	/**
	 * 得到北京、上海、广州对应的主办方ID
	 * 
	 * @return
	 */
	public static Map<String, Long> getBSGBusinessId() {
		Map<String, Long> businessIdMap = new HashMap<String, Long>();
		ReportUserContext reportUserContext = ReportThreadLocalContext.getUserContext();
		if (reportUserContext != null) {
			Long bjBusinessId = reportUserContext.getBjBusinessId();
			businessIdMap.put(DataSourceEnum.BJMaitix.getCodeStr(), bjBusinessId);
			Long shBusinessId = reportUserContext.getShBusinessId();
			businessIdMap.put(DataSourceEnum.SHMaitix.getCodeStr(), shBusinessId);
			Long gzBusinessId = reportUserContext.getGzBusinessId();
			businessIdMap.put(DataSourceEnum.GZMaitix.getCodeStr(), gzBusinessId);
		}
		return businessIdMap;
	}

	/**
	 * 得到sessionID
	 * 
	 * @return
	 */
	public static String getSessionID() {
		String sessionID = null;
		ReportUserContext reportUserContext = ReportThreadLocalContext.getUserContext();
		if (reportUserContext != null)
			sessionID = reportUserContext.getSessionID();
		return sessionID;
	}

	/**
	 * 得到用户类型，即当前登录用户角色【主办方、运营、财务、安保】
	 * 
	 * @return
	 */
	public static int getUserType() {
		int userType = 0;
		ReportUserContext reportUserContext = ReportThreadLocalContext.getUserContext();
		if (reportUserContext != null)
			userType = reportUserContext.getUserType();
		return userType;
	}

	/**
	 * 获取maitix组织机构代理商id
	 * 
	 * @return
	 */
	public static long getMaitixBusinessUserId() {
		ReportUserContext reportUserContext = ReportThreadLocalContext.getUserContext();
		if (reportUserContext != null) {
			return reportUserContext.getMaitixBusinessUserId();
		}
		return -1;
	}
}
