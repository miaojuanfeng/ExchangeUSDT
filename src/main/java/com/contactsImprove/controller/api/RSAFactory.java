package com.contactsImprove.controller.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.constant.UsersConst.UsersType;
import com.contactsImprove.entity.api.UserMerchant;
import com.contactsImprove.entity.api.Users;
import com.contactsImprove.json.JSON;
import com.contactsImprove.service.api.UserMerchantService;
import com.contactsImprove.utils.RSAUtils;
import com.contactsImprove.utils.RedisUtil;
import com.contactsImprove.utils.SessionUtil;
import com.contactsImprove.utils.StringUtil;

@Controller
@RequestMapping("/api")
public class RSAFactory {
	@Autowired
	UserMerchantService userMerchantService;
	
	/**
	 * 重新成功生成公钥私钥
	 * @param orderPara
	 * @return
	 */
	@RequestMapping(value = "/produceKey",method=RequestMethod.POST)
	@JSON(type = UserMerchant.class, filter = "status")
	public Map<String, Object> produceKey(HttpServletRequest request,Integer keySize,@RequestParam("smsCode") String smsCode){
		Map<String, Object> map = new HashMap<>();
		Users user=SessionUtil.getSession(request);	
		if (!StringUtil.isBlank(smsCode)
				&& RedisUtil.getValue(SystemConst.SMSKey + user.getPhoneNumber()).equals(smsCode)){
			RedisUtil.delValue(SystemConst.SMSKey + user.getPhoneNumber());		
			if(keySize==null || keySize==0) {
				keySize=2048;
			}			
			if(user.getType()==UsersType._1.status) {
				Map<String,String> key=RSAUtils.createKeys(keySize);
				UserMerchant um=new UserMerchant();
				um.setUserId(user.getUserId());
				um.setPrivateKey(key.get(RSAUtils.key_private));
				um.setPublicKey(key.get(RSAUtils.key_public));
				userMerchantService.updateByPrimaryKeySelective(um);						
			    map.put("code", StatusCode._200.status);
			    map.put("msg", StatusCode._200.msg);	    
			    map.put("data", um);
			}else {
			    map.put("code", StatusCode._310.status);
			    map.put("msg", StatusCode._310.msg);	
			}
		}else {
			map.put("code", StatusCode._309.getStatus());
			map.put("msg", StatusCode._309.getMsg() + "或是验证码已过期");	
		}
				
		return map;		
	}

}
