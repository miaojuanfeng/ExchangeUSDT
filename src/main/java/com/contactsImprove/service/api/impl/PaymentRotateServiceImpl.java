package com.contactsImprove.service.api.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactsImprove.constant.PaymentRotateConst.PaymentRotateStatus;
import com.contactsImprove.dao.api.PaymentRotateDao;
import com.contactsImprove.entity.api.PaymentRotate;
import com.contactsImprove.service.api.PaymentRotateService;
import com.contactsImprove.utils.DateTools;
import com.contactsImprove.utils.Tools;

@Service("paymentRotateService")
public class PaymentRotateServiceImpl implements PaymentRotateService {
	int totalSecondOfDay=86400;
	@Autowired
	PaymentRotateDao paymentRotateDao;

	@Override
	public int deleteByPrimaryKey(Long paymentId) {
		// TODO Auto-generated method stub
		return paymentRotateDao.deleteByPrimaryKey(paymentId);
	}

	@Override
	public int insertSelective(PaymentRotate record) {
		// TODO Auto-generated method stub
		return paymentRotateDao.insertSelective(record);
	}

	@Override
	public PaymentRotate selectByPrimaryKey(Long paymentId) {
		// TODO Auto-generated method stub
		return paymentRotateDao.selectByPrimaryKey(paymentId);
	}

	@Override
	public int updateByPrimaryKeySelective(PaymentRotate record) {
		// TODO Auto-generated method stub
		return paymentRotateDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<PaymentRotate> selectBySlotIndex(Integer slotIndex) {
		// TODO Auto-generated method stub
		return paymentRotateDao.selectBySlotIndex(slotIndex);
	}

	@Override
	public int updateBatchStatus(List<PaymentRotate> list) {
		// TODO Auto-generated method stub
		return paymentRotateDao.updateBatchStatus(list);
	}

	@Override
	public PaymentRotate selectByLock(Long paymentId) {
		// TODO Auto-generated method stub
		return paymentRotateDao.selectByLock(paymentId);
	}

	@Override
	public int spinPaymentRotate(Long paymentId, BigDecimal amount) {	
		PaymentRotate pr=selectByLock(paymentId);		
		PaymentRotate newPr=new PaymentRotate();
		newPr.setPaymentId(pr.getPaymentId());
		newPr.setPaymentVolume(pr.getPaymentVolume().add(amount));
		newPr.setPaymentNumber((short)(pr.getPaymentNumber()+1));
		newPr.setStatus(PaymentRotateStatus.invalid.status);
		if((newPr.getPaymentNumber()!=null && newPr.getPaymentNumber()>=pr.getLimitNumber()) || 
				(newPr.getPaymentVolume()!=null && newPr.getPaymentVolume().compareTo(pr.getLimitVolume())>=0)) {
			int slotIndex=DateTools.getSecondOfDay()%totalSecondOfDay;
			if(slotIndex>0) {
				slotIndex=slotIndex-1;
			}else {
				slotIndex=totalSecondOfDay-1;
			}
			newPr.setSlotIndex(slotIndex);
		}else {
			int slotIndex=DateTools.getSecondOfDay();
			slotIndex=(slotIndex+Tools.getRandomNumberInRange(pr.getIntervalMin(), pr.getIntervalMax()))%totalSecondOfDay;
			newPr.setSlotIndex(slotIndex);
		}
		return updateByPrimaryKeySelective(newPr);
	}

	@Override
	public List<PaymentRotate> selectByGreaterThanSlotIndex(Integer slotIndex) {
		StringBuilder sb=new StringBuilder();
		sb.append("select payment_id, payment_volume, payment_number,limit_volume, limit_number from payment_rotate");
		sb.append(" where status=0 and slot_index between "+(slotIndex-300)+" and "+slotIndex);
	    
		return paymentRotateDao.selectByGreaterThanSlotIndex(sb.toString());
	}
	
}
