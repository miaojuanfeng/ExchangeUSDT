package com.contactsImprove.utils;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.contactsImprove.constant.SystemConst;

public class Tools {
	static String http="http";
	static String https="https";
	
	/**
	 * 手机回调验签
	 * @param amount
	 * @param paymentName
	 * @param paymentNumberSuffix
	 * @param sign
	 * @return
	 */
	public static boolean verifySign(String amount,String paymentName,
			String paymentNumberSuffix,String sign) {
		String line=SystemConst.phoneCallbackKey+paymentName+paymentNumberSuffix+amount;		
		if(sign.equals(Md5Util.execute(line))){
			line=null;
			return true;
		}
		line=null;
		return false;
	}
	/**
	 * 手机回调验签
	 * @param amount
	 * @param paymentName
	 * @param paymentNumberSuffix
	 * @param sign
	 * @return
	 */
	public static boolean verifySign(String amount,String paymentNumberSuffix,String sign) {
		String line=SystemConst.phoneCallbackKey+paymentNumberSuffix+amount;		
		if(sign.equals(Md5Util.execute(line))){
			line=null;
			return true;
		}
		line=null;
		return false;
	}
	/**
	 * 随机最大最小
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomNumberInRange(int min, int max) {	
		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt(); 
	}
	/**
	 * 默认随机最大最小
	 * @return
	 */
	public static int getRandomNumberInRange() {		
		return getRandomNumberInRange(SystemConst.randomMoneyMin,SystemConst.randomMoneyMax);
	}	
	/**
	 * 获取网址前缀
	 */
	public static String getUrlHead(HttpServletRequest request,String resourceUrl) {
		String scheme=http;
		if(request.getServerPort()==SystemConst.sslPort) {
			scheme=https;
		}		
		String redirectUrl=scheme+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+resourceUrl;
		return redirectUrl;
	}
}
