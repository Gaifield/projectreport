package cn.damai.boss.projectreport.report.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.enums.SaleModeEnum;
import cn.damai.boss.projectreport.report.service.SaleDetailService;
import cn.damai.boss.projectreport.report.service.SaleService;
import cn.damai.boss.projectreport.report.vo.DisaccountVo;
import cn.damai.boss.projectreport.report.vo.PriceCellVo;
import cn.damai.boss.projectreport.report.vo.PriceVo;
import cn.damai.boss.projectreport.report.vo.SaleFilterVo;
import cn.damai.boss.projectreport.report.vo.SaleRowVo;
import cn.damai.boss.projectreport.report.vo.SaleVo;

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
 * 注释：出票汇总service实现 作者：liutengfei 【刘腾飞】 时间：14-3-14 上午10:29
 */
@Service
public class SaleServiceImpl implements SaleService {

	/**
	 * 出票汇总明细service
	 */
	@Resource
	private SaleDetailService saleDetailService;

	/**
	 * 查询出票汇总
	 * 
	 * @param filterVo
	 *            过滤参数vo
	 * @return
	 */
	@Override
	public SaleVo querySale(SaleFilterVo filterVo) {
		// 查询出票汇总信息
		List<SaleVo> saleVoList = saleDetailService.querySaleDetail(filterVo);
		// 得到所有折扣
		List<String> allDisaccountList = getAllDisaccountList(saleVoList);

		// 出票汇总vo
		SaleVo saleVo = new SaleVo();
		// 设置折扣
		List<String> disaccountName = saleDetailService.getDisaccountName(allDisaccountList);
		saleVo.setAllDisaccountVoList(disaccountName);

		// 得到项目所有的单票价格
		List<PriceVo> singlePriceVoList = getPriceVo(saleVoList, SaleModeEnum.Single.getCode());
		// 设置单票折扣信息
		setAllDisaccount(saleVo, singlePriceVoList, allDisaccountList);
		// 设置单票汇总信息
		setSaleVo(saleVo, saleVoList, singlePriceVoList, SaleModeEnum.Single.getCode());

		// 得到项目所有的套票价格
		List<PriceVo> promotionPriceVoList = getPriceVo(saleVoList, SaleModeEnum.Promotion.getCode());
		// 设置套票折扣信息
		setAllDisaccount(saleVo, promotionPriceVoList, allDisaccountList);
		// 设置套票汇总信息
		setSaleVo(saleVo, saleVoList, promotionPriceVoList, SaleModeEnum.Promotion.getCode());

		// 设置总计
		List<SaleRowVo> saleRowVoList = saleVo.getSaleRowVoList();
		if (saleRowVoList != null && saleRowVoList.size() != 0) {
			setPriceTotal(saleVo, saleRowVoList);
		}

		// 设置工作票数量、工作票数量、总数量、总金额
		setQuantityAndAmount(saleVo, saleVoList);

		// 设置项目名称
		if (saleVoList != null && saleVoList.size() != 0) {
			String projectName = saleVoList.get(0).getProjectName();
			saleVo.setProjectName(projectName);
		}
		return saleVo;
	}

	/**
	 * 设置所有的折扣信息
	 * 
	 * @param saleVo
	 *            出票汇总信息
	 * @param priceVoList
	 *            价格信息
	 * @param allDisaccountList
	 *            所有折扣信息
	 */
	private void setAllDisaccount(SaleVo saleVo, List<PriceVo> priceVoList, List<String> allDisaccountList) {
		List<SaleRowVo> saleRowVoList = saleVo.getSaleRowVoList();
		if (saleRowVoList == null) {
			saleRowVoList = new ArrayList<SaleRowVo>();
		}
		for (PriceVo priceVo : priceVoList) {
			SaleRowVo saleRowVo = new SaleRowVo();
			saleRowVo.setPriceVo(priceVo);
			List<DisaccountVo> disaccountVoList = new ArrayList<DisaccountVo>();
			for (String name : allDisaccountList) {
				DisaccountVo vo = new DisaccountVo();
				vo.setDisaccountName(name);
				vo.setQuantity(0);
				vo.setAmount(BigDecimal.ZERO);
				disaccountVoList.add(vo);
			}
			saleRowVo.setDisaccountVoList(disaccountVoList);
			saleRowVoList.add(saleRowVo);
		}
		saleVo.setSaleRowVoList(saleRowVoList);
	}

