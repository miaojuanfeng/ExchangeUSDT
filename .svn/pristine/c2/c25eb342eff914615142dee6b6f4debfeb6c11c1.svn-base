package com.contactsImprove.service.api;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.contactsImprove.entity.api.UserWallet;
import com.contactsImprove.entity.api.Users;
import com.contactsImprove.entity.api.ValidUserPayment;
import com.contactsImprove.utils.PageUtil;

public interface UserWalletService {
	
    int deleteByPrimaryKey(Long walletId);

    int insert(UserWallet record);

    int insertSelective(UserWallet record);

    UserWallet selectByPrimaryKey(Long walletId);

    int updateByPrimaryKeySelective(UserWallet record);
    
    int updateUserRate(UserWallet record);

    UserWallet findByLock(Long UserId,String currencyType);
    
    UserWallet findCompanyUserByLock();
    
    UserWallet findCompanyUser();
    
    UserWallet selectByType(Long UserId,String currencyType);
         
    List<UserWallet> selectUserWalletByCurrencyAmount(BigDecimal currencyAmount,Integer maxNumber);
    
    List<ValidUserPayment> selectUserWalletByCurrencyAmountAndType(BigDecimal currencyAmount,Integer maxNumber,Byte paymentType,int hour);
    
    List<UserWallet> selectUserWallerList();
    
    HashMap<String ,BigDecimal> selectRemainingAmount(Long userId);
    
    List<UserWallet> selectUsersRateByList(Users users,PageUtil pageUtil);
    
    List<UserWallet> selectTestUserWallerList(BigDecimal currencyAmount, Integer maxNumber,
			Byte paymentType,int hour);
    
    int updateBatch(Collection<UserWallet> list);
}
