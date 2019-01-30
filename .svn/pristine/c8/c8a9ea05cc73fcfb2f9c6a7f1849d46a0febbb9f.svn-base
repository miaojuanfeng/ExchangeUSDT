package com.contactsImprove.service.admin.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.contactsImprove.dao.admin.ResourcesUrlDao;
import com.contactsImprove.entity.admin.ResourcesUrl;
import com.contactsImprove.service.admin.ResourcesUrlService;
import com.contactsImprove.utils.TreeUtil;

@Service("resourcesUrlService")
public class ResourcesUrlServiceImpl implements ResourcesUrlService{
	
	@Autowired
	private ResourcesUrlDao resourcesUrlDao;
	
	@Override
	public List<Object> selectResourcesBypower(Long id) {
		List<ResourcesUrl> list = resourcesUrlDao.selectResourcesBypower(id);
		List<Object> l = new TreeUtil().Parent(list);
		return l;
	}

	@Override
	public int updateByPrimaryKeySelective(ResourcesUrl resourcesUrl) {
		return resourcesUrlDao.updateByPrimaryKeySelective(resourcesUrl);
	}

	@Override
	public List<ResourcesUrl> selectResourcesByList() {
		return resourcesUrlDao.selectResourcesByList();
	}

	@Override
	public ResourcesUrl selectByPrimaryKey(Long id) {
		return resourcesUrlDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		List<ResourcesUrl> list = resourcesUrlDao.selectResourcesByparentId(id);
		if(list.size() > 0) {
			for(ResourcesUrl ru: list) {
				resourcesUrlDao.deleteByPrimaryKey(ru.getId());
			}
		}
		return resourcesUrlDao.deleteByPrimaryKey(id);
	}

	@Override
	public List<ResourcesUrl> selectResourcesById(Long id) {
		return resourcesUrlDao.selectResourcesById(id);
	}

	@Override
	public int insertSelective(ResourcesUrl record) {
		return resourcesUrlDao.insertSelective(record);
	}

}
