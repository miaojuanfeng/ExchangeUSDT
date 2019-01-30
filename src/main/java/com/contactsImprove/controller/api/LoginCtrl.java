package com.contactsImprove.controller.api;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.contactsImprove.constant.JournalConst.CurrencyType;
import com.contactsImprove.constant.JournalConst.JournalType;
import com.contactsImprove.constant.MerchantOrderConst.ComplainOrderStatus;
import com.contactsImprove.constant.MerchantOrderConst.NotifyStatus;
import com.contactsImprove.constant.MerchantOrderConst.OrderType;
import com.contactsImprove.constant.MerchantOrderConst.OrdersStatus;
import com.contactsImprove.constant.MerchantOrderConst.Payment;
import com.contactsImprove.constant.PaymentModeConst.PaymentValid;
import com.contactsImprove.constant.PaymentRotateConst.PaymentRotateStatus;
import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.constant.UsersConst;
import com.contactsImprove.constant.UsersConst.UsersCentre;
import com.contactsImprove.constant.UsersConst.UsersLevel;
import com.contactsImprove.constant.UsersConst.UsersRole;
import com.contactsImprove.constant.UsersConst.UsersStatus;
import com.contactsImprove.constant.UsersConst.UsersType;
import com.contactsImprove.entity.admin.BankMark;
import com.contactsImprove.entity.api.ComplainOrder;
import com.contactsImprove.entity.api.Currency;
import com.contactsImprove.entity.api.JournalAccount;
import com.contactsImprove.entity.api.OrderQueryPara;
import com.contactsImprove.entity.api.Orders;
import com.contactsImprove.entity.api.PaymentMode;
import com.contactsImprove.entity.api.PaymentRotate;
import com.contactsImprove.entity.api.PaymentRule;
import com.contactsImprove.entity.api.UserMerchant;
import com.contactsImprove.entity.api.UserWallet;
import com.contactsImprove.entity.api.Users;
import com.contactsImprove.json.JSON;
import com.contactsImprove.mysession.SessionContext;
import com.contactsImprove.service.admin.BankMarkService;
import com.contactsImprove.service.api.ComplainOrderService;
import com.contactsImprove.service.api.CurrencyService;
import com.contactsImprove.service.api.JournalAccountService;
import com.contactsImprove.service.api.OrdersService;
import com.contactsImprove.service.api.PaymentModeService;
import com.contactsImprove.service.api.PaymentRotateService;
import com.contactsImprove.service.api.PaymentRuleService;
import com.contactsImprove.service.api.UserMerchantService;
import com.contactsImprove.service.api.UserWalletService;
import com.contactsImprove.service.api.UsersService;
import com.contactsImprove.utils.DateTools;
import com.contactsImprove.utils.Idempotent;
import com.contactsImprove.utils.LoggerUtil;
import com.contactsImprove.utils.Md5Util;
import com.contactsImprove.utils.OrderNumEngender;
import com.contactsImprove.utils.PageUtil;
import com.contactsImprove.utils.RSAUtils;
import com.contactsImprove.utils.RedisUtil;
import com.contactsImprove.utils.SMSUtil;
import com.contactsImprove.utils.SessionUtil;
import com.contactsImprove.utils.StringUtil;
import com.contactsImprove.utils.USDTUtils;
import com.contactsImprove.utils.UploadUtil;
import com.contactsImprove.utils.UserIdEngender;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/api")
public class LoginCtrl {

	@Autowired
	private UsersService usersService;
	@Autowired
	UserMerchantService userMerchantService;
	@Autowired
	UserWalletService userWalletService;
	@Autowired
	OrdersService ordersService;
	@Autowired
	PaymentModeService paymentModeService;
	@Autowired
	JournalAccountService journalAccountService;
	@Autowired
	USDTUtils uSDTUtils;
	@Autowired
	CurrencyService currencyService;
	@Autowired
	ComplainOrderService complainOrderService;
	@Autowired
	BankMarkService bankMarkService ; 
	@Autowired
	PaymentRuleService paymentRuleService;
	@Autowired
	PaymentRotateService paymentRotateService;
	
