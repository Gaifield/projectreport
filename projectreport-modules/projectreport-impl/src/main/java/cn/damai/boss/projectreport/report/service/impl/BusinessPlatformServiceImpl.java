package cn.damai.boss.projectreport.report.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.dao.BusinessPlatformDAO;
import cn.damai.boss.projectreport.report.service.BusinessPlatformService;
import cn.damai.boss.projectreport.report.vo.BMerchantInfoVo;

@Service
public class BusinessPlatformServiceImpl implements BusinessPlatformService {
	@Resource
	private BusinessPlatformDAO businessPlatformDAO;

	@Override
	public List<BMerchantInfoVo> findBMerchantInfoList(List<Long> merchantIDs,
			String companyName) throws ApplicationException {
		return businessPlatformDAO.queryBMerchantInfoList(merchantIDs,companyName);
	}
}