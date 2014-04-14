package cn.damai.boss.projectreport.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class OutExcelUtil {
	// 分区出票表头
	public static final String[] STANDSTAT_TITLES = new String[] { "看台名称", "总座位数量", "防涨票数量", "出票数量", "剩余可用数量", "上座率" };
	// 分区出票表名
	public static final String STANDSTAT_NAME = "分区出票统计";

	// 渠道销售统计表头
	public static final String[] SELLERSALE_TITLES = new String[] { "票价", "销售", "0票价", "出票总计" };
	// 渠道销售统计表名
	public static final String SELLERSALE_NAME = "渠道销售统计";

	// 预留明细统计表头
	public static final String[] RESERVESTAT_TITLES = new String[] { "价位", "总预留", "1级预留", "2级预留", "3级预留", "4级预留", "5级预留", "6级预留", "7级预留" };
	// 预留明细统计表名
	public static final String RESERVESTAT_NAME = "预留明细统计";

	// 座位汇总表表头
	public static final String[] SEATSTATINFO_TITLES = new String[] { "票价", "使用座位", "工作票", "防涨票", "可售票（总票房）" };
	// 座位汇总表表名
	public static final String SEATSTATINFO_NAME = "座位汇总表";

	// 出票汇总表表头
	public static final String[] SALEREPORT_TITLES = new String[] { "票价", "可售总票房", "出票", "剩余票房", "预留票房", "当前可售票房" };
	// 出票汇总表表名
	public static final String SALEREPORT_NAME = "出票汇总表";

	// 设置excel四周边框,列宽(非加粗字段)。用于循环数据
	private static HSSFCellStyle createBorder(HSSFCellStyle style, HSSFSheet sheet) {
		// 加边框
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		return style;
	}

	// 导出excel表格
	public static ByteArrayOutputStream outExcel(List<List<String>> strList, String reportName, int stratRow) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet(reportName);
		HSSFCellStyle style = hwb.createCellStyle();
		HSSFRow row = null;
		HSSFCell cell = null;
		if (strList != null && strList.size() != 0) {
			int tag = 0;
			String titleName = null;
			for (int i = 0; i < strList.size(); i++) {
				row = sheet.createRow(stratRow + i);
				for (int j = 0; j < strList.get(i).size(); j++) {
					cell = row.createCell(j);
					if (i > 0 && strList.get(i).get(j) == strList.get(i - 1).get(j)) {
						sheet.addMergedRegion(new CellRangeAddress(stratRow + i - 1, stratRow + i, (short) j, (short) j));
						cell.setCellStyle(createBorder(style, sheet));
					}
					if (!strList.get(i).get(j).equals(titleName)) {
						cell.setCellValue(strList.get(i).get(j));
						cell.setCellStyle(createBorder(style, sheet));
						titleName = strList.get(i).get(j);
						tag = j;
					} else {
						sheet.addMergedRegion(new CellRangeAddress(stratRow + i, stratRow + i, (short) tag, (short) j));
						cell.setCellStyle(createBorder(style, sheet));
					}
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

	// 导出pdf文档
	public static ByteArrayOutputStream outPdf(List<List<String>> strList, String reportName, int stratRow) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Document pdfDoc = new Document(PageSize.A4, 50, 50, 50, 50);
		BaseFont bfChinese;
		try {
			bfChinese = BaseFont.createFont("/SIMHEI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Font fontChinese = new Font(bfChinese, 10, Font.NORMAL, BaseColor.BLACK);
			PdfWriter.getInstance(pdfDoc, output);
			pdfDoc.open();
			if (strList != null && strList.size() != 0) {
				PdfPTable pdfTable = new PdfPTable(strList.get(0).size());
				PdfPCell cell = null;
				for (int i = 0; i < strList.size(); i++) {
					for (int j = 0; j < strList.get(i).size(); j++) {
						cell = new PdfPCell(new Phrase(strList.get(i).get(j)));
						pdfTable.addCell(cell);
					}
				}
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	// 导出excel表格
	public static ByteArrayOutputStream outExcelOther(List<String> list) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("出票汇总表");
		// HSSFCellStyle style = hwb.createCellStyle();
		HSSFRow row = null;
		HSSFCell cell = null;
		createExcelTitle(sheet, row, cell, 2, list);
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

	public static void createExcelTitle(HSSFSheet sheet, HSSFRow row, HSSFCell cell, int startRow, List<String> list) {
		// 表头第一行
		row = sheet.createRow(startRow);
		cell = row.createCell(0);
		cell.setCellValue("票价");
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, (short) 0, (short) 1));
		cell = row.createCell(2);
		cell.setCellValue("可售总票房");
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, (short) 2, (short) 2));
		cell = row.createCell(3);
		cell.setCellValue("出票");
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, (short) 3, (short) 6 + list.size()));
		cell = row.createCell(7 + list.size());
		cell.setCellValue("剩余票房");
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, (short) 7 + list.size(), (short) 7 + list.size()));
		cell = row.createCell(8 + list.size());
		cell.setCellValue("预留票房");
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, (short) 8 + list.size(), (short) 8 + list.size()));
		cell = row.createCell(9 + list.size());
		cell.setCellValue("当前可售票房");
		sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, (short) 9 + list.size(), (short) 9 + list.size()));
		// 表头第二行
		row = sheet.createRow(startRow + 1);
		cell = row.createCell(3);
		cell.setCellValue("正常出票");
		for (int i = 0; i < list.size(); i++) {
			cell = row.createCell(4 + i);
			cell.setCellValue(list.get(i));
		}
		cell = row.createCell(4 + list.size());
		cell.setCellValue("赠票出票");
		cell = row.createCell(5 + list.size());
		cell.setCellValue("工作票出票");
		cell = row.createCell(6 + list.size());
		cell.setCellValue("小计");
	}

}