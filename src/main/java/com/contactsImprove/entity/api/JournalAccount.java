package com.contactsImprove.entity.api;

import java.math.BigDecimal;
import java.util.Date;

public class JournalAccount {
    private Long journalAccountId;

    private Long userId;

    private BigDecimal changeAmount;

    private BigDecimal currencyNumberBefore;

    private BigDecimal currencyNumberAfter;

    private BigDecimal freezeNumberBefore;

    private BigDecimal freezeNumberAfter;

    private Date createTime;

    private Byte type;

    private String remark;
    
    private Byte currencyType;
    
	private Users users;
	
    private String tradeNumber;
	
    public String getTradeNumber() {
		return tradeNumber;
	}

	public void setTradeNumber(String tradeNumber) {
		this.tradeNumber = tradeNumber;
	}

	public Byte getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Byte currencyType) {
		this.currencyType = currencyType;
	}

	public Long getJournalAccountId() {
        return journalAccountId;
    }

    public void setJournalAccountId(Long journalAccountId) {
        this.journalAccountId = journalAccountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getCurrencyNumberBefore() {
        return currencyNumberBefore;
    }

    public void setCurrencyNumberBefore(BigDecimal currencyNumberBefore) {
        this.currencyNumberBefore = currencyNumberBefore;
    }

    public BigDecimal getCurrencyNumberAfter() {
        return currencyNumberAfter;
    }

    public void setCurrencyNumberAfter(BigDecimal currencyNumberAfter) {
        this.currencyNumberAfter = currencyNumberAfter;
    }

    public BigDecimal getFreezeNumberBefore() {
        return freezeNumberBefore;
    }

    public void setFreezeNumberBefore(BigDecimal freezeNumberBefore) {
        this.freezeNumberBefore = freezeNumberBefore;
    }

    public BigDecimal getFreezeNumberAfter() {
        return freezeNumberAfter;
    }

    public void setFreezeNumberAfter(BigDecimal freezeNumberAfter) {
        this.freezeNumberAfter = freezeNumberAfter;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
}