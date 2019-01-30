package com.contactsImprove.ferriswheel;


import java.util.TimerTask;

import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.utils.BridgeUSDTRate;



public class ExchangeRateTask extends TimerTask {
	
	@Override
	public void run() {
		if(!SystemConst.hasLocal) {			
				BridgeUSDTRate.setRateByCoinbase();			
		}
	}
	
}
