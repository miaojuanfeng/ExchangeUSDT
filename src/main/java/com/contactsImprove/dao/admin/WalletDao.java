package com.contactsImprove.dao.admin;

import com.contactsImprove.entity.admin.Wallet;

public interface WalletDao {
	
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Wallet record);

    Wallet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Wallet record);
    
    Wallet selectWalletByType(Integer type);

}