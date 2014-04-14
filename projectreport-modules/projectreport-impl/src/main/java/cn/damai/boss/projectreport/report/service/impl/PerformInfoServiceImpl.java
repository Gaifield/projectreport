package cn.damai.boss.projectreport.report.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.commons.utils.ConfigHelpUtil;
import cn.damai.boss.projectreport.report.dao.MaitixPerformDAO;
import cn.damai.boss.projectreport.report.dao.PerformInfoDao;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.service.PerformInfoService;
import cn.damai.boss.projectreport.report.vo.PerformVo;

/**
 * 场次信息管理
 * 
 * @author：guwei 【顾炜】 time：2014-3-1 上午2:45:52
 * 
 */
@Service
public class PerformInfoServiceImpl implements PerformInfoService {

	private static final Log log = LogFactory
			.getLog(PerformInfoServiceImpl.class);

	@Resource
	private PerformInfoDao performInfoDao;

	@Resource
	private MaitixPerformDAO maitixPerformDAO;

	@Override
	public List<PerformVo> queryPerformInfoByProjectId(Long projectId,
			Short site, String startTime, String endTime)
			throws ApplicationException {
		// TODO Auto-generated method stub

		try {

			// 判断数据来源
			if (site == null) {
				throw new ApplicationException(
						HttpStatusEnum.ClientError.getCode(),
						ConfigHelpUtil
								.getText("projectreport.perform.siteisnull"));
			}
			// BeiJing((short) 1, "北京站"), ShangHai((short) 2, "上海站"),
			// GuangZhou((short) 3,"广州站");
			// 根据站点切换数据源
			switch (site) {
			case 1:
				DynamicDataSourceHolder
						.putDataSourceName(DataSourceEnum.BJMaitix.getCodeStr());
				break;
			case 2:
				DynamicDataSourceHolder
						.putDataSourceName(DataSourceEnum.SHMaitix.getCodeStr());
				break;
			case 3:
				DynamicDataSourceHolder
						.putDataSourceName(DataSourceEnum.GZMaitix.getCodeStr());
				break;
			}
			return performInfoDao.findPerformInfoByProjectId(projectId,
					startTime, endTime);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			throw new ApplicationException(
					HttpStatusEnum.ClientError.getCode(),
					ConfigHelpUtil.getText("projectreport.perform.siteisnull"));
		}
	}

	public PageResultData findProjectPerformList(short source, long projectId,
			List<Long> performIds, Date startTime, Date endTime, int pageSize,
			int pageIndex) throws ApplicationException {		
		return maitixPerformDAO.queryPerformList(projectId, performIds,
				startTime, endTime, pageSize, pageIndex);

	}
}
