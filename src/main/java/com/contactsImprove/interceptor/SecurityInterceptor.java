package com.contactsImprove.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.constant.UsersConst.UsersStatus;
import com.contactsImprove.entity.api.Users;
import com.contactsImprove.utils.LoggerUtil;
import com.contactsImprove.utils.SessionUtil;

import net.sf.json.JSONObject;

public class SecurityInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Users user=SessionUtil.getSession(request);
		if(user==null) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("code", StatusCode._312.status);
			resultMap.put("msg", StatusCode._312.msg);
			returnJson(response, resultMap);
			return false;
		}else if(!(user.getStatus()==UsersStatus._1.status || user.getStatus()==UsersStatus.shut.status)){
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if(user.getStatus()==UsersStatus._2.status || user.getStatus()==UsersStatus._3.status){			
				resultMap.put("code", StatusCode._402.status);
				resultMap.put("msg", StatusCode._402.msg);
			}else {
				resultMap.put("code", StatusCode._401.status);
				resultMap.put("msg", StatusCode._401.msg);
			}
			returnJson(response, resultMap);
		}
				
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
	}

	private void returnJson(HttpServletResponse response, Object json) throws Exception {
		PrintWriter writer = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		try {
			JSONObject infoJson = JSONObject.fromObject(json);
			writer = response.getWriter();
			writer.print(infoJson);

		} catch (IOException e) {
			LoggerUtil.error("HandlerInterceptor response error", e);
		} finally {
			if (writer != null)
				writer.close();
		}
	}

}
