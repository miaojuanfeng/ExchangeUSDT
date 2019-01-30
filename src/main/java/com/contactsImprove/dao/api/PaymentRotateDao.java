package com.contactsImprove.dao.api;

import java.util.List;

import com.contactsImprove.entity.api.PaymentRotate;

public interface PaymentRotateDao {
    int deleteByPrimaryKey(Long paymentId);
    
    int insertSelective(PaymentRotate record);

    PaymentRotate selectByPrimaryKey(Long paymentId);

    int updateByPrimaryKeySelective(PaymentRotate record);
    
   List<PaymentRotate> selectBySlotIndex(Integer slotIndex);
   
   List<PaymentRotate> selectByGreaterThanSlotIndex(String sql);
   
   int updateBatchStatus(List<PaymentRotate> list);
   
   PaymentRotate selectByLock(Long paymentId);

}