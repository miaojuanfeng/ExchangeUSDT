package com.contactsImprove.waterwheel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.contactsImprove.constant.MerchantOrderConst.NotifyStatus;
import com.contactsImprove.entity.api.MerchantNotify;
import com.contactsImprove.entity.api.Orders;
import com.contactsImprove.service.api.MerchantNotifyService;
import com.contactsImprove.service.api.OrdersService;
import com.contactsImprove.utils.LoggerUtil;
import com.contactsImprove.utils.NotifyCommercial;

public class Discharged implements Runnable{
	
	byte[] lock=new byte[0];
	
	Integer slotIndex;
	
	boolean isBusy=false;
	
	MerchantNotifyService merchantNotifyService;
	NotifyCommercial notifyCommercial;
	OrdersService ordersService;
	
	public Discharged(MerchantNotifyService merchantNotifyService,NotifyCommercial notifyCommercial,OrdersService ordersService) {
		this.merchantNotifyService=merchantNotifyService;
		this.notifyCommercial=notifyCommercial;
		this.ordersService=ordersService;
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
					List<MerchantNotify> notifyList=merchantNotifyService.selectBySlotIndex(slotIndex);					
					if(notifyList!=null) {
						List<Orders> orderList=null;
						for(int i=0;i<notifyList.size();i++) {
							MerchantNotify mn=notifyList.get(i);
							byte status =notifyCommercial.notifyOrderFinish(mn,mn.getPrivateKey());
							if(status>NotifyStatus.notyet.status) {
								if(orderList==null) {
									orderList=new ArrayList<Orders>();
								}
								Orders order=new Orders();
								order.setTradeNumber(mn.getTradeNumber());
								order.setNotifyStatus(status);
								if(status==NotifyStatus.success.status) {
									order.setCloseTime(new Date());
								}
								orderList.add(order);
							}
						}
						if(orderList!=null && orderList.size()>0) {
							ordersService.updateBatchNotifyStatus(orderList);
						}
					}
					notifyList=null;									
				} catch (InterruptedException e) {
					LoggerUtil.error(e.getMessage(), e);
				}
			}			
		}				
	}
		

}
