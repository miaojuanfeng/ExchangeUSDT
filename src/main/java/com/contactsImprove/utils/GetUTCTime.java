package com.contactsImprove.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GetUTCTime {
	
	 // 取得本地时间：
    private static Calendar cal = Calendar.getInstance();
    // 取得时间偏移量：
    private static int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
    // 取得夏令时差：
    private static int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

    public static String getUTCTimeStr() {
    	
    	cal = Calendar.getInstance();
        // 从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        long mills = cal.getTimeInMillis();

        cal.setTimeInMillis(mills);

        SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = foo.format(cal.getTime());

        // 从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, (zoneOffset + dstOffset));
        time = foo.format(cal.getTime());
        
        return time;

    }


}
