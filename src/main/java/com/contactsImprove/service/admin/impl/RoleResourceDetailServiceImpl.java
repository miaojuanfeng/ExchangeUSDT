package com.contactsImprove.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactsImprove.dao.admin.RoleResourceDetailDao;
import com.contactsImprove.entity.admin.RoleResourceDetail;
import com.contactsImprove.service.admin.RoleResourceDetailService;

@Service("roleResourceDetailService")
public class RoleResourceDetailServiceImpl implements RoleResourceDetailService{
	
	@Autowired
	private RoleResourceDetailDao roleResourceDetailDao;

	@Override
	public int updateByRoleId(RoleResourceDetail record) {
		return roleResourceDetailDao.updateByRoleId(record);
	}

}
