package com.contactsImprove.service.api;

import com.contactsImprove.entity.api.PaymentRule;

public interface PaymentRuleService {
	
    int deleteByPrimaryKey(Byte paymentType);

    int insertSelective(PaymentRule record);

    PaymentRule selectByPrimaryKey(Byte paymentType);

    int updateByPrimaryKeySelective(PaymentRule record);

}
