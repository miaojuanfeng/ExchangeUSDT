package com.contactsImprove.controller.api;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.beanutils.BeanUtils;

import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.constant.MerchantOrderConst.Payment;
import com.contactsImprove.entity.api.NotifyPara;
import com.contactsImprove.entity.api.Orders;
import com.contactsImprove.entity.api.Users;
import com.contactsImprove.utils.DateTools;
import com.contactsImprove.utils.HttpUtil;
import com.contactsImprove.utils.Md5Util;
import com.contactsImprove.utils.OrderUtil;
import com.contactsImprove.utils.RSAUtils;
import com.contactsImprove.utils.SMSUtil;
import com.contactsImprove.utils.Tools;

import net.sf.json.JSONObject;

public class Test {
	static String privateKey="MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCESQ77aXsvaH3LQ8MlbdiFIn2sxmYmKdXkXplX9Ib27GbIIpBnjMzzUGWX4uAFgJi6mi/x0GZ8MLbbIRggmMW5lNiGQIwAYHSvFKVSAM3YvoQfhONoo3Ys6Aa/Zf3Y6zbWHKoBR4Bf6PYJlM0XR5V+s30zxkoxiC+OM+Oyj26GYmGr7GpmHa2F3YxClb6unI9yTAUOk90hS9sdq47Ej8Hu0DkmCOjlELIhZ1nGwIWVMhZQrVCLUx/v6bc3U0sA56gs078Hjpiw0ZzQkeFItmJc5fj9DfbfFjNoiipC7B/3C4jJsctrjEWpRet+SO+puwWVCC56Wvd9szLYZFBSDEp9AgMBAAECggEAeK1VV5EwaV0/dJDKk3iTz1BZqaYMRcBgeca6yPwvq06ZrPWrGmveBBFRvK+hgiOAMKq8FjLxa1XdWiXkpsf3iIb6x+5WSXm0uItzDU3ie1nBmidWdXy5L9ozXaOjYaGiEs1vYV9jAxu4Z6ExP410GXOQQg0gXfNIvscjbx9dyoThUign4eQPR7TLem1n7rjzLDlrqVQe6X1+TSXIWFkTaJMK++vtLDKKeYswb3U4YL6H9/e1kw+iQyq+SNPNOYU5YjX13yzTNgU5ld/Z2AeLEh0C5aWGZnxEVAlp3p4q5DgwJGF3RJe1P3OOg3nhNyotuc2mLoZZeaCPONtU5MVQGQKBgQDAZ1k98x0dD4JCDWlQ/4AZ/qOmzdL5o7aFYei+LRfmDA+H0RKm8w88bi/WHZgaGj4GgKtPnkC4vts4nGJU8NFmKx2UQGkLY1y0Ku3gprxPiBTu4LOfQwImwMYFH8c322NX/h4p6xtbv3yUY4dYBPqiQb4zAGflR9c/T//diQxc+wKBgQCwAqtmknLybyDcrdW1hp4MP4J7aOSvQtm8SHt2dwFI1j9c57EH9XMZe960/GZ3QXovqNImj54gKv9mFrS/kuPC526OO+LwwoOzRbGAFuS+qsAc6gnOrFjJx1nTLktPJ9Al6qpNC2T1kE7BzlbMPzq16lvFAmU+qDIDvyfv5Pfs5wKBgATkG66peXn9pPAM6zp15EnE4WAiJ1TVRpSJcqMy/kKI1/Q56mF4GFEO9ARbaDxxolYD+1bgzQBlSalcwFssbAZO3kegg4sYkYi69qUZV/wll3KjH63SxEE78Uy8DBJtb08TVKz4Hw/sBDy5cv1X0w9A4JbnUbzQYLZpUDXPgBL1AoGAUXba2B2+YCE5R4qZ2GvXKPoYLYBuIARD/NZbdog8Kvcm2pxOHs3nGQtxF7mev2Gp6PctVTWxzVe5YAnwXJ7jFinBzMlBD4goJsiCEDQIaYtkYb2dDnBMpA/Frm3F43wc5f/IFOLKNrI1EZSsLdqyFNLE6Nlj/O0iQQOvu2PxsE8CgYBmgCMr7A19G9Vdpe05+J+s2KGvc9TLbeH3zr/g4GcEOuzr6lUUuJrv6x3yY/1lNmbtsJG+7IbEWNNd4WZU7A9brcL1+oRSSB/Qf3p7J/Cq0UVKSQUYcY42ohtllW7fb91osD5e9ydt/GvmeMybTUWAa4DTHRkzsfMAV2eiPLGcIw==";
	static String publicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhEkO+2l7L2h9y0PDJW3YhSJ9rMZmJinV5F6ZV/SG9uxmyCKQZ4zM81Bll+LgBYCYupov8dBmfDC22yEYIJjFuZTYhkCMAGB0rxSlUgDN2L6EH4TjaKN2LOgGv2X92Os21hyqAUeAX+j2CZTNF0eVfrN9M8ZKMYgvjjPjso9uhmJhq+xqZh2thd2MQpW+rpyPckwFDpPdIUvbHauOxI/B7tA5Jgjo5RCyIWdZxsCFlTIWUK1Qi1Mf7+m3N1NLAOeoLNO/B46YsNGc0JHhSLZiXOX4/Q323xYzaIoqQuwf9wuIybHLa4xFqUXrfkjvqbsFlQguelr3fbMy2GRQUgxKfQIDAQAB";

	
	/**	 * 	客户付款给币商，标记订单已付款。
	 * tradeNumber:交易流水号
	 * outTradeNo:商户订单号
	 * merchantUserId:商户号
	 * timestamp：时间戳
	 * version:版本号
	 * paymentId:支付方式id
	 * @param Orders
	 * @return
	 */
	public static void main(String[] args) throws Exception{
		java.util.Random random=new java.util.Random();
		
		for(int i=0;i<10;i++) {
			System.out.println(ThreadLocalRandom.current().nextInt(10));
		}
		
//scheme=alipays://platformapi/startapp?saId=10000007&qrcode=%68%74%74%70%73%3A%2F%2F%71%72%2E%61%6C%69%70%61%79%2E%63%6F%6D%2F%62%61%78%30%38%35%30%34%68%68%6A%31%63%68%75%6B%6D%77%66%77%34%30%65%32%3F%5F%73%3D%77%65%62%2D%6F%74%68%65%72

//		String orderStr="tradeNumber=2019010921264207910000000007&merchantUserId=1828196931001&outTradeNo=1123&version=1.0&timestamp=2019-01-09 21:27:10";
//		String sign="P8dGllwylrC6zssn%2FHzxfu1hy4gCH2opx9dyMQ64a%2FfW4OIwvDGZnlvK5SSXycb3MXZPSEfrYWrFbMaNH1YTKsy7eb81RqxN7izNjC3vWl8pxEMfyCVdJKx6gJRRQnyKcI8adK%2BsQD68%2F%2FYATnChDsSGZJoPgBnzYM0UvooA%2Bb5O1KiQ2SVV6mpiUdSS1xWFYdRZya7DFpP5doMdWYqhIH7Vz0Paxp2Plv9e9pK71VLJhsV6xb1omIFVcSlEYEBfQc5FPM%2BRmNaqoRLIImpLkFOc6nuPDy19OGN7nAN1nW4Sjcc0cl3AWkILE1pQrPJRZHWgLFhXpWgwJz6CMqmMaA%3D%3D";	
//		String sign1=RSAUtils.signature(orderStr, privateKey);
//		System.out.println(java.net.URLDecoder.decode(sign, "UTF-8"));
//		System.out.println(sign1);
	//	sign1=java.net.URLEncoder.encode(sign1, "UTF-8");		
//		JSONObject json=new JSONObject();
//		json.put("payImage", "http://支付二维码图片路径");
//		json.put("bankName", "开户行");
//		json.put("branchNumber", "支行代号");
//		json.put("ownerName", "持有者名字");
//		System.out.println(json.toString());
		//sign1=java.net.URLEncoder.encode(json.toString(),"UTF-8");
		//System.out.println(sign1);
//		System.out.println(RSAUtils.verifySignature(orderStr,sign,publicKey));				
//		String url="https://www.saintenjoy.online/dist/dist/g.css";
//		java.util.regex.Pattern p=java.util.regex.Pattern.compile("/dist/([^\\.(gif|jpg|jpeg|png|bmp|swf|js|css)]?.*\\.(gif|jpg|jpeg|png|bmp|swf|js|css))$");
//		java.util.regex.Matcher m=p.matcher(url);
//		if(m.find()) {
//			System.out.println(m.group(0));
//		}		
//		transferUsdt();
//		createOrder();
//		orderPay();
//		System.out.println(new BigDecimal("20").divide(new BigDecimal("7"),6, BigDecimal.ROUND_HALF_UP));				
	}
	
