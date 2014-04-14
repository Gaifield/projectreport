package cn.damai.boss.projectreport.commons.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * 工具类
 * 
 * @author Administrator
 * 
 */
public class Utils {
	private final static DateFormat DATETIMEFORMAT= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static DateFormat DATEFORMAT= new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat DATEFORMATOTHER= new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	/**
	 * 转换以","字符字符串为List<Long>
	 * 
	 * @Title: convert2IdList
	 * @Description:
	 * @date 2014-3-1 下午2:47:16
	 * @param ids
	 *            以","字符字符串
	 * @return
	 */
	public static List<Long> convert2IdList(String ids) {
		if (StringUtils.isBlank(ids)) {
			return null;
		}
		List<Long> idList = new ArrayList<Long>();
		String[] strArray = ids.split(",");
		for (String id : strArray) {
			idList.add(Long.parseLong(id.trim()));
		}
		return idList;
	}
	
	public static String join(List<Long> ids) {
		if(ids==null || ids.size()<1){
			return "0";
		}else{
			StringBuilder sb= new StringBuilder();
			for(Long id:ids){
				if(sb.length()>0){
					sb.append(",");
				}
				sb.append(id);
			}
			return String.valueOf(sb);
		}		
	}
	
	/**
	 * 转换项目Id：来源对应的key/value列表
	 * @param projectIds 1987:1,1988:2
	 * @return
	 */
	public static List<KeyValueUtils> convertProjectSourceList(
			String projectIds) {
		if (StringUtils.isBlank(projectIds)) {
			return null;
		}
		List<KeyValueUtils> mapList = new ArrayList<KeyValueUtils>();
		String[] strArray = projectIds.split(",");
		for (String id : strArray) {
			String[] par = id.split(":");
			mapList.add(new KeyValueUtils(Long.parseLong(par[0]), par[1]));
		}
		return mapList;
	}

	/**
	 * 计算页数
	 * 
	 * @param total
	 * @param pageSize
	 * @return 返回页数
	 */
	public static int countPageSize(long total, int pageSize) {
		int pageCount = 1;
		if (pageSize > 0) {
			pageCount = (int) total / pageSize;
		}
		if (total % pageSize > 0) {
			pageCount++;
		}

		return pageCount;
	}

	/**
	 * 加减日期
	 * 
	 * @param day
	 *            正/负天数
	 * @return
	 */
	public static Date addDate(int day) {
		return new DateTime().plusDays(day).toDate();
	}

	public static Date setDateLastTime(Date date) {
		DateTime dt = new DateTime(date);
		return dt.plusDays(1).plusMillis(-1).toDate();
	}

	public static void main(String[] args) {
		Date[] dates = getAddMonthDates(-1);
	}
	
	public static Date[] getMonthDates(DateTime dateTime){
		Date[] dates= new Date[2];
		dates[0]= dateTime.dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
		dates[1]= dateTime.dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().toDate();
		return dates;
	}
	
	public static Date[] getCurrentMonthDates(){
		return getMonthDates(new DateTime());
	}
	
	public static Date[] getAddMonthDates(int months){
		return getMonthDates(new DateTime().plusMonths(months));
	}
	
	public static String formatDateTime(Date date){
		return DATETIMEFORMAT.format(date);
	}
	
	public static String formatDate(Date date){
		return DATEFORMAT.format(date);
	}
	
	public static Date parseDateTime(String dateStr){
		Date date = null;
		if(dateStr!=null && !"".equals(dateStr)){
			try {
				date = DATEFORMATOTHER.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
}