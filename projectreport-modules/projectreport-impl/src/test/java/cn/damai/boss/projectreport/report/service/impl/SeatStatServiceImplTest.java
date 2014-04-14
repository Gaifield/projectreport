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
import cn.damai.boss.projectreport.report.service.SeatStatService;
import cn.damai.boss.projectreport.report.vo.SeatStatVo;
import cn.damai.boss.projectreport.util.OutExcelUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-report.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class SeatStatServiceImplTest {

	@Resource
	private SeatStatService sss;
	
	@Test
	public void findSeatStatInfo() {
			
		try {
			sss.querySeatStatInfo(new ArrayList<Long>(9724),1L);
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	@Test
	public void outExcel(){
		List<SeatStatVo> seatList = new ArrayList<SeatStatVo>();
		for(int i=0;i<5;i++){
			SeatStatVo vo = new SeatStatVo();
			vo.setPrice(new BigDecimal(800));
			vo.setSeatQuantity(100);
			vo.setSeatAmount(new BigDecimal(111));
			vo.setStaffQuantity(200);
			vo.setStaffAmount(new BigDecimal(222));
			vo.setProtectQuantity(300);
			vo.setProtectAmount(new BigDecimal(333));
			vo.setVendibilityQuantity(400);
			vo.setVendibilityAmount(new BigDecimal(444));
			seatList.add(vo);
		}
		String pathExcel = new String("c://座位汇总.xls");
		try {
			FileOutputStream output = new FileOutputStream(new File(pathExcel));
			try {
				sss.outExcel(seatList, false).writeTo(output);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			};
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	@Test
	public void outPdf(){
		List<SeatStatVo> seatList = new ArrayList<SeatStatVo>();
		for(int i=0;i<5;i++){
			SeatStatVo vo = new SeatStatVo();
			vo.setPrice(new BigDecimal(800));
			vo.setSeatQuantity(100);
			vo.setSeatAmount(new BigDecimal(111));
			vo.setStaffQuantity(200);
			vo.setStaffAmount(new BigDecimal(222));
			vo.setProtectQuantity(300);
			vo.setProtectAmount(new BigDecimal(333));
			vo.setVendibilityQuantity(400);
			vo.setVendibilityAmount(new BigDecimal(444));
			seatList.add(vo);
		}
		String pathPdf = new String("c://座位汇总.pdf");
		try {
			FileOutputStream output = new FileOutputStream(new File(pathPdf));
			try {
				sss.outPdf(seatList, false).writeTo(output);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			};
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

}
