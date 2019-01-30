package com.contactsImprove.utils;

import java.math.BigDecimal;
import java.util.Date;

import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.constant.SystemConst.Status;
import com.contactsImprove.entity.api.Currency;
import com.contactsImprove.service.api.CurrencyService;

import net.sf.json.JSONObject;

public class BridgeUSDTRate {
	 static final String AccessKeyId="1c81c896d56-f137cf53-08672bae-62ab7";
	 static final String SecretKey="8596d470-63caa5ba-be35dec5-8ce65";
	 static final String SignatureMethod="HmacSHA256";
	 static final String SignatureVersion="2";
	static String Timestamp="";
	static final String Get="Get\n";
	static final String apiHost="api.huobi.pro\n";
	static String apiUrl="/market/trade\n";
	static String Url="https://api.hadax.com/market/trade";
	static String Signature="";
	static String symbol="";
	static 	CurrencyService currencyService;
	static String coinbaseURL="https://api.coinbase.com/v2/exchange-rates";
	//symbol 规则： 基础币种+计价币种。如BTC/USDT，symbol为btcusdt；ETH/BTC， symbol为ethbtc。以此类推
	//AccessKeyId=e2xxxxxx-99xxxxxx-84xxxxxx-7xxxx&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=2017-05-11T15%3A19%3A30&order-id=1234567890	
	public static void setCurrencyService(CurrencyService c) {
		currencyService=c;
	}	
	
	
	public static void setRateByCoinbase() {
		Currency c=currencyService.selectByPrimaryKey(SystemConst.USDT);
		if(c.getStatus()==Status._0.status) {
			return ;
		}
		int countNum=0;
		while(countNum<3) {
			countNum++;
			try {
				String body=HttpUtil.get(coinbaseURL);	
				JSONObject jObject=	JSONObject.fromObject(body);
				JSONObject data=JSONObject.fromObject(jObject.get("data"));
				JSONObject rates=JSONObject.fromObject(data.get("rates"));
				BigDecimal rate=new BigDecimal(rates.getString(SystemConst.CNY));				
				Currency record=new Currency();
				record.setCurrencyType(SystemConst.USDT);
				record.setRate(rate);
				record.setCreateTime(new Date());
				currencyService.updateByPrimaryKeySelective(record)	;	
				break;
			}catch(Exception e) {
				LoggerUtil.error("setRateByCoinbase: "+e);
			}
		}
		if(countNum>=3) {
			LoggerUtil.error("获取汇率错误~~~~~~~ ");
		}
	}
	public static void main(String[] args) {
		setRateByCoinbase();
	}
	
}
