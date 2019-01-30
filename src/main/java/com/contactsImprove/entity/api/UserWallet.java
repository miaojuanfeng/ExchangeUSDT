package com.contactsImprove.entity.api;

import java.math.BigDecimal;

public class UserWallet {
    private Long walletId;

    private Long userId;

    private String payPassword;

    private String currencyType;

    private BigDecimal currencyNumber;

    private BigDecimal freezeNumber;

    private Byte status;

    private String walletAddress;
    
    private String userName;
    
    private String phoneNumber;
    
    private Byte type;    
    
    private BigDecimal inRate;

    private BigDecimal outRate;

    private String reserve;

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public BigDecimal getInRate() {
		return inRate;
	}

	public void setInRate(BigDecimal inRate) {
		this.inRate = inRate;
	}

	public BigDecimal getOutRate() {
		return outRate;
	}

	public void setOutRate(BigDecimal outRate) {
		this.outRate = outRate;
	}

	public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword == null ? null : payPassword.trim();
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType == null ? null : currencyType.trim();
    }

    public BigDecimal getCurrencyNumber() {
        return currencyNumber;
    }

    public void setCurrencyNumber(BigDecimal currencyNumber) {
        this.currencyNumber = currencyNumber;
    }

    public BigDecimal getFreezeNumber() {
        return freezeNumber;
    }

    public void setFreezeNumber(BigDecimal freezeNumber) {
        this.freezeNumber = freezeNumber;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress == null ? null : walletAddress.trim();
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve == null ? null : reserve.trim();
    }
}