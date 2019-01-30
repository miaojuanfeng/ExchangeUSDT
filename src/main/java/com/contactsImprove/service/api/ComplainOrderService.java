package com.contactsImprove.service.api;


import com.contactsImprove.entity.api.ComplainOrder;

public interface ComplainOrderService {
	
    int deleteByPrimaryKey(String tradeNumber);

    int insertSelective(ComplainOrder record);

    ComplainOrder selectByPrimaryKey(String tradeNumber);

    int updateByPrimaryKeySelective(ComplainOrder record);

}
