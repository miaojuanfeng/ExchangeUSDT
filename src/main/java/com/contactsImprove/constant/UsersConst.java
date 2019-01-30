package com.contactsImprove.constant;

public class UsersConst {
	public static enum UsersType{
		_0((byte)0,"普通用户"),
		_1((byte)1,"商户"),
		_2((byte)2,"币商");
		public byte status;
		public String meaning;
		
		UsersType(byte status,String meaning){
			this.status=status;
			this.meaning=meaning;
		}
		
		public static String getMeaningByStatus(byte type) {			
			UsersType[] typeList=UsersType.values();
			for(UsersType t : typeList) {
				if(t.status==type) {
					return t.meaning;
				}
			}			
			return "";			
		}		
	}
	
	public static enum UsersRole{
		formal((byte)0,"正式"),
		test((byte)1,"测试");
		public byte role;
		public String meaning;
		
		UsersRole(byte role,String meaning){
			this.role=role;
			this.meaning=meaning;
		}
		
		public static String getMeaningByRole(byte role) {			
			UsersRole[] typeList=UsersRole.values();
			for(UsersRole t : typeList) {
				if(t.role==role) {
					return t.meaning;
				}
			}			
			return "";			
		}		
	}	
	
	public static enum UsersLock{
		unlock((byte)0,"无锁"),
		lock((byte)1,"锁定");
		public byte status;
		public String meaning;
		
		UsersLock(byte status,String meaning){
			this.status=status;
			this.meaning=meaning;
		}
		
		public static String getMeaningByRole(byte status) {			
			UsersLock[] typeList=UsersLock.values();
			for(UsersLock t : typeList) {
				if(t.status==status) {
					return t.meaning;
				}
			}			
			return "";			
		}		
	}	
	
	public static enum UsersStatus{
		_0((byte)0,"下线"),
		_1((byte)1,"启用"),
		_2((byte)2,"申请币商"),
		_3((byte)3,"申请商户"),
		shut((byte)4,"打烊");
		public byte status;
		public String meaning;
		
		UsersStatus(byte status,String meaning){
			this.status=status;
			this.meaning=meaning;
		}
		
		public static String getMeaningByStatus(byte status) {			
			UsersStatus[] statusList=UsersStatus.values();
			for(UsersStatus s : statusList) {
				if(s.status==status) {
					return s.meaning;
				}
			}
			
			return "";			
		}
		
	}
	
	public static enum UsersLevel{
		member((byte)1,(byte)10,"会员"),
		manager((byte)2,(byte)5,"经理"),
		director((byte)3,(byte)5,"董事"),
		shareholder((byte)4,(byte)999999999,"股东");
		public byte level;
		public byte number;
		public String meaning;
		
		UsersLevel(byte level, byte number, String meaning){
			this.level=level;
			this.number=number;
			this.meaning=meaning;
		}
		
		public static String getMeaningByRole(byte role) {			
			UsersRole[] typeList=UsersRole.values();
			for(UsersRole t : typeList) {
				if(t.role==role) {
					return t.meaning;
				}
			}			
			return "";			
		}		
	}
	
	public static enum UsersCentre{
		company((byte)1,"公司运营中心");
		public byte centre;
		public String meaning;
		
		UsersCentre(byte centre, String meaning){
			this.centre=centre;
			this.meaning=meaning;
		}
		
		public static String getMeaningByRole(byte role) {			
			UsersRole[] typeList=UsersRole.values();
			for(UsersRole t : typeList) {
				if(t.role==role) {
					return t.meaning;
				}
			}			
			return "";			
		}		
	}
	
	public static enum UsersGrade{
		one((byte)1,"一级币商邀请人"),
		two((byte)2,"二级币商邀请人"),
		three((byte)3,"股东运营中心");
		public byte grage;
		public String meaning;
		
		UsersGrade(byte grage, String meaning){
			this.grage=grage;
			this.meaning=meaning;
		}
		
		public static String getMeaningByGrade(byte grage) {			
			UsersGrade[] typeList=UsersGrade.values();
			for(UsersGrade t : typeList) {
				if(t.grage==grage) {
					return t.meaning;
				}
			}			
			return "";			
		}		
	}
	
	public static enum UsersRate{
		// 等级 ，一级提成,二级提成
		member((byte)1,(byte)10,(byte)5,"会员"),
		manager((byte)2,(byte)20,(byte)10,"经理"),
		director((byte)3,(byte)30,(byte)15,"董事"),
		shareholder((byte)4,(byte)40,(byte)20,"股东");
		public byte level;
		public byte first;
		public byte second;
		public String meaning;
		
		UsersRate(byte level, byte first, byte second, String meaning){
			this.level=level;
			this.first=first;
			this.second=second;
			this.meaning=meaning;
		}
		
		public static String getMeaningByLevel(byte level) {			
			UsersRate[] typeList=UsersRate.values();
			for(UsersRate t : typeList) {
				if(t.level==level) {
					return t.meaning;
				}
			}			
			return "";			
		}		
		
		public static Byte getRateByLevel(byte level, byte grade) {			
			UsersRate[] typeList=UsersRate.values();
			for(UsersRate t : typeList) {
				if(t.level==level) {
					if( grade == 1 ) {
						return t.first;
					}else if( grade == 2 ) {
						return t.second;
					}
				}
			}			
			return (byte)0;			
		}	
	}
}
