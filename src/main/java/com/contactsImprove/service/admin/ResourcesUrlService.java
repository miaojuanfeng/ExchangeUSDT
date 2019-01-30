package com.contactsImprove.service.admin;

import java.util.List;
import com.contactsImprove.entity.admin.ResourcesUrl;

public interface ResourcesUrlService {
	
	int updateByPrimaryKeySelective(ResourcesUrl record);
	
	List<Object> selectResourcesBypower(Long id);
	
	List<ResourcesUrl> selectResourcesByList();
	
	ResourcesUrl selectByPrimaryKey(Long id);
	
	int deleteByPrimaryKey(Long id);
	
	List<ResourcesUrl> selectResourcesById(Long id);
	
	int insertSelective(ResourcesUrl record);
}
