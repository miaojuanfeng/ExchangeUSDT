package com.contactsImprove.service.admin.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.contactsImprove.dao.admin.AdminUserDao;
import com.contactsImprove.dao.admin.PowerRoleDetailDao;
import com.contactsImprove.dao.admin.PowersDao;
import com.contactsImprove.dao.admin.ResourcesUrlDao;
import com.contactsImprove.entity.admin.AdminUser;
import com.contactsImprove.entity.admin.PowerRoleDetail;
import com.contactsImprove.entity.admin.Powers;
import com.contactsImprove.entity.admin.ResourcesUrl;
import com.contactsImprove.service.admin.AdminUserService;

@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService{
	
	@Autowired
	private AdminUserDao adminUserDao;
	
	@Autowired
	private ResourcesUrlDao resourcesUrlDao;
	
	@Autowired
	private PowersDao PowersDao;
	
	@Autowired
	private PowerRoleDetailDao powerRoleDetailDao;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return adminUserDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(AdminUser adminUser) {
		return adminUserDao.insertSelective(adminUser);
	}

	@Override
	public AdminUser selectByPrimaryKey(Long id) {
		return adminUserDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(AdminUser adminUser) {
		return adminUserDao.updateByPrimaryKeySelective(adminUser);
	}

	@Override
	public List<AdminUser> selectByPage(AdminUser adminUser) {
		StringBuffer queryStr = new StringBuffer("select au.*, p.power_name from admin_user au, powers p where au.power_id = p.id");
		if(adminUser.getUserName() != null && adminUser.getUserName() != "") {
			queryStr.append(" and user_name like '%" + adminUser.getUserName()+"%'");
		}
		
		if(adminUser.getPhone() != null && adminUser.getPhone() != "") {
			queryStr.append(" and phone like '%" + adminUser.getPhone() +"%'");
		}
		
		if(adminUser.getName() != null && adminUser.getName() != "") {
			queryStr.append(" and name like '%"+ adminUser.getName() +"%'");
		}
		adminUser.setQueryStr(queryStr.toString());
		return adminUserDao.selectByPage(adminUser);
	}

	@Override
	public AdminUser selectByUserName(AdminUser adminUser) {
		return adminUserDao.selectByUserName(adminUser);
	}

	@Override
	public Map<String, Set<String>> selectResourceMapByUserId(long powerId) {
		Map<String, Set<String>> resourceMap = new HashMap<String, Set<String>>();
        Set<String> roles = new HashSet<String>();
        Powers powers = PowersDao.selectByPrimaryKey(powerId);
        if(powers != null) {
        	roles.add(powers.getPowerName());
        }
        
        Set<String> urls = new HashSet<String>();
        PowerRoleDetail pd = powerRoleDetailDao.selectBypowerId(powers.getId());
		List<ResourcesUrl> list = resourcesUrlDao.selectResourcesById(pd.getRoleId());
		if(list != null) {
			for(ResourcesUrl ru: list) {
				if(!StringUtils.isEmpty(ru.getUrl()) && ru.getType() == 3) {
					urls.add(ru.getUrl());
				}
			}
		}
		
		resourceMap.put("roles", roles);
		resourceMap.put("urls", urls);
		return resourceMap;
	}
}
