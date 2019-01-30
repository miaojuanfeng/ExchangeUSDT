package com.contactsImprove.dao.api;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.contactsImprove.entity.api.UserWallet;
import com.contactsImprove.entity.api.ValidUserPayment;
import com.contactsImprove.utils.PageUtil;

public interface UserWalletDao {
	
    int deleteByPrimaryKey(Long walletId);

    int insertSelective(UserWallet record);

    UserWallet selectByPrimaryKey(Long walletId);

    int updateByPrimaryKeySelective(UserWallet record);
    
    int updateUserRate(UserWallet record);

    UserWallet findByLock(@Param("userId")Long userId,@Param("currencyType")String currencyType);
    
    UserWallet selectByType(@Param("userId")Long UserId,@Param("currencyType")String currencyType);
    
    List<UserWallet> selectUserWalletByCurrencyAmount(@Param("currencyAmount") BigDecimal currencyAmount,@Param("maxNumber") Integer maxNumber);    
    
    List<ValidUserPayment> selectUserWalletByCurrencyAmountAndType(String sql);
    
    List<UserWallet> selectUserWallerList();
    
    HashMap<String ,BigDecimal> selectRemainingAmount(@Param("userId")Long userId);
    
    List<UserWallet> selectUsersRateByList(PageUtil pageUtil);
    
    List<UserWallet> selectTestUserWallerList(String sql);
    
    int updateBatch(Collection<UserWallet> list);

}