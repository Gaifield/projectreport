package cn.damai.boss.projectreport.report.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.report.dao.MaitixPerformDAO;
import cn.damai.boss.projectreport.report.dao.ReportPriceDAO;
import cn.damai.boss.projectreport.report.dao.SeatStatDAO;
import cn.damai.boss.projectreport.report.enums.SeatTypeEnum;
import cn.damai.boss.projectreport.report.service.SeatStatService;
import cn.damai.boss.projectreport.report.vo.PerformVo;
import cn.damai.boss.projectreport.report.vo.PriceVo;
import cn.damai.boss.projectreport.report.vo.SeatStatVo;

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

/**
 * 注释：座位汇总service实现类 作者：liutengfei 【刘腾飞】 时间：14-3-3 下午1:58
 */
@Service
public class SeatStatServiceImpl implements SeatStatService {

	private static final Log log = LogFactory.getLog(SeatStatServiceImpl.class);

	@Resource
	private SeatStatDAO seatStatDAO;

	@Resource
	private MaitixPerformDAO maitixPerformDAO;

	@Resource
	private ReportPriceDAO peportPriceDAO;

	/**
	 * 根据项目id获取座位汇总信息
	 * 
	 * @param projectId
	 * @return
	 * @author：guwei 【顾炜】 time：2014-3-3 下午5:30:07
	 */
	@Override
	public List<SeatStatVo> querySeatStatInfo(List<Long> performIds, Long projectId) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			List<SeatStatVo> seatSLists = seatStatDAO.findSeatStatInfo(projectId, performIds);

			Map<String, PriceVo> priceMap = getPriceMap(performIds, projectId, 1);
			// 处理普通票查询到的数据,合并工作票防涨票
			seatSLists = excuteStaffAndProtectDate(seatSLists, false, priceMap);

			List<SeatStatVo> promotionList = seatStatDAO.queryPromotionSeatInfo(projectId, performIds);

