package com.contactsImprove.utils;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.contactsImprove.constant.SystemConst;

public class OrderUtil {
	
	static int appId=1000000;
	
	static byte[] lock=new byte[0];
	
	public static String createParaOrderStr(TreeMap<String, String> paramap){
	    StringBuilder sbkey = new StringBuilder();
	    Set es = paramap.entrySet();  
		Iterator it = es.iterator();
	    while(it.hasNext()) {
	        Map.Entry entry = (Map.Entry)it.next();
	        String k = (String)entry.getKey();
	        Object v = entry.getValue();
	        //空值不需要
	        if(null != v && !"".equals(v)) {
	            sbkey.append(k + "=" + v + "&");
	        }
	    }
	    return sbkey.toString().substring(0,sbkey.length()-1);
	}
	
	public static String createParaOrderStrByEncode(TreeMap<String, String> paramap){
	    StringBuilder sbkey = new StringBuilder();
	    Set es = paramap.entrySet();  
		Iterator it = es.iterator();
	    while(it.hasNext()) {
	        Map.Entry entry = (Map.Entry)it.next();
	        String k = (String)entry.getKey();
	        Object v = entry.getValue();
	        //空值不需要
	        if(null != v && !"".equals(v)) {
	            try {
					sbkey.append(k + "=" + java.net.URLEncoder.encode(v+"", RSAUtils.CHARSET) + "&");
				} catch (UnsupportedEncodingException e) {
					LoggerUtil.error(e.toString(),e);
				}
	        }
	    }
	    return sbkey.toString().substring(0,sbkey.length()-1);
	}
	
	public static String createAppId() {
		String uuId=UUID.randomUUID().toString();
		uuId=uuId.substring(0, 17).replace("-", "");
		synchronized(lock) {
			appId++;
		}				
		return uuId+appId;		
	}
	
	public static String hasOrderSign(String orderId) {
		String sign=RedisUtil.getValue(orderId);		
		if(RedisUtil.NULL.equals(sign)) {
			return "";
		}else {
			RedisUtil.delValue(orderId);
			return sign;
		}		
	}	
	public static void setOrderSign(String orderId,String value) {
		RedisUtil.setValueByTime(orderId, value,SystemConst.Order_TimeOut);
	}
}
