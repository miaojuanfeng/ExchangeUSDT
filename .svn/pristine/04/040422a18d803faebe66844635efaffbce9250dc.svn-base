package com.contactsImprove.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.contactsImprove.dao.admin.PowerRoleDetailDao;
import com.contactsImprove.entity.admin.PowerRoleDetail;
import com.contactsImprove.service.admin.PowerRoleDetailService;

@Service("powerRoleDetailService")
public class PowerRoleDetailServiceImpl implements PowerRoleDetailService{
	
	@Autowired
	private PowerRoleDetailDao powerRoleDetailDao;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return powerRoleDetailDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(PowerRoleDetail record) {
		return powerRoleDetailDao.insertSelective(record);
	}

	@Override
	public PowerRoleDetail selectByPrimaryKey(Long id) {
		return powerRoleDetailDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(PowerRoleDetail record) {
		return powerRoleDetailDao.updateByPrimaryKeySelective(record);
	}

}
