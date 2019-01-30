package com.contactsImprove.entity.api;


import java.math.BigDecimal;

import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.notefield.ColumnMapping;
import com.contactsImprove.utils.PageUtil;

public class OrderQueryPara extends PageUtil {
	@ColumnMapping(columnName="status")
	Byte status;
	@ColumnMapping(columnName="type")
	Byte type;	
	/**
	 * 0:创建时间 ，1：支付时间，2：完成时间
	 */
	Byte timeType=0;
	
	@ColumnMapping(columnName="payment_number",operatorName=SystemConst.sqlLike)
	private String paymentNumber;
	
	/**
	 * 收款尾号
	 */
	private String paymentNumberSuffix;
	
	/**
	 * 支付金额
	 */
	private String amount;
	
	/**
	 * 付款人姓名
	 */
	private String paymentName;
	
	/**
	 * 范围查询
	 */
	String startTime;
	
	String endTime;
	
	Long userId;
	
	String paymentType;
	@ColumnMapping(columnName="merchant_account")
	String merchantAccount;
	@ColumnMapping(columnName="trade_number")
	String tradeNumber;
	
	Byte userType;
	@ColumnMapping(columnName="out_trade_no")
	String outTradeNo;
	
	String sign;
	
	Byte priorMinus;

	public Byte getPriorMinus() {
		return priorMinus;
	}

	public void setPriorMinus(Byte priorMinus) {
		this.priorMinus = priorMinus;
	}

	public String getPaymentNumberSuffix() {
		return paymentNumberSuffix;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setPaymentNumberSuffix(String paymentNumberSuffix) {
		this.paymentNumberSuffix = paymentNumberSuffix;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public String getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	public Byte getTimeType() {
		if(timeType==null) {
			timeType=0;
		}
		return timeType;
	}

	public void setTimeType(Byte timeType) {
		this.timeType = timeType;
	}

	public Byte getUserType() {
		return userType;
	}

	public void setUserType(Byte userType) {
		this.userType = userType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getMerchantAccount() {
		return merchantAccount;
	}

	public void setMerchantAccount(String merchantAccount) {
		this.merchantAccount = merchantAccount;
	}

	public String getTradeNumber() {
		return tradeNumber;
	}

	public void setTradeNumber(String tradeNumber) {
		this.tradeNumber = tradeNumber;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	
	
}
