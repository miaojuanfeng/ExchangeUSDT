package com.contactsImprove.utils;

public class Idempotent {	
	static String complainOrder ="complainOrder";
	static byte[] complainOrderLock=new byte[0];
	static String MoneyMerchantComplain ="MoneyMerchantComplain";
	static byte[] MoneyMerchantComplainLock=new byte[0];	
	static String transferUsdt ="transferUsdt";
	static byte[] transferUsdtLock=new byte[0];
	static String confirmPay ="confirmPay";
	static byte[] confirmPayLock=new byte[0];				
	static String phoneCallback ="phoneCallback";
	static byte[] phoneCallbackLock=new byte[0];			
	static String supplement ="supplement";
	static byte[] supplementLock=new byte[0];	
	static String orderPay ="orderPay";
	static byte[] orderPayLock=new byte[0];	
	static String onlineOrderPay ="onlineOrderPay";
	static byte[] onlineOrderPayLock=new byte[0];
	static String createOrder ="createOrder";
	static byte[] createOrderLock=new byte[0];		
	static String testOrder ="testOrder";
	static byte[] testOrderLock=new byte[0];	
	static String qrCodePay="qrCodePay";
	static byte[] qrCodePayLock=new byte[0];	
	static String createOnLineOrder="createOnLineOrder";
	static byte[] createOnLineOrderLock=new byte[0];		
	static String randomMinusOnLineOrder="randomMinusOnLineOrder";
	static byte[] randomMinusOnLineOrderLock=new byte[0];		
	static String withdrawUSDT ="withdrawUSDT";
	static byte[] withdrawUSDTLock=new byte[0];	
	static String verifyOrder ="verifyOrder";
	static byte[] verifyOrderLock=new byte[0];		
	static String register ="register";
	static byte[] registerLock=new byte[0];
	static String withdrawal ="withdrawal";
	static byte[] withdrawalLock=new byte[0];
	static String testWithdrawal ="testWithdrawal";
	static byte[] testWithdrawalLock=new byte[0];		
	static String confirmWithdrawal ="confirmWithdrawal";
	static byte[] confirmWithdrawalLock=new byte[0];	
	static String value="1";
	static int timeout=15*60;
			
	/**
	 * 商户确认出金
	 * @param line
	 * @return
	 */
	public static boolean fx_confirmWithdrawal(String line) {	
		String md5Key=Md5Util.execute(confirmWithdrawal+line);
		synchronized(confirmWithdrawalLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}		
	}
	/**
	 * 测试版客户出金
	 * @param line
	 * @return
	 */
	public static boolean fx_testWithdrawal(String line) {	
		String md5Key=Md5Util.execute(testWithdrawal+line);
		synchronized(testWithdrawalLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}		
	}
	/**
	 * 客户出金
	 * @param line
	 * @return
	 */
	public static boolean fx_withdrawal(String line) {	
		String md5Key=Md5Util.execute(withdrawal+line);
		synchronized(withdrawalLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}		
	}
	/**
	 * 币商申诉 MoneyMerchantComplain
	 */
	public static boolean fx_MoneyMerchantComplain(String line) {	
		String md5Key=Md5Util.execute(MoneyMerchantComplain+line);
		synchronized(MoneyMerchantComplainLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}		
	}	
	/**
	 * 订单申诉
	 * @param line
	 * @return
	 */
	public static boolean fx_complainOrder(String line) {	
		String md5Key=Md5Util.execute(complainOrder+line);
		synchronized(complainOrderLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}		
	}
	
	/**
	 * 客户提币
	 * @param line
	 * @return
	 */
	public static boolean fx_transferUsdt(String line) {	
		String md5Key=Md5Util.execute(transferUsdt+line);
		synchronized(transferUsdtLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}		
		
	}	
	
		
	/**
	 * 币商确认付款
	 * @param line
	 * @return
	 */
	public static boolean fx_supplement(String line) {	
		String md5Key=Md5Util.execute(supplement+line);
		synchronized(supplementLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}	
	}	
	
	/**
	 * 手机回调确认付款
	 * @param line
	 * @return
	 */
	public static boolean fx_phoneCallback(String line) {	
		String md5Key=Md5Util.execute(phoneCallback+line);
		synchronized(phoneCallbackLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}	
	}	
	/**
	 * 币商确认付款
	 * @param line
	 * @return
	 */
	public static boolean fx_confirmPay(String line) {	
		String md5Key=Md5Util.execute(confirmPay+line);
		synchronized(confirmPayLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}	
	}		
	/**
	 * 客户确认付款
	 * @param line
	 * @return
	 */
	public static boolean fx_orderPay(String line) {	
		String md5Key=Md5Util.execute(orderPay+line);
		synchronized(orderPayLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}
	}	
	
	
	
	/**
	 * 网页版客户确认付款
	 * @param line
	 * @return
	 */
	public static boolean fx_onlineOrderPay(String line) {	
		String md5Key=Md5Util.execute(onlineOrderPay+line);
		synchronized(onlineOrderPayLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}
	}		
	/**
	 * 提币订单审核
	 * @param line
	 * @return
	 */
	public static boolean fx_verifyOrder(String line) {	
		String md5Key=Md5Util.execute(verifyOrder+line);
		synchronized(verifyOrderLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}	
	}	
	
	/**
	 * 提币
	 * @param line
	 * @return
	 */
	public static boolean fx_withdrawUSDT(String line) {	
		String md5Key=Md5Util.execute(withdrawUSDT+line);
		synchronized(withdrawUSDTLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}	
	}	
	
	/**
	 * 网页版创建订单
	 * @param line
	 * @return
	 */
	public static boolean fx_qrCodePay(String line) {	
		String md5Key=Md5Util.execute(qrCodePay+line);
		synchronized(qrCodePayLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}			
	}	
		
	/**
	 * 网页版随机减金额创建订单
	 * @param line
	 * @return
	 */
	public static boolean fx_randomMinusOnLineOrder(String line) {	
		String md5Key=Md5Util.execute(randomMinusOnLineOrder+line);
		synchronized(randomMinusOnLineOrderLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}			
	}	
	
	/**
	 * 网页版创建订单
	 * @param line
	 * @return
	 */
	public static boolean fx_createOnLineOrder(String line) {	
		String md5Key=Md5Util.execute(createOnLineOrder+line);
		synchronized(createOnLineOrderLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}			
	}	
		
	/**
	 * 创建测试订单
	 * @param line
	 * @return
	 */
	public static boolean fx_testOrder(String line) {	
		String md5Key=Md5Util.execute(testOrder+line);
		synchronized(testOrderLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}	
		
	}	
	
	/**
	 * 创建订单
	 * @param line
	 * @return
	 */
	public static boolean fx_createOrder(String line) {	
		String md5Key=Md5Util.execute(createOrder+line);
		synchronized(createOrderLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}	
		
	}	
	/**
	 * 注册
	 * @param line
	 * @return
	 */
	public static boolean fx_register(String line) {	
		String md5Key=Md5Util.execute(register+line);
		synchronized(registerLock) {
			String v=RedisUtil.getValue(md5Key);
			if(v.equals(value)) {
				return true;
			}else {
				RedisUtil.setValueByTime(md5Key, value, timeout);
			}
			return false;	
		}		
	}
}
