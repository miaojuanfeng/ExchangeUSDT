package com.contactsImprove.service.admin.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.contactsImprove.dao.admin.NotionalpoolingDao;
import com.contactsImprove.entity.admin.Notionalpooling;
import com.contactsImprove.service.admin.NotionalpoolingService;

@Service("notionalpoolingService")
public class NotionalpoolingServiceImpl implements NotionalpoolingService{
	
	@Autowired
	private NotionalpoolingDao notionalpoolingDao;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return notionalpoolingDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(Notionalpooling record) {
		return notionalpoolingDao.insertSelective(record);
	}

	@Override
	public Notionalpooling selectByPrimaryKey(Integer id) {
		return notionalpoolingDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Notionalpooling record) {
		return notionalpoolingDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<Notionalpooling> selectListBystatus() {
		return notionalpoolingDao.selectListBystatus();
	}

	@Override
	public BigDecimal selectCountByreFerenceAddress(String referenceAddress) {
		return notionalpoolingDao.selectCountByreFerenceAddress(referenceAddress);
	}

	@Override
	public int updateByreferenceAddress(Notionalpooling record) {
		return notionalpoolingDao.updateByreferenceAddress(record);
	}

}
