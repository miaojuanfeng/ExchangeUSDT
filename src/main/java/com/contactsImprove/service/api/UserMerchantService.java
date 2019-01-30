package com.contactsImprove.service.api;

import com.contactsImprove.entity.api.UserMerchant;

public interface UserMerchantService {
	
    int deleteByPrimaryKey(Long userId);

    int insert(UserMerchant record);

    int insertSelective(UserMerchant record);

    UserMerchant selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(UserMerchant record);

}
