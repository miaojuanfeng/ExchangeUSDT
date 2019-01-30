package com.contactsImprove.waterwheel;

import java.util.ArrayList;
import java.util.List;

import com.contactsImprove.service.api.MerchantNotifyService;
import com.contactsImprove.service.api.OrdersService;
import com.contactsImprove.service.api.PaymentRotateService;
import com.contactsImprove.utils.DateTools;
import com.contactsImprove.utils.LoggerUtil;
import com.contactsImprove.utils.NotifyCommercial;


public class Turntable extends Thread{
		
	private static Turntable turntable=null;	
	
	private static byte[] lock=new byte[0];
		
	public static Turntable getInstance() {		
		if(turntable==null) {
			synchronized(lock) {
				if(turntable==null) {
					turntable=new Turntable();
				}
			}						
		}
		return turntable;		
	}

	MerchantNotifyService merchantNotifyService;
	NotifyCommercial notifyCommercial;
	OrdersService ordersService;
	PaymentRotateService paymentRotateService;
	
	int poolSize=5;
	int paymentPoolsSize=3;
	List<Discharged> pools=new ArrayList<Discharged>(poolSize);
	List<DischargedPayment> paymentPools=new ArrayList<DischargedPayment>(paymentPoolsSize);
	
	long waitTimeOut=1000;
	int slot_max=86400;
	int slot_index=0;
	
	public void init(MerchantNotifyService merchantNotifyService,NotifyCommercial notifyCommercial,OrdersService ordersService,PaymentRotateService paymentRotateService) {
		this.merchantNotifyService=merchantNotifyService;
		for(int i=0;i<poolSize;i++) {
			Discharged d=new Discharged(merchantNotifyService,notifyCommercial,ordersService);
			pools.add(d);
			new Thread(d,"water-wheel-worker-"+(i+1)).start();
			d=null;
		}	
		for(int i=0;i<paymentPoolsSize;i++) {
			DischargedPayment d=new DischargedPayment(paymentRotateService);
			paymentPools.add(d);
			new Thread(d,"DischargedPayment-worker-"+(i+1)).start();
			d=null;
		}	
		
		this.setName("water-wheel");
		this.start();
	}
	
	Discharged getFreeDischarged() {		
		for(int i=0;i<poolSize;i++) {
			if(!pools.get(i).isBusy()) {
				return pools.get(i);
			}
		}
		return pools.get(poolSize-1);		
	}
	
	DischargedPayment getFreeDischargedPayment() {		
		for(int i=0;i<paymentPoolsSize;i++) {
			if(!paymentPools.get(i).isBusy()) {
				return paymentPools.get(i);
			}
		}
		return paymentPools.get(paymentPoolsSize-1);		
	}
	
	public Turntable() {
		slot_index=DateTools.getSecondOfDay();		
	}
		
	public void run() {		
		while(true) {			
			synchronized(lock) {				
				try {					
					lock.wait(waitTimeOut);
					slot_index++;
					if(slot_index==slot_max) {
						slot_index=0;
					}					
					getFreeDischarged().setSlotIndex(slot_index);	
					getFreeDischargedPayment().setSlotIndex(slot_index);
				} catch (InterruptedException e) {					
					LoggerUtil.error(e.getMessage(), e);
				}																
			}		
			
		}
		
	}
	
	public int getWaterwheelSlotIndex() {
		int index=0;
		if(slot_index==0) {
			index=slot_max-1;
		}else {
			index=slot_index-1;
		}
		return index;
	}
	
	
}
