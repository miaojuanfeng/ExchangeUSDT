package com.contactsImprove.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.contactsImprove.entity.admin.ResourcesUrl;

public class AuthTreeUtil {

	public static Map<String, Object> mapArray = new LinkedHashMap<String, Object>();
	public List<ResourcesUrl> allList;
	public List<Object> list = new ArrayList<Object>();

	public List<Object> Parent(List<ResourcesUrl> allList) {
		this.allList = allList;
		for (ResourcesUrl rs : allList) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			if (rs.getParentId() == 0) {
				map.put("value", rs.getId());
				map.put("name", rs.getResourceName());
				map.put("pid", rs.getParentId());
				map.put("checked", rs.isChecked());
				map.put("list", Child(rs.getId()));
				list.add(map);
			}
		}
		return list;
	}

	public List<?> Child(Long id) {
		List<Object> lists = new ArrayList<Object>();
		for (ResourcesUrl rs : allList) {
			Map<String, Object> child = new LinkedHashMap<String, Object>();
			if (rs.getParentId() == id) {
				child.put("value", rs.getId());
				child.put("name", rs.getResourceName());
				child.put("pid", rs.getParentId());
				child.put("checked", rs.isChecked());
				child.put("list", Menu(rs.getId()));
				lists.add(child);
			}
		}
		return lists;
	}

	public List<?> Menu(Long id) {
		List<Object> lists = new ArrayList<Object>();
		for (ResourcesUrl rs : allList) {
			Map<String, Object> menu = new LinkedHashMap<String, Object>();
			if (rs.getParentId() == id) {
				menu.put("value", rs.getId());
				menu.put("name", rs.getResourceName());
				menu.put("pid", rs.getParentId());
				menu.put("checked", rs.isChecked());
				lists.add(menu);
			}
		}
		return lists;
	}
}
