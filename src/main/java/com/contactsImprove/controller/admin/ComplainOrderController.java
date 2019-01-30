package com.contactsImprove.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contactsImprove.constant.JournalConst.CurrencyType;
import com.contactsImprove.constant.JournalConst.JournalType;
import com.contactsImprove.constant.MerchantOrderConst.ComplainOrderStatus;
import com.contactsImprove.constant.MerchantOrderConst.NotifyStatus;
import com.contactsImprove.constant.MerchantOrderConst.OrdersStatus;
import com.contactsImprove.constant.MerchantOrderConst.TradeStatus;
import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.entity.api.ComplainOrder;
import com.contactsImprove.entity.api.JournalAccount;
import com.contactsImprove.entity.api.MerchantNotify;
import com.contactsImprove.entity.api.Orders;
import com.contactsImprove.entity.api.UserMerchant;
import com.contactsImprove.entity.api.UserWallet;
import com.contactsImprove.entity.api.Users;
import com.contactsImprove.service.api.ComplainOrderService;
import com.contactsImprove.service.api.JournalAccountService;
import com.contactsImprove.service.api.OrdersService;
import com.contactsImprove.service.api.UserMerchantService;
import com.contactsImprove.service.api.UserWalletService;
import com.contactsImprove.service.api.UsersService;
import com.contactsImprove.utils.NotifyCommercial;
import com.contactsImprove.utils.StringUtil;


@Controller
@RequestMapping("/admin")
public class ComplainOrderController {
	
	@Autowired
	ComplainOrderService complainOrderService;
	@Autowired
	OrdersService ordersService;
	@Autowired
	JournalAccountService journalAccountService;
	@Autowired
	UserWalletService userWalletService;
	@Autowired
	NotifyCommercial notifyCommercial;	
	@Autowired
	UserMerchantService userMerchantService;	
	@Autowired
	UsersService usersService;
	