	/**
	 * 设置出票汇总信息
	 * 
	 * @param saleVo
	 *            出票汇总信息
	 * @param saleVoList
	 *            出票汇总明细信息
	 * @param priceVoList
	 *            价格信息
	 * @param code
	 *            0:总计，1：单票，2：套票
	 */
	private void setSaleVo(SaleVo saleVo, List<SaleVo> saleVoList, List<PriceVo> priceVoList, int code) {
		List<SaleRowVo> saleRowVoList = saleVo.getSaleRowVoList();
		if (saleRowVoList == null) {
			saleRowVoList = new ArrayList<SaleRowVo>();
		}
		for (SaleVo saleVoTemp : saleVoList) {
			Map<String, SaleRowVo> srcRowVoMap = getSaleRowVoMap(saleVoTemp.getSaleRowVoList(), code);
			Map<String, SaleRowVo> tgtRowVoMap = getSaleRowVoMap(saleVo.getSaleRowVoList(), code);
			if (srcRowVoMap == null || srcRowVoMap.size() == 0) {
				continue;
			}
			for (PriceVo priceVo : priceVoList) {
				String showName = priceVo.getPriceShowName();
				SaleRowVo srcRowVo = srcRowVoMap.get(showName);
				SaleRowVo tgtRowVo = tgtRowVoMap.get(showName);
				if (srcRowVo == null) {
					continue;
				}
				if (tgtRowVo == null) {
					tgtRowVo = new SaleRowVo();
					tgtRowVo.setPriceVo(priceVo);
				}
				saleDetailService.setRowVo(srcRowVo, tgtRowVo);
				setRowVo(saleRowVoList, tgtRowVo, priceVo);
			}

			saleVo.setSaleRowVoList(saleRowVoList);
		}
	}

	/**
	 * 设置总计
	 * 
	 * @param saleVo
	 * @param saleRowVoList
	 */
	private void setPriceTotal(SaleVo saleVo, List<SaleRowVo> saleRowVoList) {
		if (saleRowVoList == null || saleRowVoList.size() == 0) {
			return;
		}
		PriceVo priceVo = new PriceVo();
		priceVo.setPriceName("总计");
		priceVo.setTicketType((short) 0);

		SaleRowVo tgtRowVo = new SaleRowVo();
		tgtRowVo.setPriceVo(priceVo);
		for (SaleRowVo srcRowVo : saleRowVoList) {
			saleDetailService.setRowVo(srcRowVo, tgtRowVo);
		}
		saleRowVoList.add(tgtRowVo);
		saleVo.setSaleRowVoList(saleRowVoList);
	}

	/**
	 * 设置工作票数量、工作票数量、总数量、总金额
	 * 
	 * @param saleVo
	 * @param saleVoList
	 */
	private void setQuantityAndAmount(SaleVo saleVo, List<SaleVo> saleVoList) {
		for (SaleVo saleVoTemp : saleVoList) {
			// 工作票数量
			int staffQuantity = saleVo.getStaffQuantity();
			staffQuantity += saleVoTemp.getStaffQuantity();

			// 工作票数量
			int protectQuantity = saleVo.getProtectQuantity();
			protectQuantity += saleVoTemp.getProtectQuantity();

			// 总数量
			int totalQuantity = saleVo.getTotalQuantity();
			totalQuantity += saleVoTemp.getTotalQuantity();

			// 总金额
			BigDecimal totalAmount = saleVo.getTotalAmount();
			BigDecimal amount = saleVoTemp.getTotalAmount();
			totalAmount = totalAmount == null ? BigDecimal.ZERO : totalAmount;
			amount = amount == null ? BigDecimal.ZERO : amount;
			totalAmount = totalAmount.add(amount);

			// 设置数据
			saleVo.setStaffQuantity(staffQuantity);
			saleVo.setProtectQuantity(protectQuantity);
			saleVo.setTotalQuantity(totalQuantity);
			saleVo.setTotalAmount(totalAmount);
		}
	}

	/**
	 * 设置行数据
	 * 
	 * @param saleRowVoList
	 * @param priceVo
	 * @param tgtRowVo
	 */
	private void setRowVo(List<SaleRowVo> saleRowVoList, SaleRowVo tgtRowVo, PriceVo priceVo) {
		for (SaleRowVo saleRowVo : saleRowVoList) {
			PriceVo vo = saleRowVo.getPriceVo();
			if (vo != null && vo.equals(priceVo)) {
				saleRowVo = tgtRowVo;
			}
		}
	}

