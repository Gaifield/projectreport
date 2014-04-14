package cn.damai.boss.projectreport.report.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.utils.NumberUtils;
import cn.damai.boss.projectreport.report.dao.MaitixPerformDAO;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.service.ProjectReportStandStatService;
import cn.damai.boss.projectreport.report.vo.PerformStandStatVo;
import cn.damai.boss.projectreport.report.vo.PerformVo;

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
 * 分区出票报表统计接口实现类
 * 
 * @ClassName: ProjectReportStandStatServiceImpl
 * @Description:
 * @author zhangbinghong
 * @date 2014-2-27 下午2:12:31
 */
@Service
public class ProjectReportStandStatServiceImpl implements
		ProjectReportStandStatService {
	private final static String[] STANDSTATTITLES = new String[] { "看台名称",
			"总座位数量", "防涨票数量", "出票数量", "剩余可用数量", "上座率" };

	@Resource
	private MaitixPerformDAO maitixPerformDAO;

	@Override
	public PageResultData findProjectReportStandStatList(String source,long projectInfoId,
			List<Long> performIds, int pageSize, int pageIndex)
			throws ApplicationException {
		DynamicDataSourceHolder.putDataSourceName(source);
		
		// 1、分页查询项目场次数据
		PageResultData<PerformVo> pageData = maitixPerformDAO.queryPerformList(
				projectInfoId, performIds, null, null, pageSize, pageIndex);

		// 2、统计看台总座位数、防涨票数量、出票数量
		if (pageData != null && pageData.getRows() != null) {
			// 统计看台数据
			List<PerformVo> list = pageData.getRows();
			for (PerformVo item : list) {
				List<PerformStandStatVo> standStatVoList = maitixPerformDAO
						.queryPerformStandStat(projectInfoId, Arrays
								.asList(new Long[] { item.getPerformId() }));
				item.setList(standStatVoList);
			}
		}
		return pageData;
	}

	/**
	 * 导出excel表格
	 */
	@Override
	public ByteArrayOutputStream OutExcel(PageResultData pageResult,
			String projectName) throws ApplicationException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		List<PerformVo<PerformStandStatVo>> performListVos = (List<PerformVo<PerformStandStatVo>>) pageResult.getRows();
		HSSFWorkbook hwb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		String reportName = "分区出票统计";
		HSSFSheet sheet = hwb.createSheet(reportName);
		HSSFFont font = hwb.createFont();
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = null;
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = hwb.createCellStyle();
		HSSFCellStyle style2 = hwb.createCellStyle();
		HSSFCellStyle style3 = hwb.createCellStyle();
		HSSFCellStyle style4 = hwb.createCellStyle();
		// 内容居中
		// style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置四周边框
		createBorder(style, sheet, 6);
		createFontWeight(style2, sheet, 6, font);
		createFont(style3, font);
		if (performListVos != null && performListVos.size() != 0) {
			int z = 0;
			row = sheet.createRow(z++);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue(projectName);
			cell.setCellStyle(style3);
			// 合并单元格参数分别是：所需合并的起始行、终止行、起始列、终止列
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
			for (int i = 0; i < performListVos.size(); i++) {
				row = sheet.createRow(z++);
				cell = row.createCell(0);
				// cell.setCellStyle(style);
				PerformVo<PerformStandStatVo> performStandVo = performListVos
						.get(i);
				cell.setCellValue(performStandVo.getPerformName()
						+ "----"
						+ DateFormatUtils.format(
								performStandVo.getPerformTime(),
								"yyyy-MM-dd HH:mm:ss") + "("
						+ performStandVo.getFieldName() + ")");
				cell.setCellStyle(style3);
				// 合并单元格参数分别是：所需合并的起始行、终止行、起始列、终止列
				sheet.addMergedRegion(new CellRangeAddress(z - 1, z - 1, 0, 5));
				row = sheet.createRow(z++);
				// 表头
				createCellColor(style4, sheet, 6, font);
				createCommenCellIndex(row, style4);
				if (performStandVo != null && performStandVo.getList() != null
						&& performStandVo.getList().size() != 0) {
					int seatQuantitySum = 0; // 各个区座位总数
					int protectQuantitySum = 0; // 各个区防涨票数量
					int saleQuantitySum = 0; // 各个区出票数量
					for (int j = 0; j < performStandVo.getList().size(); j++) {
						seatQuantitySum = seatQuantitySum
								+ performStandVo.getList().get(j)
										.getSeatQuantity();
						protectQuantitySum = protectQuantitySum
								+ performStandVo.getList().get(j)
										.getProtectQuantity();
						saleQuantitySum = saleQuantitySum
								+ performStandVo.getList().get(j)
										.getSaleQuantity();
						row = sheet.createRow(z++);
						createCommenCellValue(row, style, performStandVo
								.getList().get(j));
					}
					row = sheet.createRow(z++);
					createCommenCellSum(row, style2, seatQuantitySum,
							protectQuantitySum, saleQuantitySum);
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

	// 字体加粗。用于项目名称，场次标题
	private void createFont(HSSFCellStyle style, HSSFFont font) {
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
	}

	// 设置excel四周边框,列宽,加粗。用于总计行
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

	// 导入表头数据
	private void createCommenCellIndex(HSSFRow row, HSSFCellStyle style) {
		HSSFCell cell = row.createCell(0);
		for (int i = 0; i < STANDSTATTITLES.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(STANDSTATTITLES[i]);
		}
	}

	// 导入总计数据
	private void createCommenCellSum(HSSFRow row, HSSFCellStyle style,
			int seatQuantitySum, int protectQuantitySum, int saleQuantitySum) {
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("总计");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue(seatQuantitySum);
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue(protectQuantitySum);
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue(saleQuantitySum);
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue(seatQuantitySum - protectQuantitySum
				- saleQuantitySum);
		cell.setCellStyle(style);

		cell = row.createCell(5);
		cell.setCellStyle(style);
		if (seatQuantitySum == 0) {
			cell.setCellValue(NumberUtils.percentFormat(0));
		} else {
			cell.setCellValue(NumberUtils.percentFormat(NumberUtils.div(
					saleQuantitySum, seatQuantitySum)));
		}
	}

	private void createCommenCellValue(HSSFRow row, HSSFCellStyle style,
			PerformStandStatVo performVo) {

		HSSFCell cell = row.createCell(0);
		cell.setCellValue(performVo.getStandName());
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue(performVo.getSeatQuantity());
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue(performVo.getProtectQuantity());
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue(performVo.getSaleQuantity());
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue(performVo.getSeatQuantity()
				- performVo.getSaleQuantity() - performVo.getProtectQuantity());
		cell.setCellStyle(style);

		cell = row.createCell(5);
		cell.setCellStyle(style);

		if (performVo.getSaleQuantity() == 0) {
			cell.setCellValue(NumberUtils.percentFormat(0));
		} else {
			cell.setCellValue(NumberUtils.percentFormat(NumberUtils.div(
					performVo.getSaleQuantity(), performVo.getSeatQuantity())));
		}
	}

	/**
	 * 导出pdf文档
	 */
	@Override
	public ByteArrayOutputStream OutPdf(PageResultData pageResult,
			String projectName) throws ApplicationException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		List<PerformVo<PerformStandStatVo>> performListVos = (List<PerformVo<PerformStandStatVo>>) pageResult
				.getRows();

		Document pdfDoc = new Document(PageSize.A4, 50, 50, 50, 50);
		try {
			BaseFont bfChinese = BaseFont.createFont("/SIMHEI.TTF",
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Font fontChinese = new Font(bfChinese, 10, Font.NORMAL,
					BaseColor.BLACK);

			PdfWriter.getInstance(pdfDoc, output);
			pdfDoc.open();
			PdfPTable pdfTable = new PdfPTable(6);
			// 写入项目名称
			PdfPCell cell = new PdfPCell(new Phrase(projectName, fontChinese));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			// 合并横向表格
			cell.setColspan(6);
			pdfTable.addCell(cell);
			if (performListVos != null && performListVos.size() != 0) {
				for (int i = 0; i < performListVos.size(); i++) {
					// 写入项目场次信息
					PerformVo<PerformStandStatVo> performStandVo = performListVos
							.get(i);
					cell = new PdfPCell(new Phrase(
							performStandVo.getPerformName()
									+ "----"
									+ DateFormatUtils.format(
											performStandVo.getPerformTime(),
											"yyyy-MM-dd HH:mm:ss") + "("
									+ performStandVo.getFieldName() + ")",
							fontChinese));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					// 合并横向表格
					cell.setColspan(6);
					pdfTable.addCell(cell);
					// 在次行写入表头
					createPdfCellIndex(pdfTable, fontChinese);
					if (performStandVo != null
							&& performStandVo.getList() != null
							&& performStandVo.getList().size() != 0) {
						int seatQuantitySum = 0; // 各个区座位总数
						int protectQuantitySum = 0; // 各个区防涨票数量
						int saleQuantitySum = 0; // 各个区出票数量
						// 写入表格内部数据
						for (int j = 0; j < performStandVo.getList().size(); j++) {
							seatQuantitySum = seatQuantitySum
									+ performStandVo.getList().get(j)
											.getSeatQuantity();
							protectQuantitySum = protectQuantitySum
									+ performStandVo.getList().get(j)
											.getProtectQuantity();
							saleQuantitySum = saleQuantitySum
									+ performStandVo.getList().get(j)
											.getSaleQuantity();
							createPdfCellValue(pdfTable, performStandVo
									.getList().get(j), fontChinese);
						}
						// 写入总计行
						createPdfCellSum(pdfTable, fontChinese,
								seatQuantitySum, protectQuantitySum,
								saleQuantitySum);
					}
				}
			}
			pdfDoc.add(pdfTable);

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
	private void createPdfCellIndex(PdfPTable table, Font fontChinese) {
		for (String title : STANDSTATTITLES) {
			table.addCell(new PdfPCell(new Phrase(title, fontChinese)));
		}
	}

	// pdf表格总计行
	private void createPdfCellSum(PdfPTable table, Font fontChinese,
			int seatQuantitySum, int protectQuantitySum, int saleQuantitySum) {
		table.addCell(new PdfPCell(new Phrase("总计", fontChinese)));
		table.addCell(seatQuantitySum + "");
		table.addCell(protectQuantitySum + "");
		table.addCell(saleQuantitySum + "");
		table.addCell(seatQuantitySum - protectQuantitySum - saleQuantitySum
				+ "");
		if (seatQuantitySum == 0) {
			table.addCell(NumberUtils.percentFormat(0));
		} else {
			table.addCell(NumberUtils.percentFormat(NumberUtils.div(
					saleQuantitySum, seatQuantitySum)));
		}
	}

	// 导入pdf表格内容
	private void createPdfCellValue(PdfPTable table,
			PerformStandStatVo performVo, Font fontChinese) {
		table.addCell(new Phrase(performVo.getStandName(), fontChinese));
		table.addCell(performVo.getSeatQuantity() + "");
		table.addCell(performVo.getProtectQuantity() + "");
		table.addCell(performVo.getSaleQuantity() + "");
		table.addCell(performVo.getSeatQuantity() - performVo.getSaleQuantity()
				- performVo.getProtectQuantity() + "");
		if (performVo.getSeatQuantity() == 0) {
			table.addCell(NumberUtils.percentFormat(0));
		} else {
			table.addCell(NumberUtils.percentFormat(NumberUtils.div(
					performVo.getSaleQuantity(), performVo.getSeatQuantity())));
		}
	}
}