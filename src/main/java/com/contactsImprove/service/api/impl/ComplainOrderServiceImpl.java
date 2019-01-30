package com.contactsImprove.service.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactsImprove.dao.api.ComplainOrderDao;
import com.contactsImprove.entity.api.ComplainOrder;
import com.contactsImprove.service.api.ComplainOrderService;

@Service("complainOrderService")
public class ComplainOrderServiceImpl implements ComplainOrderService {

	@Autowired
	ComplainOrderDao complainOrderDao;

	@Override
	public int deleteByPrimaryKey(String tradeNumber) {
		// TODO Auto-generated method stub
		return complainOrderDao.deleteByPrimaryKey(tradeNumber);
	}

	@Override
	public int insertSelective(ComplainOrder record) {
		// TODO Auto-generated method stub
		return complainOrderDao.insertSelective(record);
	}

	@Override
	public ComplainOrder selectByPrimaryKey(String tradeNumber) {
		// TODO Auto-generated method stub
		return complainOrderDao.selectByPrimaryKey(tradeNumber);
	}

	@Override
	public int updateByPrimaryKeySelective(ComplainOrder record) {
		// TODO Auto-generated method stub
		return complainOrderDao.updateByPrimaryKeySelective(record);
	}
		


	
}
