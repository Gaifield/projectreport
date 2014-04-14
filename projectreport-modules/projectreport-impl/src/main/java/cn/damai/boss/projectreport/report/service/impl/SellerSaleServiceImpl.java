package cn.damai.boss.projectreport.report.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Service;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.dao.BusinessPlatformDAO;
import cn.damai.boss.projectreport.report.dao.OrderAgentDAO;
import cn.damai.boss.projectreport.report.dao.ReportPriceDAO;
import cn.damai.boss.projectreport.report.dao.SellerSaleDao;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.AgentFromEnum;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.enums.PriceTypeEnum;
import cn.damai.boss.projectreport.report.enums.TicketPrintTypeEnum;
import cn.damai.boss.projectreport.report.service.SellerSaleService;
import cn.damai.boss.projectreport.report.vo.AgentVo;
import cn.damai.boss.projectreport.report.vo.BMerchantInfoVo;
import cn.damai.boss.projectreport.report.vo.OrderDiscountVo;
import cn.damai.boss.projectreport.report.vo.PriceCellVo;
import cn.damai.boss.projectreport.report.vo.PriceSaleStatDetailVo;
import cn.damai.boss.projectreport.report.vo.PriceSaleStatVo;
import cn.damai.boss.projectreport.report.vo.PriceVo;
import cn.damai.boss.projectreport.report.vo.SellerSaleReportVo;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class SellerSaleServiceImpl implements SellerSaleService {
	private final static Long NOTEXISTSAGENTID = -9L;

	@Resource
	private ReportPriceDAO reportPriceDAO;

	@Resource
	private SellerSaleDao sellerSaleDao;

	@Resource
	private BusinessPlatformDAO businessPlatformDAO;

	@Resource
	private OrderAgentDAO orderAgentDAO;

	@Override
	public SellerSaleReportVo findSellerSaleStat(String dataSource,
			Long projectId, List<Long> performIds, Date saleTimeStart,
			Date saleTimeEnd, String sellerName, int pageSize, int pageIndex,
			boolean addSum) throws ApplicationException {
		// 普通统计
		SellerSaleReportVo sellerSaleReportVo = statSellerSaleReport(
				dataSource, projectId, performIds, saleTimeStart, saleTimeEnd,
				sellerName, pageSize, pageIndex, false);

		// 统计总表
		if (addSum && sellerSaleReportVo != null
				&& sellerSaleReportVo.getPageData() != null
				&& sellerSaleReportVo.getPageData().getRows() != null) {
			SellerSaleReportVo sumData = statSellerSaleReport(dataSource,
					projectId, performIds, saleTimeStart, saleTimeEnd,
					sellerName, 0, 0, true);
			if (sumData != null && sumData.getPageData() != null
					&& sumData.getPageData().getRows() != null
					&& sumData.getPageData().getRows().size() > 0)

				sellerSaleReportVo.getPageData().getRows()
						.add(0, sumData.getPageData().getRows().get(0));
		}
		return sellerSaleReportVo;
	}

	private SellerSaleReportVo statSellerSaleReport(String dataSource,
			Long projectId, List<Long> performIds, Date saleTimeStart,
			Date saleTimeEnd, String sellerName, int pageSize, int pageIndex,
			boolean isGroupAll) throws ApplicationException {

		SellerSaleReportVo sellerSaleReportVo = new SellerSaleReportVo();
		// 1、查询代理商
		PageResultData<AgentVo> agentPageData = findAgentList(dataSource,
				projectId, performIds, saleTimeStart, saleTimeEnd, sellerName,
				pageSize, pageIndex);
		sellerSaleReportVo.setPageData(agentPageData);

		// 切换到北上广数据库
		DynamicDataSourceHolder.putDataSourceName(dataSource);

		// 2、查询场次价格
		List<PriceVo> prices = reportPriceDAO.queryProjectReportPrice(
				projectId, performIds);
		sellerSaleReportVo.setPrices(prices);

		// 3、查询代理销售数据
		Map<Integer, List<Long>> agentMap = getAgentMap(agentPageData);
		// 3.1、普通票销售统计
		// 3.1.1、普通票价格统计
		List<PriceSaleStatVo> priceSales = sellerSaleDao
				.queryNormalPriceDiscountSale(projectId, performIds, agentMap,
						saleTimeStart, saleTimeEnd, isGroupAll);
		// 3.1.2、普通票赠票
		List<PriceSaleStatVo> pricePreSentSales = sellerSaleDao
				.queryNormalPriceZeroDiscountSale(projectId, performIds,
						agentMap, saleTimeStart, saleTimeEnd, isGroupAll,
						TicketPrintTypeEnum.PreSent.getCode());
		// 3.1.3、普通票工作票
		List<PriceSaleStatVo> priceStaffSales = sellerSaleDao
				.queryNormalPriceZeroDiscountSale(projectId, performIds,
						agentMap, saleTimeStart, saleTimeEnd, isGroupAll,
						TicketPrintTypeEnum.Staff.getCode());
		// 3.2、套票票销售统计
		// 3.2.1、得到套票普通销售
		List<PriceSaleStatVo> agentPromotions = sellerSaleDao
				.queryPromotionPriceDiscountSale(projectId, performIds,
						agentMap, saleTimeStart, saleTimeEnd, isGroupAll);
		// 3.2.2、得到套票赠票销售
		List<PriceSaleStatVo> agentPreSentPromotions = sellerSaleDao
				.queryPromotionPriceZeroDiscountSale(projectId, performIds,
						agentMap, saleTimeStart, saleTimeEnd, isGroupAll,
						TicketPrintTypeEnum.PreSent.getCode());
		// 3.2.3、得到套票工作票销售
		List<PriceSaleStatVo> agentStaffPromotions = sellerSaleDao
				.queryPromotionPriceZeroDiscountSale(projectId, performIds,
						agentMap, saleTimeStart, saleTimeEnd, isGroupAll,
						TicketPrintTypeEnum.Staff.getCode());

		// 4、订单折扣标题列表
		List<OrderDiscountVo> discountTites = new ArrayList<OrderDiscountVo>();
		discountTites.add(new OrderDiscountVo("小计"));
		sellerSaleReportVo.setOrderDiscountList(discountTites);

		if (isGroupAll && agentPageData != null
				&& agentPageData.getRows() != null) {
			agentPageData = new PageResultData<AgentVo>();
			List<AgentVo> agentVos = new ArrayList<AgentVo>();
			AgentVo agentVoSum = new AgentVo();
			agentVoSum.setAgentName("全部");
			agentVos.add(agentVoSum);
			agentPageData.setRows(agentVos);
			sellerSaleReportVo.setPageData(agentPageData);
		}
		// 处理无效的渠道商列表
		Iterator<AgentVo> itr = agentPageData.getRows().iterator();
		while (itr.hasNext()) {
			AgentVo agentVo = itr.next();
			if (agentVo.getAgentId() != null
					&& agentVo.getAgentId().longValue() == NOTEXISTSAGENTID) {
				itr.remove();
			}
		}

		// 5、循环票价合计折扣明细
		if (agentPageData != null && agentPageData.getRows() != null) {
			for (AgentVo agentVo : agentPageData.getRows()) {

				// 代理商票价统计明细列表
				List<PriceSaleStatDetailVo> agentPriceDetails = new ArrayList<PriceSaleStatDetailVo>();
				agentVo.setPriceDetails(agentPriceDetails);

				// 代理商所有价格总计行
				PriceSaleStatDetailVo totalDetailVo = new PriceSaleStatDetailVo();
				totalDetailVo.setPriceVo(new PriceVo("总计"));
				// 汇总行折扣列表
				List<OrderDiscountVo> totalDiscountList = new ArrayList<OrderDiscountVo>();
				totalDetailVo.setDisccountList(totalDiscountList);
				// 汇总行折扣小计
				OrderDiscountVo totalDiscountSub = new OrderDiscountVo("小计");
				totalDiscountList.add(totalDiscountSub);
				// 汇总行赠品
				PriceCellVo totalPreSent = new PriceCellVo();
				totalDetailVo.setPresentSale(totalPreSent);
				// 汇总行工作票
				PriceCellVo totalstaff = new PriceCellVo();
				totalDetailVo.setStaffSale(totalstaff);
				// 汇总总额
				PriceCellVo totalSale = new PriceCellVo();
				totalDetailVo.setTotalSale(totalSale);

				for (PriceVo priceVo : prices) {
					PriceSaleStatDetailVo priceDetailVo = new PriceSaleStatDetailVo();
					priceDetailVo.setPriceVo(priceVo);
					// 票价折扣列表
					List<OrderDiscountVo> priceDiscountList = new ArrayList<OrderDiscountVo>();
					priceDetailVo.setDisccountList(priceDiscountList);
					// 票价折扣小计列
					OrderDiscountVo priceDiscountSub = new OrderDiscountVo("小计");
					priceDiscountList.add(priceDiscountSub);

					PriceCellVo pricePreSent = null;
					PriceCellVo priceStaff = null;
					// 票价出票总计
					PriceCellVo priceTotal = new PriceCellVo();
					priceDetailVo.setTotalSale(priceTotal);
					if (priceVo.getTicketType() == PriceTypeEnum.Normal
							.getCode()) {
						// 5.1、普通票

						// 5.1.2、赠票
						pricePreSent = loadPriceCell(pricePreSentSales,
								agentVo, priceVo);
						// 5.1.2、工作票
						priceStaff = loadPriceCell(priceStaffSales, agentVo,
								priceVo);
					} else if (priceVo.getTicketType() == PriceTypeEnum.Promotion
							.getCode()) {
						// 5.2、套票
						// 5.2.2、赠票
						pricePreSent = loadPriceCell(agentPreSentPromotions,
								agentVo, priceVo);
						// 5.2.2、工作票
						priceStaff = loadPriceCell(agentStaffPromotions,
								agentVo, priceVo);
					}
					// 5.1.1、折扣销售
					loadDiscountList(priceDiscountList, priceSales, agentVo,
							priceVo, priceDiscountSub, discountTites,
							totalDiscountList);
					// 5.2.1、套票折扣销售
					loadDiscountList(priceDiscountList, agentPromotions,
							agentVo, priceVo, priceDiscountSub, discountTites,
							totalDiscountList);

					// 累计票价折扣列表
					priceTotal.add(priceDiscountSub.getQuantity(),
							priceDiscountSub.getAmount());
					// 累计票价赠票
					priceDetailVo.setPresentSale(pricePreSent);
					priceTotal.add(pricePreSent);
					// 累计票价工作票
					priceDetailVo.setStaffSale(priceStaff);
					priceTotal.add(priceStaff);

					// 汇总行折扣小计列累计
					totalDiscountSub.add(priceDiscountSub);
					// 汇总行赠票列累计
					totalPreSent.add(pricePreSent);
					// 汇总行工作票列累计
					totalstaff.add(priceStaff);
					// 汇总行出票总计列累计
					totalSale.add(priceTotal);

					// 添加代理商单个票价行
					agentPriceDetails.add(priceDetailVo);
				}
				// 代理商所有票价总计行
				agentPriceDetails.add(totalDetailVo);
			}
		}

		return sellerSaleReportVo;
	}

	/**
	 * 统计票价赠票、工作票
	 * 
	 * @param saleDiscounts
	 *            票价销售明细
	 * @param agentId
	 *            代理Id
	 * @param price
	 *            票价
	 * @return
	 */
	private PriceCellVo loadPriceCell(List<PriceSaleStatVo> saleDiscounts,
			AgentVo agentVo, PriceVo price) {
		PriceCellVo priceCellVo = new PriceCellVo();
		for (PriceSaleStatVo discountSaleVo : saleDiscounts) {
			if (isAgentPrice(price, discountSaleVo, agentVo)) {
				priceCellVo.setAmount(discountSaleVo.getSumMoney());
				priceCellVo.setQuantity(discountSaleVo.getTicketQuantity());
				break;
			}
		}
		return priceCellVo;
	}

	/**
	 * @param priceDiscountList
	 *            票价折扣列表
	 * @param agentId
	 *            代理商
	 * @param price
	 *            票价
	 * @param saleDiscounts
	 *            普通票折扣统计列表
	 * @param subDiscount
	 *            票价小计
	 * @param discountTitles
	 *            订单折扣标题列表
	 * @param totalDiscountList
	 *            汇总行列表
	 * @return
	 */
	private void loadDiscountList(List<OrderDiscountVo> priceDiscountList,
			List<PriceSaleStatVo> saleDiscounts, AgentVo agentVo,
			PriceVo price, OrderDiscountVo subDiscount,
			List<OrderDiscountVo> discountTitles,
			List<OrderDiscountVo> totalDiscountList) {
		for (PriceSaleStatVo discountSaleVo : saleDiscounts) {
			// 默认添加到折扣标题列
			OrderDiscountVo.addToDiscountList(discountTitles,
					discountSaleVo.getDiscount());
			// 添加到价格总计行
			int totalDiscountIndex = OrderDiscountVo.addToDiscountList(
					totalDiscountList, discountSaleVo.getDiscount());

			// 按折扣找到对应折扣统计数据
			if (isAgentPrice(price, discountSaleVo, agentVo)) {
				// 添加到票价折扣列表
				int index = OrderDiscountVo.addToDiscountList(
						priceDiscountList, discountSaleVo.getDiscount());
				OrderDiscountVo orderDiscountVo = priceDiscountList.get(index);
				orderDiscountVo.setQuantity(discountSaleVo.getTicketQuantity());
				orderDiscountVo.setAmount(discountSaleVo.getSumMoney());

				// 累加票价折扣小计
				subDiscount.add(orderDiscountVo);

				// 累加折扣总计
				totalDiscountList.get(totalDiscountIndex).add(orderDiscountVo);
			} else {
				// 添加当前折扣默认值
				OrderDiscountVo.addToDiscountList(priceDiscountList,
						discountSaleVo.getDiscount());
			}
		}
	}

	private boolean isAgentPrice(PriceVo price, PriceSaleStatVo discountSaleVo,
			AgentVo agentVo) {
		boolean isLike = price.getTicketType() == discountSaleVo
				.getTicketType()
				&& price.getPrice().compareTo(discountSaleVo.getPrice()) == 0
				&& price.getPriceName().equals(discountSaleVo.getPriceName());
		if (isLike) {
			// 统计总表时不检查代理商是否一致
			if (discountSaleVo.getAgentFrom() != null
					&& discountSaleVo.getAgentID() != null) {
				isLike = discountSaleVo.getAgentFrom().equals(
						agentVo.getAgentFrom())
						&& discountSaleVo.getAgentID().equals(
								agentVo.getAgentId());
			}
		}
		return isLike;
	}

	/**
	 * 查询订单渠道商列表
	 * 
	 * @param projectId
	 *            项目Id
	 * @param performIds
	 *            场次Id列表
	 * @param saleTimeStart
	 *            订单创建开始时间
	 * @param saleTimeEnd
	 *            订单创建截至时间
	 * @param agentName
	 *            代理商名称
	 * @param pageSize
	 *            每页大小
	 * @param pageIndex
	 *            页码
	 * @return
	 */
	private PageResultData<AgentVo> findAgentList(String dataSource,
			long projectId, List<Long> performIds, Date saleTimeStart,
			Date saleTimeEnd, String agentName, int pageSize, int pageIndex) {
		PageResultData<AgentVo> pageData = new PageResultData<AgentVo>();

		List<Long> bPlatformMerhantIds = null;
		// 1、先查询B平台中的代理商
		if (!StringUtils.isBlank(agentName)) {
			bPlatformMerhantIds = new ArrayList<Long>();
			// 1.2、获取商业平台代理
			DynamicDataSourceHolder
					.putDataSourceName(DataSourceEnum.BUserCenter.getCodeStr());
			List<BMerchantInfoVo> bMerchantInfoList = businessPlatformDAO
					.queryBMerchantInfoList(null, agentName);
			if (bMerchantInfoList != null && bMerchantInfoList.size() > 0) {

				for (BMerchantInfoVo bMerchantInfoVo : bMerchantInfoList) {
					bPlatformMerhantIds.add(bMerchantInfoVo.getMerchantID());
				}
			} else {
				bPlatformMerhantIds.add(NOTEXISTSAGENTID);
			}
		}

		// 2、分页查询代理商信息
		DynamicDataSourceHolder.putDataSourceName(dataSource);
		pageData = orderAgentDAO.queryProjectAgentList(projectId, performIds,
				bPlatformMerhantIds, saleTimeStart, saleTimeEnd, agentName,
				pageSize, pageIndex);

		// 1、查询B平台代理商名称
		if (pageData != null && pageData.getRows() != null) {
			bPlatformMerhantIds = new ArrayList<Long>();
			for (AgentVo agentVo : pageData.getRows()) {
				if (agentVo.getAgentFrom() == AgentFromEnum.MPlus.getCode()) {
					// 查询B平台的代理商名称
					bPlatformMerhantIds.add(agentVo.getAgentId());
				}
			}
			if (bPlatformMerhantIds.size() > 0) {
				DynamicDataSourceHolder
						.putDataSourceName(DataSourceEnum.BUserCenter
								.getCodeStr());
				List<BMerchantInfoVo> merchantInfos = businessPlatformDAO
						.queryBMerchantInfoList(null, agentName);
				for (BMerchantInfoVo bMerchantInfoVo : merchantInfos) {
					for (AgentVo agentVo : pageData.getRows()) {
						if (agentVo.getAgentFrom().equals(AgentFromEnum.MPlus.getCode())
								&& agentVo.getAgentId().equals(
								bMerchantInfoVo.getMerchantID())) {
							agentVo.setAgentName(bMerchantInfoVo
									.getCompanyName());
							break;
						}
					}
				}
			}
		}

		if (!StringUtils.isBlank(agentName)) {
			if (pageData.getRows() == null) {
				List<AgentVo> agents = new ArrayList<AgentVo>();
				pageData.setRows(agents);
			}
			boolean emptyAgent = true;
			boolean emptyDamai = true;
			boolean emptyBPlatform = true;
			List<AgentVo> list = pageData.getRows();
			for (AgentVo vo : list) {
				if (vo.getAgentFrom().equals(AgentFromEnum.Maitix.getCode())) {
					emptyAgent = false;
				}
				if (vo.getAgentFrom().equals(AgentFromEnum.Damai.getCode())) {
					emptyDamai = false;
				}
				if (vo.getAgentFrom().equals(AgentFromEnum.MPlus.getCode())) {
					emptyBPlatform = false;
				}
				if (!emptyAgent && !emptyDamai && !emptyBPlatform) {
					break;
				}
			}
			if (emptyAgent) {
				AgentVo agentVo = new AgentVo();
				agentVo.setAgentId(NOTEXISTSAGENTID);
				agentVo.setAgentFrom(AgentFromEnum.Maitix.getCode());
				list.add(agentVo);
			}
			if (emptyDamai) {
				AgentVo agentVo = new AgentVo();
				agentVo.setAgentId(NOTEXISTSAGENTID);
				agentVo.setAgentFrom(AgentFromEnum.Damai.getCode());
				list.add(agentVo);
			}
			if (emptyBPlatform) {
				AgentVo agentVo = new AgentVo();
				agentVo.setAgentId(NOTEXISTSAGENTID);
				agentVo.setAgentFrom(AgentFromEnum.MPlus.getCode());
				list.add(agentVo);
			}
		}
		return pageData;
	}

	/**
	 * 返回maitix、大麦网、麦+平台代理商map
	 * 
	 * @param pageData
	 * @return
	 */
	private Map<Integer, List<Long>> getAgentMap(
			PageResultData<AgentVo> pageData) {
		if (pageData == null) {
			return null;
		}
		Map<Integer, List<Long>> agentMap = null;
		List<Long> maitixAgentList = new ArrayList<Long>();
		List<Long> damaiAgentList = new ArrayList<Long>();
		List<Long> bPlantformgentList = new ArrayList<Long>();

		for (AgentVo agentVo : pageData.getRows()) {
			// Maitix
			if (agentVo.getAgentFrom() == AgentFromEnum.Maitix.getCode()) {
				maitixAgentList.add(agentVo.getAgentId());
			}
			// B平台
			else if (agentVo.getAgentFrom() == AgentFromEnum.MPlus.getCode()) {
				bPlantformgentList.add(agentVo.getAgentId());
			}
			// 大麦网
			else if (agentVo.getAgentFrom() == AgentFromEnum.Damai.getCode()) {
				damaiAgentList.add(agentVo.getAgentId());
			}
		}
		if (maitixAgentList.size() > 0 || bPlantformgentList.size() > 0
				|| damaiAgentList.size() > 0) {
			agentMap = new HashMap<Integer, List<Long>>();
		}
		if (maitixAgentList.size() > 0) {
			agentMap.put(AgentFromEnum.Maitix.getCode(), maitixAgentList);
		}
		if (bPlantformgentList.size() > 0) {
			agentMap.put(AgentFromEnum.MPlus.getCode(), bPlantformgentList);
		}
		if (damaiAgentList.size() > 0) {
			agentMap.put(AgentFromEnum.Damai.getCode(), damaiAgentList);
		}
		return agentMap;
	}

	/**
	 * 导出excel表格
	 */
	@Override
	public ByteArrayOutputStream outExcel(SellerSaleReportVo reportVo,
			String projectName) throws ApplicationException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		HSSFWorkbook hwb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		String reportName = "渠道销售统计";
		HSSFSheet sheet = hwb.createSheet(reportName);
		HSSFFont font = hwb.createFont();
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = null;
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = hwb.createCellStyle();
		HSSFCellStyle style2 = hwb.createCellStyle();
		HSSFCellStyle style3 = hwb.createCellStyle();
		HSSFCellStyle style4 = hwb.createCellStyle();

		// 设置四周边框
		createBorder(style, sheet, 6);
		createFontWeight(style2, sheet, 6, font);
		createFont(style3, font);

		if (reportVo != null && reportVo.getPageData() != null
				&& reportVo.getPageData().getRows().size() > 0) {
			int cellCount = 2 + reportVo.getOrderDiscountList().size() + 2 + 1;

			int z = 0;
			row = sheet.createRow(z++);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue(projectName);
			cell.setCellStyle(style3);

			// 合并单元格参数分别是：所需合并的起始行、终止行、起始列、终止列
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellCount));

			for (int i = 0; i < reportVo.getPageData().getRows().size(); i++) {
				AgentVo agentVo = reportVo.getPageData().getRows().get(i);
				// 报表区块标题
				row = sheet.createRow(z++);
				cell = row.createCell(0);
				cell.setCellValue(agentVo.getAgentName());
				cell.setCellStyle(style3);
				createCellColor(style4, sheet, 6, font);

				cell = row.createCell(1);
				cell.setCellValue("汇总销售");
				cell.setCellStyle(style3);
				createCellColor(style4, sheet, 6, font);

				// 合并单元格参数分别是：所需合并的起始行、终止行、起始列、终止列
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 1,
						cellCount - 1));

				// 1、创建表格标题
				createHeader(reportVo.getOrderDiscountSortList(), sheet, z,
						style4);
				z += 2;

				// 2、创建行
				if (agentVo.getPriceDetails() != null
						&& agentVo.getPriceDetails().size() > 0) {
					for (int j = 0; j < agentVo.getPriceDetails().size(); j++) {
						// 添加票价行
						createExcelRow(sheet, z, style, agentVo
								.getPriceDetails().get(j));
						z += 2;
					}
					// 空一行
					row = sheet.createRow(z++);
				}
			}
		}

		try {
			hwb.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (hwb != null) {
					hwb = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return output;
	}

	// 导入表头数据
	private void createHeader(List<OrderDiscountVo> discountTitleList,
			HSSFSheet sheet, int rowIndex, HSSFCellStyle style) {

		int row = rowIndex;
		HSSFRow rowFirst = sheet.createRow(row++);
		HSSFRow rowSecond = sheet.createRow(row);
		HSSFCell cellFirst = null;
		HSSFCell cellSecond = null;

		int indexFirst = 0;
		int indexSecond = 0;

		cellFirst = rowFirst.createCell(indexFirst++);
		cellFirst.setCellValue("票价");
		cellFirst.setCellStyle(style);
		cellFirst = rowFirst.createCell(indexFirst++);
		cellFirst.setCellValue("票价");
		cellFirst.setCellStyle(style);

		cellSecond = rowSecond.createCell(indexSecond++);
		cellSecond.setCellValue("票价");
		cellSecond.setCellStyle(style);
		cellSecond = rowSecond.createCell(indexSecond++);
		cellSecond.setCellValue("票价");
		cellSecond.setCellStyle(style);

		for (int i = 0; i < discountTitleList.size(); i++) {
			cellFirst = rowFirst.createCell(indexFirst++);
			cellFirst.setCellStyle(style);
			cellFirst.setCellValue("销售");

			cellSecond = rowSecond.createCell(indexSecond++);
			cellSecond.setCellStyle(style);
			cellSecond.setCellValue(discountTitleList.get(i)
					.getDisaccountName());
		}

		cellFirst = rowFirst.createCell(indexFirst++);
		cellFirst.setCellValue("0价票");
		cellFirst.setCellStyle(style);
		cellSecond = rowSecond.createCell(indexSecond++);
		cellSecond.setCellValue("赠票");
		cellSecond.setCellStyle(style);

		cellFirst = rowFirst.createCell(indexFirst++);
		cellFirst.setCellValue("0价票");
		cellFirst.setCellStyle(style);
		cellSecond = rowSecond.createCell(indexSecond++);
		cellSecond.setCellValue("工作票");
		cellSecond.setCellStyle(style);

		cellFirst = rowFirst.createCell(indexFirst++);
		cellFirst.setCellValue("出票总计");
		cellFirst.setCellStyle(style);
		cellSecond = rowSecond.createCell(indexSecond++);
		cellSecond.setCellValue("出票总计");
		cellSecond.setCellStyle(style);

		// 合并单元格参数分别是：所需合并的起始行、终止行、起始列、终止列
		// 合并票价
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1, 0, 1));

		int discountSize = discountTitleList.size();
		// 合并销售
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2,
				2 + discountSize - 1));

		// 合并0价票
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex,
				2 + discountSize, 2 + discountSize + 1));

		// 合并出票总计
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1,
				2 + discountSize + 2, 2 + discountSize + 2));
	}

	// 导入价格行
	private void createExcelRow(HSSFSheet sheet, int rowIndex,
			HSSFCellStyle style, PriceSaleStatDetailVo rowVo) {
		int row = rowIndex;
		int indexQuantity = 0;
		int indexAmount = 0;

		// 数量行
		HSSFRow rowQuantity = sheet.createRow(row++);
		HSSFRow rowAmount = sheet.createRow(row);

		// 价格名称
		HSSFCell cellQuantity = rowQuantity.createCell(indexQuantity++);
		cellQuantity.setCellValue(rowVo.getPriceVo().getPriceShowName());
		cellQuantity.setCellStyle(style);
		HSSFCell cellAmount = rowAmount.createCell(indexAmount++);
		cellAmount.setCellStyle(style);

		// 数量
		cellQuantity = rowQuantity.createCell(indexQuantity++);
		cellQuantity.setCellValue("数量（张）");
		cellQuantity.setCellStyle(style);

		// 金额行
		cellAmount = rowAmount.createCell(indexAmount++);
		cellAmount.setCellValue("金额（元）");
		cellAmount.setCellStyle(style);

		// 预留等级列表
		for (OrderDiscountVo discount : rowVo.getDisccountSortList()) {
			cellQuantity = rowQuantity.createCell(indexQuantity++);
			cellQuantity.setCellValue(discount.getQuantity());
			cellQuantity.setCellStyle(style);

			cellAmount = rowAmount.createCell(indexAmount++);
			cellAmount.setCellValue("￥" + discount.getAmount().doubleValue());
			cellAmount.setCellStyle(style);
		}
		// 赠票
		cellQuantity = rowQuantity.createCell(indexQuantity++);
		cellQuantity.setCellValue(rowVo.getPresentSale().getQuantity());
		cellQuantity.setCellStyle(style);
		cellAmount = rowAmount.createCell(indexAmount++);
		cellAmount.setCellValue("￥"
				+ rowVo.getPresentSale().getAmount().doubleValue());
		cellAmount.setCellStyle(style);

		// 工作票
		cellQuantity = rowQuantity.createCell(indexQuantity++);
		cellQuantity.setCellValue(rowVo.getStaffSale().getQuantity());
		cellQuantity.setCellStyle(style);
		cellAmount = rowAmount.createCell(indexAmount++);
		cellAmount.setCellValue("￥"
				+ rowVo.getStaffSale().getAmount().doubleValue());
		cellAmount.setCellStyle(style);

		// 出票总计
		cellQuantity = rowQuantity.createCell(indexQuantity++);
		cellQuantity.setCellValue(rowVo.getTotalSale().getQuantity());
		cellQuantity.setCellStyle(style);
		cellAmount = rowAmount.createCell(indexAmount++);
		cellAmount.setCellValue("￥"
				+ rowVo.getTotalSale().getAmount().doubleValue());
		cellAmount.setCellStyle(style);

		// 合并单元格参数分别是：所需合并的起始行、终止行、起始列、终止列
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1, 0, 0));
	}

	// 字体加粗。用于项目名称，场次标题
	private void createFont(HSSFCellStyle style, HSSFFont font) {
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
	}

	// 设置excel四周边框,列宽,加粗。用于总计行
	/**
	 * @param style
	 * @param sheet
	 * @param col
	 * @param font
	 */
	private void createFontWeight(HSSFCellStyle style, HSSFSheet sheet,
			int col, HSSFFont font) {
		// 字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		createBorder(style, sheet, col);
	}

	// 设置excel四周边框,列宽,加粗。用于总计行
	private void createCellColor(HSSFCellStyle style, HSSFSheet sheet, int col,
			HSSFFont font) {
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		// 字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		createBorder(style, sheet, col);
	}

	// 设置excel四周边框,列宽(非加粗字段)。用于循环数据
	private void createBorder(HSSFCellStyle style, HSSFSheet sheet, int col) {
		// 加边框
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 列宽
		for (int i = 0; i < col; i++) {
			sheet.setColumnWidth(i, (short) 5000);
		}
	}

	@Override
	public ByteArrayOutputStream outPdf(SellerSaleReportVo reportVo,
			String projectName) throws ApplicationException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		Document pdfDoc = new Document(PageSize.A4, 50, 50, 50, 50);
		try {
			BaseFont bfChinese = BaseFont.createFont("/SIMHEI.TTF",
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Font fontChinese = new Font(bfChinese, 10, Font.NORMAL,
					BaseColor.BLACK);

			PdfWriter.getInstance(pdfDoc, output);
			pdfDoc.open();

			if (reportVo != null && reportVo.getPageData() != null
					&& reportVo.getPageData().getRows().size() > 0) {
				int cellCount = 2 + reportVo.getOrderDiscountList().size() + 2 + 1;
				PdfPTable pdfTable = new PdfPTable(cellCount);
				// 写入项目名称
				PdfPCell cell = new PdfPCell(new Phrase(projectName,
						fontChinese));
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				// 合并横向表格
				cell.setColspan(cellCount);
				pdfTable.addCell(cell);

				for (int i = 0; i < reportVo.getPageData().getRows().size(); i++) {
					AgentVo agentVo = reportVo.getPageData().getRows().get(i);

					// 写入区块标题
					cell = new PdfPCell(new Phrase(agentVo.getAgentName(),
							fontChinese));
					pdfTable.addCell(cell);

					cell = new PdfPCell(new Phrase("汇总销售", fontChinese));
					cell.setColspan(cellCount - 1);
					pdfTable.addCell(cell);

					// 在次行写入表头
					createPdfHeader(reportVo.getOrderDiscountSortList(),
							pdfTable, fontChinese);

					if (agentVo.getPriceDetails() != null
							&& agentVo.getPriceDetails().size() > 0) {
						// 写入表格内部数据
						for (int j = 0; j < agentVo.getPriceDetails().size(); j++) {
							createPdfRow(agentVo.getPriceDetails().get(j),
									pdfTable, fontChinese);
						}
					}
				}
				pdfDoc.add(pdfTable);
			}
			pdfDoc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return output;
	}

	// 导入pdf表格表头
	private void createPdfHeader(List<OrderDiscountVo> discountTitleList,
			PdfPTable table, Font fontChinese) {
		PdfPCell priceCell = new PdfPCell(new Phrase("票价", fontChinese));
		setPdfPCellTitleStyle(priceCell);
		priceCell.setColspan(2);
		priceCell.setRowspan(2);
		table.addCell(priceCell);

		priceCell = new PdfPCell(new Phrase("销售", fontChinese));
		setPdfPCellTitleStyle(priceCell);
		priceCell.setColspan(discountTitleList.size());
		table.addCell(priceCell);

		priceCell = new PdfPCell(new Phrase("0价票", fontChinese));
		setPdfPCellTitleStyle(priceCell);
		priceCell.setColspan(2);
		table.addCell(priceCell);

		priceCell = new PdfPCell(new Phrase("出票总计", fontChinese));
		setPdfPCellTitleStyle(priceCell);
		priceCell.setRowspan(2);
		table.addCell(priceCell);

		for (int i = 0; i < discountTitleList.size(); i++) {
			table.addCell(new PdfPCell(new Phrase(discountTitleList.get(i)
					.getDisaccountName(), fontChinese)));
		}

		priceCell = new PdfPCell(new Phrase("赠票", fontChinese));
		setPdfPCellTitleStyle(priceCell);
		table.addCell(priceCell);

		priceCell = new PdfPCell(new Phrase("工作票", fontChinese));
		setPdfPCellTitleStyle(priceCell);
		table.addCell(priceCell);
	}

	private void setPdfPCellTitleStyle(PdfPCell cell) {
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	}

	// 导入pdf表格内容
	private void createPdfRow(PriceSaleStatDetailVo rowVo, PdfPTable table,
			Font fontChinese) {
		PdfPCell priceCell = new PdfPCell(new Phrase(rowVo.getPriceVo()
				.getPriceName(), fontChinese));
		priceCell.setRowspan(2);
		setPdfPCellTitleStyle(priceCell);
		table.addCell(priceCell);

		// 数量行
		table.addCell(new PdfPCell(new Phrase("数量（张）", fontChinese)));
		for (OrderDiscountVo discount : rowVo.getDisccountSortList()) {
			table.addCell(new PdfPCell(new Phrase(discount.getQuantity() + "",
					fontChinese)));
		}
		table.addCell(new PdfPCell(new Phrase(rowVo.getPresentSale()
				.getQuantity() + "", fontChinese)));
		table.addCell(new PdfPCell(new Phrase(rowVo.getStaffSale()
				.getQuantity() + "", fontChinese)));
		table.addCell(new PdfPCell(new Phrase(rowVo.getTotalSale()
				.getQuantity() + "", fontChinese)));

		table.addCell(new PdfPCell(new Phrase("金额（元）", fontChinese)));
		for (OrderDiscountVo discount : rowVo.getDisccountSortList()) {
			table.addCell(new PdfPCell(new Phrase("￥" + discount.getAmount(),
					fontChinese)));
		}
		table.addCell(new PdfPCell(new Phrase("￥"
				+ rowVo.getPresentSale().getAmount(), fontChinese)));
		table.addCell(new PdfPCell(new Phrase("￥"
				+ rowVo.getStaffSale().getAmount(), fontChinese)));
		table.addCell(new PdfPCell(new Phrase("￥"
				+ rowVo.getTotalSale().getAmount(), fontChinese)));
	}
}