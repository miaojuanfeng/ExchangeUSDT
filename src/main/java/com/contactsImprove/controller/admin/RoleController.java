package com.contactsImprove.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.contactsImprove.entity.admin.PowerRoleDetail;
import com.contactsImprove.entity.admin.Powers;
import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.entity.admin.ResourcesUrl;
import com.contactsImprove.entity.admin.Role;
import com.contactsImprove.json.JSON;
import com.contactsImprove.service.admin.PowerRoleDetailService;
import com.contactsImprove.service.admin.PowersService;
import com.contactsImprove.service.admin.ResourcesUrlService;
import com.contactsImprove.service.admin.RoleService;
import com.contactsImprove.utils.LoggerUtil;

@Controller
@RequestMapping("/admin/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ResourcesUrlService resourcesUrlService;
	
	@Autowired
	private PowersService powersService;
	
	@Autowired
	private PowerRoleDetailService powerRoleDetailService;

	@RequestMapping(value = "/roles")
	public String roleView() {
		return "role";
	}
	
	@RequestMapping(value = "/selectRoleByList")
	@JSON(type = Role.class, include = "id,roleName,description,createTime,resourcesUrls")
	@JSON(type = ResourcesUrl.class, include = "id,resourceName,parentId,sort,type,url,icon,isValid")
	public Map<String, Object> selectRoleByList(Role role,int page, int limit) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Role> data = new ArrayList<>();
			role.setPageNumber(page);
			role.setPageSize(limit);
			List<Role> list = roleService.selectRoleByList(role);
			for(Role r : list) {
				List<ResourcesUrl> rlist = resourcesUrlService.selectResourcesById(r.getId());
				r.setResourcesUrls(rlist);
				data.add(r);
			}
			resultMap.put("code", StatusCode._0.getStatus());
			resultMap.put("msg", StatusCode._0.getMsg());
			resultMap.put("count", role.getPageTotal());
			resultMap.put("data", data);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/addRole")
	@ResponseBody
	@Transactional
	public Map<String, Object> addRole(Role role) { 
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			role.setCreateTime(new Date());
			roleService.insertSelective(role);
			
			Powers p = new Powers();
			p.setPowerName(role.getRoleName());
			p.setDescription(role.getDescription());
			p.setCreateTime(new Date());
			powersService.insertSelective(p);
			
			PowerRoleDetail pd = new PowerRoleDetail();
			pd.setPowerId(p.getId());
			pd.setRoleId(role.getId());
			pd.setCreateTime(new Date());
			powerRoleDetailService.insertSelective(pd);
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
	
	@RequestMapping(value = "/delRole")
	@ResponseBody
	@Transactional
	public Map<String, Object> delRole(Long id) { 
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			roleService.deleteByPrimaryKey(id);
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
