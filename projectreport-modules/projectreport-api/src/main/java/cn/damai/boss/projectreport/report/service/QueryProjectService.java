package cn.damai.boss.projectreport.report.service;

import java.util.List;
import java.util.Map;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.vo.ProjectClassVo;
import cn.damai.boss.projectreport.report.vo.RegionVo;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;
import cn.damai.boss.projectreport.report.vo.RmvenueVo;

/**
 * 注释：项目筛选service 作者：liutengfei 【刘腾飞】 时间：14-2-26 下午4:15
 */
public interface QueryProjectService {

	/**
	 * 查询项目类别
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	public List<ProjectClassVo> queryProjectClass() throws ApplicationException;

	/**
	 * 查询演出城市
	 * 
	 * @param performCity
	 *            演出城市
	 * @return
	 */
	public List<RegionVo> queryPerformCityByKeyWord(String performCity) throws ApplicationException;

	/**
	 * 根据项目名称查询项目
	 * 
	 * @param projectName
	 *            项目名称
	 * @return
	 */
	public List<ReportProjectVo> queryProjectNameByKeyWord(String projectName) throws ApplicationException;

	/**
	 * 查询演出场馆
	 * 
	 * @param performField
	 *            演出场馆
	 * @return
	 */
	public List<RmvenueVo> queryPerformFieldByKeyWord(String performField) throws ApplicationException;

	/**
	 * 根据过滤条件查询项目
	 * 
	 * @param vo
	 * @return
	 * @throws ApplicationException
	 */
	public PageResultData queryProjectByFilter(ReportProjectVo vo) throws ApplicationException;

	/**
	 * 查询正在销售和已经结束的项目
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	public Map<Integer, List<ReportProjectVo>> queryProjectSellAndOver() throws ApplicationException;

	/**
	 * 根据项目Id查询项目信息
	 * 
	 * @param projectId
	 *            项目Id
	 * @return
	 */
	public ReportProjectVo findProjectByProjectId(String source, long projectId) throws ApplicationException;

	/**
	 * 查询商家授权的项目列表
	 * 
	 * @param siteTradeIdMap
	 *            北上广对应的商家Id
	 * @return List<ReportProjectVo>
	 */
	public List<ReportProjectVo> findOwnerProjectList(Map<String, Long> siteTradeIdMap) throws ApplicationException;

	/**
	 * 判断主办方是否授权项目权限
	 * 
	 * @param source
	 * @param projectId
	 * @param traderId
	 * @return
	 * @throws ApplicationException
	 * @author：guwei 【顾炜】 2014-3-28 下午3:25:43
	 */
	public ReportProjectVo findIsOwnerProject(String source, Long projectId, Long traderId) throws ApplicationException;
}
