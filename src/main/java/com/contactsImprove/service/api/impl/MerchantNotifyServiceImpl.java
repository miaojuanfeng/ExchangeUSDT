package com.contactsImprove.service.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactsImprove.dao.api.MerchantNotifyDao;
import com.contactsImprove.entity.api.MerchantNotify;
import com.contactsImprove.service.api.MerchantNotifyService;

@Service("merchantNotifyService")
public class MerchantNotifyServiceImpl implements MerchantNotifyService{
	
	@Autowired
	MerchantNotifyDao merchantNotifyDao;

	@Override
	public int deleteByPrimaryKey(String tradeNumber) {
		// TODO Auto-generated method stub
		return merchantNotifyDao.deleteByPrimaryKey(tradeNumber);
	}

	@Override
	public int insertSelective(MerchantNotify record) {
		// TODO Auto-generated method stub
		return merchantNotifyDao.insertSelective(record);
	}

	@Override
	public MerchantNotify selectByPrimaryKey(String tradeNumber) {
		// TODO Auto-generated method stub
		return merchantNotifyDao.selectByPrimaryKey(tradeNumber);
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantNotify record) {
		// TODO Auto-generated method stub
		return merchantNotifyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<MerchantNotify> selectBySlotIndex(Integer slotIndex) {
		// TODO Auto-generated method stub
		try {
			return merchantNotifyDao.selectBySlotIndex(slotIndex);
		}catch(Exception e) {
			return null;
		}
	}

	@Override
	public List<MerchantNotify> selectByGreaterThanSlotIndex(Integer slotIndex) {
		// TODO Auto-generated method stub
		return merchantNotifyDao.selectByGreaterThanSlotIndex(slotIndex);
	}

}
