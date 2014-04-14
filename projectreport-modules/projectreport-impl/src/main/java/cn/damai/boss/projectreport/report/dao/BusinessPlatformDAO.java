package cn.damai.boss.projectreport.report.dao;

import java.util.List;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.vo.BMerchantInfoVo;

/**
 * B平台DAO接口类
 * @author Administrator
 *
 */
public interface BusinessPlatformDAO {
	/**
	 * 查询B平台商家信息列表
	 * 
	 * @param merchantIDs
	 *            商家Id
	 * @param companyName
	 *            商家名称
	 * @return
	 * @throws ApplicationException
	 */
	public List<BMerchantInfoVo> queryBMerchantInfoList(List<Long> merchantIDs,
			String companyName);
}