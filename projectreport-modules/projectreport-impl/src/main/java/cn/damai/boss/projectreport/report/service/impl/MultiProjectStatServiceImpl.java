package cn.damai.boss.projectreport.report.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Service;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.enums.HttpStatusEnum;
import cn.damai.boss.projectreport.commons.utils.KeyValueUtils;
import cn.damai.boss.projectreport.report.dao.MultiProjectStatDao;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.BSGSiteEnum;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.service.MultiProjectStatService;
import cn.damai.boss.projectreport.report.vo.ReportProjectVo;

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

@Service
public class MultiProjectStatServiceImpl implements MultiProjectStatService {

	private static final String[] tableTitles = new String[] { "项目ID", "项目名称",
			"项目状态", "演出开始时间", "演出结束时间", "演出场馆", "本日销售金额", "总销售金额", "剩余票房" };

	@Resource
	private MultiProjectStatDao multiProjectStatDao;

	@Override
	public List<ReportProjectVo> findMultiProjectStatList(
			List<KeyValueUtils> projectIds) throws ApplicationException {
		List<ReportProjectVo> list = new ArrayList<ReportProjectVo>();
		try {
			if (projectIds != null) {
				// 分库统计项目
				List<Long> bjList = new ArrayList<Long>();
				List<Long> shList = new ArrayList<Long>();
				List<Long> gzList = new ArrayList<Long>();
				for (KeyValueUtils item : projectIds) {
					if (item.getValue().equals(
							String.valueOf(BSGSiteEnum.BeiJing.getCode()))) {
						bjList.add(Long.parseLong(item.getKey()));
					} else if (item.getValue().equals(
							String.valueOf(BSGSiteEnum.ShangHai.getCode()))) {
						shList.add(Long.parseLong(item.getKey()));
					} else if (item.getValue().equals(
							String.valueOf(BSGSiteEnum.GuangZhou.getCode()))) {
						gzList.add(Long.parseLong(item.getKey()));
					}
				}
				if (bjList.size() > 0) {
					DynamicDataSourceHolder
							.putDataSourceName(DataSourceEnum.BJMaitix
									.getCodeStr());
					List<ReportProjectVo> bjProjects = multiProjectStatDao
							.queryMulitProjectInfoList(String
									.valueOf(BSGSiteEnum.BeiJing.getCode()),
									bjList);
					if (bjProjects != null) {
						list.addAll(bjProjects);
					}
				}

				if (shList.size() > 0) {
					DynamicDataSourceHolder
							.putDataSourceName(DataSourceEnum.SHMaitix
									.getCodeStr());
					List<ReportProjectVo> shProjects = multiProjectStatDao
							.queryMulitProjectInfoList(String
									.valueOf(BSGSiteEnum.ShangHai.getCode()),
									shList);
					if (shProjects != null) {
						list.addAll(shProjects);
					}
				}

				if (gzList.size() > 0) {
					DynamicDataSourceHolder
							.putDataSourceName(DataSourceEnum.GZMaitix
									.getCodeStr());
					List<ReportProjectVo> gzProjects = multiProjectStatDao
							.queryMulitProjectInfoList(String
									.valueOf(BSGSiteEnum.GuangZhou.getCode()),
									gzList);
					if (gzProjects != null) {
						list.addAll(gzProjects);
					}
				}
			}
			if (list != null) {
				Collections.sort(list, new Comparator<ReportProjectVo>() {
					@Override
					public int compare(ReportProjectVo o1, ReportProjectVo o2) {
						if(o2.getStartTime()!=null && o1.getStartTime()!=null){
							return o2.getStartTime().compareTo(o1.getStartTime());
						}else{
							return o2.getProjectId().compareTo(o1.getProjectId());
						}
					}
				});
			}
		} catch (Exception ex) {
			throw new ApplicationException(
					HttpStatusEnum.ServerError.getCode(), ex.getMessage());
		}
		return list;
	}

	private String getProjectStatus(int status) {
		String strStatus = "";
		if (status == 4) {
			strStatus = "正在销售";
		} else if (status == 5) {
			strStatus = "已结束";
		}
		return strStatus;
	}

	/**
	 * 导出excel表格
	 */
	@Override
	public ByteArrayOutputStream outExcel(List<ReportProjectVo> projects)
			throws ApplicationException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		HSSFWorkbook hwb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet

		HSSFSheet sheet = hwb.createSheet("跨项目报表");
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

