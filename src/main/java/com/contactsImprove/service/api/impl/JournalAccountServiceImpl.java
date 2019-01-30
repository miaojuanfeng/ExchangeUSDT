package com.contactsImprove.service.api.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactsImprove.dao.api.JournalAccountDao;
import com.contactsImprove.entity.api.JournalAccount;
import com.contactsImprove.entity.api.UserWallet;
import com.contactsImprove.service.api.JournalAccountService;
import com.contactsImprove.utils.PageUtil;

@Service("journalAccountService")
public class JournalAccountServiceImpl implements JournalAccountService{
	
	@Autowired
	JournalAccountDao journalAccountDao;

	@Override
	public int deleteByPrimaryKey(Long journalAccountId) {
		// TODO Auto-generated method stub
		return journalAccountDao.deleteByPrimaryKey(journalAccountId);
	}

	@Override
	public int insertSelective(JournalAccount record) {
		// TODO Auto-generated method stub
		return journalAccountDao.insertSelective(record);
	}

	@Override
	public JournalAccount selectByPrimaryKey(Long journalAccountId) {
		// TODO Auto-generated method stub
		return journalAccountDao.selectByPrimaryKey(journalAccountId);
	}

	@Override
	public int updateByPrimaryKeySelective(JournalAccount record) {
		// TODO Auto-generated method stub
		return journalAccountDao.updateByPrimaryKeySelective(record);
	}

	public List<JournalAccount> selectByUserId(Long userId,PageUtil pu) {
	
		if(pu.getPageNumber()==null || pu.getPageNumber()==0) {
			pu.setPageNumber(1);
		}
		StringBuilder sb=new StringBuilder();
		sb.append("select journal_account_id, user_id, currency_number_before, change_amount, currency_number_after, \r\n" + 
				"    freeze_number_before, freeze_number_after, create_time, remark");
		sb.append(" from journal_account where user_id="+userId);
		sb.append(" order by journal_account_id desc");
		
		pu.setQueryStr(sb.toString());
	
		return journalAccountDao.selectByUserId(pu);
	}

	@Override
	public JournalAccount logAccountChange(UserWallet oldUw, UserWallet newUw, String remark,BigDecimal changeAmount,Byte type,Byte currencyType,String tradeNumber) {		
		JournalAccount ja=new JournalAccount();	    
	    ja.setUserId(oldUw.getUserId());
	    ja.setChangeAmount(changeAmount);
	    ja.setCurrencyNumberBefore(oldUw.getCurrencyNumber());
	    ja.setCurrencyNumberAfter(newUw.getCurrencyNumber());
	    ja.setFreezeNumberBefore(oldUw.getFreezeNumber());
	    ja.setFreezeNumberAfter(newUw.getFreezeNumber());
	    ja.setCreateTime(new Date());
	    ja.setType(type);
	    ja.setRemark(remark);	
	    ja.setCurrencyType(currencyType);	
	    ja.setTradeNumber(tradeNumber);
		return ja;
	}

	@Override
	public List<JournalAccount> selectByPage(PageUtil pu,String phoneNumber,String type,String createTime) {
		StringBuffer sb = new StringBuffer("select j.* from journal_account j, users u where j.user_id = u.user_id");
		
		if(phoneNumber != null && phoneNumber != "") {
			sb.append(" and u.phone_number =" + phoneNumber);
		}
		
		if(type != null && type != "") {
			sb.append(" and j.type =" + type);
		}
		
		if(createTime != null) {
			sb.append(" and j.create_time like '%" + createTime + "%'");
		}
		pu.setQueryStr(sb.toString());
		return journalAccountDao.selectByUserId(pu);
	}

	@Override
	public int insertBatch(Collection<JournalAccount> list) {
		// TODO Auto-generated method stub
		if(list.size()>0) {
			return journalAccountDao.insertBatch(list);
		}else {
			return 0;
		}
	}
}
