package com.contactsImprove.utils;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.contactsImprove.constant.SystemConst;


public class SMSUtil {
	// 替换模板格式
	public static final java.util.regex.Pattern SMS_VARIABLE_PTTERN=java.util.regex.Pattern.compile("【变量】");
	final static String chars = "1234567890";
	final static String smsUrl="http://service.winic.org/sys_port/gateway/index.asp";
	final static String id="penganqiang";
	final static String pwd="Paj166188";
	final static String validateTemplet="您好，您的验证码是 【变量】【otcpay】";
	/* 付款提示（给卖家）: *****已确认付款（订单:**********，付款参号：*****），共计280.89 CNY，请注意查收。*/	
	final static String payRemind="您的订单：【变量】，客户通过【变量】已经付款，收款号：【变量】共收到【变量】【变量】，请注意查收。【变量】【otcpay】";
	final static String buyRemind="您的订单：【变量】，客户通过【变量】已经下单，共计【变量】【变量】，请尽快付款,付款超时是"+SystemConst.Order_TimeOut/60+"分钟。【变量】【otcpay】";
	final static String charSet="gb2312";
	final static String success="000/";  //提交成功
	final static String lessMoney="-01/";   //余额不足
	
	final static java.util.regex.Pattern ChinaPhonePattern=Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");
	
	public static boolean sentPayRemind(String phoneNumber,String...templetParas){
		String content=payRemind;
		int index=0;					
		Matcher matcher=SMS_VARIABLE_PTTERN.matcher(content);
		while(matcher.find()){
			content=content.replaceFirst(matcher.group(0), templetParas[index]);
			index++;
		}		
		return sendSMS(phoneNumber,content);
	}
	
	public static boolean sentBuyRemind(String phoneNumber,String...templetParas){
		String content=buyRemind;
		int index=0;					
		Matcher matcher=SMS_VARIABLE_PTTERN.matcher(content);
		while(matcher.find()){
			content=content.replaceFirst(matcher.group(0), templetParas[index]);
			index++;
		}		
		return sendSMS(phoneNumber,content);
	}
	
	/**
	 * 验证手机号
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isChinaPhoneLegal(String phone) {
		Matcher m = ChinaPhonePattern.matcher(phone);
		return m.matches();
	}
	
	/**
	 * 随机生成6位数字
	 * 
	 * @return
	 */
	public static String getSMSCode() {		
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			Random r = new Random();
			code.append(chars.charAt(r.nextInt(chars.length() - 1)));
		}
		return code.toString();
	}
	
	public static boolean sendValidateCode(String phoneNumber) {
		String code=getSMSCode() ;
		String content=validateTemplet;
		Matcher matcher=SMS_VARIABLE_PTTERN.matcher(content);
		if(matcher.find()) {
			content=content.replace(matcher.group(), code);
		}
		return sendSMS(phoneNumber,content,code);
	}
	
	public static boolean sendSMS(String phoneNumber,String content,String...code) {
		try {
			StringBuilder para=new StringBuilder();
			para.append("id="+id);
			para.append("&pwd="+pwd);
			para.append("&to="+phoneNumber);
			para.append("&content="+URLEncoder.encode(content,charSet));
			para.append("&time="+new Date());	
			String body="";
			if(SystemConst.hasLocal) {
				body=success;
			}else {
				body=HttpUtil.post(smsUrl, para.toString());
			}
			if(body.indexOf(success)>-1) {
				if(code!=null && code.length>=1) {
					LoggerUtil.info("发送短信至："+phoneNumber+" | code:"+code[0]);
					RedisUtil.setValueByTime(SystemConst.SMSKey+phoneNumber, code[0], 5*60);	
				}else {
					LoggerUtil.info("发送短信至："+phoneNumber+" | content:"+content);
				}
				return true;
			}else if(body.indexOf(lessMoney)>-1) {
				LoggerUtil.error("SMS账号余额不足!");
				return false;
			}
		}catch(Exception e) {
			LoggerUtil.error("发送验证码", e);
		}
		return false;
	}

}
