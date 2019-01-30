package com.contactsImprove.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//生成唯一订单编号
public class OrderNumEngender {
	
	static byte[] lock=new byte[0];
	static int min=1000000;
	static int Counter=100000000;
	static int max=	   1000000000;
	
    private static final String date_format = "yyMMddHHSSS";
    
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>(); 
	
	
	public static String getOrderNum()  {		
		String date="";
		try {
			date = formatDate(new Date());
		} catch (ParseException e) {
			LoggerUtil.error("getOrderNum", e);
		}
		synchronized(lock) {
			Counter++;
			if(Counter==max) {
				Counter=min;
			}			
		}		
		return date+Counter;
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
