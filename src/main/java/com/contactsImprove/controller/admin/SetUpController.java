package com.contactsImprove.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.entity.api.Currency;
import com.contactsImprove.service.api.CurrencyService;


@Controller
@RequestMapping("/admin/setup")
public class SetUpController {
	
	@Autowired
	CurrencyService currencyService;
	
	@RequestMapping(value = "/exchangeRate")
	public String adminUser() {
		return "exchangeRate";
	}
	
	@RequestMapping(value = "/exchangeRateList")
	@ResponseBody
	public Map<String, Object> exchangeRateList() {
		Map<String, Object> resultMap = new HashMap<String, Object>(4);
		List<Currency> list=new ArrayList<Currency>();	
		Currency c=currencyService.selectByPrimaryKey(SystemConst.USDT);
		list.add(c);			
		resultMap.put("code", StatusCode._0.getStatus());
		resultMap.put("msg", StatusCode._0.getMsg());
		resultMap.put("data", list);
		resultMap.put("count", list.size());
		return resultMap;
	}
	
	@RequestMapping(value = "/setExchangeRate")
	@ResponseBody
	public Map<String, Object> setExchangeRate(Currency c) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);		
		currencyService.updateByPrimaryKeySelective(c);		
		resultMap.put("code", StatusCode._200.getStatus());
		resultMap.put("msg", StatusCode._200.getMsg());
		return resultMap;

	}
}
