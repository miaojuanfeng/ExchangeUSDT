package com.contactsImprove.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.contactsImprove.constant.SystemConst;

public class CORSFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		String os = System.getProperty("os.name");  
		if(os.toLowerCase().startsWith("win")){  
			SystemConst.hasLocal=true; 
		}  
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {		
//		HttpServletResponse res = (HttpServletResponse) response; 
//		res.setHeader("Access-Control-Allow-Origin", "*");  
//		res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
//		res.setHeader("Access-Control-Max-Age", "3600");  
//		res.setHeader("Access-Control-Allow-Headers", "x-requested-with");  		
		chain.doFilter(request, response);	

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
