package com.contactsImprove.constant;


public class MerchantConst {
	public static enum MerchantStatus{
		_0((byte)0,"停用"),
		_1((byte)1,"启用");		
		public byte status;
		public String meaning;
		
		MerchantStatus(byte status,String meaning){
			this.status=status;
			this.meaning=meaning;
		}
		
		public static String getMeaningByStatus(byte status) {			
			MerchantStatus[] statusList=MerchantStatus.values();
			for(MerchantStatus s : statusList) {
				if(s.status==status) {
					return s.meaning;
				}
			}
			
			return "";			
		}
		
	}

}
