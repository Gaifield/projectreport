package cn.damai.boss.projectreport.report.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.commons.ApplicationException;
import cn.damai.boss.projectreport.report.service.SaleService;
import cn.damai.boss.projectreport.report.vo.DisaccountVo;
import cn.damai.boss.projectreport.report.vo.PriceCellVo;
import cn.damai.boss.projectreport.report.vo.PriceVo;
import cn.damai.boss.projectreport.report.vo.SaleRowVo;
import cn.damai.boss.projectreport.report.vo.SaleVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-report.xml" })
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class SaleServiceImplTest {

	@Resource
	private SaleService saleService;

	@Test
	public void outExcel() {
		List<SaleVo> seatList = new ArrayList<SaleVo>();

		SaleVo vo = new SaleVo();
		PriceVo pvo = new PriceVo();
		pvo.setPrice(new BigDecimal(100));
		pvo.setPriceName("100元");

		PriceCellVo pcvo = new PriceCellVo();
		pcvo.setAmount(new BigDecimal(888));
		pcvo.setQuantity(150);
		List<DisaccountVo> list = new ArrayList<DisaccountVo>();
		for (int i = 0; i < 5; i++) {
			DisaccountVo dvo = new DisaccountVo();
			dvo.setAmount(new BigDecimal(85));
			dvo.setQuantity(100);
			dvo.setDisaccountName("套票" + i);
			list.add(dvo);
		}
		List<String> lists = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			lists.add("套票" + i);
		}

		List<SaleRowVo> srvoList = new ArrayList<SaleRowVo>();

		for (int i = 0; i < 5; i++) {
			SaleRowVo svo = new SaleRowVo();
			svo.setPriceVo(pvo);
			svo.setAvailableSale(pcvo);
			svo.setPriceTotalSale(pcvo);
			svo.setDisaccountVoList(list);
			svo.setTotalSaleCell(pcvo);
			svo.setLeftSale(pcvo);
			svo.setTotalSaleCell(pcvo);
			srvoList.add(svo);

		}
		for (int i = 0; i < 5; i++) {
			vo.setSaleRowVoList(srvoList);
			vo.setTotalAmount(new BigDecimal(1000));
			vo.setTotalQuantity(100);
			vo.setAllDisaccountVoList(lists);
			vo.setPerformName("场次------" + i);
			vo.setPerformTime("2014-03-17");
			seatList.add(vo);
		}

		String pathExcel = new String("d://出票汇总.xls");
		try {
			FileOutputStream output = new FileOutputStream(new File(pathExcel));
			try {
				saleService.outExcel(seatList, "出票汇总",false).writeTo(output);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void outPdf() {
		List<SaleVo> seatList = new ArrayList<SaleVo>();

		SaleVo vo = new SaleVo();
		PriceVo pvo = new PriceVo();
		pvo.setPrice(new BigDecimal(100));
		pvo.setPriceName("100元");

		PriceCellVo pcvo = new PriceCellVo();
		pcvo.setAmount(new BigDecimal(888));
		pcvo.setQuantity(150);
		List<DisaccountVo> list = new ArrayList<DisaccountVo>();
		for (int i = 0; i < 5; i++) {
			DisaccountVo dvo = new DisaccountVo();
			dvo.setAmount(new BigDecimal(85));
			dvo.setQuantity(100);
			dvo.setDisaccountName("套票" + i);
			list.add(dvo);
		}
		List<String> lists = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			lists.add("套票" + i);
		}

		List<SaleRowVo> srvoList = new ArrayList<SaleRowVo>();

		for (int i = 0; i < 5; i++) {
			SaleRowVo svo = new SaleRowVo();
			svo.setPriceVo(pvo);
			svo.setAvailableSale(pcvo);
			svo.setPriceTotalSale(pcvo);
			svo.setDisaccountVoList(list);
			svo.setTotalSaleCell(pcvo);
			svo.setLeftSale(pcvo);
			svo.setTotalSaleCell(pcvo);
			srvoList.add(svo);

		}
		for (int i = 0; i < 5; i++) {
			vo.setSaleRowVoList(srvoList);
			vo.setTotalAmount(new BigDecimal(1000));
			vo.setTotalQuantity(100);
			vo.setAllDisaccountVoList(lists);
			vo.setPerformName("场次------" + i);
			vo.setPerformTime("2014-03-17");
			seatList.add(vo);
		}
		String pathPdf = new String("d://出票汇总.pdf");
		try {
			FileOutputStream output = new FileOutputStream(new File(pathPdf));
			try {
				saleService.outPdf(seatList, "出票汇总",false).writeTo(output);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
