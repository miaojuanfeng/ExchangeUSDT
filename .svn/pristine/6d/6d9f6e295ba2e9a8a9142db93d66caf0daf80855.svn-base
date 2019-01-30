package com.contactsImprove.service.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactsImprove.dao.api.CurrencyDao;
import com.contactsImprove.entity.api.Currency;
import com.contactsImprove.service.api.CurrencyService;

@Service("currencyService")
public class CurrencyServiceImpl implements CurrencyService {
	
	@Autowired
	CurrencyDao currencyDao;

	@Override
	public int deleteByPrimaryKey(String currencyType) {
		// TODO Auto-generated method stub
		return currencyDao.deleteByPrimaryKey(currencyType);
	}

	@Override
	public int insert(Currency record) {
		// TODO Auto-generated method stub
		return currencyDao.insert(record);
	}

	@Override
	public int insertSelective(Currency record) {
		// TODO Auto-generated method stub
		return currencyDao.insertSelective(record);
	}

	@Override
	public Currency selectByPrimaryKey(String currencyType) {
		// TODO Auto-generated method stub
		return currencyDao.selectByPrimaryKey(currencyType);
	}

	@Override
	public int updateByPrimaryKeySelective(Currency record) {
		// TODO Auto-generated method stub
		return currencyDao.updateByPrimaryKeySelective(record);
	}

	

}
