package com.contactsImprove.notefield;

import java.lang.reflect.Field;
import java.util.Date;

import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.utils.DateTools;
import com.contactsImprove.utils.LoggerUtil;
import com.contactsImprove.utils.StringUtil;

public class DynamicColumn {
	
	public static void appendWhere(StringBuilder sb ,Object pojo) {
		boolean appendWhere=sb.toString().toLowerCase().indexOf("where")==-1 ? true : false;
		boolean isFirst=true;
		Class<?> c=pojo.getClass();
		Field[] fields=c.getDeclaredFields();	
		for(Field field : fields) {
			field.setAccessible(true);
			Class<?> type=field.getType();
			ColumnMapping cm=field.getAnnotation(ColumnMapping.class);
			if(cm!=null) {				
				Object value=null;				
				try {
					value=field.get(pojo);
				} catch (IllegalArgumentException | IllegalAccessException e) {				
					LoggerUtil.error(e.getMessage(), e);
				}
				if(!StringUtil.isBlank(value)) {
					String columnName=cm.columnName();
					if(StringUtil.isBlank(columnName)){
						columnName=recursionCharacter(field.getName());
					}				
					if(isFirst && appendWhere) {
						sb.append(" where ");
						isFirst=false;
					}else {
						sb.append(" and ");
					}
					if(cm.operatorName().equals(SystemConst.sqlLike)) {
						sb.append(cm.operatorName()+"('"+value+"',"+columnName+")>0");												
					}else if(cm.operatorName().equals(SystemConst.sqlBetween)){
						 if(type == Date.class) {
							Date d=(Date)value;
							java.util.Calendar calendar= java.util.Calendar.getInstance();
							calendar.setTime(d);
							calendar.add(java.util.Calendar.DATE, 1);						
							sb.append(columnName+" "+cm.operatorName()+" '"+DateTools.DateToStr2(d)+"' and '"+DateTools.DateToStr2(calendar.getTime())+"'");
						 }
					} else{
						sb.append(columnName+" "+cm.operatorName()+" ");
						if(type==String.class ) {
							sb.append("'"+value+"'");
						}else if(type == Date.class) {
							Date d=(Date)value;
							sb.append("'"+DateTools.DateToStr2(d)+"'");
							d=null;
						}else{
							sb.append(value);
						}
					}
					columnName=null;
				}				
			}
			type=null;
			cm=null;
		}
		c=null;
		fields=null;
	}
	
	public static String recursionCharacter(String str) {
		if(str.length()<1) {
			return str;
		}
		char c=str.charAt(0);
		StringBuilder t=new StringBuilder();
		if(Character.isUpperCase(c)) {
			t.append("_"+Character.toLowerCase(c));
		}else {
			t.append(c);
		}
		return t+recursionCharacter(str.substring(1));		
	}	

}
