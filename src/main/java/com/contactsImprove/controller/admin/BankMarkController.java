package com.contactsImprove.controller.admin;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.entity.admin.BankMark;
import com.contactsImprove.service.admin.BankMarkService;

@Controller
@RequestMapping("/admin/bank")
public class BankMarkController {
	@Autowired
	BankMarkService bankMarkService ; 
	
	@RequestMapping(value = "/bankmark")
	public String bankmark() {
		return "bankmark";
	}
	
	@RequestMapping(value = "/bankmarkList")
	@ResponseBody
	public Map<String, Object> bankmarkList(BankMark bm,int page ,int limit) {
		Map<String, Object> resultMap = new HashMap<String, Object>(4);				
		bm.setPageSize(limit);
		bm.setPageNumber(page);					
		List<BankMark> list=bankMarkService.selectALlBankMark(bm);			
		resultMap.put("code", StatusCode._0.getStatus());
		resultMap.put("msg", StatusCode._0.getMsg());
		resultMap.put("data", list);
		resultMap.put("count", bm.getPageTotal());
		return resultMap;
	}
	
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public Map<String, Object> add(BankMark bm) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);		
		bm.setCreateTime(new Date());
		bankMarkService.insertSelective(bm);
		resultMap.put("code", StatusCode._0.getStatus());
		resultMap.put("msg", StatusCode._0.getMsg());
		return resultMap;
	}
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public Map<String, Object> update(BankMark bm) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);								
		bankMarkService.updateByPrimaryKeySelective(bm);
		resultMap.put("code", StatusCode._0.getStatus());
		resultMap.put("msg", StatusCode._0.getMsg());
		return resultMap;
	}
	
	@RequestMapping(value = "/selectById")
	@ResponseBody
	public Map<String, Object> selectById(int id) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);								
		BankMark bm=bankMarkService.selectByPrimaryKey(id);
		resultMap.put("code", StatusCode._0.getStatus());
		resultMap.put("msg", StatusCode._0.getMsg());
		resultMap.put("data", bm);
		return resultMap;
	}
	
}
