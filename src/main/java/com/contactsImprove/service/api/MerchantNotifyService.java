package com.contactsImprove.service.api;

import java.util.List;

import com.contactsImprove.entity.api.MerchantNotify;

public interface MerchantNotifyService {
	
    int deleteByPrimaryKey(String tradeNumber);

    int insertSelective(MerchantNotify record);

    MerchantNotify selectByPrimaryKey(String tradeNumber);

    int updateByPrimaryKeySelective(MerchantNotify record);

    List<MerchantNotify> selectBySlotIndex(Integer slotIndex);
    
    List<MerchantNotify> selectByGreaterThanSlotIndex(Integer slotIndex);    

}
