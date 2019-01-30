package com.contactsImprove.service.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.contactsImprove.dao.api.PaymentModeDao;
import com.contactsImprove.entity.api.PaymentMode;
import com.contactsImprove.service.api.PaymentModeService;

@Service("paymentModeService")
public class PaymentModeServiceImpl implements PaymentModeService {

	@Autowired
	PaymentModeDao paymentModeDao;
	
	@Override
	public int deleteByPrimaryKey(Long paymentId) {
		// TODO Auto-generated method stub
		return paymentModeDao.deleteByPrimaryKey(paymentId);
	}

	@Override
	public int insert(PaymentMode record) {
		// TODO Auto-generated method stub
		return paymentModeDao.insertSelective(record);
	}

	@Override
	public int insertSelective(PaymentMode record) {
		// TODO Auto-generated method stub
		return paymentModeDao.insertSelective(record);
	}

	@Override
	public PaymentMode selectByPrimaryKey(Long paymentId) {
		// TODO Auto-generated method stub
		return paymentModeDao.selectByPrimaryKey(paymentId);
	}

	@Override
	public int updateByPrimaryKeySelective(PaymentMode record) {
		// TODO Auto-generated method stub
		return paymentModeDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<PaymentMode> selectPayementModeByType(Long userId, Byte paymentType) {
		// TODO Auto-generated method stub
		return paymentModeDao.selectPayementModeByType(userId, paymentType);
	}

	@Override
	public PaymentMode selectPayementModeByTypeDefault(Long userId) {
		// TODO Auto-generated method stub
		return paymentModeDao.selectPayementModeByTypeDefault(userId);
	}

	@Override
	public List<PaymentMode> selectPayementList(Long userId) {
		// TODO Auto-generated method stub
		return paymentModeDao.selectPayementList(userId);
	}

	@Override
	public int setPaymentDefault(Long paymentId, Long userId) {
		// TODO Auto-generated method stub
		return paymentModeDao.setPaymentDefault(paymentId, userId);
	}

}
