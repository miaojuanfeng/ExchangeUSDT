package com.contactsImprove.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.contactsImprove.entity.admin.ResourcesUrl;

public class TreeUtil {
	
	public static Map<String,Object> mapArray = new LinkedHashMap<String, Object>(); 
	public List<ResourcesUrl> menuCommon; 
	public List<Object> list = new ArrayList<Object>(); 
	 
	public List<Object> Parent(List<ResourcesUrl> menu) {
		this.menuCommon = menu;
		for(ResourcesUrl r: menu) {
			Map<String,Object> map = new LinkedHashMap<String, Object>();
			if(r.getParentId() == 0 && r.getIsValid() == 1) {
				map.put("id", r.getId());
				map.put("resourceName", r.getResourceName());
				map.put("parentId", r.getParentId());
				map.put("sort", r.getSort());
				map.put("type", r.getType());
				map.put("url", r.getUrl());
				map.put("icon", r.getIcon());
				map.put("isValid", r.getIsValid());
				map.put("children", Child(r.getId()));
				list.add(map);
			}
		}
		return list;
	}
	 
	public List<?> Child(Long id){
		List<Object> lists = new ArrayList<Object>();
		for(ResourcesUrl rs: menuCommon){
			Map<String,Object> child = new LinkedHashMap<String, Object>();
			if(rs.getParentId() == id && rs.getIsValid() == 1){
				child.put("id", rs.getId());
				child.put("resourceName", rs.getResourceName());
				child.put("parentId", rs.getParentId());
				child.put("sort", rs.getSort());
				child.put("type", rs.getType());
				child.put("url", rs.getUrl());
				child.put("icon", rs.getIcon());
				child.put("menuList", Menu(rs.getId()));
				lists.add(child);
			}
		}
		return lists;
	}
	 
	public List<?> Menu(Long id){
		List<Object> lists = new ArrayList<Object>();
		for(ResourcesUrl rs: menuCommon){
			Map<String,Object> menu = new LinkedHashMap<String, Object>();
			if(rs.getParentId() == id && rs.getIsValid() == 1){
				menu.put("id", rs.getId());
				menu.put("resourceName", rs.getResourceName());
				menu.put("parentId", rs.getParentId());
				menu.put("sort", rs.getSort());
				menu.put("type", rs.getType());
				menu.put("url", rs.getUrl());
				menu.put("icon", rs.getIcon());
				menu.put("isValid", rs.getIsValid());
				lists.add(menu);
			}
		}
		return lists;
	}
}
