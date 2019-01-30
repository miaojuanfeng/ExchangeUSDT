package com.contactsImprove.ferriswheel;

import java.util.List;
import com.contactsImprove.service.api.OrdersService;
import com.contactsImprove.utils.LoggerUtil;
import com.contactsImprove.utils.StringUtil;

public class TimeoutDischarged implements Runnable{
	
	byte[] lock=new byte[0];	
	List<String> orderList;	
	boolean isBusy=false;	
	OrdersService ordersService;
	
	public TimeoutDischarged(OrdersService ordersService) {
		this.ordersService=ordersService;
	}
	
	public void pustTimeout(List<String> orderList) {
		this.orderList=orderList;
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
					String orderIds=StringUtil.listToString(orderList, ',');
					// timeoutIds :1  ,更新成功 ，-1 , 更新失败。	
					Integer timeoutIds=ordersService.updateOrderTimeoutById(orderIds);	
/*				出金超时回调：
 * 	NotifyOrderTimeout.getInstance().pustTimeout(orderList);*/
					orderIds=null;
					timeoutIds=null;	
					orderList.clear();
					orderList=null;
				} catch (InterruptedException e) {
					LoggerUtil.error(e.getMessage(), e);
				}
			}			
		}				
	}
		
}
