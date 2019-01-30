package com.contactsImprove.service.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.dao.api.UserWalletDao;
import com.contactsImprove.entity.api.UserWallet;
import com.contactsImprove.entity.api.Users;
import com.contactsImprove.entity.api.ValidUserPayment;
import com.contactsImprove.service.api.UserWalletService;
import com.contactsImprove.utils.DateTools;
import com.contactsImprove.utils.PageUtil;
import com.contactsImprove.utils.StringUtil;

@Service("userWalletService")
public class UserWalletServiceImpl implements UserWalletService {
	
	@Autowired
	UserWalletDao userWalletDao;

	@Override
	public int deleteByPrimaryKey(Long walletId) {
		// TODO Auto-generated method stub
		return userWalletDao.deleteByPrimaryKey(walletId);
	}

	@Override
	public int insert(UserWallet record) {
		// TODO Auto-generated method stub
		return userWalletDao.insertSelective(record);
	}

	@Override
	public int insertSelective(UserWallet record) {
		// TODO Auto-generated method stub
		return userWalletDao.insertSelective(record);
	}

	@Override
	public UserWallet selectByPrimaryKey(Long walletId) {
		// TODO Auto-generated method stub
		return userWalletDao.selectByPrimaryKey(walletId);
	}

	@Override
	public int updateByPrimaryKeySelective(UserWallet record) {
		// TODO Auto-generated method stub
		return userWalletDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public UserWallet findByLock(Long UserId, String currencyType) {
		// TODO Auto-generated method stub
		return userWalletDao.findByLock(UserId, currencyType);
	}

	@Override
	public List<UserWallet> selectUserWalletByCurrencyAmount(BigDecimal currencyAmount,Integer maxNumber) {
		// TODO Auto-generated method stub
		return userWalletDao.selectUserWalletByCurrencyAmount(currencyAmount,maxNumber);
	}

	@Override
	public UserWallet selectByType(Long UserId, String currencyType) {
		// TODO Auto-generated method stub
		return userWalletDao.selectByType(UserId, currencyType);
	}

	@Override
	public List<ValidUserPayment> selectUserWalletByCurrencyAmountAndType(BigDecimal currencyAmount, Integer maxNumber,
			Byte paymentType,int hour) {		
		StringBuilder sb=new StringBuilder();		
		sb.append("select uw.user_id,pt.payment_id from user_wallet uw,users u,payment_mode pt,payment_rotate pr where uw.user_id = u.user_id");		
		sb.append(" and pt.user_id = u.user_id and pr.payment_id=pt.payment_id and u.type = 2 and u.status =1 and u.lock=0 and u.role=0");
		sb.append(" and u.work_hours & "+hour+"<>0");
		sb.append(" and uw.currency_number>="+currencyAmount);		
		sb.append(" and pt.payment_type ="+paymentType);
		sb.append(" and pt.status = 1");	  
		sb.append(" and pr.status = 1");	
		sb.append(" and (pr.payment_volume + "+currencyAmount+")<pr.limit_volume");
		sb.append(" and case when pr.limit_number=0 then 1=1 else (pr.payment_number + 1)<pr.limit_number end ");
	    sb.append(" order by currency_number desc");	 		    
	    sb.append(" LIMIT "+maxNumber);	
	    if(sb.indexOf("#")>-1 || sb.indexOf(";")>-1) {
	    	return new ArrayList<ValidUserPayment>(1);
	    }	   		
		return userWalletDao.selectUserWalletByCurrencyAmountAndType(sb.toString());
	}
	
	@Override
	public List<UserWallet> selectTestUserWallerList(BigDecimal currencyAmount, Integer maxNumber,
			Byte paymentType,int hour) {		
		StringBuilder sb=new StringBuilder();		
		sb.append("select distinct uw.user_id from user_wallet uw,users u,payment_mode pt where uw.user_id = u.user_id");		
		sb.append(" and pt.user_id = u.user_id and u.type = 2 and u.status =1 and u.role=1");
		sb.append(" and u.work_hours & "+hour+"<>0"); 
	    sb.append(" and uw.currency_number>="+currencyAmount);			
		sb.append(" and pt.payment_type ="+paymentType);
		sb.append(" and pt.status = 1");   	
	    sb.append(" order by currency_number desc");	 		    
	    sb.append(" LIMIT "+maxNumber);	
	    if(sb.indexOf("#")>-1 || sb.indexOf(";")>-1) {
	    	return new ArrayList<UserWallet>(1);
	    }	  		
		return userWalletDao.selectTestUserWallerList(sb.toString());
	}

	@Override
	public List<UserWallet> selectUserWallerList() {
		// TODO Auto-generated method stub
		return userWalletDao.selectUserWallerList();
	}

	@Override
	public HashMap<String, BigDecimal> selectRemainingAmount(Long userId) {
		// TODO Auto-generated method stub
		return userWalletDao.selectRemainingAmount(userId);
	}

	@Override
	public UserWallet findCompanyUserByLock() {
		// TODO Auto-generated method stub
		return userWalletDao.findByLock(SystemConst.companyUserId, SystemConst.USDT);
	}

	@Override
	public UserWallet findCompanyUser() {
		// TODO Auto-generated method stub
		return userWalletDao.selectByType(SystemConst.companyUserId, SystemConst.USDT);
	}

	@Override
	public List<UserWallet> selectUsersRateByList(Users users,PageUtil pageUtil) {
		StringBuilder sb=new StringBuilder();
		sb.append("select u.user_id,u.phone_number,u.user_name,u.type,uw.wallet_id,uw.currency_type,uw.currency_number,uw.freeze_number,uw.`status`,uw.in_rate,uw.out_rate from users u,user_wallet uw where u.user_id=uw.user_id ");
		if(!StringUtil.isBlank(users.getUserName())) {
			sb.append(" and INSTR(u.user_name,'"+users.getUserName()+"')");
		}
		if(!StringUtil.isBlank(users.getPhoneNumber())) {
			sb.append(" and INSTR(u.phone_number,'"+users.getPhoneNumber()+"')");
		}
		if(!StringUtil.isBlank(users.getStatus())) {
			sb.append(" and u.`status`="+users.getStatus());
		}		
		if(!StringUtil.isBlank(users.getCreateTime())) {
			sb.append(" and u.create_time>='"+DateTools.DateToStr2(users.getCreateTime())+"'");
		}
		sb.append(" order by uw.wallet_id desc ");
		pageUtil.setQueryStr(sb.toString());
	    if(sb.indexOf("#")>-1 || sb.indexOf(";")>-1) {
	    	return new ArrayList<UserWallet>(1);
	    }else {	  
	    	return userWalletDao.selectUsersRateByList(pageUtil);
	    }
	}

	@Override
	public int updateUserRate(UserWallet record) {
		// TODO Auto-generated method stub
		return userWalletDao.updateUserRate(record);
	}

	@Override
	public int updateBatch(Collection<UserWallet> list) {
		if(list.size()>0) {
			return userWalletDao.updateBatch(list);
		}else {
			return 0;
		}
	}

	
}
