package com.contactsImprove.waterwheel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.contactsImprove.constant.PaymentRotateConst.PaymentRotateStatus;
import com.contactsImprove.entity.api.PaymentRotate;
import com.contactsImprove.service.api.PaymentRotateService;
import com.contactsImprove.utils.LoggerUtil;


public class DischargedPayment implements Runnable{
	
	byte[] lock=new byte[0];
	
	Integer slotIndex;
	
	boolean isBusy=false;
	
	PaymentRotateService paymentRotateService;
	
	public DischargedPayment(PaymentRotateService paymentRotateService) {
		this.paymentRotateService=paymentRotateService;
	}
	
	public void setSlotIndex(Integer slotIndex) {
		this.slotIndex=slotIndex;
		synchronized(lock) {
			lock.notifyAll();
		}
	}	
	public boolean isBusy() {
		return isBusy;
	}
	
	@Override
	public void run() {		
		while(true) {			
			synchronized(lock) {
				try {
					isBusy=false;
					lock.wait();
					isBusy=true;
					List<PaymentRotate> list=paymentRotateService.selectBySlotIndex(slotIndex);					
					if(list!=null && list.size()>0) {
						List<PaymentRotate> rotateList=new ArrayList<PaymentRotate>();
						for(int i=0;i<list.size();i++) {
							PaymentRotate pr=list.get(i);						
							PaymentRotate payment=new PaymentRotate();
							payment.setPaymentId(pr.getPaymentId());
							if((pr.getPaymentNumber() !=null && pr.getPaymentNumber()>= pr.getLimitNumber()) ||
									(pr.getPaymentVolume()!=null && pr.getPaymentVolume().compareTo(pr.getLimitVolume())>=0)) {
								payment.setPaymentVolume(BigDecimal.ZERO);
								payment.setPaymentNumber((short)0);
							}
							// 设置可接单
							payment.setStatus(PaymentRotateStatus.valid.status);							
							rotateList.add(payment);
						}
						if(rotateList!=null && rotateList.size()>0) {
							paymentRotateService.updateBatchStatus(rotateList);
						}
						rotateList=null;
					}									
				} catch (InterruptedException e) {
					LoggerUtil.error(e.getMessage(), e);
				}
			}			
		}				
	}
		

}
