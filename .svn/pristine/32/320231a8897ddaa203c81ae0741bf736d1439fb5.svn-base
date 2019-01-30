package com.contactsImprove.mysession;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.entity.api.Users;

public class SessionContext {

	protected static ConcurrentHashMap<String, HttpSession> singleSession=new ConcurrentHashMap<String, HttpSession>();
	
	public static void addSessionContext(HttpSession session,Users user) {
		if(session!=null) {
			singleSession.put(user.getPhoneNumber(), session);				
		}		
	}
	
	public static void delSession(HttpSession session) {
		if(session!=null) {
			Object obj=session.getAttribute(SystemConst.sessionId);
			if(obj!=null) {
				Users user=(Users)obj;
				singleSession.remove(user.getPhoneNumber());
			}			
		}	
	}
	
	public static boolean invalidSession(String phoneNumber) {
		HttpSession session=singleSession.get(phoneNumber);
		if(session!=null) {
			singleSession.remove(phoneNumber);
			session.invalidate();
			return true;
		}		
		return false;
	}
	
	public static boolean invalidSingleSession(String phoneNumber) {
		HttpSession session=singleSession.get(phoneNumber);
		if(session!=null) {
			singleSession.remove(phoneNumber);
			return true;
		}		
		return false;
	}
		
}
