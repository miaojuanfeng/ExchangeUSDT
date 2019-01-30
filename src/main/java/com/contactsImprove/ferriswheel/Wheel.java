package com.contactsImprove.ferriswheel;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.service.api.OrdersService;



public class Wheel {
	
	//位置
	private static int slot_index=0;
	//数量
	public static int slot_max=SystemConst.Order_TimeOut;
	
	public static byte[] lockWork=new byte[0];
	static int poolSize=5;
	static List<TimeoutDischarged> pools=new ArrayList<TimeoutDischarged>(poolSize);
	
	private static List<ConcurrentSkipListSet<String>> bigWheel=new ArrayList<ConcurrentSkipListSet<String>>(slot_max);
	
	protected static java.util.concurrent.ConcurrentHashMap<String, Integer> mappingSlotIndex=new java.util.concurrent.ConcurrentHashMap<String, Integer>();
	
	static void init(OrdersService ordersService) {		
		for(int i=0;i<slot_max;i++) {
			bigWheel.add(null);
		}		
		new TimeoutTimer("order-timeout-exec").start();		
		for(int i=0;i<poolSize;i++) {
			TimeoutDischarged d=new TimeoutDischarged(ordersService);
			pools.add(d);
			new Thread(d,"TimeoutDischarged-worker-"+(i+1)).start();
			d=null;
		}	
	}
		
	static TimeoutDischarged getTimeoutDischarged() {		
		for(int i=0;i<poolSize;i++) {
			if(!pools.get(i).isBusy()) {
				return pools.get(i);
			}
		}
		return pools.get(poolSize-1);		
	}
	
	public static void pushSlot(String orderId,int slotIndex) {
		mappingSlotIndex.put(orderId, slotIndex);
		synchronized(lockWork) {
			if(slotIndex>=slot_max) return ;
			ConcurrentSkipListSet<String> slot=bigWheel.get(slotIndex);
			if(slot==null) {
				slot=new ConcurrentSkipListSet<String>();
			}
			slot.add(orderId);	
			bigWheel.set(slotIndex, slot);
		}				
	}	
	// 装箱
	public static void pushSlot(String orderId) {
		synchronized(lockWork) {
			int index=-1;
			if(slot_index==0) {
				index=slot_max-1;
			}else{
				index=slot_index-1;
			}
			ConcurrentSkipListSet<String> slot=bigWheel.get(index);			
			if(slot==null) {
				slot=new ConcurrentSkipListSet<String>();
			}
			slot.add(orderId);	
			bigWheel.set(index, slot);
			mappingSlotIndex.put(orderId, index);
		}
	}
	// 拆箱
	public static void popUpSlot(){	
		List<String> copySlot=null;
		synchronized(lockWork) {
			ConcurrentSkipListSet<String> slot=bigWheel.get(slot_index);
			if(slot_index<slot_max-1) {
				slot_index++;
			}else{
				slot_index=0;
			}		
			if(slot==null || slot.size()==0) {
				 return ;
			}		
			copySlot=new ArrayList<String>(slot.size());
			copySlot.addAll(slot);			
			copySlot.forEach( key ->{
				mappingSlotIndex.remove(key);
			});
			slot.clear();
		}
		if(copySlot!=null && copySlot.size()>0) {
//			OrderTimeout.getInstance().pustTimeout(copySlot);
			getTimeoutDischarged().pustTimeout(copySlot);	
		}
	}
	
	public static void removeOrderId(String orderId) {
		Integer slotIndex=mappingSlotIndex.get(orderId);
		if(slotIndex!=null) {
			ConcurrentSkipListSet<String> slot=bigWheel.get(slotIndex);
			if(slot!=null) {
				slot.remove(orderId);		
			}
			mappingSlotIndex.remove(orderId);
		}
	}		
}
