package com.contactsImprove.entity.api;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.notefield.ColumnMapping;
import com.contactsImprove.utils.PageUtil;

public class Users extends PageUtil{
	
	@ColumnMapping(columnName="u.user_id",operatorName="=")
    private Long userId;
	
	@ColumnMapping(columnName="u.user_fid",operatorName="=")
    private Long userFid;

    @ColumnMapping(columnName="u.user_name",operatorName=SystemConst.sqlLike)
    private String userName;
    @ColumnMapping(columnName="u.phone_number",operatorName="=")
    private String phoneNumber;

    private String password;
    
    @ColumnMapping(columnName="u.level",operatorName="=")
    private Byte level;

    private Byte type;
    @ColumnMapping(columnName="u.status",operatorName="=")
    private Byte status;
    
    private Integer workHours;
    
    private UserMerchant userMerchant;
    
    private Byte role;
    
    private Byte lock;
    
    @ColumnMapping(columnName="u.centre",operatorName="=")
    private Byte centre;
    
    private UserWallet userWallet;
    @ColumnMapping(columnName="u.create_time",operatorName=SystemConst.sqlBetween)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    
    private String reserve;
    
    private BigDecimal availableCredit;
    
    private BigDecimal freezingQuota;
    
    private BigDecimal inRate;

    private BigDecimal outRate;
    
	public Byte getLock() {
		return lock;
	}

	public void setLock(Byte lock) {
		this.lock = lock;
	}

	public Byte getRole() {
		return role;
	}

	public void setRole(Byte role) {
		this.role = role;
	}

	public Byte getCentre() {
		return centre;
	}

	public void setCentre(Byte centre) {
		this.centre = centre;
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

	public Integer getWorkHours() {
		return workHours;
	}

	public void setWorkHours(Integer workHours) {
		this.workHours = workHours;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserFid() {
		return userFid;
	}

	public void setUserFid(Long userFid) {
		this.userFid = userFid;
	}

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Byte getLevel() {
		return level;
	}

	public void setLevel(Byte level) {
		this.level = level;
	}

	public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

	public UserMerchant getUserMerchant() {
		return userMerchant;
	}

	public void setUserMerchant(UserMerchant userMerchant) {
		this.userMerchant = userMerchant;
	}

	public UserWallet getUserWallet() {
		return userWallet;
	}

	public void setUserWallet(UserWallet userWallet) {
		this.userWallet = userWallet;
	}

	public BigDecimal getAvailableCredit() {
		return availableCredit;
	}

	public void setAvailableCredit(BigDecimal availableCredit) {
		this.availableCredit = availableCredit;
	}

	public BigDecimal getFreezingQuota() {
		return freezingQuota;
	}

	public void setFreezingQuota(BigDecimal freezingQuota) {
		this.freezingQuota = freezingQuota;
	}
}