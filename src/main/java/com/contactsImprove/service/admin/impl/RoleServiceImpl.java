package com.contactsImprove.service.admin.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.contactsImprove.dao.admin.RoleDao;
import com.contactsImprove.entity.admin.Role;
import com.contactsImprove.service.admin.RoleService;

@Service("roleService")
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return roleDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(Role record) {
		return roleDao.insertSelective(record);
	}

	@Override
	public Role selectByPrimaryKey(Long id) {
		return roleDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Role record) {
		return roleDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<Role> selectRoleByList(Role record) {
		StringBuffer queryStr = new StringBuffer("SELECT * FROM role");
		if(record.getRoleName() != null && record.getRoleName() != "") {
			queryStr.append(" where role_name like " + "'%" + record.getRoleName() + "%'");
			if(record.getCreateTime() != null && !record.getCreateTime().equals("")) {
				String createTime = new SimpleDateFormat("yyyy-MM-dd").format(record.getCreateTime());
				queryStr.append(" and create_time like" + "'%" + createTime + "%'");
			}
		} else {
			if(record.getCreateTime() != null && !record.getCreateTime().equals("")) {
				String createTime = new SimpleDateFormat("yyyy-MM-dd").format(record.getCreateTime());
				queryStr.append(" where create_time like" + "'%" + createTime + "%'");
			}
		}
		
		record.setQueryStr(queryStr.toString());
		return roleDao.selectRoleByList(record);
	}

	@Override
	public List<Role> selectByPrimaryList() {
		return roleDao.selectByPrimaryList();
	}
}
