package com.contactsImprove.ferriswheel;


import java.util.List;

public abstract class Timeout extends Thread {
		
	private List<String> orderList;
	
	private byte[] lock=new byte[0];
	
	public Timeout() {
		java.lang.Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				if(orderList!=null && orderList.size()>0) {
					startTimeout(orderList);
				}				
			}
		});
		this.start();
	}
	
	public void run() {		
		while(true) {
			if(orderList==null || orderList.size()==0) {
				synchronized(lock) {											
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}			
			startTimeout(orderList);
			orderList=null;	
		}				
	}
		
	public void pustTimeout(List<String> orderIds) {	
		synchronized(lock) {
			this.orderList=orderIds;	
			lock.notifyAll();
		}			
	}
	
	public abstract void startTimeout(List<String> orderList);	

}
