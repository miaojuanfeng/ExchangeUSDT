package com.contactsImprove.dao.api;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.contactsImprove.entity.api.MerchantNotify;

public interface MerchantNotifyDao {
    int deleteByPrimaryKey(String tradeNumber);

    int insertSelective(MerchantNotify record);

    MerchantNotify selectByPrimaryKey(String tradeNumber);

    int updateByPrimaryKeySelective(MerchantNotify record);
    
    List<MerchantNotify> selectBySlotIndex(@Param("slotIndex") Integer slotIndex);
        
    List<MerchantNotify> selectByGreaterThanSlotIndex(@Param("slotIndex") Integer slotIndex);    
    
}