	public static void orderPay() throws Exception{
		
		String url="http://localhost:8080/ContactsImprove/api/orderPay";	
		TreeMap<String, String> paramap = new TreeMap<String, String>();
		
		paramap.put("tradeNumber", "2018120514102161610000000001");
		paramap.put("outTradeNo", "135699");
		paramap.put("merchantUserId", "2018112710100000002");
		paramap.put("version", "1.0");
		paramap.put("timestamp", DateTools.DateToStr2(new Date()));

		String line=OrderUtil.createParaOrderStr(paramap);		
		String sign=RSAUtils.signature(line, privateKey);
		System.out.println(sign);
		System.out.println(Base64.getEncoder().encodeToString(sign.getBytes()));
		
		//sign 签名传输的时候要进行 URLEncoder.encode,或是 设置http请求头 : Content-Type: application/x-www-form-urlencoded
//		String orderStr=line+"&sign="+java.net.URLEncoder.encode(sign, RSAUtils.CHARSET);
////		System.out.println(line);
//		System.out.println(RSAUtils.verifySignature(line,sign,publicKey));
//		String result=HttpUtil.post(url, orderStr.toString());		
//		System.out.println(result);		
	}
	
	/**
	 * 创建订单，返回币商银行账号，二维付款码。
	 * outTradeNo,商户订单号
	 * merchantUserId,商户账号
	 * currencyType,电子币类型
	 * amount ,充值金额
	 * version ,版本号
	 * subject,主题
	 * notifyUrl,回调url
	 * timestamp,时间戳
	 * paymentType,支付方式。
	 * 可选参数有：
	 * merchantAccount: 商户平台用户的唯一标示。 
	 * body,交易描述	 *
	 * @param Orders
	 * @return
	 */	
	public static void createOrder() throws Exception{
		
		String url="http://localhost:8080/ContactsImprove/api/createOrder";	
		TreeMap<String, String> paramap = new TreeMap<String, String>();
		paramap.put("outTradeNo", SMSUtil.getSMSCode());
		paramap.put("merchantUserId", "2018112710100000002");
		paramap.put("currencyType", "USDT");
		paramap.put("amount", "200");
		paramap.put("version", "1.0");
		paramap.put("subject", "购买USDT");
		paramap.put("notifyUrl", "http://localhost:8080/ContactsImprove/api/notify");
		paramap.put("timestamp", DateTools.DateToStr2(new Date()));
		paramap.put("paymentType", Payment.alipay.code);
		paramap.put("merchantAccount", "flootball");	
		String line=OrderUtil.createParaOrderStr(paramap);		
		String sign=RSAUtils.signature(line, privateKey);
		//sign 签名传输的时候要进行 URLEncoder.encode,或是 设置http请求头 : Content-Type: application/x-www-form-urlencoded
		String orderStr=line+"&sign="+java.net.URLEncoder.encode(sign, RSAUtils.CHARSET);
//		System.out.println(line);
		System.out.println(RSAUtils.verifySignature(line,sign,publicKey));
		String result=HttpUtil.post(url, orderStr.toString());		
		System.out.println(result);		
	}
	
