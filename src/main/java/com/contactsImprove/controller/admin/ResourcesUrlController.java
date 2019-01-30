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

import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.entity.admin.ResourcesUrl;
import com.contactsImprove.json.JSON;
import com.contactsImprove.service.admin.ResourcesUrlService;
import com.contactsImprove.utils.AuthTreeUtil;
import com.contactsImprove.utils.LoggerUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin/resources")
public class ResourcesUrlController {
	
	@Autowired
	private ResourcesUrlService resourcesUrlService;
	
	@RequestMapping(value = "/resources")
	public String selectResourcesView() {
		return "resourcesUrl";
	}
	
	/**
	 * 根据登录权限查询菜单
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/selectResourcesBypower")
	@JSON(type = ResourcesUrl.class, include = "id,resourceName,parentId,sort,type,url,icon,isValid")
	public Map<String, Object> selectResourcesBypower(Long id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Object> list = resourcesUrlService.selectResourcesBypower(id);
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

	/**
	 * 查询所有菜单
	 * @return
	 */
	@RequestMapping(value = "/selectResourcesByList")
	@ResponseBody
	public Map<String,Object> selectResourcesByList() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<ResourcesUrl> list = resourcesUrlService.selectResourcesByList();
			JSONArray json = new JSONArray();
			for(ResourcesUrl ru: list){
				JSONObject jo = new JSONObject();
				jo.put("id", ru.getId());
				jo.put("pid", ru.getParentId());
				jo.put("resourceName", ru.getResourceName());
				jo.put("url", ru.getUrl());
				jo.put("icon", ru.getIcon());
				jo.put("type", ru.getType());
				jo.put("isValid", ru.getIsValid());
				jo.put("sort", ru.getSort());
				json.add(jo);
			}
			resultMap.put("code", StatusCode._0.getStatus());
			resultMap.put("msg", StatusCode._0.getMsg());
			resultMap.put("data", json);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/selectResourcesById")
	@ResponseBody
	public Map<String, Object> selectResourcesById(Long id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			ResourcesUrl ru = resourcesUrlService.selectByPrimaryKey(id);
			resultMap.put("code", StatusCode._0.getStatus());
			resultMap.put("msg", StatusCode._0.getMsg());
			resultMap.put("data", ru);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/selectResourcesByRolesId")
	@ResponseBody
	public Map<String, Object> selectResourcesByRolesId(Long id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<ResourcesUrl> list = resourcesUrlService.selectResourcesById(id);
			List<ResourcesUrl> allList = resourcesUrlService.selectResourcesByList();
			
			List<ResourcesUrl> data = new ArrayList<ResourcesUrl>();
			for(ResourcesUrl r : allList) {
				ResourcesUrl res =  new ResourcesUrl();
				res.setId(r.getId());
				res.setResourceName(r.getResourceName());
				res.setParentId(r.getParentId());
				for(ResourcesUrl rs : list) {
					if(r.getId() == rs.getId()) {
						res.setChecked(true);
					}
				}
				data.add(res);
			}
			List<Object> l = new AuthTreeUtil().Parent(data);
			resultMap.put("code", StatusCode._0.getStatus());
			resultMap.put("msg", StatusCode._0.getMsg());
			resultMap.put("data", l);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/updateResourcesById")
	@ResponseBody
	@Transactional
	public Map<String, Object> updateResourcesById(ResourcesUrl resourcesUrl) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(resourcesUrl.getIsValid() == null && resourcesUrl.getParentId() == null) {
				resourcesUrl.setParentId((long)0);
			}
			int result = resourcesUrlService.updateByPrimaryKeySelective(resourcesUrl);
			if(result> 0) {
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
	
	@RequestMapping(value = "/addResiurces")
	@ResponseBody
	public Map<String, Object> addResiurces(ResourcesUrl resourcesUrl) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(resourcesUrl.getParentId() == null) {
				resourcesUrl.setParentId((long)0);
			}
			resourcesUrl.setCreateTime(new Date());
			resourcesUrlService.insertSelective(resourcesUrl);
			resultMap.put("code", StatusCode._200.getStatus());
			resultMap.put("msg", StatusCode._200.getMsg());
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/deleteResourcesById")
	@ResponseBody
	@Transactional
	public Map<String, Object> deleteResourcesById(Long id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			int result = resourcesUrlService.deleteByPrimaryKey(id);
			if(result> 0) {
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
	
}
