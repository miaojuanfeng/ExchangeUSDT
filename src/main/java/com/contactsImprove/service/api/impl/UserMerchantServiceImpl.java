package com.contactsImprove.service.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactsImprove.dao.api.UserMerchantDao;
import com.contactsImprove.entity.api.UserMerchant;
import com.contactsImprove.service.api.UserMerchantService;

@Service("userMerchantService")
public class UserMerchantServiceImpl implements UserMerchantService {

	@Autowired
	UserMerchantDao userMerchantDao;
	@Override
	public int deleteByPrimaryKey(Long userId) {
		// TODO Auto-generated method stub
		return userMerchantDao.deleteByPrimaryKey(userId);
	}

	@Override
	public int insert(UserMerchant record) {
		// TODO Auto-generated method stub
		return userMerchantDao.insert(record);
	}

	@Override
	public int insertSelective(UserMerchant record) {
		// TODO Auto-generated method stub
		return userMerchantDao.insertSelective(record);
	}

	@Override
	public UserMerchant selectByPrimaryKey(Long userId) {
		// TODO Auto-generated method stub
		return userMerchantDao.selectByPrimaryKey(userId);
	}

	@Override
	public int updateByPrimaryKeySelective(UserMerchant record) {
		// TODO Auto-generated method stub
		return userMerchantDao.updateByPrimaryKeySelective(record);
	}

}
