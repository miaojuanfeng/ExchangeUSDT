package com.contactsImprove.entity.api;

import java.math.BigDecimal;

public class PaymentRotate {
    private Long paymentId;

    private BigDecimal paymentVolume;

    private Short paymentNumber;

    private Byte status;

    private Integer slotIndex;

    private Integer intervalMin;

    private Integer intervalMax;

    private BigDecimal limitVolume;

    private Short limitNumber;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getPaymentVolume() {
        return paymentVolume;
    }

    public void setPaymentVolume(BigDecimal paymentVolume) {
        this.paymentVolume = paymentVolume;
    }

    public Short getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(Short paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Integer slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getIntervalMin() {
        return intervalMin;
    }

    public void setIntervalMin(Integer intervalMin) {
        this.intervalMin = intervalMin;
    }

    public Integer getIntervalMax() {
        return intervalMax;
    }

    public void setIntervalMax(Integer intervalMax) {
        this.intervalMax = intervalMax;
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
}