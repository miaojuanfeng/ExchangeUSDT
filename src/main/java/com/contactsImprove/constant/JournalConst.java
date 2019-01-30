package com.contactsImprove.constant;


public class JournalConst {
	
	public static enum JournalType{
		buy((byte)1,"买入"),
		sall((byte)2,"卖出"),
		recharge((byte)3,"充值"),
		rollback((byte)4,"币回退"),
		transfer((byte)5,"提币"),
		freeze((byte)6,"卖出冻结"),
		inRate((byte)7,"扣入金费"),
		outRate((byte)8,"扣出金费"),
		appendUsdt((byte)9,"后台=>币商=>加USDT"),
		mtFreeze((byte)10,"后台手动冻结"),
		mtRollbackFreeze((byte)11,"后台解除冻结"),
		income((byte)12,"提成");
		public byte status;
		public String meaning;
		
		JournalType(byte status,String meaning){
			this.status=status;
			this.meaning=meaning;
		}
		
		public static String getMeaningByStatus(byte status) {			
			JournalType[] statusList=JournalType.values();
			for(JournalType s : statusList) {
				if(s.status==status) {
					return s.meaning;
				}
			}			
			return "";			
		}
		
	}
	
	public static enum CurrencyType{
		USDT((byte)0,"USDT"),
		CNY((byte)1,"CNY");
		public byte status;
		public String meaning;
		
		CurrencyType(byte status,String meaning){
			this.status=status;
			this.meaning=meaning;
		}
		
		public static String getMeaningByStatus(byte status) {			
			CurrencyType[] statusList=CurrencyType.values();
			for(CurrencyType s : statusList) {
				if(s.status==status) {
					return s.meaning;
				}
			}			
			return "";			
		}
		
	}

}