	/**
	 * 得到单票或套票价格
	 * 
	 * @param saleVoList
	 * @param code
	 * @return
	 */
	private List<PriceVo> getPriceVo(List<SaleVo> saleVoList, int code) {
		Map<String, PriceVo> priceVoMap = new HashMap<String, PriceVo>();
		for (SaleVo saleVo : saleVoList) {
			List<SaleRowVo> saleRowVoList = saleVo.getSaleRowVoList();
			for (SaleRowVo rowVo : saleRowVoList) {
				PriceVo priceVo = rowVo.getPriceVo();
				String showName = priceVo.getPriceShowName();
				if (priceVo.getTicketType() == code && !priceVoMap.containsKey(showName)) {
					priceVoMap.put(showName, priceVo);
				}
			}
		}

		List<PriceVo> priceVoList = new ArrayList<PriceVo>();
		Set<String> showNameSet = priceVoMap.keySet();
		for (String showName : showNameSet) {
			priceVoList.add(priceVoMap.get(showName));
		}

		// 排序
		Collections.sort(priceVoList, new Comparator<PriceVo>() {
			@Override
			public int compare(PriceVo src, PriceVo tgt) {
				BigDecimal srcPrice = src.getPrice();
				BigDecimal tgtPrice = tgt.getPrice();
				return tgtPrice.compareTo(srcPrice);
			}
		});

		return priceVoList;
	}

	/**
	 * 得到SaleRowVo对应的map
	 * <p>
	 * key：价格名称 value：SaleRowVo
	 * </p>
	 * 
	 * @param saleRowVoList
	 * @param code
	 * @return
	 */
	private Map<String, SaleRowVo> getSaleRowVoMap(List<SaleRowVo> saleRowVoList, int code) {
		Map<String, SaleRowVo> saleRowVoMap = new HashMap<String, SaleRowVo>();
		if (saleRowVoList == null || saleRowVoList.size() == 0) {
			return saleRowVoMap;
		}
		for (SaleRowVo rowVo : saleRowVoList) {
			PriceVo priceVo = rowVo.getPriceVo();
			if (priceVo != null && priceVo.getTicketType() == code) {
				String showName = priceVo.getPriceShowName();
				saleRowVoMap.put(showName, rowVo);
			}
		}
		return saleRowVoMap;
	}

	/**
	 * 得到所有的折扣
	 * 
	 * @param saleVoList
	 * @return
	 */
	private List<String> getAllDisaccountList(List<SaleVo> saleVoList) {
		Set<String> disaccountSet = new HashSet<String>();
		for (SaleVo saleVo : saleVoList) {
			List<SaleRowVo> saleRowVoList = saleVo.getSaleRowVoList();
			for (SaleRowVo saleRowVo : saleRowVoList) {
				List<DisaccountVo> disaccountVoList = saleRowVo.getDisaccountVoList();
				if (disaccountVoList == null || disaccountVoList.size() == 0) {
					continue;
				}
				for (DisaccountVo vo : disaccountVoList) {
					disaccountSet.add(vo.getDisaccountName());
				}
			}
		}
		List<String> disaccountList = new ArrayList<String>(disaccountSet);
		Collections.sort(disaccountList);
		return disaccountList;
	}

	/**
	 * 出票汇总excel导出
	 * 
	 * @param saleDetailList
	 * @param projectName
	 * @return
	 * @throws ApplicationException
	 * @author：guwei 【顾炜】 2014-3-14 下午7:02:36
	 */
	@Override
	public ByteArrayOutputStream outExcel(List<SaleVo> saleDetailList, String projectName, boolean isHuiZong) throws ApplicationException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		// 1.0 创建一个excel
		HSSFWorkbook hwb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		String reportName = "出票明细表";
		HSSFSheet sheet = hwb.createSheet(reportName);
		HSSFFont font = hwb.createFont();
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = null;
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = hwb.createCellStyle();
		HSSFCellStyle style2 = hwb.createCellStyle();

