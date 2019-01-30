package com.contactsImprove.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.entity.admin.Powers;
import com.contactsImprove.service.admin.PowersService;
import com.contactsImprove.utils.LoggerUtil;

@Controller
@RequestMapping("/admin/powers")
public class PowersController {
	
	@Autowired
	private PowersService powersService;
	
	@RequestMapping(value = "/selectPowersByList")
	@ResponseBody
	public Map<String, Object> selectPowersByList() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Powers> list = powersService.selectPowersByList();
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
