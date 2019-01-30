package com.contactsImprove.service.api;

import java.math.BigDecimal;
import java.util.List;

import com.contactsImprove.entity.api.PaymentRotate;

public interface PaymentRotateService {
	
    int deleteByPrimaryKey(Long paymentId);
    
    int insertSelective(PaymentRotate record);

    PaymentRotate selectByPrimaryKey(Long paymentId);

    int updateByPrimaryKeySelective(PaymentRotate record);
    
    List<PaymentRotate> selectBySlotIndex(Integer slotIndex);
    
    int updateBatchStatus(List<PaymentRotate> list);
    
    PaymentRotate selectByLock(Long paymentId);
    
    int spinPaymentRotate(Long paymentId,BigDecimal amount);
    
    List<PaymentRotate> selectByGreaterThanSlotIndex(Integer slotIndex);

}
