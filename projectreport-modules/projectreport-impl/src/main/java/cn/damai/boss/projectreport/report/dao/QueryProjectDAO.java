package cn.damai.boss.projectreport.report.dao;

import java.util.List;
import java.util.Map;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.report.vo.ProjectClassVo;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;

/**
 * 注释：项目筛选DAO扩展接口 作者：liutengfei 【刘腾飞】 时间：14-2-26 下午4:41
 */
public interface QueryProjectDAO {

	/**
	 * 查询项目类别
	 * 
	 * @return
	 */
	public List<ProjectClassVo> findProjectClass();

	/**
	 * 根据项目名称查询项目
	 * 
	 * @param projectName
	 *            项目名称
	 * @return
	 */
	public List<ReportProjectVo> findProjectNameByKeyWord(String projectName);

	/**
	 * 根据过滤条件查询项目
	 * 
	 * 
	 * @param vo
	 * @return
	 */
	public PageResultData queryProjectByFilter(ReportProjectVo vo);

	/**
	 * 根据项目Id查询项目信息
	 * 
	 * @param projectId
	 *            项目Id
	 * @return
	 */
	public ReportProjectVo queryProjectByProjectId(long projectId);

	/**
	 * 查询商家授权的项目列表
	 * 
	 * @param siteTradeIdMap
	 *            北上广对应的商家Id
	 * @return List<ReportProjectVo>
	 */
	public List<ReportProjectVo> queryOwnerProjectList(Map<String, Long> siteTradeIdMap);

	/**
	 * 验证主办方是否被授于项目权限
	 * 
	 * @param projectId
	 *            项目id
	 * @param traderId
	 *            主办id
	 * @return
	 * @author：guwei 【顾炜】 2014-3-28 下午3:19:23
	 */
	public ReportProjectVo queryIsOwnerProject(Long projectId, Long traderId);
}