	public static void transferUsdt() throws Exception{		
		String url="http://localhost:8080/ContactsImprove/api/transferUsdt";	
		TreeMap<String, String> paramap = new TreeMap<String, String>();
		paramap.put("outTradeNo", SMSUtil.getSMSCode());
		paramap.put("merchantUserId", "2018112710100000002");
		paramap.put("currencyType", "USDT");
		paramap.put("currencyAmount", "200");
		paramap.put("version", "1.0");
		paramap.put("subject", "提币USDT");
		paramap.put("notifyUrl", "http://localhost:8080/ContactsImprove/api/notify");
		paramap.put("timestamp", DateTools.DateToStr2(new Date()));
		paramap.put("merchantAccount", "flootball");	
		paramap.put("toAddress", "oxfdkfakdfjaljderuoejfdkjfpe");
		String line=OrderUtil.createParaOrderStr(paramap);		
		String sign=RSAUtils.signature(line, privateKey);
		//sign 签名传输的时候要进行 URLEncoder.encode,或是 设置http请求头 : Content-Type: application/x-www-form-urlencoded
		String orderStr=line+"&sign="+java.net.URLEncoder.encode(sign, RSAUtils.CHARSET);
//		System.out.println(line);
		System.out.println(RSAUtils.verifySignature(line,sign,publicKey));
		String result=HttpUtil.post(url, orderStr.toString());		
		System.out.println(result);		
	}
}
