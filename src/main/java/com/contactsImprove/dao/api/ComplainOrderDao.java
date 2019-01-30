package com.contactsImprove.dao.api;

import com.contactsImprove.entity.api.ComplainOrder;

public interface ComplainOrderDao {
    int deleteByPrimaryKey(String tradeNumber);

    int insertSelective(ComplainOrder record);

    ComplainOrder selectByPrimaryKey(String tradeNumber);

    int updateByPrimaryKeySelective(ComplainOrder record);

}