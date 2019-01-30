package com.contactsImprove.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.entity.api.JournalAccount;
import com.contactsImprove.entity.api.Users;
import com.contactsImprove.json.JSON;
import com.contactsImprove.service.api.JournalAccountService;
import com.contactsImprove.service.api.UsersService;
import com.contactsImprove.utils.LoggerUtil;
import com.contactsImprove.utils.PageUtil;

@Controller
@RequestMapping("/admin/JournalAccount")
public class JournalAccountController {
	
	@Autowired
	private JournalAccountService journalAccountService;
	
	@Autowired
	private UsersService usersService;
	
	@RequestMapping(value = "/journalAccounts")
	public String JournalAccounts() {
		return "journalAccount";
	}
	
	@RequestMapping(value = "/selectByPage")
	@JSON(type = JournalAccount.class, include = "journalAccountId,userId,changeAmount,currencyNumberBefore,currencyNumberAfter,freezeNumberBefore,freezeNumberAfter,createTime,type,remark,users")
	@JSON(type = Users.class, include = "userName,phoneNumber")
	public Map<String, Object> selectByPage(int page, int limit,String phoneNumber,String type,String createTime) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			PageUtil pu = new PageUtil();
			pu.setPageNumber(page);
			pu.setPageSize(limit);
			List<JournalAccount> list = journalAccountService.selectByPage(pu,phoneNumber,type,createTime);
			List<JournalAccount> data = new ArrayList<>();
			for(JournalAccount journal : list) {
				Users users = usersService.selectByPrimaryKey(journal.getUserId());
				journal.setUsers(users);
				data.add(journal);
			}
			
			resultMap.put("code", StatusCode._0.getStatus());
			resultMap.put("msg", StatusCode._0.getMsg());
			resultMap.put("count", pu.getPageTotal());
			resultMap.put("data", data);
			return resultMap;
		} catch (Exception e) {
			LoggerUtil.error(e.toString(), e);
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			return resultMap;
		}
	}

}
