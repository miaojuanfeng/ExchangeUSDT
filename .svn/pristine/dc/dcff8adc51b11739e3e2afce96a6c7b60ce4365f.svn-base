package com.contactsImprove.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.constant.JournalConst.CurrencyType;
import com.contactsImprove.constant.JournalConst.JournalType;
import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.constant.UsersConst.UsersType;
import com.contactsImprove.entity.api.JournalAccount;
import com.contactsImprove.entity.api.UserMerchant;
import com.contactsImprove.entity.api.UserWallet;
import com.contactsImprove.entity.api.Users;
import com.contactsImprove.json.JSON;
import com.contactsImprove.service.api.JournalAccountService;
import com.contactsImprove.service.api.UserWalletService;
import com.contactsImprove.service.api.UsersService;
import com.contactsImprove.utils.LoggerUtil;
import com.contactsImprove.utils.PageUtil;

@Controller
@RequestMapping("/admin/user")
public class UsersController {
	@Autowired
	JournalAccountService journalAccountService;
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private UserWalletService userWalletService;

	@RequestMapping(value = "/merchants")
	public String merchantView() {
		return "merchant";
	}
	
	@RequestMapping(value = "/userRate")
	public String userRateView() {
		return "userRate";
	}
	
	@RequestMapping(value = "/users")
	public String usersView() {
		return "users";
	}
	
	@RequestMapping(value = "/moneyusers")
	public String moneyusersView() {
		return "moneyusers";
	}
	
	@RequestMapping(value = "/examine")
	public String examine() {
		return "examine";
	}
	
