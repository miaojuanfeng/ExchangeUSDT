package com.contactsImprove.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class StringUtil {
	
	public static String listToString(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				sb.append(list.get(i));
			} else {
				sb.append(list.get(i));
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 判断String是否为空
	 * @param obj
	 * @return
	 */
	public static final boolean isBlank(Object obj) {
		return obj==null || "".equals(obj) || "null".equals(obj);
	}

	/**
	 * 判断是否有内容
	 * @param obj
	 * @return
	 */
	public static final boolean hasText(Object obj) {
		return !isBlank(obj);
	}
	


	/**
	 * Object转换为String 
	 * @param obj
	 * @return
	 */
	public static final String toString(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}

	/**
	 * 数组转换为String
	 * @param array
	 * @return
	 */
	public static final String toString(Object[] array) {
		StringBuilder sb = new StringBuilder();
		if (array != null && array.length > 0) {
			sb.append(array[0]);
			for (int i = 1; i < array.length; i++) {
				sb.append(",").append(array[i]);
			}
		}
		return sb.toString();
	}
	
	/**
	 * List对象转换成 String
	 * @param list
	 * @return
	 */
	public static final String toString(List<Object> list) {
		StringBuilder sb = new StringBuilder();
		if (list != null && !list.isEmpty()) {
			sb.append(list.get(0));
			for (int i = 1; i < list.size(); i++) {
				sb.append(",").append(list.get(i));
			}
		}
		return sb.toString();
	}

	/**
	 * 去换 String 左右空格
	 * @param obj
	 * @return
	 */
	public static final String trim(Object obj) {
		return obj == null ? "" : String.valueOf(obj).trim();
	}

	/**
	 * Object对象用分隔符转List
	 * @param obj
	 * @param separtor
	 * @return
	 */
	public static final List<String> toList(Object obj, String separtor) {
		List<String> list = new ArrayList<String>();
		if (hasText(obj)) {
			String[] temp = trim(obj).split(separtor);
			for (String str : temp) {
				if (hasText(str)) {
					list.add(str);
				}
			}
		}
		return list;
	}

	/**
	 * Object 转 List
	 * @param obj
	 * @return
	 */
	public static final List<String> toList(Object obj) {
		return toList(obj, ",");
	}

	/**
	 * Object对象用分隔符转 Array
	 * @param obj
	 * @param separtor
	 * @return
	 */
	public static final String[] toArray(Object obj, String separtor) {
		return toList(obj, separtor).toArray(new String[0]);
	}

	/**
	 * Object对象转Array
	 * @param obj
	 * @return
	 */
	public static final String[] toArray(Object obj) {
		return toList(obj, ",").toArray(new String[0]);
	}

	/**
	 * 转小写字符
	 * @param obj
	 * @return
	 */
	public static final String toLowerCase(Object obj) {
		if (hasText(obj)) {
			return toString(obj).toLowerCase();
		} else {
			return null;
		}
	}

	/**
	 * 转大写字符
	 * @param obj
	 * @return
	 */
	public static final String toUpperCase(Object obj) {
		if (hasText(obj)) {
			return toString(obj).toUpperCase();
		} else {
			return null;
		}
	}

	/**
	 * 比较对象
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static final boolean equals(Object obj1, Object obj2) {
		boolean equals = false;

		if (obj1 != null && obj2 != null) {
			equals = obj1.equals(obj2);
		}

		return equals;
	}

	/**
	 * 忽略大小写比较
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static final boolean equalsIgnoreCase(Object obj1, Object obj2) {
		boolean equals = false;

		if (obj1 != null && obj2 != null) {
			equals = obj1.toString().equalsIgnoreCase(obj2.toString());
		}

		return equals;
	}

	
	public static String beanToString(Object bean) {
		return ToStringBuilder.reflectionToString(bean,ToStringStyle.MULTI_LINE_STYLE);
	}

	public static String beanToString(Object bean, ToStringStyle toStringStyle) {
		return ToStringBuilder.reflectionToString(bean, toStringStyle);
	}

	public static String TransactSQLInjection(String sql) {
		return sql.replaceAll(".*([';]+|(--)+).*", " ");
	}

	/**
	 * 根据request对象获得IP地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * String 类型转Integer
	 * @param str
	 * @return
	 */
	public static final Integer toInteger(String str) {
		Integer it = Integer.valueOf(str);
		return it;
	}
	
	/**
	 * double 类型转 String
	 * @param d
	 * @return
	 */
	public static final String doubletoStr(double d) {
		DecimalFormat format= new DecimalFormat("#.00");
		String t_str=format.format(d);
		return t_str;
	}
	
	/**
	 * String 类型转 double
	 * @param str
	 * @return
	 */
	public static final double toDouble(String str) {
		double d = Double.valueOf(str);
		return d;
	}
	
	/**
	 * 将以逗号分隔的字符串转换成字符串数组
	 * @param valStr
	 * @return String[]
	 */
	public static String[] StrList(String valStr){
	    int i = 0;
	    String TempStr = valStr;
	    String[] returnStr = new String[valStr.length() + 1 - TempStr.replace(",", "").length()];
	    valStr = valStr + ",";
	    while (valStr.indexOf(',') > 0)
	    {
	        returnStr[i] = valStr.substring(0, valStr.indexOf(','));
	        valStr = valStr.substring(valStr.indexOf(',')+1 , valStr.length());
	        
	        i++;
	    }
	    return returnStr;
	}
	
	/**获取字符串编码
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {      
	       String encode = "GB2312";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s = encode;      
	              return s;      
	           }      
	       } catch (Exception exception) {      
	       }      
	       encode = "ISO-8859-1";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s1 = encode;      
	              return s1;      
	           }      
	       } catch (Exception exception1) {      
	       }      
	       encode = "UTF-8";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s2 = encode;      
	              return s2;      
	           }      
	       } catch (Exception exception2) {      
	       }      
	       encode = "GBK";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s3 = encode;      
	              return s3;      
	           }      
	       } catch (Exception exception3) {      
	       }      
	      return "";      
	   } 
	
	/**
	 * 取得ASCII码
	 * <一句话功能简介>
	 * <功能详细描述>
	 * @param value
	 * @return [参数说明]
	 *
	 * @author XIECHENGHAO
	 * @see [类、类#方法、类#成员]
	 */
	public static String stringToAscii(String value){    
	    StringBuffer sbu = new StringBuffer();  
	    char[] chars = value.toCharArray();   
	    for (int i = 0; i < chars.length; i++) {  
	        if(i != chars.length - 1)  
	        {  
	            sbu.append((int)chars[i]).append(",");  
	        }  
	        else {  
	            sbu.append((int)chars[i]);  
	        }  
	    }  
	    return sbu.toString();  
	}  
	
	/** 
     * 将double格式化为指定小数位的String，不足小数位用0补全 
     * 
     * @param v     需要格式化的数字 
     * @param scale 小数点后保留几位 
     * @return 
     */  
    public static String roundByScale(double v, int scale) {  
        if (scale < 0) {  
            throw new IllegalArgumentException(  
                    "The   scale   must   be   a   positive   integer   or   zero");  
        }  
        if(scale == 0){  
            return new DecimalFormat("0").format(v);  
        }  
        String formatStr = "0.";  
        for(int i=0;i<scale;i++){  
            formatStr = formatStr + "0";  
        }  
        return new DecimalFormat(formatStr).format(v);  
    }  
    
}
