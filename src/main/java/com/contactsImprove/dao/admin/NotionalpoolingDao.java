package com.contactsImprove.dao.admin;

import java.math.BigDecimal;
import java.util.List;
import com.contactsImprove.entity.admin.Notionalpooling;

public interface NotionalpoolingDao {
	
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Notionalpooling record);

    Notionalpooling selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Notionalpooling record);
    
    List<Notionalpooling> selectListBystatus();
    
    BigDecimal selectCountByreFerenceAddress(String referenceAddress);
    
    int updateByreferenceAddress(Notionalpooling record);

}