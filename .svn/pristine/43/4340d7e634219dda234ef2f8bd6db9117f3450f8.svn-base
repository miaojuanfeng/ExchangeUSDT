package com.contactsImprove.ferriswheel;

public class TimeoutTimer extends Thread {
	
	private long timeout=1*1000;
	
	private byte[] lock=new byte[0];
	
	public TimeoutTimer(String threadName) {
		this.setName(threadName);
	}
	
	public void run() {
		
		while(true) {
			synchronized(lock) {				
				Wheel.popUpSlot();				
				try {
					lock.wait(timeout);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}							
			}
		}
						
	}
	

}
