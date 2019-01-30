package com.contactsImprove.service.admin;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.contactsImprove.entity.admin.AdminUser;

public interface AdminUserService {
	
	int deleteByPrimaryKey(Long id);

    int insertSelective(AdminUser adminUser);

    AdminUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminUser adminUser);
    
    List<AdminUser> selectByPage(AdminUser adminUser);
    
    AdminUser selectByUserName(AdminUser adminUser);
    
    Map<String, Set<String>> selectResourceMapByUserId(long powerId);
}
