package com.contactsImprove.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.contactsImprove.dao.admin.WalletDao;
import com.contactsImprove.entity.admin.Wallet;
import com.contactsImprove.service.admin.WalletService;

@Service("walletService")
public class WalletServiceImpl implements WalletService{
	
	@Autowired
	private  WalletDao walletDao;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return walletDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(Wallet record) {
		return walletDao.insertSelective(record);
	}

	@Override
	public Wallet selectByPrimaryKey(Integer id) {
		return walletDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Wallet record) {
		return walletDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public Wallet selectWalletByType(Integer type) {
		return walletDao.selectWalletByType(type);
	}

}