	/**
	 * 币商发起申诉
	 * tradeNumber:交易流水号
	 * @return
	 */
	@RequestMapping(value = "/MoneyMerchantComplain",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public Map<String, Object> MoneyMerchantComplain(HttpServletRequest request,@RequestParam("tradeNumber")String tradeNumber,@RequestParam("body")String body){
		Map<String, Object> map = new HashMap<>();	
		Users sessionUser = SessionUtil.getSession(request);
	    Orders oldOrder=ordersService.selectByPrimaryKey(tradeNumber);	
	    if(oldOrder==null){
			map.put("code", StatusCode._309.status);
			map.put("msg", StatusCode._309.msg);
			return map;
	    }	    
	    if(!(oldOrder.getType()==OrderType._0.type || oldOrder.getType()==OrderType._2.type)
	    	|| !sessionUser.getUserId().equals(oldOrder.getUserId()) || oldOrder.getStatus()==OrdersStatus._6.status
	    	|| oldOrder.getStatus()==OrdersStatus._2.status || oldOrder.getStatus()==OrdersStatus._5.status
	    	|| oldOrder.getStatus()==OrdersStatus._7.status 
	    	) {
			map.put("code", StatusCode._310.status);
			map.put("msg", StatusCode._310.msg);
			return map;
	    }	    	    
    	if(Idempotent.fx_MoneyMerchantComplain(tradeNumber)) {
			map.put("code", StatusCode._315.status);
			map.put("msg", StatusCode._315.msg);
			return map;
    	}    	
	    Orders newOrder=new Orders();
	    newOrder.setTradeNumber(tradeNumber);
	    if(oldOrder.getStatus()<OrdersStatus._2.status) {
	    	newOrder.setStatus(OrdersStatus._5.status);
	    }else if(oldOrder.getStatus()==OrdersStatus._3.status || oldOrder.getStatus()==OrdersStatus._4.status){
	    	newOrder.setStatus(OrdersStatus._7.status);
	    }
	    ordersService.updateByPrimaryKeySelective(newOrder);
	    ComplainOrder co=new ComplainOrder();
	    co.setTradeNumber(tradeNumber);
	    co.setBody(body);
	    co.setComplainStatus(ComplainOrderStatus._1.status);
	    co.setCreateTime(new Date());	    	  
	    complainOrderService.insertSelective(co);	    
	    map.put("code", StatusCode._200.status);
	    map.put("msg", StatusCode._200.msg);
 	    		
	    return map;
	}	
	
	/**
	 * 工作时间
	 */
	@RequestMapping(value = "/workHours", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> remainingAmount(HttpServletRequest request,@RequestParam("workHours") Integer workHours) 
	{
		Map<String, Object> map = new HashMap<>(2);
		Users sessionUser = SessionUtil.getSession(request);	
		Users user=new Users();
		user.setUserId(sessionUser.getUserId());		
		user.setWorkHours(workHours);
		usersService.updateByPrimaryKeySelective(user);				
		map.put("code", StatusCode._200.status);
		map.put("msg", StatusCode._200.msg);
		
		return map;
	}
	
	/**
	 * 用户余额
	 */
	@RequestMapping(value = "/remainingAmount", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> remainingAmount(HttpServletRequest request) 
	{
		Map<String, Object> map = new HashMap<>(2);
		Users sessionUser = SessionUtil.getSession(request);		
		HashMap<String,BigDecimal> hm=userWalletService.selectRemainingAmount(sessionUser.getUserId());				
		map.put("code", StatusCode._200.status);
		map.put("msg", StatusCode._200.msg);
		map.put("data", hm);
		return map;
	}
	
	/**
	 * 商户出金
	 */
	@RequestMapping(value = "/goldYield", method = RequestMethod.POST)
	@JSON(type=Orders.class,include="tradeNumber,merchantUserId,paymentTypeName,paymentNumber,currencyType,currencyAmount,exchangeRate,createTime,amount,type,status,ext")
	public Map<String, Object> goldYield(HttpServletRequest request,
			@RequestParam("bankCardNumber")String bankCardNumber,
			@RequestParam("branchNumber")String branchNumber,
			@RequestParam("bankName")String bankName,
			@RequestParam("ownerName")String ownerName,
			@RequestParam("amount") BigDecimal amount,@RequestParam("password") String password) 
	{
		Map<String, Object> map = new HashMap<>(2);
		Users sessionUser = SessionUtil.getSession(request);
		String secretPassword = Md5Util.execute(sessionUser.getUserId() + password);
		if (StringUtil.isBlank(password) || !secretPassword.equals(sessionUser.getPassword())){
			map.put("code", StatusCode._204.getStatus());
			map.put("msg", StatusCode._204.getMsg());
			return map;
		}
		if(amount.compareTo(SystemConst.limitCNYOut)>0) {	
			map.put("code", StatusCode._317.getStatus());
			map.put("msg", StatusCode._317.getMsg());
			return map;			
		}
		Orders order=new Orders();
		order.setMerchantUserId(sessionUser.getUserId());
		order.setCurrencyType(CurrencyType.USDT.meaning);		
		order.setAmount(amount);
		order.setPaymentNumber(bankCardNumber);
		
		JSONObject json=new JSONObject();
		json.put("payImage", "");
		json.put("bankName", bankName);
		json.put("branchNumber", branchNumber);
		json.put("ownerName", ownerName);
		order.setExt(json.toString());
	    // 获取汇率
	    Currency c=currencyService.selectByPrimaryKey(order.getCurrencyType());
	    if(c==null) {
			map.put("code", StatusCode._201.status);
			map.put("msg", StatusCode._201.msg);
			return map;
	    }
	    byte paymentType=Payment.union_pay.type;
    	// 判断币商额度，匹配币商接单  
	    int hour=(int)Math.pow(2, java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY));
    	List<Users> list=usersService.selectWaitByUsdt(order.getAmount(), SystemConst.random_limit, paymentType,hour);    	
	    if(list==null ||list.size()<1) {
			map.put("code", StatusCode._316.status);
			map.put("msg", "暂时无法匹配承兑商，请稍后重试");
			return map;
	    }
	    order.setActualAmount(order.getAmount());
	    java.util.Random random=new java.util.Random();	    
	    long userId=list.get(random.nextInt(list.size())).getUserId();	
	    usersService.lockUser(userId);
	    UserWallet bsRate=userWalletService.selectByType(userId, order.getCurrencyType());
	    BigDecimal bsoutRate=bsRate.getOutRate();	  
	    order.setUnderwriterRate(bsoutRate);
	    UserWallet uwRate=userWalletService.selectByType(order.getMerchantUserId(), order.getCurrencyType());
	    BigDecimal outRate=uwRate.getOutRate();	  
	    order.setMerchantRate(outRate);
	    BigDecimal companyAmount =order.getAmount().divide(c.getRate(),SystemConst.decimal_place_num, BigDecimal.ROUND_HALF_UP).multiply(outRate);	    
	    BigDecimal bsAmount= order.getAmount().divide(c.getRate().subtract(bsoutRate),SystemConst.decimal_place_num, BigDecimal.ROUND_HALF_UP);	    	  	   
	    BigDecimal totalAmount=companyAmount.add(bsAmount);
	    order.setCurrencyAmount(totalAmount);
	    	   	 
	    String tradeNumber=OrderNumEngender.getOrderNum();
	    order.setTradeNumber(tradeNumber);	 
	 	    
	   // 判读商户是否有足够金额的币    
	    UserWallet uw=userWalletService.findByLock(order.getMerchantUserId(),order.getCurrencyType());	    
	    if(uw==null || uw.getCurrencyNumber().compareTo(order.getCurrencyAmount())<0 || uw.getCurrencyNumber().compareTo(SystemConst.minNum)<0) {
			map.put("code", StatusCode._201.status);
			map.put("msg", order.getCurrencyType()+"数量不足");
			return map;
	    }	    	   	    	   	    	    
	    order.setUserId(userId);
	    order.setPayTimeout(SystemConst.Order_TimeOut);	    	   
	    order.setExchangeRate(c.getRate());
	    order.setCreateTime(new Date());
		if (!StringUtil.isBlank(order.getNotifyUrl())) {
			order.setNotifyStatus(NotifyStatus.notyet.status);
		}else {
			order.setNotifyStatus(NotifyStatus.notNofity.status);
		}	
	    order.setType(OrderType._2.type);	    
	    order.setStatus(OrdersStatus._0.status);	
	    order.setPaymentTypeName(Payment.getMeaningByType(paymentType));
	    Users user=usersService.selectByPrimaryKeyForLock(userId);
	    if(user.getAvailableCredit().compareTo(order.getAmount())<0){
			map.put("code", StatusCode._316.status);
			map.put("msg", StatusCode._316.msg);
			return map;
	    }
	    List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>();
	    // 减去币商的可用额度
	    Users newUser=new Users();
	    newUser.setUserId(userId);
	    newUser.setAvailableCredit(user.getAvailableCredit().subtract(order.getAmount()));
	    newUser.setFreezingQuota(user.getFreezingQuota().add(order.getAmount()));
	    
	    UserWallet oldUser=new UserWallet();	
	    oldUser.setUserId(userId);
	    oldUser.setCurrencyNumber(user.getAvailableCredit());
	    oldUser.setFreezeNumber(user.getFreezingQuota());	 
	    
	    UserWallet newUserW=new UserWallet();	    
	    newUserW.setCurrencyNumber(newUser.getAvailableCredit());
	    newUserW.setFreezeNumber(newUser.getFreezingQuota());	    
	    usersService.updateByPrimaryKeySelective(newUser);
	    journalAccountList.add(journalAccountService.logAccountChange(oldUser, newUserW, "出金买入额度冻结："+order.getTradeNumber(), order.getAmount(),JournalType.buy.status,CurrencyType.CNY.status,order.getTradeNumber()));
	    
	    // 冻结商户的币
	    UserWallet userWallet=new UserWallet();
	    userWallet.setWalletId(uw.getWalletId());
	    userWallet.setCurrencyNumber(uw.getCurrencyNumber().subtract(order.getCurrencyAmount())); 
	    userWallet.setFreezeNumber(uw.getFreezeNumber().add(order.getCurrencyAmount()));
	    userWalletService.updateByPrimaryKeySelective(userWallet);
	    userWallet.setFreezeNumber(uw.getFreezeNumber().add(order.getCurrencyAmount()));   	    
	    journalAccountList.add(journalAccountService.logAccountChange(uw, userWallet, "出金卖出币冻结："+order.getTradeNumber(), order.getCurrencyAmount(),JournalType.sall.status,CurrencyType.USDT.status,order.getTradeNumber()));
	     		    
	    ordersService.insert(order);	
	    journalAccountService.insertBatch(journalAccountList);
/*	    // 加入大转盘
	    Wheel.pushSlot(tradeNumber);	 */   
	    SMSUtil.sentBuyRemind(user.getPhoneNumber(), tradeNumber,order.getPaymentTypeName(),order.getAmount()+""," "+SystemConst.CNY,DateTools.DateToStr2(new Date()));	    
	    map.put("code", StatusCode._200.status);
	    map.put("msg", "提交成功，本次交易超过"+SystemConst.Order_TimeOut/60+"分钟订单将会取消");
	    order.setExt(json);
	    map.put("data", order);	
		return map;
	}	
	
	/**
	 * 流水账目
	 */
	@RequestMapping(value = "/journalAccount", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> journalAccount(HttpServletRequest request,PageUtil pu) {
		Map<String, Object> retval = new HashMap<>(2);
		Users user = SessionUtil.getSession(request);
		List<JournalAccount> list=journalAccountService.selectByUserId(user.getUserId(),pu);
		retval.put("code", StatusCode._200.status);
		retval.put("msg", StatusCode._200.msg);
		Map<String, Object> data = new HashMap<>(2);
		data.put("journalList", list);
		data.put("pageNumber", pu.getPageNumber());
		data.put("pageSize", pu.getPageSize());
		data.put("totalNumber", pu.getPageTotal());
		retval.put("data", data);
		return retval;
	}	
	/**
	 * 提币
	 */
	@RequestMapping(value = "/withdrawUSDT", method = RequestMethod.POST)
	@JSON(type = UserWallet.class, include = "walletAddress,status")
	@Transactional
	public Map<String, Object> withdrawUSDT(HttpServletRequest request,
			@RequestParam("toAddress") String toAddress,@RequestParam("amount") BigDecimal amount,@RequestParam("smsCode") String smsCode) {
		Map<String, Object> retval = new HashMap<>(2);
		Users user = SessionUtil.getSession(request);
		if (!StringUtil.isBlank(smsCode)
				&& RedisUtil.getValue(SystemConst.SMSKey + user.getPhoneNumber()).equals(smsCode)){
			RedisUtil.delValue(SystemConst.SMSKey + user.getPhoneNumber());
			if(amount.compareTo(BigDecimal.ZERO)<=0) {
				retval.put("code", StatusCode._309.getStatus());
				retval.put("msg", StatusCode._309.getMsg());
				return retval;
			}
			if(amount.compareTo(SystemConst.limitUSDTOut)>0) {
				retval.put("code", StatusCode._318.getStatus());
				retval.put("msg", StatusCode._318.getMsg());
				return retval;
			}			
	    	if(Idempotent.fx_withdrawUSDT(amount+smsCode+toAddress)) {
	    		retval.put("code", StatusCode._315.status);
	    		retval.put("msg", StatusCode._315.msg);
				return retval;
	    	}
	    	
	    	UserWallet uw=userWalletService.findByLock(user.getUserId(), SystemConst.USDT);
			if(uw.getCurrencyNumber().compareTo(amount)<0  || uw.getCurrencyNumber().compareTo(SystemConst.minNum)<0) {
				retval.put("code", StatusCode._206.getStatus());
				retval.put("msg", StatusCode._206.getMsg());
				return retval;
			}										
	    	String tradeNumber=OrderNumEngender.getOrderNum();    	
	    	Orders order=new Orders();	    	
	    	order.setUserId(user.getUserId());
	    	order.setTradeNumber(tradeNumber);
	    	order.setAmount(BigDecimal.ZERO);
	    	order.setCurrencyType(SystemConst.USDT);
	    	order.setCurrencyAmount(amount);
	    	order.setToAddress(toAddress);
	    	order.setType(OrderType._1.type);
	    	order.setStatus(OrdersStatus._6.status);
	    	order.setCreateTime(new Date());
	    	if(user.getType()==UsersType._1.status){
	    		order.setSubject("商户提币");
	    		order.setMerchantRate(uw.getOutRate());
	    	}else if(user.getType()==UsersType._2.status) {
	    		order.setSubject("币商提币");
	    		order.setUnderwriterRate(BigDecimal.ZERO);
	    	}	    	
	    	ordersService.insert(order);	 
	    	List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>(1);
	    	UserWallet nowUw=new UserWallet();
			nowUw.setWalletId(uw.getWalletId());
			nowUw.setCurrencyNumber(uw.getCurrencyNumber().subtract(order.getCurrencyAmount()));
			nowUw.setFreezeNumber(uw.getFreezeNumber().add(order.getCurrencyAmount()));
			userWalletService.updateByPrimaryKeySelective(nowUw);					
			journalAccountList.add(journalAccountService.logAccountChange(uw, nowUw, "商户提币冻结："+tradeNumber, order.getCurrencyAmount(),JournalType.transfer.status,CurrencyType.USDT.status,order.getTradeNumber()));
			journalAccountService.insertBatch(journalAccountList);    	
			retval.put("code", StatusCode._402.getStatus());
			retval.put("msg", "订单已经提交，请联系或等待客户审核！");
			return retval;			
		}else {
			retval.put("code", StatusCode._309.getStatus());
			retval.put("msg", StatusCode._309.getMsg() + "或是验证码已过期");			
		}
		return retval;
	}
	
	/**
	 * 
	 * 我的钱包地址
	 */
	@RequestMapping(value = "/walletAddress", method = RequestMethod.POST)
	@JSON(type = UserWallet.class, include = "walletAddress,status")
	public Map<String, Object> walletAddress(HttpServletRequest request) {
		Map<String, Object> retval = new HashMap<>(2);
		Users user = SessionUtil.getSession(request);

		UserWallet uw = userWalletService.selectByType(user.getUserId(), SystemConst.USDT);
		if (uw != null) {
			retval.put("data", uw);
			retval.put("code", StatusCode._200.getStatus());
			retval.put("msg", StatusCode._200.getMsg());
		} else {
			retval.put("code", StatusCode._310.getStatus());
			retval.put("msg", StatusCode._310.getMsg());
		}

		return retval;
	}

	/**
	 * 获取短信
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sendValidSMS", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> sendValidSMS(HttpServletRequest request,
			@RequestParam("phoneNumber") String phoneNumber) {
		Map<String, Object> retval = new HashMap<>(2);

		if (!StringUtil.isBlank(phoneNumber) && SMSUtil.isChinaPhoneLegal(phoneNumber)) {
			SMSUtil.sendValidateCode(phoneNumber);
			retval.put("code", StatusCode._200.getStatus());
			retval.put("msg", StatusCode._200.getMsg());
		} else {
			retval.put("code", StatusCode._205.getStatus());
			retval.put("msg", StatusCode._205.getMsg());
		}

		return retval;
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modifyPwd(HttpServletRequest request, @RequestParam("newPwd") String newPwd,
			@RequestParam("smsCode") String smsCode) {
		Map<String, Object> retval = new HashMap<>(3);
		Users user = SessionUtil.getSession(request);
		if (!StringUtil.isBlank(smsCode)
				&& RedisUtil.getValue(SystemConst.SMSKey + user.getPhoneNumber()).equals(smsCode)) {
			RedisUtil.delValue(SystemConst.SMSKey + user.getPhoneNumber());
			String secretPassword = Md5Util.execute(user.getUserId() + newPwd);
			Users newUser = new Users();
			newUser.setUserId(user.getUserId());
			newUser.setPassword(secretPassword);
			usersService.updateByPrimaryKeySelective(newUser);
			retval.put("code", StatusCode._200.getStatus());
			retval.put("msg", StatusCode._200.getMsg());
		} else {
			retval.put("code", StatusCode._309.getStatus());
			retval.put("msg", StatusCode._309.getMsg() + "或是验证码已过期");
		}
		return retval;
	}

	/**
	 * 个人中心
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/userCenter", method = RequestMethod.POST)
	@JSON(type = Users.class, filter = "userMerchant,userWallet,pageNumber,pageSize,pageTotal,queryStr,password")
	public Map<String, Object> userCenter(HttpServletRequest request) {
		Map<String, Object> retval = new HashMap<>(3);
		Users user = SessionUtil.getSession(request);
		Users userNew = usersService.selectUserInfo(user.getUserId());
		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());
		retval.put("data", userNew);
		return retval;
	}

	/**
	 * 删除支付方式
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delPayment", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delPayment(HttpServletRequest request, @RequestParam("paymentId") Long paymentId) {
		Map<String, Object> retval = new HashMap<>(3);
		Users user = SessionUtil.getSession(request);
		PaymentMode pm = paymentModeService.selectByPrimaryKey(paymentId);
		if (pm==null || !pm.getUserId().equals(user.getUserId())) {
			retval.put("code", StatusCode._309.getStatus());
			retval.put("msg", StatusCode._309.getMsg());
			return retval;
		}
		if (!StringUtil.isBlank(pm.getPaymentImage())) {
			File file = new File(request.getSession().getServletContext().getRealPath("/"), pm.getPaymentImage());
			if (file.exists() && file.isFile()) {
				file.delete();
			}
		}
		paymentModeService.deleteByPrimaryKey(paymentId);

		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());

		return retval;
	}

	
	/**
	 * 收款方式启用，停用
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/setPaymentValid", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> setPaymentValid(HttpServletRequest request,
			@RequestParam("paymentId") Long paymentId,@RequestParam("status")Byte status) {
		Map<String, Object> retval = new HashMap<>(3);
		Users user = SessionUtil.getSession(request);
		PaymentMode pm = paymentModeService.selectByPrimaryKey(paymentId);
		if (pm == null || !pm.getUserId().equals(user.getUserId())) {
			retval.put("code", StatusCode._309.getStatus());
			retval.put("msg", StatusCode._309.getMsg());
			return retval;
		}
		if(status==PaymentValid.start.status || status==PaymentValid.stop.status) {
			PaymentMode newPm=new PaymentMode();
			newPm.setPaymentId(pm.getPaymentId());
			newPm.setStatus(status);
			paymentModeService.updateByPrimaryKeySelective(newPm);				
		}					
		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());
		return retval;
	}
	
	/**
	 * 设为收款号
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/setPaymentDefault", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> setPaymentDefault(HttpServletRequest request,
			@RequestParam("paymentId") Long paymentId) {
		Map<String, Object> retval = new HashMap<>(3);
		Users user = SessionUtil.getSession(request);
		PaymentMode pm = paymentModeService.selectByPrimaryKey(paymentId);
		if (pm == null || !pm.getUserId().equals(user.getUserId())) {
			retval.put("code", StatusCode._309.getStatus());
			retval.put("msg", StatusCode._309.getMsg());
			return retval;
		}
		paymentModeService.setPaymentDefault(paymentId, user.getUserId());

		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());

		return retval;
	}

	/**
	 * 修改支付二维码
	 * 
	 * @param request
	 * @param pm
	 * @return
	 */

	@RequestMapping(value = "/modifyQR", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modifyQR(HttpServletRequest request, PaymentMode pm) {
		Map<String, Object> retval = new HashMap<>(3);
		Users user = SessionUtil.getSession(request);
		if (user.getType() == UsersType._2.status) {
			PaymentMode pmOld = paymentModeService.selectByPrimaryKey(pm.getPaymentId());
			if (pmOld == null || !user.getUserId().equals(pmOld.getUserId()) || pmOld.getPaymentType() == null
					|| StringUtil.isBlank(pmOld.getPaymentNumber()) || StringUtil.isBlank(pmOld.getPaymentImage())
					|| StringUtil.isBlank(pmOld.getUserName()) || pmOld.getPaymentType() > 3
					|| pmOld.getPaymentType() < 0) {
				retval.put("code", StatusCode._309.getStatus());
				retval.put("msg", StatusCode._309.getMsg());
				return retval;
			}

			if (!pm.getPaymentQR().isEmpty() && pm.getPaymentQR().getSize() > 3) {
				String path = UploadUtil.uploadPaymentFile(request.getSession(), pm, pmOld.getPaymentImage());
				pm.setPaymentImage(path);
			}
		}

		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());

		return retval;
	}

	/**
	 * 添加收款方式
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addPayment", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public Map<String, Object> addPayment(HttpServletRequest request, PaymentMode pm) {
		Map<String, Object> retval = new HashMap<>(3);
		Users user = SessionUtil.getSession(request);
		if (user.getType() == UsersType._2.status) {
			if (pm.getPaymentType() == null || StringUtil.isBlank(pm.getPaymentNumber())
					|| StringUtil.isBlank(pm.getUserName()) || pm.getPaymentType() > 3 || pm.getPaymentType() < 0) {
				retval.put("code", StatusCode._309.getStatus());
				retval.put("msg", StatusCode._309.getMsg());
				return retval;
			}
			pm.setUserId(user.getUserId());
			if (pm.getPaymentQR() != null && !pm.getPaymentQR().isEmpty() && pm.getPaymentQR().getSize() > 3) {
				String path = UploadUtil.uploadPaymentFile(request.getSession(), pm);
				pm.setPaymentImage(path);
			}
			pm.setUserId(user.getUserId());
			pm.setCreateTime(new Date());
			pm.setStatus((byte) 1);
			paymentModeService.insert(pm);
			PaymentRule pr=paymentRuleService.selectByPrimaryKey(pm.getPaymentType());
			if(pr!=null) {
				PaymentRotate prt=new PaymentRotate();
				prt.setPaymentId(pm.getPaymentId());
				prt.setIntervalMax(pr.getRotateIntervalMax());
				prt.setIntervalMin(pr.getRotateIntervalMin());
				prt.setLimitVolume(pr.getLimitVolume());
				prt.setLimitNumber(pr.getLimitNumber());
				prt.setStatus(PaymentRotateStatus.valid.status);					
				paymentRotateService.insertSelective(prt);
			}			
		}
		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());

		return retval;
	}

	/**
	 * 我的收款方式
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/paylist", method = RequestMethod.POST)
	@JSON(type = PaymentMode.class, filter = "paymentType,paymentQR")
	public Map<String, Object> paylist(HttpServletRequest request, PaymentMode pm) {
		Map<String, Object> retval = new HashMap<>(3);
		Users user = SessionUtil.getSession(request);
		if (user.getType() == UsersType._2.status) {
			List<PaymentMode> list = paymentModeService.selectPayementList(user.getUserId());
			list.forEach(pay -> {
				pay.setPaymentTypeName(Payment.getMeaningByType(pay.getPaymentType()));
				if (!StringUtil.isBlank(pay.getPaymentImage())) {
					pay.setPaymentImage(request.getContextPath() + pay.getPaymentImage());
				}
			});
			retval.put("data", list);
		}
		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());

		return retval;
	}

	/**
	 * 设置钱包地址
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/setWalletAddress", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> setWalletAddress(HttpServletRequest request, @RequestParam("address") String address) {
		Map<String, Object> retval = new HashMap<>(3);
		Users user = SessionUtil.getSession(request);
		int a=443;
		if (user.getType() != UsersType._1.status || a==SystemConst.sslPort) {
			retval.put("code", StatusCode._310.getStatus());
			retval.put("msg", StatusCode._310.getMsg());
			return retval;
		}
		UserWallet uw = userWalletService.selectByType(user.getUserId(), SystemConst.USDT);

		UserWallet newAddress = new UserWallet();
		newAddress.setWalletId(uw.getWalletId());
		newAddress.setWalletAddress(address);
		userWalletService.updateByPrimaryKeySelective(newAddress);

		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());

		return retval;
	}

	
	/**
	 * 根据条件查询订单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryOrder", method = RequestMethod.POST)
	@JSON(type = Orders.class, filter = "notifyUrl,notifyStatus,timestamp,payTimeout,sign,queryStr,pageSize,pageTotal,pageNumber,paymentType,types")
	public Map<String, Object> queryOrder(HttpServletRequest request,OrderQueryPara oqp) {
		Map<String, Object> retval = new HashMap<>(3);
		Map<String, Object> data = new HashMap<>(1);
		Users user = SessionUtil.getSession(request);
		data.put("Orders", findUserOrders(oqp, user));
		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());
		retval.put("data", data);
		return retval;
	}
	
	/**
	 * 用户首页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	@JSON(type = UserWallet.class, include = "currencyNumber,freezeNumber")
	@JSON(type = Orders.class, filter = "notifyUrl,notifyStatus,timestamp,payTimeout,sign,queryStr,pageSize,pageTotal,pageNumber,paymentType,types")
	public Map<String, Object> index(HttpServletRequest request,OrderQueryPara oqp) {
		Map<String, Object> retval = new HashMap<>(3);
		Map<String, Object> data = new HashMap<>(2);
		Users user = SessionUtil.getSession(request);
		UserWallet uw = userWalletService.selectByType(user.getUserId(), SystemConst.USDT);
		data.put("UserWallet", uw);
		data.put("Orders", findUserOrders(oqp, user));
		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());
		retval.put("data", data);
		return retval;
	}

	public Map<String, Object> findUserOrders(OrderQueryPara oqp, Users user) {
		Map<String, Object> data = new HashMap<>(4);
		if(user.getStatus()==UsersStatus._1.status || user.getStatus()==UsersStatus.shut.status) {		
			oqp.setUserType(user.getType());
			oqp.setUserId(user.getUserId());
			List<Orders> list = ordersService.selectOrderByPage(oqp);
			list.forEach(order->{			
				if(!StringUtil.isBlank(order.getExt())) {
				    try {
					    JSONObject json=JSONObject.fromObject(order.getExt());
					    order.setExt(json);
				    }catch(Exception e) {
				    	LoggerUtil.error(e.getMessage(), e);
				    }
				}			
			});
			data.put("OrdersList", list);
			data.put("SumOrders", ordersService.selectSumOrderByPara(oqp));
			data.put("pageNumber", oqp.getPageNumber());
			data.put("pageSize", oqp.getPageSize());
			data.put("totalNumber", oqp.getPageTotal());
		}else {
			data.put("OrdersList", null);
			data.put("SumOrders", null);
			data.put("pageNumber", oqp.getPageNumber());
			data.put("pageSize", oqp.getPageSize());
			data.put("totalNumber", oqp.getPageTotal());
		}
		return data;
	}	

	/**
	 * 全部订单
	 * 
	 * @param request
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/userOrders", method = RequestMethod.POST)
	@JSON(type = Orders.class, filter = "notifyUrl,notifyStatus,timestamp,payTimeout,sign,queryStr,pageSize,pageTotal,pageNumber,paymentType,types")
	public Map<String, Object> userOrders(HttpServletRequest request, OrderQueryPara oqp) {
		Map<String, Object> retval = new HashMap<>(3);
		Map<String, Object> data = new HashMap<>(2);
		Users user = SessionUtil.getSession(request);
		data.put("Orders", findUserOrders(oqp, user));
		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());
		retval.put("data", data);
		return retval;
	}

	/**
	 * 按状态查询
	 * 
	 * @param request
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/userOrdersByStatus", method = RequestMethod.POST)
	@JSON(type = Orders.class, filter = "notifyUrl,notifyStatus,timestamp,payTimeout,sign,queryStr,pageSize,pageTotal,pageNumber,paymentType,types")
	public Map<String, Object> userOrdersByStatus(HttpServletRequest request, OrderQueryPara oqp) {
		Map<String, Object> retval = new HashMap<>(3);
		Map<String, Object> data = new HashMap<>(2);
		Users user = SessionUtil.getSession(request);
		data.put("Orders", findUserOrders(oqp, user));
		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());
		retval.put("data", data);
		return retval;
	}

	/**
	 * 退出
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loginOut", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loginOut(HttpServletRequest request) {
		Map<String, Object> retval = new HashMap<>();
		Users user = SessionUtil.getSession(request);
		SessionContext.invalidSession(user.getPhoneNumber());		
		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());		
		return retval;
	}
		
	/**
	 * 打烊/正常营业
	 * 
	 * @param status
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/closedOpen", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> closedOpen(HttpServletRequest request,@RequestParam("status") Byte status,@RequestParam("password") String password) {
		Map<String, Object> retval = new HashMap<>();
		Users user = SessionUtil.getSession(request);			
		String secretPassword = Md5Util.execute(user.getUserId() + password);
		if (user.getType()==UsersType._2.status && 
				!StringUtil.isBlank(password) && secretPassword.equals(user.getPassword())){
			if(status==UsersStatus._1.status || status==UsersStatus.shut.status) {
				Users users = new Users();
				users.setUserId(user.getUserId());
				users.setStatus(status);
				usersService.updateByPrimaryKeySelective(users);
			}		
			retval.put("code", StatusCode._200.getStatus());
			if(status==UsersStatus._1.status) {	
				retval.put("msg", "欢迎回来!");
			}else if(status==UsersStatus.shut.status) {
				retval.put("msg", "好好休息一下，祝你玩的愉快！");
			}
		}else {
			retval.put("code", StatusCode._204.getStatus());
			retval.put("msg", StatusCode._204.getMsg() );
		}
		return retval;
	}
	
	/**
	 * 登入
	 * 
	 * @param phoneNumber
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@JSON(type = Users.class, filter = "userMerchant,userWallet,pageNumber,pageSize,pageTotal,queryStr,password,status")
	public Map<String, Object> login(@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> retval = new HashMap<>();

		if (StringUtils.isEmpty(phoneNumber)) {
			retval.put("code", StatusCode._203.getStatus());
			retval.put("msg", StatusCode._203.getMsg());
		}

		if (StringUtils.isEmpty(password)) {
			retval.put("code", StatusCode._204.getStatus());
			retval.put("msg", StatusCode._204.getMsg());
		}

		Users users = usersService.selectByPhoneNumber(phoneNumber);
		if (users != null) {
			String secretPassword = Md5Util.execute(users.getUserId() + password);
			if (secretPassword.equals(users.getPassword())) {
				retval.put("code", StatusCode._200.getStatus());
				retval.put("msg", StatusCode._200.getMsg());
				SessionUtil.setSession(request, users);
				retval.put("data", users);
			} else {
				retval.put("code", StatusCode._204.getStatus());
				retval.put("msg", StatusCode._204.getMsg());
			}
		} else {
			retval.put("code", StatusCode._203.getStatus());
			retval.put("msg", StatusCode._203.getMsg());
		}

		return retval;
	}

	/**
	 * 商户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/merchantInfo", method = RequestMethod.POST)
	@JSON(type = UserMerchant.class, include = "userId,privateKey,publicKey")
	public Map<String, Object> merchantInfo(HttpServletRequest request) {
		Map<String, Object> retval = new HashMap<>();
		Users user = SessionUtil.getSession(request);
		if(user.getType()==UsersType._1.status) {
			long userId = user.getUserId();
			UserMerchant um = userMerchantService.selectByPrimaryKey(userId);
			if (um != null) {
				retval.put("code", StatusCode._200.getStatus());
				retval.put("msg", StatusCode._200.getMsg());
				retval.put("data", um);
			} else {
				retval.put("code", StatusCode._203.getStatus());
				retval.put("msg", StatusCode._203.getMsg());
			}
		}else {
			retval.put("code", StatusCode._310.getStatus());
			retval.put("msg", StatusCode._310.getMsg());
		}
		return retval;
	}

	/**
	 * 注册
	 * 
	 * @param phoneNumber
	 * @param password
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public Map<String, Object> register(@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("password") String password, @RequestParam("type") Byte type,String userName,
			@RequestParam("smsCode") String smsCode, String fatherPhoneNumber) {
		Map<String, Object> retval = new HashMap<>();
		if (StringUtils.isEmpty(phoneNumber)) {
			retval.put("code", StatusCode._203.getStatus());
			retval.put("msg", StatusCode._203.getMsg());
		}
		if (StringUtils.isEmpty(password)) {
			retval.put("code", StatusCode._204.getStatus());
			retval.put("msg", StatusCode._204.getMsg());
		}
		Users users = usersService.selectExistPhoneNumber(phoneNumber);
		if (users == null) {
			if (!StringUtil.isBlank(smsCode) && RedisUtil.getValue(SystemConst.SMSKey + phoneNumber).equals(smsCode)) {
				RedisUtil.delValue(SystemConst.SMSKey + phoneNumber);
		    	if(Idempotent.fx_register(phoneNumber+smsCode)) {
		    		retval.put("code", StatusCode._315.status);
		    		retval.put("msg", StatusCode._315.msg);
					return retval;
		    	}
		    	Long userFid = null;
		    	if( fatherPhoneNumber != null ) {
		    		Users fUsers = usersService.selectByPhoneNumber(fatherPhoneNumber);
		    		if( fUsers == null ) {
		    			retval.put("code", StatusCode._322.status);
			    		retval.put("msg", StatusCode._322.msg);
						return retval;
		    		}
		    		userFid = fUsers.getUserId();
		    		// 用户升级策略
		    		usersService.userLevelUpgrade(userFid, UsersConst.UsersLevel.member.level, UsersConst.UsersLevel.manager.level);
		    	}
				Long userId = UserIdEngender.createUserId();
				String secretPassword = Md5Util.execute(userId + password);
				Users newUser = new Users();
				newUser.setUserId(userId);
				newUser.setUserFid(userFid);
				newUser.setPassword(secretPassword);
				newUser.setType(UsersType._0.status);
				newUser.setPhoneNumber(phoneNumber);
				newUser.setLevel(UsersLevel.member.level);
				newUser.setCentre(UsersCentre.company.centre);
				if(!StringUtil.isBlank(userName)) {
					if(userName.length()>25) {
						userName=userName.substring(0, 25);
					}
					newUser.setUserName(userName);
				}
				newUser.setRole(UsersRole.test.role);
				newUser.setCreateTime(new Date());
				switch(type) {
				case 1:
					newUser.setStatus(UsersConst.UsersStatus._3.status);
					break;
				case 2:
					newUser.setStatus(UsersConst.UsersStatus._2.status);
					break;
				}
				usersService.insertSelective(newUser);
				if (type == UsersType._1.status) {
					UserMerchant um = new UserMerchant();
					Map<String, String> key = RSAUtils.createKeys(2048);
					um.setUserId(userId);
					um.setPrivateKey(key.get(RSAUtils.key_private));
					um.setPublicKey(key.get(RSAUtils.key_public));
					um.setStatus((byte) 1);
					um.setCreateTime(new Date());
					userMerchantService.insertSelective(um);
				}

				UserWallet uw = new UserWallet();
				uw.setUserId(userId);
				uw.setCurrencyType(SystemConst.USDT);
				uw.setStatus((byte) 1);
				String address = new USDTUtils().getNewAddress();
				String privkey = new USDTUtils().dumpprivkey(address);
				uw.setWalletAddress(address);
				uw.setReserve(privkey);
				userWalletService.insertSelective(uw);

				retval.put("code", StatusCode._200.getStatus());
				retval.put("msg", StatusCode._200.getMsg());
			} else {
				retval.put("code", StatusCode._309.getStatus());
				retval.put("msg", StatusCode._309.getMsg() + "或是验证码已过期");
			}
		} else {
			retval.put("code", StatusCode._202.getStatus());
			retval.put("msg", StatusCode._202.getMsg());
		}

		return retval;
	}
	
	/**
	 * h5登入界面
	 * 
	 * @param phoneNumber
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mobile/welcome", method = RequestMethod.GET)

	public String welcome() {
		
		return "mobileLogin";
	}	
	
	/**
	 * h5首页
	 * 
	 * @param phoneNumber
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mobile/index", method = RequestMethod.GET)
	public String index() {
		
		return "mobileIndex";
	}	
	
	/**
	 *银行简介
	 * 
	 * @return
	 */
	@RequestMapping(value = "/bankMarkList", method = RequestMethod.POST)
	@JSON(type=BankMark.class,include="bankName,bankMark")
	public Map<String, Object> bankMarkList(){		
		Map<String, Object> retval = new HashMap<>();			
		List<BankMark> list=bankMarkService.selectBankMarkList();
		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());
		retval.put("data", list);				
		return retval;
	}	
	
	
/*	@RequestMapping(value = "/test", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public Map<String, Object> test(@RequestParam("userId") Long userId){
		Map<String, Object> retval = new HashMap<>();			
		
//		usersService.userLevelUpgrade(userFid, UsersConst.UsersLevel.member.level, UsersConst.UsersLevel.manager.level);
		Users users = usersService.selectUserWithWallet(userId);
		usersService.userIncomeMoney(users, "asdasdasdasdasd", new BigDecimal(1000), new Date());
		
		retval.put("code", StatusCode._200.getStatus());
		retval.put("msg", StatusCode._200.getMsg());
		return retval;
	}*/
		
}
