package com.contactsImprove.service.admin;

import java.util.List;
import com.contactsImprove.entity.admin.Powers;

public interface PowersService {
	
	int deleteByPrimaryKey(Long id);

    int insertSelective(Powers record);

    Powers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Powers record);
    
    List<Powers> selectPowersByList();

}
