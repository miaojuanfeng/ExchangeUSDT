package com.contactsImprove.controller.api;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contactsImprove.constant.MerchantOrderConst.TradeStatus;
import com.contactsImprove.entity.api.UserMerchant;
import com.contactsImprove.service.api.UserMerchantService;
import com.contactsImprove.utils.LoggerUtil;
import com.contactsImprove.utils.OrderUtil;
import com.contactsImprove.utils.RSAUtils;

@Controller
@RequestMapping("/api")
public class MerchantNotify {
	java.util.Random r=new Random();
	@Autowired
	UserMerchantService userMerchantService;
	/**
	 * 回调
	 * @param orderPara
	 * @return
	 */
	@RequestMapping(value = "/notify")
	@Transactional
	@ResponseBody
	public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception{
		PrintWriter out=response.getWriter();
		if(TradeStatus.success.meaning.equals(request.getParameter("tradeStatus"))) {
			Enumeration<?> resparam = request.getParameterNames();
			TreeMap<String, String> paramap = new TreeMap<String, String>();
			while(resparam.hasMoreElements()) {
				String resname = (String) resparam.nextElement();
				paramap.put(resname, request.getParameter(resname));
			}
			String sign=paramap.get("sign");
			paramap.remove("sign");
			String line=OrderUtil.createParaOrderStr(paramap);
			String userId=paramap.get("merchantUserId");
			UserMerchant uw=userMerchantService.selectByPrimaryKey(Long.parseLong(userId));						
			boolean isPass=RSAUtils.verifySignature(line,sign,uw.getPublicKey());
			LoggerUtil.info("回调商户:"+userId+" 验签状态："+isPass);
			if(isPass){
				if(r.nextInt(10)%2==0) {
					out.write("fail");
				}else {
					out.write("success");
				}
			}else {
				out.write("fail");
			}						
		}
			out.flush();
	}
	
}
