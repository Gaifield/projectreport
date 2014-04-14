package cn.damai.boss.projectreport.report.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.commons.utils.ModelUtils;
import cn.damai.boss.projectreport.report.dao.QueryProjectDAO;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.enums.ProjectStatusEnum;
import cn.damai.boss.projectreport.report.service.QueryProjectService;
import cn.damai.boss.projectreport.report.vo.FieldDataVo;
import cn.damai.boss.projectreport.report.vo.ProjectClassVo;
import cn.damai.boss.projectreport.report.vo.RegionVo;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;
import cn.damai.boss.projectreport.report.vo.RmvenueVo;
import cn.damai.commons.referencedata.regiondata.api.RegionDataService;
import cn.damai.component.region.enums.RegionTypeEnum;
import cn.damai.component.region.vo.Region;

import com.alibaba.dubbo.common.json.JSON;

/**
 * 注释：项目筛选service实现类 作者：liutengfei 【刘腾飞】 时间：14-2-26 下午4:38
 */
@Service
public class QueryProjectServiceImpl implements QueryProjectService {

	@Resource
	private QueryProjectDAO queryProjectDAO;

	@Resource
	private RegionDataService regionDataService;

	@Value("${venueCenterUrl}")
	private String venueCenterUrl;

	/**
	 * 查询项目类别
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	@Override
	public List<ProjectClassVo> queryProjectClass() throws ApplicationException {
		try {
			List<ProjectClassVo> projectClassVoList = queryProjectDAO.findProjectClass();
			return projectClassVoList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
		}
	}

	/**
	 * 查询演出城市
	 * 
	 * @param performCity
	 *            演出城市
	 * @return
	 * @throws ApplicationException
	 */
	@Override
	public List<RegionVo> queryPerformCityByKeyWord(String performCity) throws ApplicationException {
		try {
			List<Region> regionList = regionDataService.findRegionLikeNameAndRegionType(performCity, RegionTypeEnum.CITY);
			List<RegionVo> voList = new ArrayList<RegionVo>();
			if (regionList != null && regionList.size() != 0) {
				RegionVo vo = null;
				for (int i = 0; i < regionList.size(); i++) {
					vo = ModelUtils.fromDomainObjectToVo(RegionVo.class, regionList.get(i));
					voList.add(vo);
				}

			}
			return voList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
		}
	}

	/**
	 * 根据项目名称查询项目
	 * 
	 * @param projectName
	 *            项目名称
	 * @return
	 */
	@Override
	public List<ReportProjectVo> queryProjectNameByKeyWord(String projectName) throws ApplicationException {
		try {
			DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.Maitix.getCodeStr());
			List<ReportProjectVo> reportProjectVoList = queryProjectDAO.findProjectNameByKeyWord(projectName);
			return reportProjectVoList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
		}
	}

	/**
	 * 查询演出场馆
	 * 
	 * @param performField
	 *            演出场馆
	 * @return
	 * @throws ApplicationException
	 */
	@Override
	public List<RmvenueVo> queryPerformFieldByKeyWord(String performField) throws ApplicationException {
		try {
			GetMethod method = new GetMethod();
			StringBuffer field = new StringBuffer(MessageFormat.format(venueCenterUrl, performField));
			method.setURI(new URI(field.toString(), false, "UTF-8"));
			HttpClient httpClient = new HttpClient();
			httpClient.executeMethod(method);
			String res = method.getResponseBodyAsString();
			FieldDataVo parse = JSON.parse(res, FieldDataVo.class);
			List rmvenuelist = parse.getRmvenuelist();
			List<RmvenueVo> rmvenueVos = new ArrayList<RmvenueVo>();
			if (rmvenuelist != null && rmvenuelist.size() != 0) {

				for (int i = 0; i < rmvenuelist.size(); i++) {
					RmvenueVo vo = new RmvenueVo();
					Map<String, Object> rmvenueVo = (Map<String, Object>) rmvenuelist.get(i);
					vo.setName(rmvenueVo.get("Name").toString());
					vo.setVenueID(Long.valueOf(rmvenueVo.get("VenueID").toString()));
					rmvenueVos.add(vo);
				}
			}
			return rmvenueVos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
		}
	}

	/**
	 * 根据过滤条件查询项目
	 * 
	 * @param vo
	 * @return
	 * @throws ApplicationException
	 */
	@Override
	public PageResultData queryProjectByFilter(ReportProjectVo vo) throws ApplicationException {
		try {
			DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.Maitix.getCodeStr());
			PageResultData pageData = queryProjectDAO.queryProjectByFilter(vo);
			return pageData;
		} catch (Exception e) {
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
		}
	}

	/**
	 * 查询正在销售和已经结束的项目
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	@Override
	public Map<Integer, List<ReportProjectVo>> queryProjectSellAndOver() throws ApplicationException {
		try {
			DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.Maitix.getCodeStr());

			ReportProjectVo vo = new ReportProjectVo();
			vo.setPage(1);
			vo.setPageSize(7);
			vo.setIndex(true);

			// 查询正在销售的项目
			Integer start = Integer.valueOf(ProjectStatusEnum.Approve.getCodeStr());
			vo.setProjectStatus(start);
			PageResultData sellPageData = queryProjectDAO.queryProjectByFilter(vo);

			// 查询已经结束的项目
			Integer over = Integer.valueOf(ProjectStatusEnum.Over.getCodeStr());
			vo.setProjectStatus(over);
			PageResultData overPageData = queryProjectDAO.queryProjectByFilter(vo);
			Map<Integer, List<ReportProjectVo>> listMap = new HashMap<Integer, List<ReportProjectVo>>();
			listMap.put(start, sellPageData.getRows());
			listMap.put(over, overPageData.getRows());
			return listMap;
		} catch (Exception e) {
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
		}
	}

	/**
	 * 根据项目Id查询项目信息
	 * 
	 * @param projectId
	 *            项目Id
	 * @return
	 */
	public ReportProjectVo findProjectByProjectId(String source, long projectId) throws ApplicationException {
		try {
			DynamicDataSourceHolder.putDataSourceName(source);
			ReportProjectVo reportProjectVo = queryProjectDAO.queryProjectByProjectId(projectId);
			if (reportProjectVo != null) {
				reportProjectVo.setDataResource(source);
			}
			return reportProjectVo;
		} catch (Exception e) {
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
		}
	}

	public List<ReportProjectVo> findOwnerProjectList(Map<String, Long> siteTradeIdMap) throws ApplicationException {
		try {
			DynamicDataSourceHolder.putDataSourceName(DataSourceEnum.Maitix.getCodeStr());
			return queryProjectDAO.queryOwnerProjectList(siteTradeIdMap);
		} catch (Exception e) {
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
		}
	}

	/**
	 * 判断主办方是否授权项目权限
	 * 
	 * @param source
	 * @param projectId
	 * @param traderId
	 * @return
	 * @throws ApplicationException
	 * @author：guwei 【顾炜】 2014-3-28 下午3:27:58
	 */
	@Override
	public ReportProjectVo findIsOwnerProject(String source, Long projectId, Long traderId) throws ApplicationException {
		try {
			DynamicDataSourceHolder.putDataSourceName(source);
			return queryProjectDAO.queryIsOwnerProject(projectId, traderId);
		} catch (Exception e) {
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
		}
	}

	/**
	 * 转换vo
	 * 
	 * @return
	 */
	private List<ReportProjectVo> convertReportProjectVo(List<ReportProjectVo> reportProjectVoList) {
		return reportProjectVoList;
	}

}
