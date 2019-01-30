package com.contactsImprove.constant;


public class NofitySlotConst {
	
	public static enum SlotIndex{
		_0(0,"立即"),
		_5(5*60,"5分钟"),
		_30(30*60,"半个小时"),
		_60(60*60,"一个小时"),
		_240(4*60*60,"4个小时"),
		_720(12*60*60,"12小时");
							
		public int minute;
		public String meaning;
		
		SlotIndex(int minute,String meaning){
			this.minute=minute;
			this.meaning=meaning;
		}
		
		public static String getMeaningByStatus(int status) {
			
			SlotIndex[] statusList=SlotIndex.values();
			for(SlotIndex s : statusList) {
				if(s.minute==status) {
					return s.meaning;
				}
			}
			
			return "";
			
		}

	}

}