		if (projects != null && projects.size() != 0) {
			int z = 0;
			// 1、创建表格标题
			row = sheet.createRow(z++);
			createHeader(row, style4);

			// 2、创建行
			BigDecimal[] sums = new BigDecimal[] { new BigDecimal("0"),
					new BigDecimal("0"), new BigDecimal("0") };
			for (int j = 0; j < projects.size(); j++) {
				row = sheet.createRow(z++);
				createExcelRow(sheet, row, style, projects.get(j));
				sums[0].add(new BigDecimal(projects.get(j).getTodayMoney()));
				sums[1].add(new BigDecimal(projects.get(j).getTotalMoney()));
				sums[2].add(new BigDecimal(projects.get(j)
						.getRemainingBoxOffice()));
			}
			// 合计行
			row = sheet.createRow(z++);
			int i = 0;
			for (; i < 6; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
				if (i == 0) {
					cell.setCellValue("合计");
				} else {
					cell.setCellValue("");
				}
			}

			HSSFCell cell = row.createCell(i++);
			cell.setCellStyle(style);
			cell.setCellValue(sums[0].toString());

			cell = row.createCell(i++);
			cell.setCellStyle(style);
			cell.setCellValue(sums[1].toString());

			cell = row.createCell(i++);
			cell.setCellStyle(style);
			cell.setCellValue(sums[2].toString());
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
	private void createHeader(HSSFRow row, HSSFCellStyle style) {

		for (int i = 0; i < tableTitles.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(tableTitles[i]);
		}
	}

	// 导入价格行
	private void createExcelRow(HSSFSheet sheet, HSSFRow row,
			HSSFCellStyle style, ReportProjectVo projectVo) {

		int cellIndex = 0;
		HSSFCell cell = row.createCell(cellIndex++);
		cell.setCellStyle(style);
		cell.setCellValue(projectVo.getProjectId());

		cell = row.createCell(cellIndex++);
		cell.setCellStyle(style);
		cell.setCellValue(projectVo.getProjectName());

		cell = row.createCell(cellIndex++);
		cell.setCellStyle(style);
		cell.setCellValue(getProjectStatus(projectVo.getProjectStatus()));

		cell = row.createCell(cellIndex++);
		cell.setCellStyle(style);
		cell.setCellValue(projectVo.getStartTime());

		cell = row.createCell(cellIndex++);
		cell.setCellStyle(style);
		cell.setCellValue(projectVo.getEndTime());

		cell = row.createCell(cellIndex++);
		cell.setCellStyle(style);
		cell.setCellValue(projectVo.getPerformField());

		cell = row.createCell(cellIndex++);
		cell.setCellStyle(style);
		cell.setCellValue(projectVo.getTodayMoney());

		cell = row.createCell(cellIndex++);
		cell.setCellStyle(style);
		cell.setCellValue(projectVo.getTotalMoney());

		cell = row.createCell(cellIndex++);
		cell.setCellStyle(style);
		cell.setCellValue(projectVo.getRemainingBoxOffice());
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
	public ByteArrayOutputStream outPdf(List<ReportProjectVo> projects)
			throws ApplicationException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		Document pdfDoc = new Document(PageSize.A4, 50, 50, 50, 50);
		try {
			BaseFont bfChinese = BaseFont.createFont("/SIMHEI.TTF",
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Font fontChinese = new Font(bfChinese, 10, Font.NORMAL,
					BaseColor.BLACK);

			PdfWriter.getInstance(pdfDoc, output);
			pdfDoc.open();

			if (projects != null) {
				int cellCount = tableTitles.length;
				PdfPTable pdfTable = new PdfPTable(cellCount);

				// 在次行写入表头
				createPdfHeader(pdfTable, fontChinese);

				// 写入表格内部数据
				BigDecimal[] sums = new BigDecimal[] { new BigDecimal("0"),
						new BigDecimal("0"), new BigDecimal("0") };
				if (projects.size() > 0) {
					for (int j = 0; j < projects.size(); j++) {
						createPdfRow(projects.get(j), pdfTable, fontChinese);

						sums[0].add(new BigDecimal(projects.get(j)
								.getTodayMoney()));
						sums[1].add(new BigDecimal(projects.get(j)
								.getTotalMoney()));
						sums[2].add(new BigDecimal(projects.get(j)
								.getRemainingBoxOffice()));
					}

					// 合计行
					pdfTable.addCell(new PdfPCell(new Phrase("合计", fontChinese)));

					int i = 1;
					for (; i < 6; i++) {
						pdfTable.addCell(new PdfPCell(new Phrase("",
								fontChinese)));
					}
					pdfTable.addCell(new PdfPCell(new Phrase(
							sums[0].toString(), fontChinese)));
					pdfTable.addCell(new PdfPCell(new Phrase(
							sums[1].toString(), fontChinese)));
					pdfTable.addCell(new PdfPCell(new Phrase(
							sums[2].toString(), fontChinese)));
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
	private void createPdfHeader(PdfPTable table, Font fontChinese) {
		for (int i = 0; i < tableTitles.length; i++) {
			table.addCell(new PdfPCell(new Phrase(tableTitles[i], fontChinese)));
		}
	}

	// 导入pdf表格内容
	private void createPdfRow(ReportProjectVo project, PdfPTable table,
			Font fontChinese) {
		table.addCell(new PdfPCell(new Phrase(project.getProjectId() + "",
				fontChinese)));
		table.addCell(new PdfPCell(new Phrase(project.getProjectName(),
				fontChinese)));
		table.addCell(new PdfPCell(new Phrase(getProjectStatus(project
				.getProjectStatus()), fontChinese)));
		table.addCell(new PdfPCell(new Phrase(project.getStartTime(),
				fontChinese)));
		table.addCell(new PdfPCell(
				new Phrase(project.getEndTime(), fontChinese)));
		table.addCell(new PdfPCell(new Phrase(project.getPerformField(),
				fontChinese)));
		table.addCell(new PdfPCell(new Phrase(project.getTodayMoney(),
				fontChinese)));
		table.addCell(new PdfPCell(new Phrase(project.getTotalMoney(),
				fontChinese)));
		table.addCell(new PdfPCell(new Phrase(project.getRemainingBoxOffice(),
				fontChinese)));
	}
}
