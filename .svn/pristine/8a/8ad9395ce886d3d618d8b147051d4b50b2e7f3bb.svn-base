package com.contactsImprove.controller.admin;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.entity.api.Orders;
import com.contactsImprove.entity.api.Users;
import com.contactsImprove.service.api.OrdersService;
import com.contactsImprove.service.api.UsersService;
import com.contactsImprove.utils.LoggerUtil;

@RequestMapping("/admin/echarts")
@Controller
public class EchartsController {
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private UsersService usersService;
	
	@RequestMapping(value = "/orderEcharts")
	public String orderEcharts() {
		return "orderEcharts";
	}
	
	@RequestMapping(value = "/selectOrder")
	@ResponseBody
	public Map<String, Object> selectOrder(String year) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			List<Object> list = orderService.selectOrderCountByYear(new SimpleDateFormat("YYYY").parse("2018"));
			List<Object> totla = orderService.selectOrdercount();
			
			resultMap.put("code", StatusCode._200.getStatus());
			resultMap.put("msg", StatusCode._200.getMsg());
			resultMap.put("data", list);
			resultMap.put("totla", totla);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/selectOrderTotal")
	@ResponseBody
	public Map<String, Object> selectOrderTotal(Orders orders, String phoneNumber) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Users users = new Users();
			if(phoneNumber != null) {
				users = usersService.selectByPhoneNumber(phoneNumber);
				if(users != null) {
					orders.setUserId(users.getUserId());
				}
			}
			
			List<Object> list = orderService.selectOrderTotal(orders);
			
			resultMap.put("code", StatusCode._200.getStatus());
			resultMap.put("msg", StatusCode._200.getMsg());
			resultMap.put("data", list);
			resultMap.put("type", users.getType());
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/journalAccountEcharts")
	public String JurnalAccount() {
		return "journalAccountEcharts";
	}
	
	@RequestMapping(value = "/selectJurnalAccount")
	@ResponseBody
	public Map<String, Object> selectJurnalAccount() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Object> list = orderService.selectJournalAccountEcharts(new SimpleDateFormat("YYYY").parse("2018"));
			
			resultMap.put("code", StatusCode._200.getStatus());
			resultMap.put("msg", StatusCode._200.getMsg());
			resultMap.put("data", list);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/customEcharts")
	public String customEcharts() {
		return "customEcharts";
	}
	
	@RequestMapping(value = "/custom")
	@ResponseBody
	public Map<String, Object> custom() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Object> list = orderService.selectJournalAccountEcharts(new SimpleDateFormat("YYYY").parse("2018"));
			
			resultMap.put("code", StatusCode._200.getStatus());
			resultMap.put("msg", StatusCode._200.getMsg());
			resultMap.put("data", list);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}

}
