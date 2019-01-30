package com.contactsImprove.entity.api;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.notefield.ColumnMapping;
import com.contactsImprove.utils.PageUtil;

public class Orders extends PageUtil {
	
	@ColumnMapping(columnName="o1.trade_number",operatorName="=")
	private String tradeNumber;
	private String tradeFnumber;
	@ColumnMapping(columnName="o1.out_trade_no",operatorName="=")
	private String outTradeNo;
	@ColumnMapping(columnName="o1.merchant_user_id",operatorName="=")
	private Long merchantUserId;
	@ColumnMapping(columnName="o1.user_id",operatorName="=")
	private Long userId;

	private Integer payTimeout;
	@ColumnMapping(columnName="u2.user_name",operatorName=SystemConst.sqlLike)
	private String userName;
	
	@ColumnMapping(columnName="o1.type",operatorName="=")
	private Byte type;

	private String merchantAccount;

	private Long paymentId;
	@ColumnMapping(columnName="o1.payment_type_name",operatorName=SystemConst.sqlLike)
	private String paymentTypeName;
	@ColumnMapping(columnName="o1.payment_number",operatorName="=")
	private String paymentNumber;
	
	private String paymentName;
	
	private String paymentNumberSuffix;

	private String sign;
	
	private String paymentType;

	private String currencyType;

	private BigDecimal currencyAmount;

	private BigDecimal exchangeRate;

	private BigDecimal amount;

	private String fromAddress;
	
	private String toAddress;
	@ColumnMapping(columnName="o1.status",operatorName="=")
	private Byte status;
	@ColumnMapping(columnName="o1.create_time",operatorName=SystemConst.sqlBetween)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createTime;
	@ColumnMapping(columnName="o1.payment_time",operatorName=SystemConst.sqlBetween)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date paymentTime;
	@ColumnMapping(columnName="o1.close_time",operatorName=SystemConst.sqlBetween)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date closeTime;

	private String version;

	private String subject;

	private String body;
	@ColumnMapping(columnName="o1.deal_type")
	private Byte dealType;

	private String notifyUrl;
	@ColumnMapping(columnName="o1.notify_status",operatorName="=")
	private Byte notifyStatus;

	private String timestamp;

	private Object ext;
	
	private String types;
	
    private BigDecimal actualAmount; //实际收款

    private BigDecimal preferentialAmount;//优惠多少价格
	
    private BigDecimal actualCurrencyAmount;

    private BigDecimal poundage;

    private BigDecimal merchantRate;

    private BigDecimal underwriterRate;    
	/**
	 * 范围查询
	 */
    @ColumnMapping(columnName="o1.create_time",operatorName=">=")    
	String startTime;
    @ColumnMapping(columnName="o1.create_time",operatorName="<")	
	String endTime;    
    	
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

	public String getPaymentNumberSuffix() {
		return paymentNumberSuffix;
	}

	public void setPaymentNumberSuffix(String paymentNumberSuffix) {
		this.paymentNumberSuffix = paymentNumberSuffix;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public BigDecimal getPreferentialAmount() {
		return preferentialAmount;
	}

	public void setPreferentialAmount(BigDecimal preferentialAmount) {
		this.preferentialAmount = preferentialAmount;
	}

	public BigDecimal getActualCurrencyAmount() {
		return actualCurrencyAmount;
	}

	public void setActualCurrencyAmount(BigDecimal actualCurrencyAmount) {
		this.actualCurrencyAmount = actualCurrencyAmount;
	}

	public BigDecimal getPoundage() {
		return poundage;
	}

	public void setPoundage(BigDecimal poundage) {
		this.poundage = poundage;
	}

	public BigDecimal getMerchantRate() {
		return merchantRate;
	}

	public void setMerchantRate(BigDecimal merchantRate) {
		this.merchantRate = merchantRate;
	}

	public BigDecimal getUnderwriterRate() {
		return underwriterRate;
	}

	public void setUnderwriterRate(BigDecimal underwriterRate) {
		this.underwriterRate = underwriterRate;
	}

	public String getTradeFnumber() {
		return tradeFnumber;
	}

	public void setTradeFnumber(String tradeFnumber) {
		this.tradeFnumber = tradeFnumber;
	}

	public Byte getDealType() {
		return dealType;
	}

	public void setDealType(Byte dealType) {
		this.dealType = dealType;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Integer getPayTimeout() {
		return payTimeout;
	}

	public void setPayTimeout(Integer payTimeout) {
		this.payTimeout = payTimeout;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getTradeNumber() {
		return tradeNumber;
	}

	public void setTradeNumber(String tradeNumber) {
		this.tradeNumber = tradeNumber == null ? null : tradeNumber.trim();
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
	}

	public Long getMerchantUserId() {
		return merchantUserId;
	}

	public void setMerchantUserId(Long merchantUserId) {
		this.merchantUserId = merchantUserId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMerchantAccount() {
		return merchantAccount;
	}

	public void setMerchantAccount(String merchantAccount) {
		this.merchantAccount = merchantAccount == null ? null : merchantAccount.trim();
	}
	
	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName == null ? null : paymentTypeName.trim();
	}

	public String getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber == null ? null : paymentNumber.trim();
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType == null ? null : currencyType.trim();
	}

	public BigDecimal getCurrencyAmount() {
		return currencyAmount;
	}

	public void setCurrencyAmount(BigDecimal currencyAmount) {
		this.currencyAmount = currencyAmount;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version == null ? null : version.trim();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject == null ? null : subject.trim();
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body == null ? null : body.trim();
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl == null ? null : notifyUrl.trim();
	}

	public Byte getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(Byte notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(Object ext) {
		this.ext = ext;
	}

}