		HSSFCell cell = null;
		int z = 0;
		if (saleDetailList != null) {
			for (int i = 0; i < saleDetailList.size(); i++) {
				SaleVo saleVo = saleDetailList.get(i);
				int size = saleVo.getAllDisaccountVoList().size();
				if (!isHuiZong) {
					row = sheet.createRow(z++);
					for (int j = 0; j < 9 + size; j++) {
						cell = row.createCell(j);
						cell.setCellValue(saleDetailList.get(i).getPerformName() + " ---- " + saleDetailList.get(i).getPerformTime());
						cell.setCellStyle(style);
					}
					sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 0, 8 + size));
				}

				// 定义表头
				List<String> allDisaccountVoList = saleDetailList.get(i).getAllDisaccountVoList();

				// 列宽
				int priceCol = allDisaccountVoList.size() + 9;
				// 定义表头样式
				createCellColor(style, sheet, priceCol, font);

				createTitle(sheet, z++, row, cell, style, allDisaccountVoList);
				// createCellColor(style4, sheet, 6, font);

				// row = sheet.createRow(z++);
				List<SaleRowVo> saleRowVoList = saleVo.getSaleRowVoList();

				// 定义body样式
				createBorder(style2, sheet, priceCol);
				createTable(saleRowVoList, sheet, style, style2, row, cell, z++);

				int length = saleRowVoList.size();
				z += length * 2;

				row = sheet.createRow(z++);
				cell = row.createCell(0);
				cell.setCellValue("附加出票");
				cell.setCellStyle(style);

				// 工作票
				cell = row.createCell(1);
				cell.setCellValue("工作票(张)");
				cell.setCellStyle(style);

