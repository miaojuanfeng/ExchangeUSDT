package com.contactsImprove.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class DateTools {
	/**
	 * 字符串转date型 
	 * @param str 
	 * @return yyyy-MM-dd HH:mm:ss Date对象
	 */
	public static Date StrToDate(String str){		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		Date date = null;
		try {
			 date = sdf.parse(str);
		} catch (ParseException e) {
			LoggerUtil.error(e.getMessage(), e);			
			}
		return date;
	}
	/**
	 * 字符串转Date
	 * @param str
	 * @return yyyy-MM-dd Date对象 
	 */
	public static Date StrToDate2(String str){	
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");	
		Date date = null;
		try {
			 date = sdf.parse(str);
		} catch (ParseException e) {LoggerUtil.error(e.getMessage(), e);}
		return date;
	}
	/**
	 * 字符串转date
	 * @param str
	 * @return yyyy-MM-dd HH:mm:ss.SSS Date对象
	 */
	public static Date StrToDate3(String str){		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");	
		Date date = null;
		try {
			 date = sdf.parse(str);
		} catch (ParseException e) {LoggerUtil.error(e.getMessage(), e);}
		return date;
	}
	/**
	 * Date转字符串
	 * @param date
	 * @return yyyyMMddHHmmssSSS 格式字符串
	 */
	public static String DateToStr(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");	
		return sdf.format(date);
	}
	/**
	 * date转字符串
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss 格式字符串
	 */
	public static String DateToStr2(Date date){
		if(date==null) {
			return null;
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		return sdf.format(date);
	}
	/**
	 * date转字符串
	 * @param date
	 * @return yyyy-MM-dd 格式字符串
	 */
	public static String DateToStr3(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");	
		return sdf.format(date);
	}
	/**
	 * date转字符串 日月
	 * @param date
	 * @return MM-dd 格式字符串
	 */
	public static String DateToMonthDay(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd");	
		return sdf.format(date);
	}
	/**
	 * date转字符串 短年日月
	 * @param date
	 * @return yy-MM-dd 格式字符串
	 */
	public static String DateToShortYearMonthDay(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd");	
		return sdf.format(date);
	}
	/**
	 * 判断是否比当前时间早或晚
	 * @param s1 字符串(最好 yyyy-MM-dd HH:mm:ss)
	 * @return 当不是yyyy-MM-dd HH:mm:ss形式的字符串会 直接抛异常并返回false
	 *          如果字符串时间比当前时间晚刚返回false 否则返回true; 
	 */
	public static  boolean compDateinSysDate(String s1){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 try {	     
			 	return new Date().after(sf.parse(s1));
	    	} catch (ParseException e) { LoggerUtil.error(e.getMessage(), e);}
			return false;
	    }
	/**
	 * 返回二字符串日期相差的天数
	 * @param s1 格式最好为 yyyy-MM-dd
	 * @param s2 格式最好为 yyyy-MM-dd
	 * @return 二日期相差天数
	 * @throws ParseException 并返回-1
	 */
	public static  Integer compDate2(String start,String end){	
	     Calendar startDate=Calendar.getInstance();
	     Calendar endDate=Calendar.getInstance();
	    SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	    try {
		    endDate.setTime(sf.parse(end));
		    startDate.setTime(sf.parse(start));
		    return fateDays(endDate,startDate);
	    	} catch (ParseException e) { LoggerUtil.error(e.getMessage(), e);}
	   return -1;
	  } 
	/**
	 * 
	 * 高效率返回两个日期相差天数.
	 * 
	 */
	public static int fateDays(java.util.Calendar d1, java.util.Calendar d2)
	{
		   if (d1.after(d2)) {   
		        java.util.Calendar swap = d1;   
		        d1 = d2;   
		        d2 = swap;   
		    }   
		    int days = d2.get(java.util.Calendar.DAY_OF_YEAR) -   
		               d1.get(java.util.Calendar.DAY_OF_YEAR);   
		    int y2 = d2.get(java.util.Calendar.YEAR);   
		    if (d1.get(java.util.Calendar.YEAR) != y2) {   
		        d1 = (java.util.Calendar) d1.clone();   
		        do {   
		            days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);   
		            d1.add(java.util.Calendar.YEAR, 1);   
		        } while (d1.get(java.util.Calendar.YEAR) != y2);   
		    }   
		    return days;   
	}
	/**
	 * 比较二日期的早晚
	 * @param DATE1 格式 yyyy-MM-dd HH:mm
	 * @param DATE2格式 yyyy-MM-dd HH:mm
	 * @return 当date1大于date2时，返回1 否则返回-1
	 * @throws ParseException 当二日期相同时抛此异常并返回0
	 */
	 public static int compare_date(String DATE1, String DATE2) {	        	        
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        try {
	            Date dt1 = df.parse(DATE1);
	            Date dt2 = df.parse(DATE2);
	            if (dt1.after(dt2)) {
	                return 1;
	            } else if (dt2.after(dt1)) {
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception e) {
	        	LoggerUtil.error(e.getMessage(), e);
	        }
	        return 0;
	    }
	 /**
		 * 比较二日期的早晚
		 * @param DATE1 格式 yyyy-MM-dd
		 * @param DATE2格式 yyyy-MM-dd
		 * @return 当date1大于date2时，返回1 date1小于date2返回-1 如果相同返回0
		 * @throws ParseException 当二日期相同时抛此异常并返回0
		 */
	 public static int compare_date2(String DATE1, String DATE2) {	        	       
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            Date dt1 = df.parse(DATE1);
	            Date dt2 = df.parse(DATE2);
	            if (dt1.after(dt2)) {
	                return 1;
	            } else if (dt2.after(dt1)) {
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception e) {
	        	LoggerUtil.error(e.getMessage(), e);
	        }
	        return 0;
	    }
	 /**
	  * 根据您输入的小时与分不同，返回不同的结果的，比如你输入的参数今天中午与昨天晚上的两个时间，它就会告诉你相差0天
	  * @param fDate
	  * @param oDate
	  * @return
	  */
	 public static int getIntervalDays(Date fDate, Date oDate) {
	       if (null == fDate || null == oDate) {
	           return -1;
	       }
	       long intervalMilli = oDate.getTime() - fDate.getTime();
	       return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	    }
	/**
	 * 比如你输入的参数今天中午与昨天晚上的两个时间，它就会告诉你相差1天
	 * 只关心天
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {
		 if(fDate==null||oDate==null){return -1;}
	       Calendar aCalendar = Calendar.getInstance();
	       aCalendar.setTime(fDate);
	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
	       aCalendar.setTime(oDate);
	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
	       return (day2 - day1);
	    }
	/**
	 * 比如你输入的参数今天中午与昨天晚上的两个时间，它就会告诉你相差1天
	 * 只关心天
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int commonpedate(Date fDate, Date oDate) {
		if(fDate==null||oDate==null){return -1;}
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(fDate);
		int day1 = aCalendar.get(Calendar.DAY_OF_MONTH);
		aCalendar.setTime(oDate);
		int day2 = aCalendar.get(Calendar.DAY_OF_MONTH);
		return (day2 - day1);
	}
	/**一天当中 ，到指定时间差多少毫秒。
	 * 24小时制。
	 * 24点：0
	 * 早上8点:8
	 * 13点：13
	 * @throws Exception 
	 * 
	 */		
	public static long toHourForMsec(int pointHour,int pointMinute,int pointSecond) throws Exception {
		if(pointHour>24 || pointMinute>60 || pointSecond>60) {
			throw new Exception("时 ,分 ,秒 大于最大值！");
		}
		pointHour=pointHour==24 ? 0 : pointHour;
		pointMinute=pointMinute==60 ? 0 : pointMinute;
		pointSecond=pointSecond==60 ? 0 : pointSecond;

		Date nowDate=new Date();
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(nowDate);				
		int nowHour=aCalendar.get(Calendar.HOUR_OF_DAY);
		if(nowHour==pointHour) {
			if(pointMinute<aCalendar.get(Calendar.MINUTE)) {
				aCalendar.add(Calendar.DATE, 1);
			}else 
			if(pointMinute==aCalendar.get(Calendar.MINUTE) && aCalendar.get(Calendar.SECOND)>=pointSecond) {
				aCalendar.add(Calendar.DATE, 1);
			}
		}else 
		if(nowHour>pointHour){
			aCalendar.add(Calendar.DATE, 1);
		}
		StringBuilder hms=new StringBuilder();
		hms.append(" ");
		hms.append(zeroize(pointHour)+":");
		hms.append(zeroize(pointMinute)+":");
		hms.append(zeroize(pointSecond)+".000");		
		
		String to24Hour=DateToStr3(aCalendar.getTime())+hms.toString();		
		Date to24Date=StrToDate3(to24Hour);		
		long msec=to24Date.getTime()-nowDate.getTime();
		return msec;		
	}
	public static String zeroize(int num) {		
		if(num<10) {
			return "0"+num;
		}else {
			return ""+num;
		}
	}
	
	/**
	 * 获取当前时间是一天当中多少秒。
	 */	
	public static int getSecondOfDay() {
		Date nowDate=new Date();		
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(nowDate);			
		String zeroHour=DateToStr3(aCalendar.getTime())+" 00:00:00.000";		
		Date zeroDate=StrToDate3(zeroHour);			
		int second=(int)((nowDate.getTime()-zeroDate.getTime())/1000);
		return second;
	}
	
	/**
	 *  离24点还有多少是秒
	 * @return
	 */
	public static long secondTo24H() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.add(Calendar.DAY_OF_MONTH, 1);
        int month = calendar.get(Calendar.MONTH) + 1;//获取月份
        String monthStr = month < 10 ? "0" + month : month + "";
        int day = calendar.get(Calendar.DATE);//获取日
        String dayStr = day < 10 ? "0" + day : day + "";	        
        String tomorrowStr=calendar.get(Calendar.YEAR)+"-"+monthStr+"-"+dayStr+" 00:00:00";	        	        
		Date tomorrow=null;
		tomorrow = StrToDate(tomorrowStr);
		monthStr=null;
		tomorrowStr=null;
		dayStr=null;
		if(tomorrow==null) {
			return 8*3600l;
		}else {
			return (tomorrow.getTime()-System.currentTimeMillis())/1000;
		}
		
	}
	
	public static void main(String[] args) {	

		
	}
}
