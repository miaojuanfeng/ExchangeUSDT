package com.contactsImprove.dao.admin;

import java.util.List;
import com.contactsImprove.entity.admin.AdminUser;

public interface AdminUserDao {
	
    int deleteByPrimaryKey(Long id);

    int insertSelective(AdminUser adminUser);

    AdminUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminUser adminUser);
    
    List<AdminUser> selectByPage(AdminUser adminUser);
    
    AdminUser selectByUserName(AdminUser adminUser);
    
}