				for (int j = 2; j < 3 + size; j++) {
					// 工作票张数
					cell = row.createCell(j);
					cell.setCellValue(saleVo.getStaffQuantity() + "");
					cell.setCellStyle(style2);
				}
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 2, 2 + size));

				// 防涨票
				cell = row.createCell(3 + size);
				cell.setCellValue("防涨票(张)");
				cell.setCellStyle(style);

				cell = row.createCell(4 + size);
				cell.setCellValue(saleVo.getProtectQuantity() + "");
				cell.setCellStyle(style2);
				cell = row.createCell(5 + size);
				cell.setCellValue(saleVo.getProtectQuantity() + "");
				cell.setCellStyle(style2);
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 4 + size, 5 + size));

				cell = row.createCell(6 + size);
				cell.setCellStyle(style);
				cell.setCellValue("注：附加出票为场馆、公安工作票、防涨票出票");
				cell = row.createCell(7 + size);
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 6 + size, 8 + size));
				cell.setCellStyle(style);
				cell = row.createCell(8 + size);
				cell.setCellStyle(style);
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 6 + size, 8 + size));

				row = sheet.createRow(z++);
				cell = row.createCell(0);
				cell.setCellStyle(style);
				cell.setCellValue("总计出票数量(张)");
				cell = row.createCell(1);
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 0, 1));
				cell.setCellStyle(style);
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 0, 1));

				for (int j = 2; j < 3 + size; j++) {
					cell = row.createCell(j);
					cell.setCellValue(saleVo.getTotalQuantity() + "");
					cell.setCellStyle(style2);
				}
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 2, 2 + size));

				cell = row.createCell(2);
				cell.setCellValue(saleVo.getTotalQuantity() + "");
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 2, 2 + size));
				cell.setCellStyle(style2);

				cell = row.createCell(3 + size);
				cell.setCellStyle(style);
				cell.setCellValue("总计出票金额(元)");
				cell = row.createCell(4 + size);
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 3 + size, 4 + size));
				cell.setCellStyle(style);
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 3 + size, 4 + size));

				cell = row.createCell(5 + size);
				cell.setCellStyle(style2);
				cell.setCellValue("￥" + saleVo.getTotalAmount());
				cell = row.createCell(6 + size);
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 5 + size, 8 + size));
				cell.setCellStyle(style2);
				cell = row.createCell(7 + size);
				cell.setCellStyle(style2);
				cell = row.createCell(8 + size);
				cell.setCellStyle(style2);
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 5 + size, 8 + size));

				row = sheet.createRow(z++);
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 0, 8 + size));
				row = sheet.createRow(z++);
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 0, 8 + size));
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
	public void createTitle(HSSFSheet sheet, int startRow, HSSFRow row, HSSFCell cell, HSSFCellStyle style, List<String> discountPriceName) {

		row = sheet.createRow(startRow);
		cell = row.createCell(0);
		cell.setCellValue("票价");
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, (short) 0, (short) 1));
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("可售总票房");
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, (short) 2, (short) 2));
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("出票");
		int size = discountPriceName.size();
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, (short) 3, (short) 5 + size));
		cell.setCellStyle(style);
		cell = row.createCell(6 + size);
		cell.setCellValue("剩余票房");
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, (short) 6 + size, (short) 6 + size));
		cell.setCellStyle(style);
		cell = row.createCell(7 + size);
		cell.setCellValue("预留票房");
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, (short) 7 + size, (short) 7 + size));
		cell.setCellStyle(style);
		cell = row.createCell(8 + size);
		cell.setCellValue("当前可售票房");
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, (short) 8 + size, (short) 8 + size));
		cell.setCellStyle(style);
		// 表头第二行
		row = sheet.createRow(startRow + 1);
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell = row.createCell(6 + size);
		cell.setCellStyle(style);
		cell = row.createCell(7 + size);
		cell.setCellStyle(style);
		cell = row.createCell(8 + size);
		cell.setCellStyle(style);
		for (int i = 0; i < size; i++) {
			cell = row.createCell(3 + i);
			cell.setCellValue(discountPriceName.get(i));
			cell.setCellStyle(style);
		}
		cell = row.createCell(3 + size);
		cell.setCellValue("赠票出票");
		cell.setCellStyle(style);
		cell = row.createCell(4 + size);
		cell.setCellValue("工作票出票");
		cell.setCellStyle(style);
		cell = row.createCell(5 + size);
		cell.setCellValue("小计");
		cell.setCellStyle(style);

	}

	// 导入excel表格数据,包括总计行
	public void createTable(List<SaleRowVo> lists, HSSFSheet sheet, HSSFCellStyle style, HSSFCellStyle style2, HSSFRow row, HSSFCell cell,
			int startRow) {
		int z = startRow + 1;
		if (lists != null && lists.size() != 0) {
			for (int i = 0; i < lists.size(); i++) {
				// 第一行
				row = sheet.createRow(z++);
				cell = row.createCell(0);
				SaleRowVo vo = lists.get(i);

				// 票价/*vo.getPriceVo().getPriceName()*/
				cell.setCellValue(vo.getPriceVo().getPriceShowName());
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z, 0, 0));
				cell.setCellStyle(style2);
				// 数量
				cell = row.createCell(1);
				cell.setCellValue("数量");
				cell.setCellStyle(style2);

				// 可售总票房
				PriceCellVo availableTotalSale = vo.getPriceTotalSale();
				cell = row.createCell(2);
				cell.setCellValue(availableTotalSale == null ? 0 : availableTotalSale.getQuantity());// 可售总票房数量
				cell.setCellStyle(style2);

				// 折扣
				List<DisaccountVo> disaccountVoList = vo.getDisaccountVoList();
				int size = disaccountVoList.size();
				for (int j = 0; j < size; j++) {
					cell = row.createCell(3 + j);
					DisaccountVo disaccountVo = disaccountVoList.get(j);
					cell.setCellValue(disaccountVo == null ? 0 : disaccountVo.getQuantity());// 折扣数量
					cell.setCellStyle(style2);

				}
				// 赠票出票
				PriceCellVo presentSale = vo.getPresentSale();
				cell = row.createCell(3 + size);
				cell.setCellValue(presentSale == null ? 0 : presentSale.getQuantity());// 赠票出票数量
				cell.setCellStyle(style2);

				// 工作票出票
				PriceCellVo staffSale = vo.getStaffSale();
				cell = row.createCell(3 + size + 1);
				cell.setCellValue(staffSale == null ? 0 : staffSale.getQuantity());// 工作票出票数量
				cell.setCellStyle(style2);

				// 小计
				PriceCellVo totalSaleCell = vo.getTotalSaleCell();
				cell = row.createCell(3 + size + 2);
				cell.setCellValue(totalSaleCell == null ? 0 : totalSaleCell.getQuantity());// 小计数量
				cell.setCellStyle(style2);

				// 剩余票房
				PriceCellVo leftSale = vo.getLeftSale();
				cell = row.createCell(3 + size + 3);
				cell.setCellValue(leftSale == null ? 0 : leftSale.getQuantity());// 剩余票房数量
				cell.setCellStyle(style2);

				// 预留票房
				PriceCellVo reserveSale = vo.getReserveSale();
				cell = row.createCell(3 + size + 4);
				cell.setCellValue(reserveSale == null ? 0 : reserveSale.getQuantity());// 预留票房数量
				cell.setCellStyle(style2);

				// 当前可售票房
				PriceCellVo availableSale = vo.getAvailableSale();
				cell = row.createCell(3 + size + 5);
				cell.setCellValue(availableSale == null ? 0 : availableSale.getQuantity());// 当前可售票房数量
				cell.setCellStyle(style2);

				/*********************************************************************************************/
				// 新建行，第二行
				row = sheet.createRow(z++);
				// 金额
				cell = row.createCell(1);
				cell.setCellValue("金额");
				cell.setCellStyle(style2);

				cell = row.createCell(2);
				cell.setCellValue(availableTotalSale == null ? "￥0" : "￥" + availableTotalSale.getAmount().toString());// 可售总票房金额
				cell.setCellStyle(style2);

				for (int j = 0; j < size; j++) {
					cell = row.createCell(3 + j);
					DisaccountVo disaccountVo = disaccountVoList.get(j);
					cell.setCellValue(disaccountVo == null ? "￥0" : "￥" + disaccountVo.getAmount().toString());// 折扣金额
					cell.setCellStyle(style2);
				}

				// 赠票出票
				cell = row.createCell(3 + size);
				cell.setCellValue(presentSale == null ? "￥0" : "￥" + presentSale.getAmount().toString());// 赠票出票金额
				cell.setCellStyle(style2);

				// 工作票出票
				cell = row.createCell(3 + size + 1);
				cell.setCellValue(staffSale == null ? "￥0" : "￥" + staffSale.getAmount().toString());// 工作票出票金额
				cell.setCellStyle(style2);

				// 小计
				cell = row.createCell(3 + size + 2);
				cell.setCellValue(totalSaleCell == null ? "￥0" : "￥" + totalSaleCell.getAmount().toString());// 小计金额
				cell.setCellStyle(style2);

				// 剩余票房
				cell = row.createCell(3 + size + 3);
				cell.setCellValue(leftSale == null ? "￥0" : "￥" + leftSale.getAmount().toString());// 剩余票房金额
				cell.setCellStyle(style2);

				// 预留票房
				cell = row.createCell(3 + size + 4);
				cell.setCellValue(reserveSale == null ? "￥0" : "￥" + reserveSale.getAmount().toString());// 预留票房金额
				cell.setCellStyle(style2);

				// 当前可售票房
				cell = row.createCell(3 + size + 5);
				cell.setCellValue(availableSale == null ? "￥0" : "￥" + availableSale.getAmount().toString()); // 当前可售票房金额
				cell.setCellStyle(style2);
			}

		}
	}

	// 字体加粗
	private void createFontWeight(HSSFCellStyle style, HSSFSheet sheet, int col, HSSFFont font) {
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		createBorder(style, sheet, col);
	}

	// 设置excel四周边框,列宽,加粗。用于总计行
	private void createCellColor(HSSFCellStyle style, HSSFSheet sheet, int col, HSSFFont font) {
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		createFontWeight(style, sheet, col, font);
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

	/**
	 * pdf 导出
	 * 
	 * @param saleDetailList
	 * @param projectName
	 * @return
	 * @throws ApplicationException
	 * @author：guwei 【顾炜】 2014-3-17 下午2:17:07
	 */
	@Override
	public ByteArrayOutputStream outPdf(List<SaleVo> saleDetailList, String projectName, boolean isHuiZong) throws ApplicationException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Document pdfDoc = new Document(PageSize.A4, 50, 50, 50, 50);

		try {
			BaseFont bfChinese = BaseFont.createFont("/SIMHEI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Font fontChinese = new Font(bfChinese, 10, Font.NORMAL, BaseColor.BLACK);
			Font apaceRow = new Font(bfChinese, 10, Font.UNDEFINED, BaseColor.WHITE);
			PdfWriter.getInstance(pdfDoc, output);
			pdfDoc.open();

			if (saleDetailList != null) {
				for (int i = 0; i < saleDetailList.size(); i++) {
					SaleVo saleVo = saleDetailList.get(i);
					List<String> allDisaccountVoList = saleVo.getAllDisaccountVoList();
					int size = allDisaccountVoList.size();
					PdfPTable pdfTable = new PdfPTable(size + 9);
					pdfTable.setWidthPercentage(110);

					PdfPCell cell = null;

					if (!isHuiZong) {
						String title = saleVo.getPerformName() + "----" + saleVo.getPerformTime();
						cell = new PdfPCell(new Phrase(title, fontChinese));
						cell.setColspan(9 + size);
						pdfTable.addCell(cell);
					}

					createPdfTitle(pdfTable, fontChinese, allDisaccountVoList);
					createPdf(saleVo.getSaleRowVoList(), pdfTable, fontChinese);

					cell = new PdfPCell(new Phrase("附件出票", fontChinese));
					cell.setColspan(2);
					pdfTable.addCell(cell);

					// 工作票
					cell = new PdfPCell(new Phrase("工作票(张)", fontChinese));
					pdfTable.addCell(cell);

					cell = new PdfPCell(new Phrase(saleVo.getStaffQuantity() + "", fontChinese));
					cell.setColspan(size);
					pdfTable.addCell(cell);
					// 防涨票

					cell = new PdfPCell(new Phrase("防涨票(张)", fontChinese));
					cell.setColspan(2);
					pdfTable.addCell(cell);

					cell = new PdfPCell(new Phrase(saleVo.getProtectQuantity() + "", fontChinese));
					pdfTable.addCell(cell);

					cell = new PdfPCell(new Phrase("注：附加出票为场馆、公安工作票、防涨票出票", fontChinese));
					cell.setColspan(4);
					pdfTable.addCell(cell);

					cell = new PdfPCell(new Phrase("总计出票数量(张)", fontChinese));
					cell.setColspan(2);
					pdfTable.addCell(cell);

					cell = new PdfPCell(new Phrase(saleVo.getTotalQuantity() + "", fontChinese));
					cell.setColspan(size + 1);
					pdfTable.addCell(cell);

					cell = new PdfPCell(new Phrase("总计出票金额(元)", fontChinese));
					cell.setColspan(2);
					pdfTable.addCell(cell);

					cell = new PdfPCell(new Phrase("￥" + saleVo.getTotalAmount(), fontChinese));
					cell.setColspan(4);
					pdfTable.addCell(cell);

					cell = new PdfPCell(new Phrase(" ", apaceRow));
					cell.setColspan(9 + size);
					pdfTable.addCell(cell);

					pdfDoc.add(pdfTable);
				}
			}

			pdfDoc.close();

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return output;
	}

	// 导入pdf表头
	public void createPdfTitle(PdfPTable pdfTable, Font fontChinese, List<String> discountPriceName) {
		PdfPCell cell = new PdfPCell(new Phrase("票价", fontChinese));
		cell.setColspan(2);
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pdfTable.addCell(cell);

		cell = new PdfPCell(new Phrase("可售总票房", fontChinese));
		cell.setRowspan(2);
		pdfTable.addCell(cell);

		cell = new PdfPCell(new Phrase("出票", fontChinese));
		int size = discountPriceName.size();
		cell.setColspan(size + 3);
		pdfTable.addCell(cell);

		cell = new PdfPCell(new Phrase("剩余票房", fontChinese));
		cell.setRowspan(2);
		pdfTable.addCell(cell);

		cell = new PdfPCell(new Phrase("预留票房", fontChinese));
		cell.setRowspan(2);
		pdfTable.addCell(cell);

		cell = new PdfPCell(new Phrase("当前可售票房", fontChinese));
		cell.setRowspan(2);
		pdfTable.addCell(cell);

		for (String string : discountPriceName) {
			pdfTable.addCell(new PdfPCell(new Phrase(string, fontChinese)));
		}
		pdfTable.addCell(new PdfPCell(new Phrase("赠票出票", fontChinese)));
		pdfTable.addCell(new PdfPCell(new Phrase("工作票出票", fontChinese)));
		pdfTable.addCell(new PdfPCell(new Phrase("小计", fontChinese)));

	}

	//
	public void createPdf(List<SaleRowVo> lists, PdfPTable pdfTable, Font fontChinese) {

		if (lists != null && lists.size() > 0) {
			for (SaleRowVo saleRowVo : lists) {

				// 第一行
				// 票价
				PdfPCell cell = new PdfPCell(new Phrase(saleRowVo.getPriceVo().getPriceName(), fontChinese));
				cell.setRowspan(2);
				pdfTable.addCell(cell);

				// 数量
				cell = new PdfPCell(new Phrase("数量(张)", fontChinese));
				pdfTable.addCell(cell);

				// 可售总票房
				PriceCellVo availableTotalSale = saleRowVo.getPriceTotalSale();
				cell = new PdfPCell(new Phrase(availableTotalSale == null ? "0" : availableTotalSale.getQuantity() + "", fontChinese));// 可售总票房数量
				pdfTable.addCell(cell);

				// 折扣
				List<DisaccountVo> disaccountVoList = saleRowVo.getDisaccountVoList();
				for (DisaccountVo disaccountVo : disaccountVoList) {

					cell = new PdfPCell(new Phrase(disaccountVo == null ? "0" : disaccountVo.getQuantity() + "", fontChinese));// 折扣数量
					pdfTable.addCell(cell);
				}
				// 赠票出票
				PriceCellVo presentSale = saleRowVo.getPresentSale();
				cell = new PdfPCell(new Phrase(presentSale == null ? "0" : presentSale.getQuantity() + "", fontChinese));// 赠票出票数量
				pdfTable.addCell(cell);

				// 工作票出票
				PriceCellVo staffSale = saleRowVo.getStaffSale();
				cell = new PdfPCell(new Phrase(staffSale == null ? "0" : staffSale.getQuantity() + "", fontChinese));// 工作票出票数量
				pdfTable.addCell(cell);

				// 小计
				PriceCellVo totalSaleCell = saleRowVo.getTotalSaleCell();
				cell = new PdfPCell(new Phrase(totalSaleCell == null ? "0" : totalSaleCell.getQuantity() + "", fontChinese));// 小计数量
				pdfTable.addCell(cell);

				// 剩余票房
				PriceCellVo leftSale = saleRowVo.getLeftSale();
				cell = new PdfPCell(new Phrase(leftSale == null ? "0" : leftSale.getQuantity() + "", fontChinese)); // 剩余票房数量
				pdfTable.addCell(cell);

				// 预留票房
				PriceCellVo reserveSale = saleRowVo.getReserveSale();
				cell = new PdfPCell(new Phrase(reserveSale == null ? "0" : reserveSale.getQuantity() + "", fontChinese)); // 预留票房数量
				pdfTable.addCell(cell);

				// 当前可售票房
				PriceCellVo availableSale = saleRowVo.getAvailableSale();
				cell = new PdfPCell(new Phrase(availableSale == null ? "0" : availableSale.getQuantity() + "", fontChinese)); // 当前可售票房数量
				pdfTable.addCell(cell);

				// 新建行，第二行
				// 金额
				cell = new PdfPCell(new Phrase("金额(元)", fontChinese));
				pdfTable.addCell(cell);

				cell = new PdfPCell(new Phrase(availableTotalSale == null ? "￥0" : "￥" + availableTotalSale.getAmount(), fontChinese)); // 可售总票房金额
				pdfTable.addCell(cell);

				for (DisaccountVo disaccountVo : disaccountVoList) {
					cell = new PdfPCell(new Phrase(disaccountVo == null ? "￥0" : "￥" + disaccountVo.getAmount(), fontChinese)); // 折扣金额
					pdfTable.addCell(cell);
				}

				// 赠票出票
				cell = new PdfPCell(new Phrase(presentSale == null ? "￥0" : "￥" + presentSale.getAmount(), fontChinese)); // 赠票出票金额
				pdfTable.addCell(cell);

				// 工作票出票
				cell = new PdfPCell(new Phrase(staffSale == null ? "￥0" : "￥" + staffSale.getAmount(), fontChinese)); // 工作票出票金额
				pdfTable.addCell(cell);

				// 小计
				cell = new PdfPCell(new Phrase(totalSaleCell == null ? "￥0" : "￥" + totalSaleCell.getAmount(), fontChinese)); // 小计金额
				pdfTable.addCell(cell);

				// 剩余票房
				cell = new PdfPCell(new Phrase(leftSale == null ? "￥0" : "￥" + leftSale.getAmount(), fontChinese)); // 剩余票房金额
				pdfTable.addCell(cell);

				// 预留票房
				cell = new PdfPCell(new Phrase(reserveSale == null ? "￥0" : "￥" + reserveSale.getAmount(), fontChinese)); // 预留票房金额
				pdfTable.addCell(cell);

				// 当前可售票房
				cell = new PdfPCell(new Phrase(availableSale == null ? "￥0" : "￥" + availableSale.getAmount().toString(), fontChinese)); // 当前可售票房金额
				pdfTable.addCell(cell);

			}
		}
	}
}
