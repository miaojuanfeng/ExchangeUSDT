package com.contactsImprove.ferriswheel;

import java.util.List;

import com.contactsImprove.service.api.OrdersService;
import com.contactsImprove.utils.StringUtil;

public class OrderTimeout extends Timeout  {

	
	OrdersService ordersService;
	
	private static OrderTimeout orderTimeout=null;
	
	private static byte[] lock=new byte[0];
		
	public static OrderTimeout getInstance() {		
		if(orderTimeout==null) {
			synchronized(lock) {
				if(orderTimeout==null) {
					orderTimeout=new OrderTimeout();
				}
			}						
		}
		return orderTimeout;		
	}

	public void setOrderService(OrdersService ordersService) {
		this.ordersService=ordersService;
	}
	
	@SuppressWarnings("unused")
	@Override
	public void startTimeout(List<String> orderList) {
		String orderIds=StringUtil.listToString(orderList, ',');
		// timeoutIds :1  ,更新成功 ，-1 , 更新失败。	
		Integer timeoutIds=ordersService.updateOrderTimeoutById(orderIds);	
//		NotifyOrderTimeout.getInstance().pustTimeout(orderList);
		orderIds=null;
		timeoutIds=null;			
//		SendSMSOrderTimeout.getInstance().pustTimeout(orderList);	
	}
		
}
