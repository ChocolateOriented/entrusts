/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.entrusts.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	protected static Logger logger = LoggerFactory.getLogger(DateUtils.class);

	private static String[] parsePatterns = { "yyyy-MM-dd",
			"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd",
			"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd",
			"yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
	 * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60
				* 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "."
				+ sss;
	}

	public static Date timeMillis2Date(long timeMillis){
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long time=new Long(timeMillis);
		String d = format.format(time);
		Date date = new Date();
		try {
			 date=format.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	public static Date endDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, 23);
		calendar.add(Calendar.MINUTE, 59);
		calendar.add(Calendar.SECOND, 59);
		calendar.add(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	public static Date endDate2(Date date) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			date = df.parse(df.format(new Date(date.getTime() + 24 * 60 * 59
					* 1000)));
		} catch (ParseException e) {
			logger.info("解析时间发生错误",e);
		}
		return date;
	}

	/**
	 * 得到某年某月的第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getFirstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
		return new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());
	}

	/**
	 * 得到某年某月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, value);
		return new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());
	}

	
	/**
	 * 根據參數返回日期 
	 * @param amount
	 * @return
	 */
	public static Date getDate(int amount){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,amount);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date time = cal.getTime();
		return time;
	}
	
	/**
	 * 当月最后一天
	 * @return
	 */
	public static Date getMonthLastDayDate(){
		Calendar cal=Calendar.getInstance();
//		cal.add(Calendar.DATE,1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
		Date time = cal.getTime();
		return time;
	}
	
	/**
	 * 当月第一天
	 * @return
	 */
	public static Date getMonthFirstDayDate(){
		Calendar cal=Calendar.getInstance();
//		cal.add(Calendar.DATE,1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_MONTH,1);  
		Date time = cal.getTime();
		return time;
	}
	
	/**
	 * 返回指定日期
	 * @param day
	 * @return
	 */
	 public static Date getDateOfMonth(int day) {
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.DATE, day);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	  }
	 /**
		 * 获取昨天的当前时间日期
		 * @param newDate
		 * @return
		 */
		public static Date getBeforeDay(){
			
			Date today = new Date();
			Date yesterday = new Date(today.getTime() - 86400000L);
			return yesterday;
		}
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		System.out.println(getDate(1));
		
		// System.out.println(formatDate(parseDate("2010/3/6")));
		// System.out.println(getDate("yyyy年MM月dd日 E"));
		// long time = new Date().getTime()-parseDate("2012-11-19").getTime();
		// System.out.println(time/(24*60*60*1000));

//		Calendar cal = Calendar.getInstance();
//		// 不加下面2行，就是取当前时间前一个月的第一天及最后一天
//		cal.set(Calendar.YEAR, 2012);
//		cal.set(Calendar.MONTH, 6);
//		cal.set(Calendar.DAY_OF_MONTH, 1);
//		cal.add(Calendar.DAY_OF_MONTH, -1);
//		Date lastDate = cal.getTime();
//
//		cal.set(Calendar.DAY_OF_MONTH, 1);
//		Date firstDate = cal.getTime();

	}
}
