package com.contactsImprove.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.entity.admin.AdminUser;
import com.contactsImprove.entity.admin.Powers;
import com.contactsImprove.json.JSON;
import com.contactsImprove.service.admin.AdminUserService;
import com.contactsImprove.service.admin.PowersService;
import com.contactsImprove.utils.LoggerUtil;
import com.contactsImprove.utils.Md5Util;

@Controller
@RequestMapping("/admin")
public class AdminUserController {
	
	@Autowired
	private AdminUserService adminUserService;
	
	@Autowired
	private PowersService powersService;
	
	@RequestMapping(value = "/adminUsers")
	public String adminUser() {
		return "adminUser";
	}
	
	@RequestMapping(value = "/selectAdminUserByPage")
	@JSON(type = AdminUser.class, include = "id,powerId,userName,name,phone,isValid,createTime,loginTime,ip,powers")
	@JSON(type = Powers.class, include = "powerName")
	public Map<String, Object> selectAdminUserByPage(AdminUser adminUser,int page, int limit) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			adminUser.setPageNumber(page);
			adminUser.setPageSize(limit);
			List<AdminUser> list = adminUserService.selectByPage(adminUser);
			resultMap.put("code", StatusCode._0.getStatus());
			resultMap.put("msg", StatusCode._0.getMsg());
			resultMap.put("count", adminUser.getPageTotal());
			resultMap.put("data", list);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/selectAdminUserById")
	@JSON(type = AdminUser.class, include = "id,powerId,userName,password,name,phone,lpowers")
	@JSON(type = Powers.class, include = "id,powerName")
	public Map<String, Object> selectAdminUserById(Long id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			AdminUser adminUser = adminUserService.selectByPrimaryKey(id);
			List<Powers>  list = powersService.selectPowersByList();
			adminUser.setLpowers(list);
			resultMap.put("code", StatusCode._200.getStatus());
			resultMap.put("msg", StatusCode._200.getMsg());
			resultMap.put("data", adminUser);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/addAdminUser")
	@ResponseBody
	@Transactional
	public Map<String, Object> addAdminUser(AdminUser adminUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			AdminUser user = adminUserService.selectByUserName(adminUser);
			if(user != null) {
				resultMap.put("code", StatusCode._313.getStatus());
				resultMap.put("msg", StatusCode._313.getMsg());
				return resultMap;
			}
			
			if(adminUser != null) {
				adminUser.setPassword(Md5Util.execute(adminUser.getPassword()));
				adminUser.setCreateTime(new Date());
				adminUserService.insertSelective(adminUser);
				resultMap.put("code", StatusCode._200.getStatus());
				resultMap.put("msg", StatusCode._200.getMsg());
			} else {
				resultMap.put("code", StatusCode._201.getStatus());
				resultMap.put("msg", StatusCode._201.getMsg());
			}
			return resultMap;
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/updateAdminUser")
	@ResponseBody
	@Transactional
	public Map<String, Object> updateAdminUser(AdminUser adminUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			AdminUser user = adminUserService.selectByPrimaryKey(adminUser.getId());
			if(adminUser != null) {
				if(adminUser.getPassword() != null && !user.getPassword().equals(adminUser.getPassword())) {
					adminUser.setPassword(Md5Util.execute(adminUser.getPassword()));
				}
				
				int result = adminUserService.updateByPrimaryKeySelective(adminUser);
				if(result > 0) {
					resultMap.put("code", StatusCode._200.getStatus());
					resultMap.put("msg", StatusCode._200.getMsg());
				} else {
					resultMap.put("code", StatusCode._201.getStatus());
					resultMap.put("msg", StatusCode._201.getMsg());
				}
			} 
			return resultMap;
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/deleteAdminUser")
	@ResponseBody
	@Transactional
	public Map<String, Object> deleteAdminUser(Long id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(id != null) {
				int result = adminUserService.deleteByPrimaryKey(id);
				if(result > 0) {
					resultMap.put("code", StatusCode._200.getStatus());
					resultMap.put("msg", StatusCode._200.getMsg());
				} else {
					resultMap.put("code", StatusCode._201.getStatus());
					resultMap.put("msg", StatusCode._201.getMsg());
				}
			} 
			return resultMap;
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/authenTication")
	@JSON(type = AdminUser.class, include = "id,urls")
	public Map<String, Object> authenTication(Long id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			AdminUser user = adminUserService.selectByPrimaryKey(id);
			Map<String, Set<String>> map = adminUserService.selectResourceMapByUserId(user.getPowerId());
			user.setUrls(map.get("urls"));
			resultMap.put("code", StatusCode._200.getStatus());
			resultMap.put("msg", StatusCode._200.getMsg());
			resultMap.put("data", user);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
}
