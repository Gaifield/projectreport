package cn.damai.boss.projectreport.manager.dao.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.damai.boss.projectreport.manager.dao.OperatorDAO;
import cn.damai.boss.projectreport.util.OutExcelUtil;
import cn.damai.boss.projectreport.domain.Upt01Operator;


/**
 * Created by 温俊荣 on 14-2-22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@TransactionConfiguration(transactionManager = "projectReportTransactionManager", defaultRollback = false)
public class OperatorDAOCustomImplTest {
	
	@Autowired
	private  OperatorDAO operatorDao;
	
	@Test
	public void queryOperatorByUserId(){
		List<Upt01Operator> operators = operatorDao.queryOperatorByUserId((long)37);
		if(operators!=null&&operators.size()!=0){
		System.out.println(operators.size());
		}
	}
	
	@Test
	public void findByUserId(){
		Upt01Operator operators = operatorDao.findByUserId((long)37);
		System.out.println(operators==null);
	}
	
	@Test
	public void save(){
		Upt01Operator operator = new Upt01Operator();
		operator.setOperatorId((long)2);
		operator.setUserId((long)37);
		operator.setPermissionLevel((short)1);
		operator.setStatus((short)2);
		operator.setCreateUserId((long)23);
		operator.setCreateTime(new Timestamp(new Date().getTime()));
		operator.setModifyUserId((long)23);
		operator.setModifyTime(new Timestamp(new Date().getTime()));
		Upt01Operator operators = operatorDao.save(operator);
	}
	
	@Test
	public void queryAllOperator(){
		List<Upt01Operator> operators = operatorDao.queryAllOperator((long)2,(short)1,(short)1);
		if(operators!=null&&operators.size()!=0){
			System.out.println(operators.size());
		}
	}
	
	@Test
	public void outExcel(){
		List<List<String>> strLists = new ArrayList<List<String>>();
		List<String> list = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		List<String> list3 = new ArrayList<String>();
		List<String> list4 = new ArrayList<String>();
		list.add("A");
		list.add("A");
		list.add("B");
		list.add("C");
		list.add("C");
		list.add("C");
		list.add("D");
		list.add("E");
		list2.add("A");
		list2.add("A");
		list2.add("B");
		list2.add("cc");
		list2.add("dd");
		list2.add("ee");
		list2.add("D");
		list2.add("E");
		list3.add("800");
		list3.add("票价（元）");
		list3.add("1200$");
		list3.add("1500$");
		list3.add("1600$");
		list3.add("2000$");
		list3.add("3000$");
		list3.add("5000$");
		list4.add("800");
		list4.add("数量（张）");
		list4.add("120");
		list4.add("150");
		list4.add("160");
		list4.add("200");
		list4.add("300");
		list4.add("500");
		strLists.add(list);
		strLists.add(list2);
		strLists.add(list3);
		strLists.add(list4);
		
		String pathExcel = new String("c://分区出票统计.xls");
		try {
			FileOutputStream output = new FileOutputStream(new File(pathExcel));
			try {
				OutExcelUtil.outExcel(strLists,OutExcelUtil.STANDSTAT_NAME,2).writeTo(output);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			};
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
		
	@Test
	public void outExcelOther(){
		List<String> list = new ArrayList<String>();
		list.add("95折");
		list.add("85折");
		list.add("75折");
		list.add("65折");
		String pathExcel = new String("c://出票汇总表.xls");
		try {
			FileOutputStream output = new FileOutputStream(new File(pathExcel));
			try {
				OutExcelUtil.outExcelOther(list).writeTo(output);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			};
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
