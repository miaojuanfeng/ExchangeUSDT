package com.contactsImprove.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.entity.api.Users;
import com.contactsImprove.mysession.SessionContext;

public class SessionUtil {
	
	public static void setSession(HttpServletRequest request,Users users) {
		HttpSession session=request.getSession(true);		
		if(session.isNew() || session.getAttribute(SystemConst.sessionId)==null) {
			SessionContext.invalidSession(users.getPhoneNumber());
			session.setAttribute(SystemConst.sessionId, users);
			SessionContext.addSessionContext(session, users);
		}else{			
			if(!session.isNew()) {
				Users sessionUser=getSession(request);				
				if(!sessionUser.getPhoneNumber().equals(users.getPhoneNumber())) {				
					SessionContext.invalidSingleSession(sessionUser.getPhoneNumber());				
					session.setAttribute(SystemConst.sessionId, users);
					SessionContext.addSessionContext(session, users);
				}else {
					session.setAttribute(SystemConst.sessionId, users);
				}
			}
		}
	}
	
	public static Users getSession(HttpServletRequest request) {
		try {
			Object user=request.getSession().getAttribute(SystemConst.sessionId);
			if(user!=null) {
				return (Users)user;
			}
		}catch(Exception e) {
			
		}
		return null;
	}

}
