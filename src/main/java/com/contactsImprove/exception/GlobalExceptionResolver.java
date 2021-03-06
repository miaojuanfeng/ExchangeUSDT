package com.contactsImprove.exception;


import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.druid.support.json.JSONUtils;
import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.utils.LoggerUtil;


public class GlobalExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		try {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", StatusCode._500.status);
            map.put("msg", StatusCode._500.msg);            
            writer.write(JSONUtils.toJSONString(map));
            writer.flush();
        } catch (Exception e) {
     		  
        }
		LoggerUtil.error(ex.getMessage(),ex);				
		return null;
	}

}