	@RequestMapping(value = "/modifyComplainStatus")
	@ResponseBody
	@Transactional
	public Map<String, Object> modifyComplainStatus(@RequestParam("tradeNumber") String tradeNumber,@RequestParam("status")Byte status,
			String remark) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ComplainOrder co=complainOrderService.selectByPrimaryKey(tradeNumber);
		Orders oldOrder=ordersService.selectByPrimaryKey(tradeNumber);
		if(co==null || co.getComplainStatus()==ComplainOrderStatus._3.status 
				|| co.getComplainStatus()==ComplainOrderStatus._4.status
				|| (oldOrder.getStatus()!=OrdersStatus._5.status && oldOrder.getStatus()!=OrdersStatus._7.status)
				|| (status!=ComplainOrderStatus._3.status && status!=ComplainOrderStatus._4.status)
			) {
			resultMap.put("code", StatusCode._319.getStatus());
			resultMap.put("msg", StatusCode._319.getMsg());
			return resultMap;
		}
		Date closeTime=new Date();
		Orders newOrder=new Orders();
		newOrder.setTradeNumber(tradeNumber);
		newOrder.setCloseTime(closeTime);
		if (!StringUtil.isBlank(oldOrder.getNotifyUrl())) {
			newOrder.setNotifyStatus(NotifyStatus.notyet.status);
		}else {
			newOrder.setNotifyStatus(NotifyStatus.notNofity.status);
		}		
		ComplainOrder newCo=new ComplainOrder();
		newCo.setTradeNumber(tradeNumber);
		newCo.setEndTime(closeTime);		
    	List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>(4);
    	List<UserWallet> userWalletList=new ArrayList<UserWallet>(3);
		if(oldOrder.getType()>1){		// 出金申诉
			if(status==ComplainOrderStatus._3.status) {				
				// 状态3 ：申诉失败 ，设置订单失败，申诉状态失败，币商币回滚 。	
				newCo.setComplainStatus(ComplainOrderStatus._3.status);
				if(StringUtil.isBlank(remark)) {
					newCo.setRemark("经调查本次申诉无效!");
				}else {
					newCo.setRemark(remark);
				}
				// 设置订单失败
				newOrder.setStatus(OrdersStatus._4.status);
				if(oldOrder.getStatus()!=OrdersStatus._7.status) {
					// 商户的币回滚
					UserWallet uwBs=userWalletService.findByLock(oldOrder.getMerchantUserId(), oldOrder.getCurrencyType());			
					UserWallet userWallet=new UserWallet();
					userWallet.setWalletId(uwBs.getWalletId());
					userWallet.setFreezeNumber(uwBs.getFreezeNumber().subtract(oldOrder.getCurrencyAmount()));
					userWallet.setCurrencyNumber(uwBs.getCurrencyNumber().add(oldOrder.getCurrencyAmount()));					
					userWalletList.add(userWallet);
					journalAccountList.add(journalAccountService.logAccountChange(uwBs, userWallet, "出金途径申诉最终失败：" + oldOrder.getTradeNumber(),oldOrder.getCurrencyAmount(),JournalType.sall.status,CurrencyType.USDT.status,oldOrder.getTradeNumber()));						
					// 币商可用额度增加
					Users u=usersService.selectByPrimaryKeyForLock(oldOrder.getUserId());				
				    Users newUser=new Users();
				    newUser.setUserId(u.getUserId());
				    newUser.setAvailableCredit(u.getAvailableCredit().add(oldOrder.getAmount()));
				    newUser.setFreezingQuota(u.getFreezingQuota().subtract(oldOrder.getAmount()));
				    
				    UserWallet oldUser=new UserWallet();	    
				    oldUser.setUserId(u.getUserId());
				    oldUser.setCurrencyNumber(u.getAvailableCredit());
				    oldUser.setFreezeNumber(u.getFreezingQuota());	 
				    
				    UserWallet newUserW=new UserWallet();	    
				    newUserW.setCurrencyNumber(newUser.getAvailableCredit());
				    newUserW.setFreezeNumber(newUser.getFreezingQuota());	    
				    usersService.updateByPrimaryKeySelective(newUser);		    
				    journalAccountList.add(journalAccountService.logAccountChange(oldUser, newUserW, "出金买币途径申诉额度回退："+oldOrder.getTradeNumber(), oldOrder.getAmount(),JournalType.buy.status,CurrencyType.CNY.status,oldOrder.getTradeNumber()));			    			    
				}
			}else if(status==ComplainOrderStatus._4.status) {													
				// 状态4：申诉成功 ，设置订单交易成功，申诉状态失败，币商减去相应的币，商户增加相应的币 。
				newCo.setComplainStatus(ComplainOrderStatus._4.status);
				if(StringUtil.isBlank(remark)) {
					newCo.setRemark("经调查本次申诉成功!");
				}else {
					newCo.setRemark(remark);
				}
				// 设置订单已完成
				newOrder.setStatus(OrdersStatus._2.status);
					//商户减去相应的币
					UserWallet uwBs=userWalletService.findByLock(oldOrder.getMerchantUserId(), oldOrder.getCurrencyType());							
					// 扣取出金费率.
					BigDecimal outRate=oldOrder.getMerchantRate();
					BigDecimal companyAmount=oldOrder.getCurrencyAmount().multiply(outRate);			
					BigDecimal bsAmount=oldOrder.getCurrencyAmount().subtract(companyAmount);
					newOrder.setActualCurrencyAmount(bsAmount);
					newOrder.setPoundage(companyAmount);								
					UserWallet userWallet=new UserWallet();
					userWallet.setWalletId(uwBs.getWalletId());
					if(oldOrder.getStatus()==OrdersStatus._7.status) {
						if(uwBs.getCurrencyNumber().compareTo(oldOrder.getCurrencyAmount())<0) {
							resultMap.put("code", StatusCode._201.getStatus());
							resultMap.put("msg", "商户:"+oldOrder.getMerchantUserId()+"没有足够的币!");
							return resultMap;
						}
						userWallet.setCurrencyNumber(uwBs.getCurrencyNumber().subtract(oldOrder.getCurrencyAmount()));
					}else {
						userWallet.setFreezeNumber(uwBs.getFreezeNumber().subtract(oldOrder.getCurrencyAmount()));	
					}
					userWalletList.add(userWallet);
					if(oldOrder.getStatus()==OrdersStatus._7.status) {
						userWallet.setFreezeNumber(uwBs.getFreezeNumber());	
					}else {
						userWallet.setCurrencyNumber(uwBs.getCurrencyNumber());	
					}
					journalAccountList.add(journalAccountService.logAccountChange(uwBs, userWallet, "出金途径申诉最终成功：" + oldOrder.getTradeNumber(),oldOrder.getCurrencyAmount(),JournalType.sall.status,CurrencyType.USDT.status,oldOrder.getTradeNumber()));
					newOrder.setFromAddress(uwBs.getWalletAddress());
					// 币商相应币种增加
					UserWallet uwSh=userWalletService.findByLock(oldOrder.getUserId(), oldOrder.getCurrencyType());
					usersService.unLockUser(oldOrder.getUserId());
					UserWallet merchantWallet = new UserWallet();
					merchantWallet.setWalletId(uwSh.getWalletId());
					merchantWallet.setCurrencyNumber(uwSh.getCurrencyNumber().add(bsAmount));
					userWalletList.add(merchantWallet);
					merchantWallet.setFreezeNumber(uwSh.getFreezeNumber());
					journalAccountList.add(journalAccountService.logAccountChange(uwSh, merchantWallet, "出金买入途径申诉最终成功：" + oldOrder.getTradeNumber(),bsAmount,JournalType.buy.status,CurrencyType.USDT.status,oldOrder.getTradeNumber()));
					newOrder.setToAddress(uwSh.getWalletAddress());		
					//收取手续费
					if(companyAmount.compareTo(BigDecimal.ZERO)>0) {
						UserWallet companyUserWallet=userWalletService.findCompanyUserByLock();
						UserWallet cum=new UserWallet();
						cum.setWalletId(companyUserWallet.getWalletId());				
						cum.setCurrencyNumber(companyUserWallet.getCurrencyNumber().add(companyAmount));
						userWalletList.add(cum);
						cum.setFreezeNumber(companyUserWallet.getFreezeNumber());
						journalAccountList.add(journalAccountService.logAccountChange(companyUserWallet, cum, "公司收取出金费：" + oldOrder.getTradeNumber(),
								companyAmount,JournalType.outRate.status,CurrencyType.USDT.status,oldOrder.getTradeNumber()));
					}					
					// 币商可用额度增加
					Users u=usersService.selectByPrimaryKeyForLock(oldOrder.getUserId());				
				    Users newUser=new Users();
				    newUser.setUserId(u.getUserId());
				    newUser.setAvailableCredit(u.getAvailableCredit().add(oldOrder.getAmount()));
				    newUser.setFreezingQuota(u.getFreezingQuota().subtract(oldOrder.getAmount()));
				    
				    UserWallet oldUser=new UserWallet();	    
				    oldUser.setUserId(u.getUserId());
				    oldUser.setCurrencyNumber(u.getAvailableCredit());
				    oldUser.setFreezeNumber(u.getFreezingQuota());	 
				    
				    UserWallet newUserW=new UserWallet();	    
				    newUserW.setCurrencyNumber(newUser.getAvailableCredit());
				    newUserW.setFreezeNumber(newUser.getFreezingQuota());	    
				    usersService.updateByPrimaryKeySelective(newUser);		    
				    journalAccountList.add(journalAccountService.logAccountChange(oldUser, newUserW, "出金买币途径申诉额度回退："+oldOrder.getTradeNumber(), oldOrder.getAmount(),JournalType.buy.status,CurrencyType.CNY.status,oldOrder.getTradeNumber()));
			    
			}											
		}else {		
			if(status==ComplainOrderStatus._3.status) {
				// 状态3 ：申诉失败 ，设置订单失败，申诉状态失败，币商币回滚 。	
				newCo.setComplainStatus(ComplainOrderStatus._3.status);
				if(StringUtil.isBlank(remark)) {
					newCo.setRemark("经调查本次申诉无效!");
				}else {
					newCo.setRemark(remark);
				}
				// 设置订单失败
				newOrder.setStatus(OrdersStatus._4.status);				
				if(oldOrder.getStatus()!=OrdersStatus._7.status) {
					//币回滚
					UserWallet uwBs=userWalletService.findByLock(oldOrder.getUserId(), oldOrder.getCurrencyType());			
					UserWallet userWallet=new UserWallet();
					userWallet.setWalletId(uwBs.getWalletId());
					userWallet.setFreezeNumber(uwBs.getFreezeNumber().subtract(oldOrder.getCurrencyAmount()));
					userWallet.setCurrencyNumber(uwBs.getCurrencyNumber().add(oldOrder.getCurrencyAmount()));			
					userWalletList.add(userWallet);
					journalAccountList.add(journalAccountService.logAccountChange(uwBs, userWallet, "订单卖出途径申诉最终失败：" + oldOrder.getTradeNumber(),oldOrder.getCurrencyAmount(),JournalType.sall.status,CurrencyType.USDT.status,oldOrder.getTradeNumber()));
				}
			}else if(status==ComplainOrderStatus._4.status) {
				// 状态4：申诉成功 ，设置订单交易成功，申诉状态失败，币商减去相应的币，商户增加相应的币 。
				newCo.setComplainStatus(ComplainOrderStatus._4.status);
				if(StringUtil.isBlank(remark)) {
					newCo.setRemark("经调查本次申诉成功!");
				}else {
					newCo.setRemark(remark);
				}
				// 设置订单已完成
				newOrder.setStatus(OrdersStatus._2.status);			
				//币商减去相应的币
				UserWallet uwBs=userWalletService.findByLock(oldOrder.getUserId(), oldOrder.getCurrencyType());			
				UserWallet userWallet=new UserWallet();
				userWallet.setWalletId(uwBs.getWalletId());
				if(oldOrder.getStatus()==OrdersStatus._7.status) {
					if(uwBs.getCurrencyNumber().compareTo(oldOrder.getCurrencyAmount())<0) {
						resultMap.put("code", StatusCode._201.getStatus());
						resultMap.put("msg", "币商："+oldOrder.getUserId()+"币不足!");							
						return resultMap;
					}
					userWallet.setCurrencyNumber(uwBs.getCurrencyNumber().subtract(oldOrder.getCurrencyAmount()));
				}else {
					userWallet.setFreezeNumber(uwBs.getFreezeNumber().subtract(oldOrder.getCurrencyAmount()));	
				}
				usersService.unLockUser(oldOrder.getUserId());
				userWalletList.add(userWallet);
				if(oldOrder.getStatus()==OrdersStatus._7.status) {
					userWallet.setFreezeNumber(uwBs.getFreezeNumber());
				}else {
					userWallet.setCurrencyNumber(uwBs.getCurrencyNumber());	
				}				
				journalAccountList.add(journalAccountService.logAccountChange(uwBs, userWallet, "订单卖出途径申诉最终成功：" + oldOrder.getTradeNumber(),oldOrder.getCurrencyAmount(),JournalType.sall.status,CurrencyType.USDT.status,oldOrder.getTradeNumber()));
				newOrder.setFromAddress(uwBs.getWalletAddress());												
				// 商户相应币种增加
				UserWallet uwSh=userWalletService.findByLock(oldOrder.getMerchantUserId(), oldOrder.getCurrencyType());				
				// 扣取入金费率.
				BigDecimal inRate=oldOrder.getMerchantRate();
				BigDecimal companyAmount=oldOrder.getCurrencyAmount().multiply(inRate);
				BigDecimal shCurrentAmount=oldOrder.getCurrencyAmount().subtract(companyAmount);
				newOrder.setActualCurrencyAmount(shCurrentAmount);
				newOrder.setPoundage(companyAmount);
				if(companyAmount.compareTo(BigDecimal.ZERO)>0) {
					
					Users userUwbs=usersService.selectByPrimaryKey(uwBs.getUserId());
					BigDecimal fees=usersService.userIncomeMoney(userUwbs, oldOrder.getTradeNumber(), oldOrder.getCurrencyAmount(), new Date());				
					companyAmount=companyAmount.subtract(fees);
					
					UserWallet companyUserWallet=userWalletService.findCompanyUserByLock();
					UserWallet cum=new UserWallet();
					cum.setWalletId(companyUserWallet.getWalletId());				
					cum.setCurrencyNumber(companyUserWallet.getCurrencyNumber().add(companyAmount));
					userWalletList.add(cum);
					cum.setFreezeNumber(companyUserWallet.getFreezeNumber());
					journalAccountList.add(journalAccountService.logAccountChange(companyUserWallet, cum, "公司收取入金费：" + oldOrder.getTradeNumber(),
							companyAmount,JournalType.inRate.status,CurrencyType.USDT.status,oldOrder.getTradeNumber()));
				}									
				UserWallet merchantWallet = new UserWallet();
				merchantWallet.setWalletId(uwSh.getWalletId());
				merchantWallet.setCurrencyNumber(uwSh.getCurrencyNumber().add(shCurrentAmount));
				userWalletList.add(merchantWallet);
				merchantWallet.setFreezeNumber(uwSh.getFreezeNumber());
				journalAccountList.add(journalAccountService.logAccountChange(uwSh, merchantWallet, "订单买入途径申诉最终成功：" + oldOrder.getTradeNumber(),shCurrentAmount,JournalType.buy.status,CurrencyType.USDT.status,oldOrder.getTradeNumber()));
				newOrder.setToAddress(uwSh.getWalletAddress());
			}		
		}
				
		// 创建回调
		if (!StringUtil.isBlank(oldOrder.getNotifyUrl())) {
			MerchantNotify mn = notifyCommercial.createNotify(oldOrder,status==ComplainOrderStatus._4.status ? TradeStatus.success.meaning : TradeStatus.fail.meaning);
			UserMerchant um = userMerchantService.selectByPrimaryKey(oldOrder.getMerchantUserId());
			// 立即回调
			byte notifyStatus=notifyCommercial.notifyOrderFinish(mn, um.getPrivateKey());
			if(notifyStatus>NotifyStatus.notyet.status) {
				if(status==NotifyStatus.success.status) {
					newOrder.setCloseTime(new Date());
				}
				newOrder.setNotifyStatus(notifyStatus);
			}
		}
		userWalletService.updateBatch(userWalletList);
		journalAccountService.insertBatch(journalAccountList);
		complainOrderService.updateByPrimaryKeySelective(newCo);
		ordersService.updateByPrimaryKeySelective(newOrder);
		
		resultMap.put("code", StatusCode._200.getStatus());
		resultMap.put("msg", StatusCode._200.getMsg());
			
		return resultMap;
	}

}
