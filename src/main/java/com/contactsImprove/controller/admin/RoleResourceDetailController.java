package com.contactsImprove.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.entity.admin.RoleResourceDetail;
import com.contactsImprove.service.admin.RoleResourceDetailService;
import com.contactsImprove.utils.LoggerUtil;

@RequestMapping("/admin/roleResourceDetail")
@Controller
public class RoleResourceDetailController {
	
	@Autowired
	private RoleResourceDetailService roleResourceDetailService;
	
	@RequestMapping(value = "/updateRoleResourceDetail")
	@ResponseBody
	@Transactional
	public Map<String, Object> updateRoleResourceDetail(RoleResourceDetail roleResourceDetail) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			roleResourceDetailService.updateByRoleId(roleResourceDetail);
			resultMap.put("code", StatusCode._200.getStatus());
			resultMap.put("msg", StatusCode._200.getMsg());
			return resultMap;
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}

}