	@RequestMapping("/mercexamine")
	public String mrcexamine() {
		return "mercexamine";
	}
	
	
	
	
	/**
	 * 手动冻结USDT
	 * @param users
	 * @return
	 */
	@RequestMapping(value = "/freezeUsdt")
	@ResponseBody
	public Map<String, Object> freezeUsdt(Long userId,BigDecimal freezeNumber){		
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		UserWallet uw=userWalletService.findByLock(userId, SystemConst.USDT);
		if(uw!=null){
			if(uw.getCurrencyNumber().compareTo(freezeNumber)<0) {
				resultMap.put("code", StatusCode._201.getStatus());
				resultMap.put("msg", "余额不足！");
				return resultMap;
			}
			UserWallet newUw=new UserWallet();
			newUw.setWalletId(uw.getWalletId());			
			newUw.setFreezeNumber(uw.getFreezeNumber().add(freezeNumber));	
			newUw.setCurrencyNumber(uw.getCurrencyNumber().subtract(freezeNumber));	
			int result=userWalletService.updateByPrimaryKeySelective(newUw);
			if(result>0) {
				List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>();
				journalAccountList.add(journalAccountService.logAccountChange(uw,newUw, "管理员在后台手动冻结USDT数量："+freezeNumber+"个",freezeNumber,JournalType.freeze.status,CurrencyType.USDT.status,""));
				journalAccountService.insertBatch(journalAccountList);
			}			
			resultMap.put("code", StatusCode._200.getStatus());
			resultMap.put("msg", StatusCode._200.getMsg());
		}else {
			resultMap.put("code", StatusCode._201.getStatus());
			resultMap.put("msg", StatusCode._201.getMsg());
		}
		return resultMap;
	}
	
	
	/**
	 * 分页查询用户钱包
	 * @param users
	 * @return
	 */
	@RequestMapping(value = "/selectUserRate")
	@JSON(type = UserWallet.class, filter = "payPassword,status,reserve,walletAddress")
	public Map<String, Object> selectUserRate(Users users,int page, int limit) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		PageUtil pageUtil=new PageUtil();			
		pageUtil.setPageNumber(page);
		pageUtil.setPageSize(limit);		
		List<UserWallet> list=userWalletService.selectUsersRateByList(users,pageUtil);		
		resultMap.put("code", StatusCode._0.getStatus());
		resultMap.put("msg", StatusCode._0.getMsg());
		resultMap.put("count", pageUtil.getPageTotal());
		resultMap.put("data", list);
		return resultMap;
	}
	
	/**
	 * 更新费率
	 * @param users
	 * @return
	 */
	@RequestMapping(value = "/udpateUserRate")
	@Transactional
	@ResponseBody
	public Map<String, Object> udpateUserRate(UserWallet uw) {		
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		UserWallet oldUw=userWalletService.selectByPrimaryKey(uw.getWalletId());
		if(oldUw==null) {
			resultMap.put("code", StatusCode._201.getStatus());
			resultMap.put("msg", StatusCode._201.getMsg());
			return resultMap;
		}
		if(uw.getCurrencyNumber()!=null && uw.getCurrencyNumber().compareTo(BigDecimal.ZERO)>0){			
			Users user=usersService.selectByPrimaryKey(oldUw.getUserId());
			if(user!=null && user.getType()==UsersType._2.status) {				
				UserWallet lockUw=userWalletService.findByLock(oldUw.getUserId(), oldUw.getCurrencyType());				
				UserWallet newUw=new UserWallet();
				newUw.setWalletId(lockUw.getWalletId());
				newUw.setInRate(uw.getInRate());
				newUw.setOutRate(uw.getOutRate());
				newUw.setCurrencyNumber(lockUw.getCurrencyNumber().add(uw.getCurrencyNumber()));					
				int result=userWalletService.updateUserRate(newUw);
				newUw.setFreezeNumber(lockUw.getFreezeNumber());	
				if(result>0) {
					List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>();
					journalAccountList.add(journalAccountService.logAccountChange(lockUw,newUw, "管理员在后台手动追加USDT数量："+uw.getCurrencyNumber()+"个", uw.getCurrencyNumber(),JournalType.appendUsdt.status,CurrencyType.USDT.status,""));
					journalAccountService.insertBatch(journalAccountList);
				}
				resultMap.put("code", StatusCode._200.getStatus());
				resultMap.put("msg", StatusCode._200.getMsg());
			}else {
				resultMap.put("code", StatusCode._201.getStatus());
				resultMap.put("msg", StatusCode._201.getMsg());
			}
		}else {
			uw.setCurrencyNumber(null);
			if(uw.getInRate()==null && uw.getOutRate()==null ){
				resultMap.put("code", StatusCode._201.getStatus());
				resultMap.put("msg", StatusCode._201.getMsg());			
			}else {
				int result=userWalletService.updateByPrimaryKeySelective(uw);		
				if(result > 0) {
					resultMap.put("code", StatusCode._200.getStatus());
					resultMap.put("msg", StatusCode._200.getMsg());
				} else {
					resultMap.put("code", StatusCode._201.getStatus());
					resultMap.put("msg", StatusCode._201.getMsg());
				}
			}
		}
		return resultMap;
	}
	
	
	@RequestMapping(value = "/examineUser")
	@ResponseBody
	public Map<String, Object> examineUser(Users users,int page, int limit) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			users.setPageNumber(page);
			users.setPageSize(limit);
			List<Users> list = usersService.selectUsersByStatus(users);
			resultMap.put("code", StatusCode._0.getStatus());
			resultMap.put("msg", StatusCode._0.getMsg());
			resultMap.put("count", users.getPageTotal());
			resultMap.put("data", list);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}

	/**
	 * 分页查询商户
	 * 
	 * @param users
	 * @return
	 */
	@RequestMapping(value = "/selectUsersByMerchantList")
	@JSON(type = Users.class, include = "userId,userName,phoneNumber,status,createTime,userMerchant,userWallet,role")
	@JSON(type = UserMerchant.class, include = "privateKey,publicKey,createTime")
	@JSON(type = UserWallet.class, include = "currencyNumber,freezeNumber,walletAddress")
	public Map<String, Object> selectUsersByMerchantList(Users users,int page, int limit,@DateTimeFormat(pattern = "yyyy-MM-dd")Date createTime) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			UserMerchant um = new UserMerchant();
			um.setCreateTime(createTime);
			users.setUserMerchant(um);
			users.setPageNumber(page);
			users.setPageSize(limit);
			List<Users> list = usersService.selectUsersByMerchantList(users);
			resultMap.put("code", StatusCode._0.getStatus());
			resultMap.put("msg", StatusCode._0.getMsg());
			resultMap.put("count", users.getPageTotal());
			resultMap.put("data", list);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}

	/**
	 * 分页查询用户
	 * 
	 * @param users
	 * @return
	 */
	@RequestMapping(value = "/selectUsersByType")
	@JSON(type = Users.class, include = "role,userId,userName,phoneNumber,createTime,status,availableCredit,freezingQuota,userWallet")
	@JSON(type = UserMerchant.class, include = "currencyNumber,freezeNumber,walletAddress")
	public Map<String, Object> selectUsersByType(Users users,int page, int limit) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			users.setPageNumber(page);
			users.setPageSize(limit);
			List<Users> list = usersService.selectUsersByType(users);
			resultMap.put("code", StatusCode._0.getStatus());
			resultMap.put("msg", StatusCode._0.getMsg());
			resultMap.put("count", users.getPageTotal());
			resultMap.put("data", list);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}
	
	@RequestMapping(value = "/updateUser")
	@ResponseBody
	@Transactional
	public Map<String, Object> update(Users users,BigDecimal currencyNumber) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(users != null) {
				Users u = usersService.selectByPrimaryKeyForLock(users.getUserId());
				if(u.getStatus() == 2) {
					users.setType((byte)2);
				} else if(u.getStatus() == 3) {
					users.setType((byte)1);
				}
				
				if(users.getAvailableCredit() != null) {
					users.setAvailableCredit(u.getAvailableCredit().add(users.getAvailableCredit()));
				}				
				int result = usersService.updateByPrimaryKeySelective(users);				
				if(currencyNumber!=null && currencyNumber.compareTo(BigDecimal.ZERO)>0){
					result=0;
					UserWallet lockUw=userWalletService.findByLock(users.getUserId(), SystemConst.USDT);				
					UserWallet newUw=new UserWallet();
					newUw.setWalletId(lockUw.getWalletId());
					newUw.setCurrencyNumber(lockUw.getCurrencyNumber().add(currencyNumber));					
					result=userWalletService.updateUserRate(newUw);
					newUw.setFreezeNumber(lockUw.getFreezeNumber());	
					if(result>0) {
						List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>(1);
						journalAccountList.add(journalAccountService.logAccountChange(lockUw,newUw, "管理员在后台手动追加USDT数量："+currencyNumber+"个", currencyNumber,JournalType.appendUsdt.status,CurrencyType.USDT.status,""));
						journalAccountService.insertBatch(journalAccountList);
					}
				}
				resultMap.put("code", StatusCode._200.getStatus());
				resultMap.put("msg", StatusCode._200.getMsg());
								
			} else {
				resultMap.put("code", StatusCode._201.getStatus());
				resultMap.put("msg", StatusCode._201.getMsg());
			}
			return resultMap;
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultMap.put("code", StatusCode._500.getStatus());
			resultMap.put("msg", StatusCode._500.getMsg());
			LoggerUtil.error(e.toString(), e);
			return resultMap;
		}
	}

}