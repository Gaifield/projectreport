package cn.damai.boss.projectreport.report.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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

import cn.damai.boss.projectreport.common.vo.PageResultData;
import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.commons.utils.NumberUtils;
import cn.damai.boss.projectreport.report.dao.ReportPriceDAO;
import cn.damai.boss.projectreport.report.dao.ReserveDAO;
import cn.damai.boss.projectreport.report.dao.SeatReserveDAO;
import cn.damai.boss.projectreport.report.datasource.DynamicDataSourceHolder;
import cn.damai.boss.projectreport.report.enums.DataSourceEnum;
import cn.damai.boss.projectreport.report.enums.ReserveStatTypeEnum;
import cn.damai.boss.projectreport.report.service.ProjectReportReserveStatService;
import cn.damai.boss.projectreport.report.vo.PerformStandStatVo;
import cn.damai.boss.projectreport.report.vo.PerformVo;
import cn.damai.boss.projectreport.report.vo.PriceStatRowVo;
import cn.damai.boss.projectreport.report.vo.PriceCellVo;
import cn.damai.boss.projectreport.report.vo.PriceVo;
import cn.damai.boss.projectreport.report.vo.ReserveStatVo;
import cn.damai.boss.projectreport.report.vo.ReserveDetailVo;
import cn.damai.boss.projectreport.report.vo.ReserveStatTypeVo;
import cn.damai.boss.projectreport.report.vo.ReserveTyleVo;

