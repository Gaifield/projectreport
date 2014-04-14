package cn.damai.boss.projectreport.report.service;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.vo.ReserveStatVo;
import cn.damai.boss.projectreport.report.vo.SellerSaleReportVo;

/**
 * 渠道销售统计
 * @author：guwei 【顾炜】
 * time：2014-3-6 下午3:54:44
 *
 */
public interface SellerSaleService {

	
	/**
	 * 渠道销售统计
	 * @param projectId
	 * @param performIds
	 * @param saleTimeStart
	 * @param saleTimeEnd
	 * @param sellerName
	 * @param pageSize
	 * @param pageIndex
	 * @param addSum
	 * @return
	 */
	public SellerSaleReportVo findSellerSaleStat(String dataSource,Long projectId,
			List<Long> performIds, Date saleTimeStart, Date saleTimeEnd,
			String sellerName,int pageSize,int pageIndex,boolean addSum)throws ApplicationException;
	
	
	/**
	 * 导出excel
	 * @param reportVo 渠道统计表对象
	 * @return
	 * @throws ApplicationException
	 */
	public ByteArrayOutputStream outExcel(SellerSaleReportVo reportVo,String projectName) throws ApplicationException;
			
	/**
	 * 导出Pdf
	 * @param reportVo 渠道统计表对象
	 * @return
	 * @throws ApplicationException
	 */
	public ByteArrayOutputStream outPdf(SellerSaleReportVo reportVo,String projectName) throws ApplicationException;
}
