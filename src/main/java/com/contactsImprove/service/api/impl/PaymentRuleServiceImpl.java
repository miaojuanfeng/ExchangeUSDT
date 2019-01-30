package com.contactsImprove.service.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactsImprove.dao.api.PaymentRuleDao;
import com.contactsImprove.entity.api.PaymentRule;
import com.contactsImprove.service.api.PaymentRuleService;

@Service("paymentRuleService")
public class PaymentRuleServiceImpl implements PaymentRuleService {
	
	@Autowired
	PaymentRuleDao paymentRuleDao;

	@Override
	public int deleteByPrimaryKey(Byte paymentType) {
		// TODO Auto-generated method stub
		return paymentRuleDao.deleteByPrimaryKey(paymentType);
	}

	@Override
	public int insertSelective(PaymentRule record) {
		// TODO Auto-generated method stub
		return paymentRuleDao.insertSelective(record);
	}

	@Override
	public PaymentRule selectByPrimaryKey(Byte paymentType) {
		// TODO Auto-generated method stub
		return paymentRuleDao.selectByPrimaryKey(paymentType);
	}

	@Override
	public int updateByPrimaryKeySelective(PaymentRule record) {
		// TODO Auto-generated method stub
		return paymentRuleDao.updateByPrimaryKeySelective(record);
	}

}
