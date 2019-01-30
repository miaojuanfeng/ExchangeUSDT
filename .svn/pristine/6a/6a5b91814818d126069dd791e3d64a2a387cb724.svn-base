package com.contactsImprove.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactsImprove.dao.admin.BankMarkDao;
import com.contactsImprove.entity.admin.BankMark;
import com.contactsImprove.notefield.DynamicColumn;
import com.contactsImprove.service.admin.BankMarkService;
import com.contactsImprove.utils.PageUtil;

@Service("bankMarkService")
public class BankMarkServiceImpl implements BankMarkService {

	@Autowired
	BankMarkDao bankMarkDao;
	
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return bankMarkDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(BankMark record) {
		// TODO Auto-generated method stub
		return bankMarkDao.insertSelective(record);
	}

	@Override
	public BankMark selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return bankMarkDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(BankMark record) {
		// TODO Auto-generated method stub
		return bankMarkDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<BankMark> selectALlBankMark(BankMark bm) {
		
		StringBuilder sb=new StringBuilder();
		sb.append("select * from bank_mark");
		DynamicColumn.appendWhere(sb, bm);
					
		sb.append(" order by id desc");
		bm.setQueryStr(sb.toString());
		return bankMarkDao.selectALlBankMark(bm);
	}

	@Override
	public List<BankMark> selectBankMarkList() {
		// TODO Auto-generated method stub
		return bankMarkDao.selectBankMarkList();
	}
	
}
