package com.contactsImprove.constant;


public class PaymentRotateConst {
	
	public static enum PaymentRotateStatus{
		valid((byte)1,"可接单"),
		invalid((byte)0,"不可接单");
		public byte status;
		public String meaning;
		
		PaymentRotateStatus(byte status,String meaning){
			this.status=status;
			this.meaning=meaning;
		}
		
		public static String getMeaningByStatus(byte status) {			
			PaymentRotateStatus[] statusList=PaymentRotateStatus.values();
			for(PaymentRotateStatus s : statusList) {
				if(s.status==status) {
					return s.meaning;
				}
			}			
			return "";			
		}
		
	}

}
