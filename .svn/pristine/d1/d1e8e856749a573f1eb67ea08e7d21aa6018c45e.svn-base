package com.contactsImprove.service.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.contactsImprove.entity.api.Users;

public interface UsersService {
	int insertSelective(Users record);
	
	Users selectByPhoneNumber(String phoneNumber);
	
    Users selectByPrimaryKey(Long userId);
    
    Users selectUserInfo(Long userId);
    
    Users selectUserWithWallet(Long userId);
    
    int updateByPrimaryKeySelective(Users record);    
    
    List<Users> selectUsersByMerchantList(Users record);
    
    List<Users> selectUsersByType(Users record);
    
    List<Users> selectUsersByStatus(Users record);
    
    List<Users> selectWaitByUsdt(BigDecimal amount,Integer maxNumber,Byte paymentType,int huor);
    
    Users selectByPrimaryKeyForLock(Long userId);
    
    List<Users> selectTestWaitByUsdt(BigDecimal amount, Integer maxNumber, Byte paymentType, int hour);
    
    int lockUser(Long userId);    
    
    int unLockUser(Long userId);  
    
    Users selectExistPhoneNumber(String phoneNumber);
    
    List<Users> selectUsersByUserFidAndLevel(Long userFid, Byte level);
    
    void userLevelUpgrade(Long userId, Byte childLevel, Byte upgradeLevel);
    
    BigDecimal userIncomeMoney(Users user, String tradeNumber, BigDecimal amount, Date date);
    
    List<Users> hasUser(Long userId);
    
    List<Users> selectUsersByLevelAndCentre(Byte level, Byte centre);
}
