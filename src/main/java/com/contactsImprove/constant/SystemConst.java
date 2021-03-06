package com.contactsImprove.constant;

import java.math.BigDecimal;


public class SystemConst {
	
	public static String payUrl="alipays://platformapi/startapp?appId=09999988&actionType=toCard&sourceId=bill";
	
	/**
	 * 随机减金额max
	 */
	public static int randomMoneyMax=10;	
	/**
	 * 随机减金额min
	 */
	public static int randomMoneyMin=1;	
	/**
	 * 回调key
	 */
	public static String phoneCallbackKey="phonePluginKey16787";	
	/**
	 * 在线key
	 */
	public static String onLineKey="OtcPay499192";	
	/**
	 * 2小时有多少毫秒
	 */
	public static long FourHourMillisecond=3600*2*1000;
	/**
	 * 银行卡号尾数数量
	 */
	public static int suffixNumber=4;
	/**
	 * 是否是本地
	 */
	public static boolean hasLocal=false;
	
	/**
	 * 订单付款超时时间
	 */
	public static final int Order_TimeOut=10*60;//单位：秒.
	/**
	 * 版本号
	 */
	public static final String version="1.0";
	/**
	 * 随机获取最大币商数。
	 */
	public static final int random_limit=30;
	/**\
	 * 用户登入后的回话key
	 */
	public static final String sessionId="userId";
	/**
	 * 币种USDT
	 */
	public static final String USDT="USDT";
	
	/**
	 * CNY
	 */
	public static final String CNY="CNY";	
	/**
	 * 短信key .
	 */
	public static final String SMSKey="SMS";
	/**
	 * 手续费
	 */
	public static final BigDecimal poundage=new BigDecimal("0.000");	
	/**
	 * 起提数量
	 */
	public static final BigDecimal minNum=new BigDecimal("0.00001");	
	/**
	 * 第一次回调标示
	 */
	public static final int fisrtNotify=-1;
	/**
	 * ssl默认端口
	 */
	public static final int sslPort=443;	
	/**
	 * 保留小数位数
	 */
	public static final int holdDigit=6;
	/**
	 * 公司用户账号
	 */
	public static final long companyUserId=20199999999999l;
	/**
	 * mysql like 
	 */
	public static final String sqlLike="LOCATE";
	/**
	 * mysql like 
	 */
	public static final String sqlBetween ="between";
	
	/**
	 * USDT保留小数位
	 * 
	 */
	public static final int decimal_place_num=8;
	/**
	 * 金额保留小数位
	 * 
	 */
	public static final int decimal_place_money_num=2;
	/**
	 * 出金人民币限额
	 */
	public static final BigDecimal limitCNYOut=new BigDecimal(50000);	
	/**
	 * 出金USDT限额
	 */
	public static final BigDecimal limitUSDTOut=new BigDecimal(50000);
	/**
	 * 币商提成
	 */
	public static final BigDecimal income_user_money_rate=new BigDecimal(0.002);
	/**
	 * 股东提成
	 */
	public static final BigDecimal income_shareholder_money_rate=new BigDecimal(0.05);
	
	
	
	public static enum Status{
		_0((byte)0,"下线"),
		_1((byte)1,"启用");
		public byte status;
		public String meaning;
		
		Status(byte status,String meaning){
			this.status=status;
			this.meaning=meaning;
		}
		
		public static String getMeaningByStatus(byte status) {			
			Status[] typeList=Status.values();
			for(Status t : typeList) {
				if(t.status==status) {
					return t.meaning;
				}
			}			
			return "";			
		}		
	}
	
}