			if (promotionList != null) {
				priceMap = getPriceMap(performIds, projectId, 2);
				promotionList = excuteStaffAndProtectDate(promotionList, false, priceMap);
				seatSLists.addAll(promotionList);
			}
			return seatSLists;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			throw new ApplicationException(HttpStatusEnum.ServerError.getCode(), e.getMessage());
		}
	}

	/**
	 * 根据藏瓷idlist 去查询座位汇总信息
	 * 
	 * @param performIds
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 * @throws ApplicationException
	 * @author：guwei 【顾炜】 time：2014-3-3 下午8:13:52
	 */
	@Override
	public PageResultData<SeatStatVo> querySeatStatDetail(Long projectInfoId, List<Long> performIds, int pageSize, int pageIndex)
			throws ApplicationException {
		try {

			// 分页处理
			List<Long> subList = new ArrayList<Long>();
			PageResultData pageData = maitixPerformDAO.queryPerformList(projectInfoId, performIds, null, null, pageSize, pageIndex);
			if (pageData != null && pageData.getRows() != null) {
				List<PerformVo> data = pageData.getRows();
				for (PerformVo rp : data) {
					long performId = rp.getPerformId();
					subList.add(performId);
				}
				List<SeatStatVo> seatStatList = new ArrayList<SeatStatVo>();
				List<SeatStatVo> seatStatVoList = seatStatDAO.findSeatStatDetail(subList);

				Map<String, PriceVo> priceMap = getPriceMap(performIds, projectInfoId, 1);
				// 处理普通票查询到的数据,合并工作票防涨票
				seatStatVoList = excuteStaffAndProtectDate(seatStatVoList, true, priceMap);

				List<SeatStatVo> promotionList = seatStatDAO.queryPromotionSeatDetail(subList);

				if (promotionList != null) {
					priceMap = getPriceMap(performIds, projectInfoId, 2);
					promotionList = excuteStaffAndProtectDate(promotionList, true, priceMap);
					seatStatVoList.addAll(promotionList);
				}
				Map<Long, List<SeatStatVo>> map = new HashMap<Long, List<SeatStatVo>>();

				filterByPerform(seatStatVoList, map, seatStatList, data);

				PageResultData<SeatStatVo> seatStatVoPager = new PageResultData<SeatStatVo>();
				seatStatVoPager.setRows(seatStatList);
				seatStatVoPager.setTotal(pageData.getTotal());
				return seatStatVoPager;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return null;
	}

	/**
	 * 根据价格排序
	 * 
	 * @param seatSLists
	 * @author：guwei 【顾炜】 2014-4-1 下午2:31:02
	 */
	private void collectionsByPrice(List<SeatStatVo> seatSLists) {
		// 排序
		Collections.sort(seatSLists, new Comparator<SeatStatVo>() {
			@Override
			public int compare(SeatStatVo src, SeatStatVo tgt) {
				BigDecimal srcPrice = src.getPrice();
				BigDecimal tgtPrice = tgt.getPrice();
				return srcPrice.compareTo(tgtPrice) * (-1);
			}
		});
	}

	/**
	 * 获取套票的map
	 * 
	 * @param performIds
	 * @param projectId
	 * @param priceType
	 * @return
	 * @author：guwei 【顾炜】 2014-3-21 下午7:12:40
	 */
	private Map<String, PriceVo> getPriceMap(List<Long> performIds, Long projectId, int priceType) {
		// 1、查询分组价格
		List<PriceVo> prices = peportPriceDAO.queryProjectReportPrice(projectId, performIds);
		Map<String, PriceVo> priceMap = new HashMap<String, PriceVo>();
		for (PriceVo pvo : prices) {
			if (pvo.getTicketType() == priceType) {
				priceMap.put(pvo.getPrice() + "-" + pvo.getPriceName(), pvo);
			}
		}
		return priceMap;
	}

	/**
	 * 处理 合并 数据中的工作票防涨票数据
	 * 
	 * @param seatSLists
	 * @param performBool
	 *            是否按场次分组
	 * @return
	 * @author：guwei 【顾炜】 2014-3-21 下午5:28:35
	 */
	private List<SeatStatVo> excuteStaffAndProtectDate(List<SeatStatVo> seatSLists, boolean performBool, Map<String, PriceVo> priceMap) {
		if (seatSLists == null) {
			return null;
		}
		List<SeatStatVo> seatStatVos = new ArrayList<SeatStatVo>();
		Map<String, SeatStatVo> map = new HashMap<String, SeatStatVo>();
		for (SeatStatVo vo : seatSLists) {
			int seatType = vo.getSeatType();
			BigDecimal price = vo.getPrice();
			String priceName = vo.getPriceName();
			String key = price + "-" + priceName;
			if (performBool) {
				key = price + "-" + vo.getPerformId();
			}
			if (seatType == SeatTypeEnum.Normal.getCode()) {
				map.put(key, vo);
			}
		}
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			SeatStatVo vos = map.get(iterator.next());
			BigDecimal prices = vos.getPrice();
			PriceVo priceVo = priceMap.get(prices + "-" + vos.getPriceName());
			if (priceVo == null) {
				priceVo = priceMap.get(prices + ".000");
			}
			if (priceVo != null) {
				vos.setPriceShowName(priceVo.getPriceShowName());
			} else {
				vos.setPriceShowName(prices + "");
			}
			Long performIds = vos.getPerformId();
			BigDecimal seatAmounts = vos.getSeatAmount();
			seatAmounts = seatAmounts == null ? new BigDecimal(0) : seatAmounts;
			Integer seatQuantitys = vos.getSeatQuantity();
			Integer staffQuantitys = 0;
			Integer protectQuantitys = 0;
			BigDecimal staffAmounts = vos.getStaffAmount();
			staffAmounts = staffAmounts == null ? new BigDecimal(0) : staffAmounts;
			BigDecimal protectAmounts = vos.getProtectAmount();
			protectAmounts = protectAmounts == null ? new BigDecimal(0) : protectAmounts;

			for (SeatStatVo vo : seatSLists) {
				BigDecimal price = vo.getPrice();
				Long performId = vo.getPerformId();
				BigDecimal seatAmount = vo.getSeatAmount();
				Integer seatQuantity = vo.getSeatQuantity();

				int seatType = vo.getSeatType();

				if (performBool) {
					if (prices.compareTo(price) == 0 && performIds.equals(performId)) {
						if (seatType == SeatTypeEnum.Staff.getCode()) {
							staffQuantitys += seatQuantity;
							staffAmounts = staffAmounts.add(seatAmount);

						} else if (seatType == SeatTypeEnum.Protect.getCode()) {
							protectQuantitys += seatQuantity;
							protectAmounts = protectAmounts.add(seatAmount);
						}
					}

				} else {
					if (prices.compareTo(price) == 0) {
						if (seatType == SeatTypeEnum.Staff.getCode()) {
							staffQuantitys += seatQuantity;
							staffAmounts = staffAmounts.add(seatAmount);
						} else if (seatType == SeatTypeEnum.Protect.getCode()) {
							protectQuantitys += seatQuantity;
							protectAmounts = protectAmounts.add(seatAmount);
						}
					}
				}

			}
			vos.setSeatQuantity(seatQuantitys + staffQuantitys + protectQuantitys);
			vos.setSeatAmount(seatAmounts.add(staffAmounts).add(protectAmounts));
			vos.setStaffQuantity(staffQuantitys);
			vos.setStaffAmount(staffAmounts);
			vos.setProtectQuantity(protectQuantitys);
			vos.setProtectAmount(protectAmounts);
			seatStatVos.add(vos);
		}
		// 根据价格排序
		collectionsByPrice(seatStatVos);
		return seatStatVos;
	}

	/**
	 * 根据场次过滤数据
	 * 
	 * @param seatList
	 * @param map
	 * @param seatStatList
	 * @author：guwei 【顾炜】 2014-4-1 下午2:43:15
	 */
	private void filterByPerform(List<SeatStatVo> seatList, Map<Long, List<SeatStatVo>> map, List<SeatStatVo> seatStatList, List<PerformVo> data) {
		if (seatList != null) {
			Map<Long, SeatStatVo> seatVomap = new HashMap<Long, SeatStatVo>();
			for (SeatStatVo seatVo : seatList) {
				Long performId = seatVo.getPerformId();

				SeatStatVo seatStatVo = seatVo;
				if (seatVomap.containsKey(performId)) {
					seatStatVo = seatVomap.get(performId);
					List<SeatStatVo> seatStatVoList = seatStatVo.getSeatStatVoList();
					seatStatVoList.add(seatVo);
					seatStatVo.setSeatStatVoList(seatStatVoList);
				} else {
					List<SeatStatVo> list = new ArrayList<SeatStatVo>();
					list.add(seatVo);
					seatStatVo.setSeatStatVoList(list);
				}

				seatStatVo.setPerformId(performId);
				seatVomap.put(performId, seatStatVo);
			}
			// 判断是否需要不全数据场次
			boolean bool = data.size() > map.size();
			if (bool) {
				for (PerformVo pv : data) {
					long performId = pv.getPerformId();
					if (!seatVomap.containsKey(performId)) {
						SeatStatVo ssvo = new SeatStatVo();
						ssvo.setPerformId(performId);
						ssvo.setPerformName(pv.getPerformName());
						ssvo.setPerformTime(pv.getPerformTime());
						seatVomap.put(performId, ssvo);
					}
				}
			}
			seatStatList.addAll(seatVomap.values());
		}
	}

	/**
	 * 座位汇总表导出excel
	 */
	@Override
	public ByteArrayOutputStream outExcel(List<SeatStatVo> seatList, boolean tag) throws ApplicationException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		String reportName = "座位汇总表";
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet(reportName);
		HSSFFont font = hwb.createFont();
		HSSFCellStyle style = hwb.createCellStyle();
		// style四周加边框，字体加粗，列宽，加背景色，用于表头
		createCellColor(style, sheet, 6, font);
		HSSFCellStyle style1 = hwb.createCellStyle();
		// style1四周加边框，列宽，用户表体数据循环
		createBorder(style1, sheet, 6);
		HSSFCellStyle style2 = hwb.createCellStyle();
		// style2四周加边框，列宽，字体加粗，用户总计行
		createFontWeight(style2, sheet, 6, font);
		HSSFRow row = null;
		HSSFCell cell = null;
		int z = 0;
		if (tag == true && seatList != null && seatList.size() != 0 && seatList.get(0).getSeatStatVoList() != null
				&& seatList.get(0).getSeatStatVoList().size() != 0) { // tag为true时指的是明细表，false时指的是汇总表
			for (int i = 0; i < seatList.size(); i++) {
				row = sheet.createRow(z++);
				cell = row.createCell(0);
				cell.setCellStyle(style1);
				SeatStatVo seatStatVo = seatList.get(i);
				String performName = seatStatVo.getPerformName();
				String performTimeStr = seatStatVo.getPerformTimeStr();
				cell.setCellValue(performName + " ---- " + performTimeStr);
				cell = row.createCell(1);
				cell.setCellStyle(style1);
				cell.setCellValue(performName + " ---- " + performTimeStr);
				cell = row.createCell(2);
				cell.setCellStyle(style1);
				cell.setCellValue(performName + " ---- " + performTimeStr);
				cell = row.createCell(3);
				cell.setCellStyle(style1);
				cell.setCellValue(performName + " ---- " + performTimeStr);
				cell = row.createCell(4);
				cell.setCellStyle(style1);
				cell.setCellValue(performName + " ---- " + performTimeStr);
				cell = row.createCell(5);
				cell.setCellStyle(style1);
				cell.setCellValue(performName + " ---- " + performTimeStr);
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 0, 5));
				List<SeatStatVo> lists = seatStatVo.getSeatStatVoList();
				createTitle(sheet, style, row, cell, z++);
				createTable(lists, sheet, style1, style2, row, cell, z++);
				if (lists != null) {
					int size = lists.size();
					z = z + size + 7;
				}
			}

		}
		if (tag == false && seatList != null && seatList.size() != 0) {
			createTitle(sheet, style, row, cell, z++);
			createTable(seatList, sheet, style1, style2, row, cell, z);
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

	// 设置excel四周边框,列宽,加粗。用于总计行
	/**
	 * @param style
	 * @param sheet
	 * @param col
	 * @param font
	 */
	private void createFontWeight(HSSFCellStyle style, HSSFSheet sheet, int col, HSSFFont font) {
		// 字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		createBorder(style, sheet, col);
	}

	// 设置excel四周边框,列宽,加粗。用于总计行
	private void createCellColor(HSSFCellStyle style, HSSFSheet sheet, int col, HSSFFont font) {
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

	// 导入excel表头
	public void createTitle(HSSFSheet sheet, HSSFCellStyle style, HSSFRow row, HSSFCell cell, int startRow) {
		row = sheet.createRow(startRow);
		cell = row.createCell(0);
		cell.setCellValue("票价");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("票价");
		cell.setCellStyle(style);
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, (short) 0, (short) 1));
		cell = row.createCell(2);
		cell.setCellStyle(style);
		cell.setCellValue("使用座位");
		cell = row.createCell(3);
		cell.setCellStyle(style);
		cell.setCellValue("工作票");
		cell = row.createCell(4);
		cell.setCellStyle(style);
		cell.setCellValue("防涨票");
		cell = row.createCell(5);
		cell.setCellStyle(style);
		cell.setCellValue("可售票（总票房）");
	}

	// 导入excel表格数据,包括总计行
	public void createTable(List<SeatStatVo> lists, HSSFSheet sheet, HSSFCellStyle style1, HSSFCellStyle style2, HSSFRow row, HSSFCell cell,
			int startRow) {
		long seatQuantitySum = 0;
		long staffQuantitySum = 0;
		long protectQuantitySum = 0;
		long vendibilityQuantitySum = 0;
		BigDecimal seatAmountSum = new BigDecimal(0);
		BigDecimal staffAmountSum = new BigDecimal(0);
		BigDecimal protectAmountSum = new BigDecimal(0);
		BigDecimal vendibilityAmountSum = new BigDecimal(0);
		if (lists != null && lists.size() != 0) {
			for (int i = 0; i < lists.size(); i++) {
				row = sheet.createRow(startRow);
				cell = row.createCell(0);
				SeatStatVo vo = lists.get(i);
				cell.setCellValue(vo.getPriceShowName().toString());
				sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, (short) 0, (short) 0));
				cell.setCellStyle(style1);
				// startRow = startRow+2;
				cell = row.createCell(1);
				cell.setCellStyle(style1);
				cell.setCellValue("数量（张）");
				cell = row.createCell(2);
				cell.setCellStyle(style1);
				seatQuantitySum = seatQuantitySum + vo.getSeatQuantity();
				cell.setCellValue(vo.getSeatQuantity());
				cell = row.createCell(3);
				cell.setCellStyle(style1);
				staffQuantitySum = staffQuantitySum + vo.getStaffQuantity();
				cell.setCellValue(vo.getStaffQuantity());
				cell = row.createCell(4);
				cell.setCellStyle(style1);
				protectQuantitySum = protectQuantitySum + vo.getProtectQuantity();
				cell.setCellValue(vo.getProtectQuantity());
				cell = row.createCell(5);
				cell.setCellStyle(style1);
				vendibilityQuantitySum = vendibilityQuantitySum + vo.getVendibilityQuantity();
				cell.setCellValue(vo.getVendibilityQuantity());
				row = sheet.createRow(startRow + 1);
				cell = row.createCell(1);
				cell.setCellStyle(style1);
				cell.setCellValue("金额（元）");
				cell = row.createCell(2);
				cell.setCellStyle(style1);
				seatAmountSum = seatAmountSum.add(vo.getSeatAmount());
				cell.setCellValue("￥" + vo.getSeatAmount().toString());
				cell = row.createCell(3);
				cell.setCellStyle(style1);
				staffAmountSum = staffAmountSum.add(vo.getStaffAmount());
				cell.setCellValue("￥" + vo.getStaffAmount().toString());
				cell = row.createCell(4);
				cell.setCellStyle(style1);
				protectAmountSum = protectAmountSum.add(vo.getProtectAmount());
				cell.setCellValue("￥" + vo.getProtectAmount().toString());
				cell = row.createCell(5);
				cell.setCellStyle(style1);
				vendibilityAmountSum = vendibilityAmountSum.add(vo.getVendibilityAmount());
				cell.setCellValue("￥" + vo.getVendibilityAmount().toString());
				startRow = startRow + 2;
			}
			row = sheet.createRow(startRow);
			cell = row.createCell(0);
			cell.setCellValue("总计");
			sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, 0, 0));
			cell.setCellStyle(style2);
			cell = row.createCell(1);
			cell.setCellStyle(style2);
			cell.setCellValue("数量（张）");
			cell = row.createCell(2);
			cell.setCellStyle(style2);
			cell.setCellValue(seatQuantitySum);
			cell = row.createCell(3);
			cell.setCellStyle(style2);
			cell.setCellValue(staffQuantitySum);
			cell = row.createCell(4);
			cell.setCellStyle(style2);
			cell.setCellValue(protectQuantitySum);
			cell = row.createCell(5);
			cell.setCellStyle(style2);
			cell.setCellValue(vendibilityQuantitySum);
			row = sheet.createRow(startRow + 1);
			cell = row.createCell(0);
			cell.setCellStyle(style2);
			cell.setCellValue("总计");
			cell = row.createCell(1);
			cell.setCellStyle(style2);
			cell.setCellValue("金额（元）");
			cell = row.createCell(2);
			cell.setCellStyle(style2);
			cell.setCellValue("￥" + seatAmountSum.toString());
			cell = row.createCell(3);
			cell.setCellStyle(style2);
			cell.setCellValue("￥" + staffAmountSum.toString());
			cell = row.createCell(4);
			cell.setCellStyle(style2);
			cell.setCellValue("￥" + protectAmountSum.toString());
			cell = row.createCell(5);
			cell.setCellStyle(style2);
			cell.setCellValue("￥" + vendibilityAmountSum.toString());
		}
	}

	/**
	 * 导出座位汇总pdf
	 */
	@Override
	public ByteArrayOutputStream outPdf(List<SeatStatVo> seatList, boolean tag) throws ApplicationException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Document pdfDoc = new Document(PageSize.A4, 50, 50, 50, 50);

		try {
			BaseFont bfChinese = BaseFont.createFont("/SIMHEI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Font fontChinese = new Font(bfChinese, 10, Font.NORMAL, BaseColor.BLACK);
			PdfWriter.getInstance(pdfDoc, output);
			pdfDoc.open();
			PdfPTable pdfTable = new PdfPTable(6);
			pdfTable.setWidthPercentage(110);
			if (tag == true && seatList != null && seatList.size() != 0 && seatList.get(0).getSeatStatVoList() != null
					&& seatList.get(0).getSeatStatVoList().size() != 0) {
				for (int i = 0; i < seatList.size(); i++) {
					SeatStatVo seatStatVo = seatList.get(i);
					PdfPCell cell = new PdfPCell(new Phrase(seatStatVo.getPerformName() + " ---- " + seatStatVo.getPerformTimeStr(), fontChinese));
					cell.setColspan(6);
					pdfTable.addCell(cell);
					createPdfTitle(pdfTable, fontChinese);
					createPdf(seatStatVo.getSeatStatVoList(), pdfTable, fontChinese);
				}

			}
			if (tag == false && seatList != null && seatList.size() != 0) {
				createPdfTitle(pdfTable, fontChinese);
				createPdf(seatList, pdfTable, fontChinese);
			}
			pdfDoc.add(pdfTable);
			pdfDoc.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	// 导入pdf表头
	public void createPdfTitle(PdfPTable pdfTable, Font fontChinese) {
		PdfPCell cell = new PdfPCell(new Phrase("票价", fontChinese));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pdfTable.addCell(cell);
		pdfTable.addCell(new PdfPCell(new Phrase("使用座位", fontChinese)));
		pdfTable.addCell(new PdfPCell(new Phrase("工作票", fontChinese)));
		pdfTable.addCell(new PdfPCell(new Phrase("防涨票", fontChinese)));
		pdfTable.addCell(new PdfPCell(new Phrase("可售票（总票房）", fontChinese)));
	}

	// 导入pdf表格数据
	public void createPdf(List<SeatStatVo> lists, PdfPTable pdfTable, Font fontChinese) {
		long seatQuantitySum = 0;
		long staffQuantitySum = 0;
		long protectQuantitySum = 0;
		long vendibilityQuantitySum = 0;
		BigDecimal seatAmountSum = new BigDecimal(0);
		BigDecimal staffAmountSum = new BigDecimal(0);
		BigDecimal protectAmountSum = new BigDecimal(0);
		BigDecimal vendibilityAmountSum = new BigDecimal(0);
		if (lists != null && lists.size() != 0) {
			// 表身数据
			for (int i = 0; i < lists.size(); i++) {
				SeatStatVo vo = lists.get(i);
				PdfPCell cell = new PdfPCell(new Phrase(vo.getPriceShowName().toString()));
				cell.setRowspan(2);
				pdfTable.addCell(cell);
				pdfTable.addCell(new PdfPCell(new Phrase("数量（张）", fontChinese)));
				seatQuantitySum = seatQuantitySum + vo.getSeatQuantity();
				pdfTable.addCell(new PdfPCell(new Phrase(vo.getSeatQuantity() + "")));
				staffQuantitySum = staffQuantitySum + vo.getStaffQuantity();
				pdfTable.addCell(new PdfPCell(new Phrase(vo.getStaffQuantity() + "")));
				protectQuantitySum = protectQuantitySum + vo.getProtectQuantity();
				pdfTable.addCell(new PdfPCell(new Phrase(vo.getProtectQuantity() + "")));
				vendibilityQuantitySum = vendibilityQuantitySum + vo.getVendibilityQuantity();
				pdfTable.addCell(new PdfPCell(new Phrase(vo.getVendibilityQuantity() + "")));

				pdfTable.addCell(new PdfPCell(new Phrase("金额（元）", fontChinese)));
				seatAmountSum = seatAmountSum.add(vo.getSeatAmount());
				pdfTable.addCell(new PdfPCell(new Phrase("￥" + vo.getSeatAmount().toString(), fontChinese)));
				staffAmountSum = staffAmountSum.add(vo.getStaffAmount());
				pdfTable.addCell(new PdfPCell(new Phrase("￥" + vo.getStaffAmount().toString(), fontChinese)));
				protectAmountSum = protectAmountSum.add(vo.getProtectAmount());
				pdfTable.addCell(new PdfPCell(new Phrase("￥" + vo.getProtectAmount().toString(), fontChinese)));
				vendibilityAmountSum = vendibilityAmountSum.add(vo.getVendibilityAmount());
				pdfTable.addCell(new PdfPCell(new Phrase("￥" + vo.getVendibilityAmount().toString(), fontChinese)));
			}
			// 总计行
			PdfPCell cell = new PdfPCell(new Phrase("总计", fontChinese));
			cell.setRowspan(2);
			pdfTable.addCell(cell);
			pdfTable.addCell(new PdfPCell(new Phrase("数量（张）", fontChinese)));
			pdfTable.addCell(new PdfPCell(new Phrase(seatQuantitySum + "")));
			pdfTable.addCell(new PdfPCell(new Phrase(staffQuantitySum + "")));
			pdfTable.addCell(new PdfPCell(new Phrase(protectQuantitySum + "")));
			pdfTable.addCell(new PdfPCell(new Phrase(vendibilityQuantitySum + "")));

			pdfTable.addCell(new PdfPCell(new Phrase("金额（元）", fontChinese)));
			pdfTable.addCell(new PdfPCell(new Phrase("￥" + seatAmountSum.toString(), fontChinese)));
			pdfTable.addCell(new PdfPCell(new Phrase("￥" + staffAmountSum.toString(), fontChinese)));
			pdfTable.addCell(new PdfPCell(new Phrase("￥" + protectAmountSum.toString(), fontChinese)));
			pdfTable.addCell(new PdfPCell(new Phrase("￥" + vendibilityAmountSum.toString(), fontChinese)));
		}

	}

}
