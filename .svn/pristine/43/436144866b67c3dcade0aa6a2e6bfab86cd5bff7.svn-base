package com.contactsImprove.ferriswheel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.contactsImprove.constant.MerchantOrderConst.NotifyStatus;
import com.contactsImprove.constant.MerchantOrderConst.TradeStatus;
import com.contactsImprove.entity.api.MerchantNotify;
import com.contactsImprove.entity.api.Orders;
import com.contactsImprove.entity.api.UserMerchant;
import com.contactsImprove.service.api.JournalAccountService;
import com.contactsImprove.service.api.OrdersService;
import com.contactsImprove.service.api.UserMerchantService;
import com.contactsImprove.utils.NotifyCommercial;
import com.contactsImprove.utils.StringUtil;


public class NotifyOrderTimeout extends Timeout {
		
	OrdersService orderService;
	JournalAccountService journalAccountService;
	NotifyCommercial notifyCommercial;
	UserMerchantService userMerchantService;	
	OrdersService ordersService;	
	public void initNotifyOrderTimeout(JournalAccountService journalAccountService,OrdersService orderService,NotifyCommercial notifyCommercial,
			OrdersService ordersService,UserMerchantService userMerchantService) {
		this.journalAccountService=journalAccountService;
		this.orderService=orderService;
		this.notifyCommercial= notifyCommercial;
		this.userMerchantService= userMerchantService;
		this.ordersService=ordersService;
	}

	private static NotifyOrderTimeout notifyOrderTimeout=null;
	
	private static byte[] lock=new byte[0];
		
	public static NotifyOrderTimeout getInstance() {		
		if(notifyOrderTimeout==null) {
			synchronized(lock) {
				if(notifyOrderTimeout==null) {
					notifyOrderTimeout=new NotifyOrderTimeout();
				}
			}						
		}
		return notifyOrderTimeout;		
	}

	@Override
	public void startTimeout(List<String> orderList) {
		String orderIds=StringUtil.listToString(orderList, ',');
		List<Orders> list=orderService.selectListByOrderId(orderIds);
		List<Orders> updateList=new ArrayList<Orders>();
		list.forEach(order ->{			
			// 创建回调
			if (!StringUtil.isBlank(order.getNotifyUrl())) {
				// 创建回调
				MerchantNotify mn = notifyCommercial.createNotify(order,TradeStatus.fail.meaning);
				UserMerchant um = userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
				// 立即回调
				byte status=notifyCommercial.notifyOrderFinish(mn, um.getPrivateKey());
				if(status>NotifyStatus.notyet.status) {
					Orders newOrder=new Orders();
					newOrder.setTradeNumber(order.getTradeNumber());
					newOrder.setNotifyStatus(status);
					if(status==NotifyStatus.success.status) {
						newOrder.setCloseTime(new Date());
					}
					updateList.add(newOrder);
				}
			}			
		});
		if(updateList.size()>0) {
			orderService.updateBatchNotifyStatus(updateList);
		}
		orderList.clear();
	}


}
