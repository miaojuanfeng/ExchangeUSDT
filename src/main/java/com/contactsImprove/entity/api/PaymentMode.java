package com.contactsImprove.entity.api;

import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

public class PaymentMode {
    private Long paymentId;

    private Long userId;

    private Byte paymentType;
    
    private String paymentTypeName;

    private String paymentNumber;

    private String paymentName;
    
    private String bankMark;

    private String paymentImage;
    
    private MultipartFile paymentQR; 

    private String userName;

    private Byte paymentDefault;

    private Byte status;

    private Date createTime;

    private String remark;

	public String getBankMark() {
		return bankMark;
	}

	public void setBankMark(String bankMark) {
		this.bankMark = bankMark;
	}

	public MultipartFile getPaymentQR() {
		return paymentQR;
	}

	public void setPaymentQR(MultipartFile paymentQR) {
		this.paymentQR = paymentQR;
	}

	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}

	public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Byte paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber == null ? null : paymentNumber.trim();
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName == null ? null : paymentName.trim();
    }

    public String getPaymentImage() {
        return paymentImage;
    }

    public void setPaymentImage(String paymentImage) {
        this.paymentImage = paymentImage == null ? null : paymentImage.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Byte getPaymentDefault() {
        return paymentDefault;
    }

    public void setPaymentDefault(Byte paymentDefault) {
        this.paymentDefault = paymentDefault;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}