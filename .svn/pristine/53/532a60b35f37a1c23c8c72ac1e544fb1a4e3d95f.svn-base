package com.contactsImprove.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.contactsImprove.dao.admin.PowersDao;
import com.contactsImprove.entity.admin.Powers;
import com.contactsImprove.service.admin.PowersService;

@Service("powersService")
public class PowersServiceImpl implements PowersService{
	
	@Autowired
	private PowersDao powersDao;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return powersDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(Powers record) {
		return powersDao.insertSelective(record);
	}

	@Override
	public Powers selectByPrimaryKey(Long id) {
		return powersDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Powers record) {
		return powersDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<Powers> selectPowersByList() {
		return powersDao.selectPowersByList();
	}

}
