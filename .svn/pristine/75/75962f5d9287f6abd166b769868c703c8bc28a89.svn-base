package com.contactsImprove.dao.admin;

import java.util.List;
import com.contactsImprove.entity.admin.ResourcesUrl;

public interface ResourcesUrlDao {
	
    int deleteByPrimaryKey(Long id);

    int insertSelective(ResourcesUrl record);

    ResourcesUrl selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ResourcesUrl ResourcesUrl);
    
    List<ResourcesUrl> selectResourcesBypower(Long id);
    
    List<ResourcesUrl> selectResourcesByparentId(Long parentId);
    
    List<ResourcesUrl> selectResourcesByList();
    
    List<ResourcesUrl> selectResourcesById(Long id);
}