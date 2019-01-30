package com.contactsImprove.constant;


public class PaymentModeConst {
	
	public static enum PaymentValid{
		stop((byte)0,"停用"),
		start((byte)1,"启用");
		public byte status;
		public String meaning;
		
		PaymentValid(byte status,String meaning){
			this.status=status;
			this.meaning=meaning;
		}
		
		public static String getMeaningByStatus(byte type) {			
			PaymentValid[] typeList=PaymentValid.values();
			for(PaymentValid t : typeList) {
				if(t.status==type) {
					return t.meaning;
				}
			}
			
			return "";			
		}
		
	}

}