@Service
public class ProjectReportReserveStatServiceImpl implements
		ProjectReportReserveStatService {
	@Resource
	private ReportPriceDAO peportPriceDAO;
	@Resource
	private ReserveDAO reserveDAO;
	@Resource
	private SeatReserveDAO seatReserveDAO;

	@Override
	public ReserveStatVo findProjectReportReserveStatList(String source,
			long projectId, List<Long> performInfoIds)
			throws ApplicationException {
		DynamicDataSourceHolder.putDataSourceName(source);
		
		ReserveStatVo resultVo = new ReserveStatVo();

		// 1、查询分组价格
		List<PriceVo> prices = peportPriceDAO.queryProjectReportPrice(
				projectId, performInfoIds);
		resultVo.setPrices(prices);

		// 2、查询项目预留级别
		List<ReserveTyleVo> reserveTypes = reserveDAO
				.queryProjectReserveTyleList(projectId);
		resultVo.setReserveTyles(reserveTypes);

		// 3、分别统计总预留、管理端预留、客户端预留
		ReserveStatTypeEnum[] dataSourceEnumArr = ReserveStatTypeEnum.values();
		
		List<ReserveStatTypeVo> statTypes = new ArrayList<ReserveStatTypeVo>(dataSourceEnumArr.length);
		resultVo.setReserveStatTypes(statTypes);
		for (ReserveStatTypeEnum type : dataSourceEnumArr) {
			//循环统计3种类型报表
			List<ReserveDetailVo> reserveDetails = seatReserveDAO
					.querySeatReserveList(projectId, performInfoIds,
							type.getCode());

			ReserveStatTypeVo reserveStatTypeVo = new ReserveStatTypeVo();
			statTypes.add(reserveStatTypeVo);
			//添加报表统计类型
			reserveStatTypeVo.setReserveStatType(type.getCode());
			
			//当前类型添加行
			List<PriceStatRowVo> rows = new ArrayList<PriceStatRowVo>(prices.size());
			reserveStatTypeVo.setRows(rows);
			
			//总计行
			PriceStatRowVo sumRrow = new PriceStatRowVo();
			sumRrow.setPrice(new PriceVo("合计"));
			List<PriceCellVo> sumRrowCells =PriceCellVo.initCells(reserveTypes.size()+1);
			sumRrow.setRowCells(sumRrowCells);
			
			//按价格进行行遍历
			for (PriceVo price : prices) {
				// 添加价格行
				PriceStatRowVo row = new PriceStatRowVo();
				row.setPrice(price);
				rows.add(row);
				
				// 初始化行单元格集合
				List<PriceCellVo> rowCells = new ArrayList<PriceCellVo>(reserveTypes.size());
				row.setRowCells(rowCells);

				// 先添加总计列
				PriceCellVo totalVo = new PriceCellVo();
				rowCells.add(totalVo);

				// 遍历预留级别
				int cellIndex=1;
				for (ReserveTyleVo reserveTyle : reserveTypes) {
					PriceCellVo statData = new PriceCellVo();
					// 填充明细项目
					for (ReserveDetailVo detail : reserveDetails) {
						// 预留级别、价格、价格名称是否相等
						if (detail.getReserveTyle() == reserveTyle
								.getReserveTyle()
								&& detail.getPrice().equals(price.getPrice())
								&& detail.getPriceName().equals(
										price.getPriceName())) {
							statData.setQuantity(detail.getQuantity());
							statData.setAmount(detail.getAmount());
							
							//合计列
							totalVo.addQuantity(detail.getQuantity());
							totalVo.addAmount(detail.getAmount());
							
							//总计行合计列
							sumRrowCells.get(0).addQuantity(detail.getQuantity());
							sumRrowCells.get(0).addAmount(detail.getAmount());
						   //总计行明细列
							sumRrowCells.get(cellIndex).addQuantity(detail.getQuantity());
							sumRrowCells.get(cellIndex).addAmount(detail.getAmount());
							break;
						}
					}
					rowCells.add(statData);
					cellIndex++;
				}
			}
			rows.add(sumRrow);
		}
		return resultVo;
	}
	
	/**
	 * 导出excel表格
	 */
	@Override
	public ByteArrayOutputStream outExcel(ReserveStatVo reserveStatVo,
			String projectName) throws ApplicationException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		List<ReserveStatTypeVo> reports = reserveStatVo.getReserveStatTypes();
		HSSFWorkbook hwb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		String reportName = "预留统计";
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
		
		if (reports != null && reports.size() != 0) {
			int cellCount=reserveStatVo.getReserveTyles().size()+3;
			int z = 0;
			row = sheet.createRow(z++);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue(projectName);
			cell.setCellStyle(style3);
			// 合并单元格参数分别是：所需合并的起始行、终止行、起始列、终止列
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellCount));
			
			for (int i = 0; i < reports.size(); i++) {
				ReserveStatTypeVo report = reports.get(i);
				
				// 报表区块标题
				row = sheet.createRow(z++);
				cell = row.createCell(0);				
				cell.setCellValue(report.getReserveStatName());
				cell.setCellStyle(style3);
				createCellColor(style4, sheet, 6, font);
				sheet.addMergedRegion(new CellRangeAddress(z-1, z - 1, 0, 1));					
				
				//1、创建表格标题
				row = sheet.createRow(z++);
				createHeader(reserveStatVo.getReserveTyles(), row, style4);
				// 合并单元格参数分别是：所需合并的起始行、终止行、起始列、终止列
				sheet.addMergedRegion(new CellRangeAddress(z-1,z-1, 0, 1));
				
				//2、创建行
				if (report.getRows() != null& report.getRows().size() != 0) {
					for (int j = 0; j < report.getRows().size(); j++) {
						//添加票价行
						createExcelRow(sheet,z, style, report.getRows().get(j));
						z+=2;
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
	private void createHeader(List<ReserveTyleVo> reserveTyleVos, HSSFRow row,
			HSSFCellStyle style) {
		HSSFCell cell = row.createCell(0);
		int cellIndex = 0;
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("价位");
		cell.setCellStyle(style);

		cell = row.createCell(cellIndex++);
		cell.setCellValue("");
		cell.setCellStyle(style);		
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("总预留");
		cell.setCellStyle(style);
		
		for (int i = 0; i < reserveTyleVos.size(); i++) {
			cell = row.createCell(cellIndex++);
			cell.setCellStyle(style);
			cell.setCellValue(reserveTyleVos.get(i).getTyleName());
		}
	}

	//导入价格行	
	private void createExcelRow(HSSFSheet sheet,int rowIndex, HSSFCellStyle style,
			PriceStatRowVo rowVo) {
		int row=rowIndex;
		int indexQuantity =0;
		int indexAmount=0;
		
		//数量行
		HSSFRow rowQuantity  = sheet.createRow(rowIndex++);		
		//价格名称
		HSSFCell cellQuantity = rowQuantity.createCell(indexQuantity++);
		cellQuantity.setCellValue(rowVo.getPrice().getPriceShowName());
		cellQuantity.setCellStyle(style);
		//数量
		cellQuantity = rowQuantity.createCell(indexQuantity++);
		cellQuantity.setCellValue("总数量（张）");
		cellQuantity.setCellStyle(style);
		
		//金额行
		HSSFRow rowAmount  = sheet.createRow(rowIndex);	
		HSSFCell cellAmount = rowAmount.createCell(indexAmount++);
		cellAmount.setCellStyle(style);
		//金额
		cellAmount = rowAmount.createCell(indexAmount++);
		cellAmount.setCellValue("总金额（元）");
		cellAmount.setCellStyle(style);		
		
		//预留等级列表
		for(PriceCellVo priceCellVo: rowVo.getRowCells()){
			cellQuantity = rowQuantity.createCell(indexQuantity++);
			cellQuantity.setCellValue(priceCellVo.getQuantity());
			cellQuantity.setCellStyle(style);
			
			cellAmount = rowAmount.createCell(indexAmount++);
			cellAmount.setCellValue(priceCellVo.getAmount().doubleValue());
			cellAmount.setCellStyle(style);
		}
		
		// 合并单元格参数分别是：所需合并的起始行、终止行、起始列、终止列
		sheet.addMergedRegion(new CellRangeAddress(row, row+1, 0, 0));
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
	public ByteArrayOutputStream outPdf(ReserveStatVo reserveStatVo,
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
			
			if (reserveStatVo != null && reserveStatVo.getReserveStatTypes() != null) {
				int cellCount=reserveStatVo.getReserveTyles().size()+3;
				PdfPTable pdfTable = new PdfPTable(cellCount);
				// 写入项目名称
				PdfPCell cell = new PdfPCell(new Phrase(projectName, fontChinese));
				//cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				//cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				// 合并横向表格
				cell.setColspan(cellCount);
				pdfTable.addCell(cell);
				
				for (int i = 0; i < reserveStatVo.getReserveStatTypes().size(); i++) {
					ReserveStatTypeVo report = reserveStatVo.getReserveStatTypes().get(i);
					// 写入区块标题
					cell = new PdfPCell(new Phrase(report.getReserveStatName(),fontChinese));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setColspan(cellCount);
					pdfTable.addCell(cell);
					
					// 在次行写入表头
					createPdfHeader(reserveStatVo.getReserveTyles(), pdfTable, fontChinese);
					if (report.getRows() != null && report.getRows().size() != 0) {
						// 写入表格内部数据
						for (int j = 0; j < report.getRows().size(); j++) {
							createPdfRow(report.getRows().get(j) ,pdfTable, fontChinese);
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
	private void createPdfHeader(List<ReserveTyleVo> reserveTyleVos,PdfPTable table, Font fontChinese) {
		PdfPCell priceCell = new PdfPCell(new Phrase("价位", fontChinese));
		priceCell.setColspan(2);
		priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		priceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(priceCell);
		
		table.addCell(new PdfPCell(new Phrase("总预留", fontChinese)));		
		for (int i = 0; i < reserveTyleVos.size(); i++) {			
			table.addCell(new PdfPCell(new Phrase(reserveTyleVos.get(i).getTyleName(), fontChinese)));	
		}
	}

	// 导入pdf表格内容
	private void createPdfRow(PriceStatRowVo rowVo,PdfPTable table, Font fontChinese) {
		PdfPCell priceCell=new PdfPCell(new Phrase(rowVo.getPrice().getPriceShowName(), fontChinese));
		priceCell.setRowspan(2);
		priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		priceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(priceCell);

		
		//数量行
		table.addCell(new PdfPCell(new Phrase("总数量（张）", fontChinese)));
		for(PriceCellVo priceCellVo: rowVo.getRowCells()){
			table.addCell(new PdfPCell(new Phrase(priceCellVo.getQuantity()+"", fontChinese)));
		}
		//table.addCell(new PdfPCell(new Phrase(rowVo.getPrice().getPriceName(), fontChinese)));
		table.addCell(new PdfPCell(new Phrase("总金额（元）", fontChinese)));
		for(PriceCellVo priceCellVo: rowVo.getRowCells()){
			table.addCell(new PdfPCell(new Phrase(priceCellVo.getAmount()+"", fontChinese)));
		}		
	}
}