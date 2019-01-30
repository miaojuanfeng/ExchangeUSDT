package com.contactsImprove.service.api;

import java.util.List;
import com.contactsImprove.entity.api.PaymentMode;

public interface PaymentModeService {
	
    int deleteByPrimaryKey(Long paymentId);

    int insert(PaymentMode record);

    int insertSelective(PaymentMode record);

    PaymentMode selectByPrimaryKey(Long paymentId);

    int updateByPrimaryKeySelective(PaymentMode record);

    List<PaymentMode> selectPayementModeByType(Long userId,Byte paymentType);    
    
    PaymentMode selectPayementModeByTypeDefault(Long userId);    
    
    List<PaymentMode> selectPayementList(Long userId);
    
    int setPaymentDefault(Long paymentId,Long userId);

}
