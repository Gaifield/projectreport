package cn.damai.boss.projectreport.report.service;

import java.util.List;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.vo.BMerchantInfoVo;

/**
 * B平台数据库查询服务接口
 * @author Administrator
 *
 */
public interface BusinessPlatformService {

    /**
     * 查询B平台商家信息列表
     * @param merchantIDs 商家Id
     * @param merchantIDs 商家名称
     * @return
     * @throws ApplicationException
     */
	public List<BMerchantInfoVo> findBMerchantInfoList(List<Long> merchantIDs,String companyName)throws ApplicationException;
}