package com.contactsImprove.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//生成唯一订单编号
public class UserIdEngender {
	
	static byte[] lock=new byte[0];
	
	static long Counter=1000l;
	
    private static final String date_format = "yymmssSSS";
    
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>(); 
	
	
	public static Long createUserId()  {		
		String date="";
		try {
			date = formatDate(new Date());
		} catch (ParseException e) {
			LoggerUtil.error("createUserId", e);
		}
		synchronized(lock) {
			Counter++;
			if (Counter >= 10000L) {
				Counter = 1000L;
			}
		}		
		
		return Long.parseLong(date+Counter);
	}
	
    private static DateFormat getDateFormat()   
    {  
        DateFormat df = threadLocal.get();  
        if(df==null){
        	synchronized(lock) {
        		if(df==null){
		            df = new SimpleDateFormat(date_format);  
		            threadLocal.set(df);
        		}
        	}
        }  
        return df;  
    }  

    private static String formatDate(Date date) throws ParseException {
        return getDateFormat().format(date);
    }
}
