package com.contactsImprove.entity.api;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentRule {
    private Byte paymentType;

    private BigDecimal limitVolume;

    private Short limitNumber;

    private String paymentName;

    private Date createTime;

    private Integer rotateIntervalMax;

    private Integer rotateIntervalMin;

    private String reserve;

    public Byte getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Byte paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getLimitVolume() {
        return limitVolume;
    }

    public void setLimitVolume(BigDecimal limitVolume) {
        this.limitVolume = limitVolume;
    }

    public Short getLimitNumber() {
        return limitNumber;
    }

    public void setLimitNumber(Short limitNumber) {
        this.limitNumber = limitNumber;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName == null ? null : paymentName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRotateIntervalMax() {
        return rotateIntervalMax;
    }

    public void setRotateIntervalMax(Integer rotateIntervalMax) {
        this.rotateIntervalMax = rotateIntervalMax;
    }

    public Integer getRotateIntervalMin() {
        return rotateIntervalMin;
    }

    public void setRotateIntervalMin(Integer rotateIntervalMin) {
        this.rotateIntervalMin = rotateIntervalMin;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve == null ? null : reserve.trim();
    }
}