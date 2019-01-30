package com.contactsImprove.controller.api;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contactsImprove.constant.JournalConst.CurrencyType;
import com.contactsImprove.constant.JournalConst.JournalType;
import com.contactsImprove.constant.MerchantOrderConst.ComplainOrderStatus;
import com.contactsImprove.constant.MerchantOrderConst.NotifyStatus;
import com.contactsImprove.constant.MerchantOrderConst.OrderType;
import com.contactsImprove.constant.MerchantOrderConst.OrdersStatus;
import com.contactsImprove.constant.MerchantOrderConst.Payment;
import com.contactsImprove.constant.MerchantOrderConst.TradeStatus;
import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.constant.UsersConst.UsersRole;
import com.contactsImprove.constant.UsersConst.UsersType;
import com.contactsImprove.entity.api.ComplainOrder;
import com.contactsImprove.entity.api.Currency;
import com.contactsImprove.entity.api.JournalAccount;
import com.contactsImprove.entity.api.MerchantNotify;
import com.contactsImprove.entity.api.OrderQueryPara;
import com.contactsImprove.entity.api.Orders;
import com.contactsImprove.entity.api.PaymentMode;
import com.contactsImprove.entity.api.UserMerchant;
import com.contactsImprove.entity.api.UserWallet;
import com.contactsImprove.entity.api.Users;
import com.contactsImprove.entity.api.ValidUserPayment;
import com.contactsImprove.ferriswheel.Wheel;
import com.contactsImprove.json.JSON;
import com.contactsImprove.service.api.ComplainOrderService;
import com.contactsImprove.service.api.CurrencyService;
import com.contactsImprove.service.api.JournalAccountService;
import com.contactsImprove.service.api.OrdersService;
import com.contactsImprove.service.api.PaymentModeService;
import com.contactsImprove.service.api.PaymentRotateService;
import com.contactsImprove.service.api.UserMerchantService;
import com.contactsImprove.service.api.UserWalletService;
import com.contactsImprove.service.api.UsersService;
import com.contactsImprove.utils.DateTools;
import com.contactsImprove.utils.Idempotent;
import com.contactsImprove.utils.LoggerUtil;
import com.contactsImprove.utils.Md5Util;
import com.contactsImprove.utils.NotifyCommercial;
import com.contactsImprove.utils.OrderNumEngender;
import com.contactsImprove.utils.OrderUtil;
import com.contactsImprove.utils.RSAUtils;
import com.contactsImprove.utils.SMSUtil;
import com.contactsImprove.utils.SessionUtil;
import com.contactsImprove.utils.StringUtil;
import com.contactsImprove.utils.Tools;
import com.contactsImprove.utils.USDTUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/api")
public class InitiateRecharge {
	
	@Autowired
	UserMerchantService userMerchantService;	
	@Autowired
	OrdersService ordersService;	
	@Autowired
	PaymentModeService paymentModeService;
	@Autowired
	UserWalletService userWalletService;
	@Autowired
	NotifyCommercial notifyCommercial;
	@Autowired
	CurrencyService currencyService;
	@Autowired
	UsersService usersService;
	@Autowired
	JournalAccountService journalAccountService;
	@Autowired
	USDTUtils uSDTUtils;
	@Autowired
	ComplainOrderService complainOrderService;
	@Autowired
	PaymentRotateService paymentRotateService;
	
	/**	  商户余额查询。
	 * merchantUserId:商户号
	 * timestamp：时间戳
	 * version:版本号
	 * @param Orders
	 * @return
	 */	
	@RequestMapping(value = "/queryUserBalance",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryUserBalance(Orders order){
		Map<String, Object> map = new HashMap<>();		
		TreeMap<String, String> paramap = new TreeMap<String, String>();
		if(StringUtil.isBlank(order.getMerchantUserId()) 
		|| StringUtil.isBlank(order.getVersion()) || !SystemConst.version.equals(order.getVersion())
		|| StringUtil.isBlank(order.getTimestamp())	
		) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			return map;
		}		
		UserMerchant umt=userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
		if(umt==null || umt.getStatus()==0) {
			map.put("code", StatusCode._301.status);
			map.put("msg", StatusCode._301.msg);
			return map;
		}
		paramap.put("merchantUserId", order.getMerchantUserId()+"");
		paramap.put("version", order.getVersion());
		paramap.put("timestamp", order.getTimestamp());
		String line=OrderUtil.createParaOrderStr(paramap);
	    boolean isPass=RSAUtils.verifySignature(line, order.getSign(), umt.getPublicKey());
	    if(!isPass) {
			map.put("code", StatusCode._304.status);
			map.put("msg", StatusCode._304.msg);
			return map;
	    }
	    HashMap<String,BigDecimal> hm=userWalletService.selectRemainingAmount(order.getMerchantUserId());		
	    map.put("code", StatusCode._200.status);
	    map.put("msg", StatusCode._200.msg);
	    map.put("data", hm);
	    return map;
	}
		
	/**	  订单状态查询。
	 * tradeNumber:交易流水号
	 * outTradeNo:商户订单号
	 * merchantUserId:商户号
	 * timestamp：时间戳
	 * version:版本号
	 * @param Orders
	 * @return
	 */	
	@RequestMapping(value = "/queryOrderById",method=RequestMethod.POST)
	@JSON(type = Orders.class, include = "currencyAmount,tradeNumber,outTradeNo,merchantAccount,paymentType,amount,paymentNumber,status,notifyStatus,type,createTime,paymentTime,closeTime,dealType")
	public Map<String, Object> queryOrderById(Orders order){
		Map<String, Object> map = new HashMap<>();		
		TreeMap<String, String> paramap = new TreeMap<String, String>();
		if(StringUtil.isBlank(order.getMerchantUserId()) 
		|| StringUtil.isBlank(order.getOutTradeNo())
		|| StringUtil.isBlank(order.getVersion()) || !SystemConst.version.equals(order.getVersion())
		|| StringUtil.isBlank(order.getTimestamp())	
		) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			return map;
		}		
		UserMerchant umt=userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
		if(umt==null || umt.getStatus()==0) {
			map.put("code", StatusCode._301.status);
			map.put("msg", StatusCode._301.msg);
			return map;
		}
		paramap.put("merchantUserId", order.getMerchantUserId()+"");
		paramap.put("outTradeNo", order.getOutTradeNo());		
		paramap.put("version", order.getVersion());
		paramap.put("timestamp", order.getTimestamp());		
		paramap.put("tradeNumber", order.getTradeNumber());	
		String line=OrderUtil.createParaOrderStr(paramap);
	    boolean isPass=RSAUtils.verifySignature(line, order.getSign(), umt.getPublicKey());
	    if(!isPass) {
			map.put("code", StatusCode._304.status);
			map.put("msg", StatusCode._304.msg);
			return map;
	    }
	    if(StringUtil.isBlank(order.getTradeNumber())) {
	    	List<Orders> orders=ordersService.selectOrderByMerchantUserId(order.getMerchantUserId(), order.getOutTradeNo());
	    	if(orders!=null && orders.size()>0) {
		    	orders.forEach( tradeOrder ->{
		    	    if(!StringUtil.isBlank(tradeOrder.getPaymentTypeName())) {	    	
		    	    	tradeOrder.setPaymentType(Payment.getCodeByMeaning(tradeOrder.getPaymentTypeName()));
		    	    }	
		    	});
	    	}
	    	map.put("data", orders);	    	    	
	    }else {
	    	 Orders oldOrder=ordersService.selectByPrimaryKey(order.getTradeNumber());	
	    	 if(oldOrder==null) {
    			map.put("code", StatusCode._314.status);
    			map.put("msg", StatusCode._314.msg);
    			return map;
	    	 }
    	    if(!oldOrder.getMerchantUserId().equals(order.getMerchantUserId())) {
    			map.put("code", StatusCode._310.status);
    			map.put("msg", StatusCode._310.msg);
    			return map;
    	    }
    	    if(!StringUtil.isBlank(oldOrder.getPaymentTypeName())) {	    	
    	    	oldOrder.setPaymentType(Payment.getCodeByMeaning(oldOrder.getPaymentTypeName()));
    	    }	
	    	 map.put("data", oldOrder);
	    }
	
	    map.put("code", StatusCode._200.status);
	    map.put("msg", StatusCode._200.msg);	   
	    return map;
	}
	/**
	 * 商户客户出金
	 * outTradeNo:商户订单号
	 * merchantUserId:商户号
	 * amount:金额
	 * currencyType:币种类型
	 * timestamp:时间戳
	 * paymentType:收款方式
	 * paymentNumber:收款卡号,或是淘宝，微信号。
	 * ext:{"payImage":"外网可以访问的支付二维码图片路径","bankName":"开户行","branchNumber":"支行代号","ownerName":持有者名字}
	 * subject:主题
	 * version:版本号
	 * 可选
	 * merchantAccount：商户的客户号
	 * notifyUrl:回调地址
	 * body:交易描述
	 * @return
	 */
	@RequestMapping(value = "/withdrawal",method=RequestMethod.POST)
	@Transactional
	@JSON(type = Orders.class,include = "tradeNumber,outTradeNo,merchantUserId,merchantAccount,paymentType,currencyType,currencyAmount,exchangeRate,amount,payTimeout,paymentNumber,ext")
	public Map<String, Object> withdrawal (Orders order){
		Map<String, Object> map = new HashMap<>();	
		byte paymentType=Payment.getTypeByCode(order.getPaymentType());
		TreeMap<String, String> paramap = new TreeMap<String, String>();		
		if(StringUtil.isBlank(order.getMerchantUserId()) || StringUtil.isBlank(order.getOutTradeNo())
		|| (StringUtil.isBlank(order.getAmount()) && order.getAmount().compareTo(BigDecimal.ZERO)<=0) 
		|| StringUtil.isBlank(order.getVersion()) || !SystemConst.version.equals(order.getVersion())
		|| StringUtil.isBlank(order.getSubject()) || StringUtil.isBlank(order.getTimestamp())
		|| paymentType==0 || StringUtil.isBlank(order.getPaymentNumber())
		|| StringUtil.isBlank(order.getCurrencyType())
		){
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			return map;
		}
		JSONObject json=null;
		try {
			json=JSONObject.fromObject(order.getExt());
			if(json==null ) {
				map.put("code", StatusCode._303.status);
				map.put("msg", StatusCode._303.msg);
				return map;
			}else {
				if(!json.has("ownerName")) {
					map.put("code", StatusCode._303.status);
					map.put("msg", StatusCode._303.msg);
					return map;
				}
				String imgUrl=json.getString("payImage");
				if(!StringUtil.isBlank(imgUrl) && !imgUrl.toLowerCase().startsWith("http")) {
					map.put("code", StatusCode._303.status);
					map.put("msg", "图片二维码外网必须可以访问");
					return map;
				}
				if(paymentType==Payment.union_pay.type) {
					String bankName=json.getString("bankName");
					if(StringUtil.isBlank(bankName)){
						map.put("code", StatusCode._303.status);
						map.put("msg", "缺少开户行名称");
						return map;
					}
				}
			}
		}catch(Exception e) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
	    	LoggerUtil.error(e.getMessage(), e);
			return map;
	    }
						
		UserMerchant umt=userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
		if(umt==null || umt.getStatus()==0) {
			map.put("code", StatusCode._301.status);
			map.put("msg", StatusCode._301.msg);
			return map;
		}		
		paramap.put("merchantUserId", order.getMerchantUserId()+"");
		paramap.put("outTradeNo", order.getOutTradeNo());		
		paramap.put("amount", order.getAmount()+"");		
		paramap.put("version", order.getVersion());
		paramap.put("subject", order.getSubject());
		paramap.put("body", order.getBody());
		paramap.put("currencyType", order.getCurrencyType());
		paramap.put("paymentType", order.getPaymentType());
		paramap.put("paymentNumber", order.getPaymentNumber());
		paramap.put("ext", order.getExt()+"");
		paramap.put("timestamp", order.getTimestamp());	
		paramap.put("notifyUrl", order.getNotifyUrl());
		paramap.put("merchantAccount", order.getMerchantAccount());	
		order.setActualAmount(order.getAmount());
		String line=OrderUtil.createParaOrderStr(paramap);
	    boolean isPass=RSAUtils.verifySignature(line, order.getSign(), umt.getPublicKey());
	    if(!isPass) {
			map.put("code", StatusCode._304.status);
			map.put("msg", StatusCode._304.msg);
			return map;
	    }
	    // 获取汇率
	    Currency c=currencyService.selectByPrimaryKey(order.getCurrencyType());
	    if(c==null) {
			map.put("code", StatusCode._201.status);
			map.put("msg", StatusCode._201.msg);
			return map;	    	
	    }
    	// 判断币商额度，匹配币商接单  
	    int hour=(int)Math.pow(2, java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY));
    	List<Users> list=usersService.selectWaitByUsdt(order.getAmount(), SystemConst.random_limit, paymentType,hour);    	
	    if(list==null ||list.size()<1) {
			map.put("code", StatusCode._316.status);
			map.put("msg", "暂时无法匹配承兑商，请稍后重试");
			return map;
	    }		       
	    long userId=list.get(ThreadLocalRandom.current().nextInt(list.size())).getUserId();	
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
	    // 幂等
    	if(Idempotent.fx_withdrawal(line)) {
			map.put("code", StatusCode._315.status);
			map.put("msg", StatusCode._315.msg);
			return map;
    	}
    	List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>(2);
    	List<UserWallet> userWalletList=new ArrayList<UserWallet>(1);
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
	    userWalletList.add(userWallet); 	    
	    journalAccountList.add(journalAccountService.logAccountChange(uw, userWallet, "出金卖出币冻结："+order.getTradeNumber(), order.getCurrencyAmount(),JournalType.sall.status,CurrencyType.USDT.status,order.getTradeNumber()));
	    
	    userWalletService.updateBatch(userWalletList);
	    ordersService.insert(order);	
	    journalAccountService.insertBatch(journalAccountList);
/*	    // 加入大转盘
	    Wheel.pushSlot(tradeNumber);*/	    
	    SMSUtil.sentBuyRemind(user.getPhoneNumber(), tradeNumber,order.getPaymentTypeName(),order.getAmount()+""," "+SystemConst.CNY,DateTools.DateToStr2(new Date()));	    
	    map.put("code", StatusCode._200.status);
	    map.put("msg", "本次交易超时时间"+SystemConst.Order_TimeOut/60+"分钟");	  		    
		order.setExt(json);
	    map.put("data", order);	
	    return map;
	}
		
	/**
	 * 商户客户发起申诉
	 * tradeNumber:交易流水号
	 * outTradeNo:商户订单号
	 * merchantUserId:商户号
	 * timestamp:时间戳
	 * body:申诉原因
	 * version:版本号
	 * sign:签名
	 * 可选
	 * merchantAccount：商户的客户号
	 * @return
	 */
	@RequestMapping(value = "/complainOrder",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public Map<String, Object> complainOrder(Orders order){
		Map<String, Object> map = new HashMap<>();	
		TreeMap<String, String> paramap = new TreeMap<String, String>();		
		if(StringUtil.isBlank(order.getMerchantUserId()) 
		|| StringUtil.isBlank(order.getOutTradeNo()) || StringUtil.isBlank(order.getTradeNumber()) 
		|| StringUtil.isBlank(order.getVersion()) || !SystemConst.version.equals(order.getVersion())
		|| StringUtil.isBlank(order.getTimestamp())
		){
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			return map;
		}
		UserMerchant umt=userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
		if(umt==null || umt.getStatus()==0) {
			map.put("code", StatusCode._301.status);
			map.put("msg", StatusCode._301.msg);
			return map;
		}
		paramap.put("tradeNumber", order.getTradeNumber());
		paramap.put("merchantUserId", order.getMerchantUserId()+"");
		paramap.put("outTradeNo", order.getOutTradeNo());			
		paramap.put("version", order.getVersion());
		paramap.put("body", order.getBody());
		paramap.put("timestamp", order.getTimestamp());
		paramap.put("merchantAccount", order.getMerchantAccount());	
		String line=OrderUtil.createParaOrderStr(paramap);
	    boolean isPass=RSAUtils.verifySignature(line, order.getSign(), umt.getPublicKey());
	    if(!isPass) {
			map.put("code", StatusCode._304.status);
			map.put("msg", StatusCode._304.msg);
			return map;
	    }
	    Orders oldOrder=ordersService.selectByPrimaryKey(order.getTradeNumber());	  
	    if(oldOrder.getType()==OrderType._0.type || oldOrder.getType()==OrderType._2.type) {
		    if(!oldOrder.getMerchantUserId().equals(order.getMerchantUserId()) 
		     || oldOrder.getStatus()==OrdersStatus._2.status || oldOrder.getType()==OrdersStatus._5.status
		     || oldOrder.getType()==OrdersStatus._7.status
		     ){
				map.put("code", StatusCode._310.status);
				map.put("msg", StatusCode._310.msg);
				return map;
		    }
	    }	    
    	if(Idempotent.fx_complainOrder(line)) {
			map.put("code", StatusCode._315.status);
			map.put("msg", StatusCode._315.msg);
			return map;
    	}
	    Orders newOrder=new Orders();
	    newOrder.setTradeNumber(order.getTradeNumber());
	    if(oldOrder.getStatus()<OrdersStatus._2.status) {
	    	newOrder.setStatus(OrdersStatus._5.status);
	    }else if(oldOrder.getStatus()==OrdersStatus._3.status || oldOrder.getStatus()==OrdersStatus._4.status){
	    	newOrder.setStatus(OrdersStatus._7.status);
	    }else {
			map.put("code", StatusCode._319.status);
			map.put("msg", StatusCode._319.msg);
			return map;
	    }
	    ordersService.updateByPrimaryKeySelective(newOrder);
	    ComplainOrder co=new ComplainOrder();
	    co.setTradeNumber(order.getTradeNumber());
	    co.setBody(order.getBody());
	    co.setComplainStatus(ComplainOrderStatus._1.status);
	    co.setCreateTime(new Date());	    	  
	    complainOrderService.insertSelective(co);
	    
	    map.put("code", StatusCode._200.status);
	    map.put("msg", StatusCode._200.msg);
 	    		
	    return map;
	}
	
		
	/**
	 * 商户客户提币
	 * outTradeNo:商户订单号
	 * merchantUserId:商户号
	 * currencyAmount:数量
	 * timestamp:时间戳
	 * currencyType:币种类型
	 * toAddress:提现地址
	 * subject:主题
	 * version:版本号
	 * 可选
	 * merchantAccount：商户的客户号
	 * notifyUrl:回调地址
	 * body:交易描述
	 * @return
	 */
	@RequestMapping(value = "/transferUsdt",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public Map<String, Object> transferUsdt(Orders order){
		Map<String, Object> map = new HashMap<>();	
		TreeMap<String, String> paramap = new TreeMap<String, String>();		
		if(StringUtil.isBlank(order.getMerchantUserId()) 
		|| StringUtil.isBlank(order.getOutTradeNo())
		|| StringUtil.isBlank(order.getCurrencyType())
		|| (StringUtil.isBlank(order.getCurrencyAmount()) && order.getCurrencyAmount().compareTo(BigDecimal.ZERO)<=0) 
		|| StringUtil.isBlank(order.getVersion()) || !SystemConst.version.equals(order.getVersion())
		|| StringUtil.isBlank(order.getSubject()) || StringUtil.isBlank(order.getToAddress())
		|| StringUtil.isBlank(order.getTimestamp())
		) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			return map;
		}
		UserMerchant umt=userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
		if(umt==null || umt.getStatus()==0) {
			map.put("code", StatusCode._301.status);
			map.put("msg", StatusCode._301.msg);
			return map;
		}		
		paramap.put("merchantUserId", order.getMerchantUserId()+"");
		paramap.put("outTradeNo", order.getOutTradeNo());		
		paramap.put("currencyType", order.getCurrencyType());
		paramap.put("currencyAmount", order.getCurrencyAmount()+"");		
		paramap.put("version", order.getVersion());
		paramap.put("subject", order.getSubject());
		paramap.put("body", order.getBody());
		paramap.put("timestamp", order.getTimestamp());		
		paramap.put("notifyUrl", order.getNotifyUrl());
		paramap.put("merchantAccount", order.getMerchantAccount());	
		paramap.put("toAddress", order.getToAddress());
		String line=OrderUtil.createParaOrderStr(paramap);
	    boolean isPass=RSAUtils.verifySignature(line, order.getSign(), umt.getPublicKey());
	    if(!isPass) {
			map.put("code", StatusCode._304.status);
			map.put("msg", StatusCode._304.msg);
			return map;
	    }
	    UserWallet uw=userWalletService.findByLock(order.getMerchantUserId(), order.getCurrencyType());	    
	    order.setMerchantRate(uw.getOutRate());
	    if(uw==null || uw.getCurrencyNumber().compareTo(order.getCurrencyAmount())<0 || uw.getCurrencyNumber().compareTo(SystemConst.minNum)<0) {
			map.put("code", StatusCode._201.status);
			map.put("msg", order.getCurrencyType()+"数量不足");
			return map;
	    }	    	   	    	   	    
    	if(Idempotent.fx_transferUsdt(line)) {
			map.put("code", StatusCode._315.status);
			map.put("msg", StatusCode._315.msg);
			return map;
    	}
    	List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>(2);
	    String tradeNumber=OrderNumEngender.getOrderNum();
	    order.setTradeNumber(tradeNumber);	    
		BigDecimal fees=order.getCurrencyAmount().multiply(uw.getOutRate());	
		//收取手续费
		if(fees.compareTo(BigDecimal.ZERO)>0) {
			UserWallet companyUserWallet=userWalletService.findCompanyUserByLock();
			UserWallet cum=new UserWallet();
			cum.setWalletId(companyUserWallet.getWalletId());				
			cum.setCurrencyNumber(companyUserWallet.getCurrencyNumber().add(fees));
			userWalletService.updateByPrimaryKeySelective(cum);
			cum.setFreezeNumber(companyUserWallet.getFreezeNumber());
			journalAccountList.add(journalAccountService.logAccountChange(companyUserWallet, cum, "公司收取提币手续费：" + order.getTradeNumber(),
					fees,JournalType.outRate.status,CurrencyType.USDT.status,order.getTradeNumber()));
		}			
		BigDecimal factAmount=order.getCurrencyAmount().subtract(fees);
		order.setActualCurrencyAmount(factAmount);
		order.setPoundage(fees);
	    Map<String,Object> result=uSDTUtils.send(uw.getWalletAddress(), order.getToAddress(), factAmount+"");
	    order.setCreateTime(new Date());
	    order.setPaymentTime(order.getCreateTime());
	    order.setCloseTime(order.getCreateTime());
		if (!StringUtil.isBlank(order.getNotifyUrl())) {
			order.setNotifyStatus(NotifyStatus.notyet.status);
		}else {
			order.setNotifyStatus(NotifyStatus.notNofity.status);
		}	
	    order.setType(OrderType._1.type);
	    
	    if(Integer.parseInt(result.get("code")+"")==200){
		    order.setFromAddress(uw.getWalletAddress());
		    order.setToAddress(order.getToAddress());
	    	order.setStatus(OrdersStatus._2.status);
		
		    UserWallet userWallet=new UserWallet();
		    userWallet.setWalletId(uw.getWalletId());
		    userWallet.setCurrencyNumber(uw.getCurrencyNumber().subtract(order.getCurrencyAmount())); 
		    userWalletService.updateByPrimaryKeySelective(userWallet);	
		    userWallet.setFreezeNumber(uw.getFreezeNumber());    	    	    
		    journalAccountList.add(journalAccountService.logAccountChange(uw, userWallet, "客户提币："+order.getTradeNumber(), order.getCurrencyAmount(),JournalType.transfer.status,CurrencyType.USDT.status,order.getTradeNumber()));		    
		    // 创建回调
		    if(!StringUtil.isBlank(order.getNotifyUrl())){
		        // 创建回调
		    	MerchantNotify mn=notifyCommercial.createNotify(order,TradeStatus.success.meaning);
			    UserMerchant um=userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
			    //立即回调
			   byte status= notifyCommercial.notifyOrderFinish(mn, um.getPrivateKey());
			   if(status>NotifyStatus.notyet.status) {
					if(status==NotifyStatus.success.status) {
						order.setCloseTime(new Date());
					}
				   order.setNotifyStatus(status);
			   }
		    }	    		    
		    ordersService.insert(order);	
		    journalAccountService.insertBatch(journalAccountList);
		    map.put("code", StatusCode._200.status);
		    map.put("msg", "交易大约需要30~120分钟到账");
	    }else {
	    	order.setStatus(OrdersStatus._4.status);
	    	ordersService.insert(order);
	    	map.put("code", StatusCode._201.getStatus());
	    	map.put("msg", "本次提币失败");
	    }	        	    		

	    return map;
	}
	
	/**
	 * 币商确认出金
	 * @param orderPara
	 * @return
	 */
	@RequestMapping(value = "/confirmWithdrawal",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public Map<String, Object> confirmWithdrawal(@RequestParam("tradeNumber")String tradeNumber,HttpServletRequest request,@RequestParam("password") String password){
		Map<String, Object> map = new HashMap<>();		
		if(StringUtil.isBlank(tradeNumber)) {
			map.put("code", StatusCode._308.status);
			map.put("msg", StatusCode._308.msg);
			return map;
		}			
		Users user=SessionUtil.getSession(request);
		long userId=user.getUserId();		
		String secretPassword = Md5Util.execute(user.getUserId() + password);
		if (!StringUtil.isBlank(password) && secretPassword.equals(user.getPassword())){
			Orders order=ordersService.selectByPrimaryKey(tradeNumber);		
			if(order==null || !order.getUserId().equals(user.getUserId()) || order.getType()!=OrderType._2.type
			  || order.getStatus()==OrdersStatus._2.status || order.getStatus()==OrdersStatus._4.status 
			  || order.getStatus()==OrdersStatus._6.status || order.getStatus()==OrdersStatus._3.status )
			{
				map.put("code", StatusCode._310.status);
				map.put("msg", StatusCode._310.msg);
				return map;
			}		
			Orders newOrder=new Orders();
			newOrder.setTradeNumber(tradeNumber);	
			Date now=new Date();
			newOrder.setPaymentTime(now);
			newOrder.setCloseTime(now);	
			order.setCloseTime(now);			
			newOrder.setStatus(OrdersStatus._2.status);
			if (!StringUtil.isBlank(order.getNotifyUrl())) {
				newOrder.setNotifyStatus(NotifyStatus.notyet.status);
			}else {
				newOrder.setNotifyStatus(NotifyStatus.notNofity.status);
			}		
	    	if(Idempotent.fx_confirmWithdrawal(tradeNumber+password)) {
				map.put("code", StatusCode._315.status);
				map.put("msg", StatusCode._315.msg);
				return map;
	    	}
			//是否是申诉状态 。是，修改申诉完成。
			if(order.getStatus()==OrdersStatus._5.status) {
				ComplainOrder newCo=new ComplainOrder();
				newCo.setTradeNumber(tradeNumber);
				newCo.setEndTime(new Date());
				newCo.setComplainStatus(ComplainOrderStatus._4.status);
				newCo.setRemark("币商确认已付款,申诉自动完成.");
				complainOrderService.updateByPrimaryKeySelective(newCo);				
			}			
			List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>(4);
			List<UserWallet> userWalletList=new ArrayList<UserWallet>(3);
			UserWallet uwSh=userWalletService.findByLock(order.getMerchantUserId(), order.getCurrencyType());			
			// 扣取出金费率.
			BigDecimal outRate=order.getMerchantRate();
			BigDecimal companyAmount=order.getCurrencyAmount().multiply(outRate);			
			BigDecimal bsAmount=order.getCurrencyAmount().subtract(companyAmount);
			newOrder.setPoundage(companyAmount);
			newOrder.setActualCurrencyAmount(bsAmount);
			// 商户相应币种减少
			UserWallet merchantWallet = new UserWallet();
			merchantWallet.setWalletId(uwSh.getWalletId());
			merchantWallet.setFreezeNumber(uwSh.getFreezeNumber().subtract(order.getCurrencyAmount()));
			userWalletList.add(merchantWallet);
			merchantWallet.setCurrencyNumber(uwSh.getCurrencyNumber());
			journalAccountList.add(journalAccountService.logAccountChange(uwSh, merchantWallet, "出金卖币：" + order.getTradeNumber(),
					order.getCurrencyAmount(),JournalType.buy.status,CurrencyType.USDT.status,order.getTradeNumber()));
			//收取手续费
			if(companyAmount.compareTo(BigDecimal.ZERO)>0 && user.getRole()==UsersRole.formal.role) {
				UserWallet companyUserWallet=userWalletService.findCompanyUserByLock();
				UserWallet cum=new UserWallet();
				cum.setWalletId(companyUserWallet.getWalletId());				
				cum.setCurrencyNumber(companyUserWallet.getCurrencyNumber().add(companyAmount));
				userWalletList.add(cum);
				cum.setFreezeNumber(companyUserWallet.getFreezeNumber());
				journalAccountList.add(journalAccountService.logAccountChange(companyUserWallet, cum, "公司收取出金费：" + order.getTradeNumber(),
						companyAmount,JournalType.outRate.status,CurrencyType.USDT.status,order.getTradeNumber()));
			}				
			
			UserWallet uwBs=userWalletService.findByLock(userId, order.getCurrencyType());
			usersService.unLockUser(userId);
			// 增加币商的币				
			UserWallet userWallet = new UserWallet();
			userWallet.setWalletId(uwBs.getWalletId());
			userWallet.setCurrencyNumber(uwBs.getCurrencyNumber().add(bsAmount));	
			userWalletList.add(userWallet);
			userWallet.setFreezeNumber(uwBs.getFreezeNumber());
			journalAccountList.add(journalAccountService.logAccountChange(uwBs, userWallet, "出金买币：" + order.getTradeNumber(),
					bsAmount,JournalType.sall.status,CurrencyType.USDT.status,order.getTradeNumber()));
			// 币商可用额度增加
			Users u=usersService.selectByPrimaryKeyForLock(userId);				
		    Users newUser=new Users();
		    newUser.setUserId(userId);
		    newUser.setAvailableCredit(u.getAvailableCredit().add(order.getAmount()));
		    newUser.setFreezingQuota(u.getFreezingQuota().subtract(order.getAmount()));
		    
		    UserWallet oldUser=new UserWallet();	  
		    oldUser.setUserId(userId);
		    oldUser.setCurrencyNumber(u.getAvailableCredit());
		    oldUser.setFreezeNumber(u.getFreezingQuota());	 
		    
		    UserWallet newUserW=new UserWallet();	    
		    newUserW.setCurrencyNumber(newUser.getAvailableCredit());
		    newUserW.setFreezeNumber(newUser.getFreezingQuota());	    
		    usersService.updateByPrimaryKeySelective(newUser);		    
		    journalAccountList.add(journalAccountService.logAccountChange(oldUser, newUserW, "出金买币额度回退："+order.getTradeNumber(), order.getAmount(),JournalType.buy.status,CurrencyType.CNY.status,order.getTradeNumber()));			
			userWalletService.updateBatch(userWalletList);			
		    journalAccountService.insertBatch(journalAccountList);
			// 创建回调
			if (!StringUtil.isBlank(order.getNotifyUrl())) {
				// 创建回调
				MerchantNotify mn = notifyCommercial.createNotify(order,TradeStatus.success.meaning);
				UserMerchant um = userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
				// 立即回调
				byte status=notifyCommercial.notifyOrderFinish(mn, um.getPrivateKey());
				if(status>NotifyStatus.notyet.status) {
					if(status==NotifyStatus.success.status) {
						newOrder.setCloseTime(new Date());
					}
					newOrder.setNotifyStatus(status);
				}
			}
		    //删除超时倒计时。
		    Wheel.removeOrderId(order.getTradeNumber());	   
			ordersService.updateByPrimaryKeySelective(newOrder);
		    map.put("code", StatusCode._200.status);
		    map.put("msg", StatusCode._200.msg);
		}else {
			map.put("code", StatusCode._309.getStatus());
			map.put("msg", StatusCode._309.getMsg() + "或是密码错误");
		}
		return map;
	}
	
	/**
	 * 币商确认付款
	 * @param orderPara
	 * @return
	 */
	@RequestMapping(value = "/confirmPay",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public Map<String, Object> confirmPay(@RequestParam("tradeNumber")String tradeNumber,HttpServletRequest request,@RequestParam("password") String password){
		Map<String, Object> map = new HashMap<>();		
		if(StringUtil.isBlank(tradeNumber)) {
			map.put("code", StatusCode._308.status);
			map.put("msg", StatusCode._308.msg);
			return map;
		}			
		Users user=SessionUtil.getSession(request);
		long userId=user.getUserId();		
		String secretPassword = Md5Util.execute(user.getUserId() + password);
		if (!StringUtil.isBlank(password) && secretPassword.equals(user.getPassword())){
			Orders order=ordersService.selectByPrimaryKey(tradeNumber);		
			if(order==null || !order.getUserId().equals(user.getUserId()) || order.getType()!=OrderType._0.type
			  || order.getStatus()==OrdersStatus._2.status || order.getStatus()==OrdersStatus._4.status 
			  || order.getStatus()==OrdersStatus._6.status || order.getStatus()==OrdersStatus._3.status )
			{
				map.put("code", StatusCode._310.status);
				map.put("msg", StatusCode._310.msg);
				return map;
			}		
			Orders newOrder=new Orders();
			newOrder.setTradeNumber(tradeNumber);		
			newOrder.setCloseTime(new Date());	
			newOrder.setPaymentTime(newOrder.getCloseTime());
			order.setCloseTime(newOrder.getCloseTime());
			order.setPaymentTime(newOrder.getCloseTime());
			UserWallet uwBs=userWalletService.findByLock(userId, order.getCurrencyType());
			usersService.unLockUser(userId);
			UserWallet uwSh=userWalletService.findByLock(order.getMerchantUserId(), order.getCurrencyType());
			//Map<String,Object> result=uSDTUtils.send(uwBs.getWalletAddress(), uwSh.getWalletAddress(), order.getCurrencyAmount()+"");
			newOrder.setFromAddress(uwBs.getWalletAddress());
			newOrder.setToAddress(uwSh.getWalletAddress());
			newOrder.setStatus(OrdersStatus._2.status);
			if (!StringUtil.isBlank(order.getNotifyUrl())) {
				newOrder.setNotifyStatus(NotifyStatus.notyet.status);
				order.setFromAddress(newOrder.getFromAddress());
				order.setToAddress(newOrder.getToAddress());
			}else {
				newOrder.setNotifyStatus(NotifyStatus.notNofity.status);
			}		
	    	if(Idempotent.fx_confirmPay(tradeNumber+password)) {
				map.put("code", StatusCode._315.status);
				map.put("msg", StatusCode._315.msg);
				return map;
	    	}
			//是否是申诉状态 。是，修改申诉完成。
			if(order.getStatus()==OrdersStatus._5.status) {
				ComplainOrder newCo=new ComplainOrder();
				newCo.setTradeNumber(tradeNumber);
				newCo.setEndTime(new Date());
				newCo.setComplainStatus(ComplainOrderStatus._4.status);
				newCo.setRemark("币商确认已付款,申诉自动完成.");
				complainOrderService.updateByPrimaryKeySelective(newCo);				
			}			
			List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>(3);
			List<UserWallet> userWalletList=new ArrayList<UserWallet>(3);
			// 减去币商卖出的币 。
			UserWallet userWallet = new UserWallet();
			userWallet.setWalletId(uwBs.getWalletId());
			userWallet.setFreezeNumber(uwBs.getFreezeNumber().subtract(order.getCurrencyAmount()));
			userWalletList.add(userWallet);
			userWallet.setCurrencyNumber(uwBs.getCurrencyNumber());
			journalAccountList.add(journalAccountService.logAccountChange(uwBs, userWallet, "订单卖出：" + order.getTradeNumber(),
					order.getCurrencyAmount(),JournalType.sall.status,CurrencyType.USDT.status,order.getTradeNumber()));
			// 扣取入金费率.
			BigDecimal inRate=order.getMerchantRate();
			BigDecimal companyAmount=order.getCurrencyAmount().multiply(inRate);
			BigDecimal shCurrentAmount=order.getCurrencyAmount().subtract(companyAmount);
			newOrder.setActualCurrencyAmount(shCurrentAmount);
			newOrder.setPoundage(companyAmount);
			if(companyAmount.compareTo(BigDecimal.ZERO)>0 && user.getRole()==UsersRole.formal.role) {
				
				Users userUwbs=usersService.selectByPrimaryKey(uwBs.getUserId());
				BigDecimal fees=usersService.userIncomeMoney(userUwbs, order.getTradeNumber(), order.getCurrencyAmount(), new Date());				
				companyAmount=companyAmount.subtract(fees);
				
				UserWallet companyUserWallet=userWalletService.findCompanyUserByLock();
				UserWallet cum=new UserWallet();
				cum.setWalletId(companyUserWallet.getWalletId());				
				cum.setCurrencyNumber(companyUserWallet.getCurrencyNumber().add(companyAmount));
				userWalletList.add(cum);
				cum.setFreezeNumber(companyUserWallet.getFreezeNumber());
				journalAccountList.add(journalAccountService.logAccountChange(companyUserWallet, cum, "公司收取入金费：" + order.getTradeNumber(),
						companyAmount,JournalType.inRate.status,CurrencyType.USDT.status,order.getTradeNumber()));
			}												
			// 商户相应币种增值
			UserWallet merchantWallet = new UserWallet();
			merchantWallet.setWalletId(uwSh.getWalletId());
			merchantWallet.setCurrencyNumber(uwSh.getCurrencyNumber().add(shCurrentAmount));
			userWalletList.add(merchantWallet);
			merchantWallet.setFreezeNumber(uwSh.getFreezeNumber());
			journalAccountList.add(journalAccountService.logAccountChange(uwSh, merchantWallet, "订单买入：" + order.getTradeNumber(),
					shCurrentAmount,JournalType.buy.status,CurrencyType.USDT.status,order.getTradeNumber()));
			userWalletService.updateBatch(userWalletList);
			journalAccountService.insertBatch(journalAccountList);
			// 创建回调
			if (!StringUtil.isBlank(order.getNotifyUrl())) {
				// 创建回调
				MerchantNotify mn = notifyCommercial.createNotify(order,TradeStatus.success.meaning);
				UserMerchant um = userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
				// 立即回调
				byte status=notifyCommercial.notifyOrderFinish(mn, um.getPrivateKey());
				if(status>NotifyStatus.notyet.status) {
					if(status==NotifyStatus.success.status) {
						newOrder.setCloseTime(new Date());
					}
					newOrder.setNotifyStatus(status);
				}
			}
			ordersService.updateByPrimaryKeySelective(newOrder);
		    map.put("code", StatusCode._200.status);
		    map.put("msg", StatusCode._200.msg);
		}else {
			map.put("code", StatusCode._309.getStatus());
			map.put("msg", StatusCode._309.getMsg() + "或是密码错误");
		}
		return map;
	}
		
	/**	 * 	客户付款给币商，标记订单已付款。
	 * tradeNumber:交易流水号
	 * outTradeNo:商户订单号
	 * merchantUserId:商户号
	 * timestamp：时间戳
	 * version:版本号
	 * @param Orders
	 * @return
	 */	
	@RequestMapping(value = "/orderPay",method=RequestMethod.POST)
	@Transactional
	@ResponseBody
	public Map<String, Object> orderPay(Orders order){
		Map<String, Object> map = new HashMap<>();		
		TreeMap<String, String> paramap = new TreeMap<String, String>();
		if(StringUtil.isBlank(order.getMerchantUserId()) 
		|| StringUtil.isBlank(order.getOutTradeNo())
		|| StringUtil.isBlank(order.getTradeNumber())
		|| StringUtil.isBlank(order.getVersion()) || !SystemConst.version.equals(order.getVersion())
		|| StringUtil.isBlank(order.getTimestamp())	
		) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			return map;
		}		
		UserMerchant umt=userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
		if(umt==null || umt.getStatus()==0) {
			map.put("code", StatusCode._301.status);
			map.put("msg", StatusCode._301.msg);
			return map;
		}
		paramap.put("merchantUserId", order.getMerchantUserId()+"");
		paramap.put("outTradeNo", order.getOutTradeNo());		
		paramap.put("version", order.getVersion());
		paramap.put("timestamp", order.getTimestamp());		
		paramap.put("tradeNumber", order.getTradeNumber());	
		String line=OrderUtil.createParaOrderStr(paramap);
	    boolean isPass=RSAUtils.verifySignature(line, order.getSign(), umt.getPublicKey());
	    if(!isPass) {
			map.put("code", StatusCode._304.status);
			map.put("msg", StatusCode._304.msg);
			return map;
	    }	    
	    Orders oldOrder=ordersService.selectByPrimaryKey(order.getTradeNumber());	    	    
	    if(!oldOrder.getMerchantUserId().equals(order.getMerchantUserId()) || oldOrder.getStatus()>0) {
			map.put("code", StatusCode._310.status);
			map.put("msg", StatusCode._310.msg);
			return map;
	    }
	    order.setStatus(OrdersStatus._1.status);
	    order.setPaymentTime(new Date());	    	    
    	if(Idempotent.fx_orderPay(line)) {
			map.put("code", StatusCode._315.status);
			map.put("msg", StatusCode._315.msg);
			return map;
    	}
	    ordersService.updateByPrimaryKeySelective(order);
	    //删除超时倒计时。
	    Wheel.removeOrderId(order.getTradeNumber());	
	    Wheel.pushSlot(order.getTradeNumber());
	    /** 
	     *发短信 ，消息通知币商。 
	     */
	    /*Users user= usersService.selectByPrimaryKey(oldOrder.getUserId());	    
	    SMSUtil.sentPayRemind(user.getPhoneNumber(), oldOrder.getTradeNumber(),oldOrder.getPaymentTypeName(),oldOrder.getPaymentNumber(),oldOrder.getAmount()+""," "+SystemConst.CNY,DateTools.DateToStr2(new Date()));*/
	    	    
	    map.put("code", StatusCode._200.status);
	    map.put("msg", StatusCode._200.msg);
	    return map;
	}
		
	/***************接口版~~~~~~~~~~~~~~~~ 
	 * 创建订单，返回币商银行账号，二维付款码。
	 * outTradeNo,商户订单号
	 * merchantUserId,商户账号
	 * currencyType,电子币类型
	 * amount ,充值金额
	 * version ,版本号
	 * subject,主题
	 * notifyUrl,回调url
	 * timestamp,时间戳
	 * paymentType,支付方式。
	 * 可选参数有：
	 * merchantAccount: 商户平台用户的唯一标示。 
	 * body,交易描述	 *
	 * @param Orders
	 * @return
	 */
	@RequestMapping(value = "/createOrder",method = RequestMethod.POST )
	@Transactional
	@JSON(type = Orders.class,include = "tradeNumber,outTradeNo,merchantUserId,merchantAccount,paymentType,currencyType,currencyAmount,exchangeRate,amount,payTimeout")
	@JSON(type = PaymentMode.class,include = "paymentId,paymentTypeName,paymentNumber,paymentName,paymentImage,userName")
	public Map<String, Object> createOrder(Orders order,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();		
		byte paymentType=Payment.getTypeByCode(order.getPaymentType());
		TreeMap<String, String> paramap = new TreeMap<String, String>();		
		if(StringUtil.isBlank(order.getMerchantUserId()) 
		|| StringUtil.isBlank(order.getOutTradeNo()) || StringUtil.isBlank(order.getCurrencyType())
		|| StringUtil.isBlank(order.getAmount()) || order.getAmount().compareTo(BigDecimal.ZERO)<=0
		|| StringUtil.isBlank(order.getVersion()) || !SystemConst.version.equals(order.getVersion())
		|| StringUtil.isBlank(order.getSubject()) || StringUtil.isBlank(order.getTimestamp())	
		|| StringUtil.isBlank(order.getPaymentType()) || paymentType==0
		) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			return map;
		}
		UserMerchant umt=userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
		if(umt==null || umt.getStatus()==0) {
			map.put("code", StatusCode._301.status);
			map.put("msg", StatusCode._301.msg);
			return map;
		}		
		paramap.put("merchantUserId", order.getMerchantUserId()+"");
		paramap.put("outTradeNo", order.getOutTradeNo());		
		paramap.put("currencyType", order.getCurrencyType());
		paramap.put("amount", order.getAmount()+"");		
		paramap.put("paymentType", order.getPaymentType());
		paramap.put("version", order.getVersion());
		paramap.put("subject", order.getSubject());
		paramap.put("body", order.getBody());
		paramap.put("timestamp", order.getTimestamp());		
		paramap.put("notifyUrl", order.getNotifyUrl());
		paramap.put("merchantAccount", order.getMerchantAccount());	
		String line=OrderUtil.createParaOrderStr(paramap);
	    boolean isPass=RSAUtils.verifySignature(line, order.getSign(), umt.getPublicKey());
	    if(!isPass) {
			map.put("code", StatusCode._304.status);
			map.put("msg", StatusCode._304.msg);
			return map;
	    }
	    String vtbTradeNo=OrderNumEngender.getOrderNum();
	    order.setTradeNumber(vtbTradeNo);
	    order.setStatus(OrdersStatus._0.status);
	    order.setCreateTime(new Date());	    
	
	    Currency c=currencyService.selectByPrimaryKey(order.getCurrencyType());
	    if(c==null) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			return map;	    	
	    }
	    order.setCurrencyAmount(order.getAmount().divide(c.getRate(),SystemConst.decimal_place_num, BigDecimal.ROUND_HALF_UP));
	    order.setActualAmount(order.getAmount());
	    /**
	     * 随机分配订单给币商
	     */
	    int hour=(int)Math.pow(2, java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY));
	    List<ValidUserPayment> list=userWalletService.selectUserWalletByCurrencyAmountAndType(order.getCurrencyAmount(), SystemConst.random_limit, paymentType,hour);	 	    
	    if(list==null ||list.size()<1) {
			map.put("code", StatusCode._311.status);
			map.put("msg","暂时无法匹配承兑商，请稍后重试");
			return map;
	    }	    
	    ValidUserPayment vup=list.get(ThreadLocalRandom.current().nextInt(list.size()));
	    long userId=vup.getUserId();	   
	    usersService.lockUser(userId);
	    order.setUserId(userId);
	    order.setPayTimeout(SystemConst.Order_TimeOut);	    	   
	    order.setExchangeRate(c.getRate());
	    order.setType(OrderType._0.type);
		if (!StringUtil.isBlank(order.getNotifyUrl())) {
			order.setNotifyStatus(NotifyStatus.notyet.status);
		}else {
			order.setNotifyStatus(NotifyStatus.notNofity.status);
		}    
    	PaymentMode p=paymentModeService.selectByPrimaryKey(vup.getPaymentId());
		p.setPaymentTypeName(Payment.getMeaningByType(p.getPaymentType()));    		
		if(!StringUtil.isBlank(p.getPaymentImage())) {
			String scheme="http";
			if(request.getServerPort()==SystemConst.sslPort) {
				scheme="https";
			}
			String url=scheme+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
			if(p.getPaymentImage().startsWith("/")) {
				p.setPaymentImage(url+p.getPaymentImage());
			}else {
				p.setPaymentImage(url+"/"+p.getPaymentImage());
			}
		}    	
	    order.setPaymentNumber(p.getPaymentNumber());
	    if(p.getPaymentNumber()!=null && p.getPaymentNumber().length()>SystemConst.suffixNumber) {
	    	String paymentNumbe=p.getPaymentNumber().trim();
	    	String suffix=paymentNumbe.substring(paymentNumbe.length()-SystemConst.suffixNumber, paymentNumbe.length());
	    	order.setPaymentNumberSuffix(suffix);
	    }	    
	    order.setPaymentTypeName(Payment.getMeaningByType((p.getPaymentType())));
    	order.setPaymentId(p.getPaymentId());    	
    	List<PaymentMode> paymentlist=new ArrayList<PaymentMode>();
    	paymentlist.add(p);    	
    	if(Idempotent.fx_createOrder(line)) {
			map.put("code", StatusCode._315.status);
			map.put("msg", StatusCode._315.msg);
			return map;
    	}   
	    UserWallet shuw=userWalletService.selectByType(order.getMerchantUserId(), order.getCurrencyType());
	    if(shuw==null) {
			map.put("code", StatusCode._301.status);
			map.put("msg", StatusCode._301.msg);
			return map;	
	    }
	    order.setMerchantRate(shuw.getInRate());
	    List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>(1);
	    /**
	     * 冻结币商的币 。
	     */
	    UserWallet uw= userWalletService.findByLock(userId, order.getCurrencyType());
	    UserWallet userWallet=new UserWallet();
	    userWallet.setWalletId(uw.getWalletId());
	    userWallet.setCurrencyNumber(uw.getCurrencyNumber().subtract(order.getCurrencyAmount()));
	    userWallet.setFreezeNumber(uw.getFreezeNumber().add(order.getCurrencyAmount()));	    
	    userWalletService.updateByPrimaryKeySelective(userWallet);	    	    	    
	    journalAccountList.add(journalAccountService.logAccountChange(uw, userWallet, "客户下单USDT冻结："+order.getTradeNumber(), order.getCurrencyAmount(),JournalType.freeze.status,CurrencyType.USDT.status,order.getTradeNumber()));	   	    
	    order.setUnderwriterRate(uw.getInRate());	    
	    ordersService.insert(order);
	    journalAccountService.insertBatch(journalAccountList);
	    // 加入大转盘
	    Wheel.pushSlot(vtbTradeNo);	 	    
	    
	    // 旋转收款账号
	    paymentRotateService.spinPaymentRotate(p.getPaymentId(), order.getActualAmount());
	    
		/**
		 * 返回银行卡 号 和订单 。 	    
		 */	    
	    Map<String, Object> mapData = new HashMap<>();
    	mapData.put("paymentList", paymentlist);	    	    	    
	    mapData.put("orderDetail", order);
	    
	    map.put("code", StatusCode._200.status);
	    map.put("msg", StatusCode._200.msg);
	    map.put("data", mapData);
	    
		return map;
		
	}
	
	/*************网页版填付款姓名版~~~~~~~~~~~~~~~
	 * 创建订单，返回币商银行账号，二维付款码。
	 * outTradeNo,商户订单号
	 * merchantUserId,商户账号
	 * currencyType,电子币类型
	 * amount ,充值金额
	 * version ,版本号
	 * subject,主题
	 * notifyUrl,回调url
	 * timestamp,时间戳
	 * paymentType,支付方式。
	 * 可选参数有：
	 * merchantAccount: 商户平台用户的唯一标示。 
	 * body,交易描述	 *
	 * @param Orders
	 * @return
	 */
	@RequestMapping(value = "/createPayNameOrder",method = RequestMethod.GET )
	@Transactional
	public String createPayNameOrder(Orders order,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<>();		
		byte paymentType=Payment.getTypeByCode(order.getPaymentType());
		TreeMap<String, String> paramap = new TreeMap<String, String>();		
		if(StringUtil.isBlank(order.getMerchantUserId()) 
		|| StringUtil.isBlank(order.getOutTradeNo()) || StringUtil.isBlank(order.getCurrencyType())
		|| StringUtil.isBlank(order.getAmount()) || order.getAmount().compareTo(BigDecimal.ZERO)<=0 
		|| StringUtil.isBlank(order.getVersion()) || !SystemConst.version.equals(order.getVersion())
		|| StringUtil.isBlank(order.getSubject()) || StringUtil.isBlank(order.getTimestamp())	
		|| StringUtil.isBlank(order.getPaymentType()) || paymentType==0
		) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			request.setAttribute("MSG", map);
			return "error";
		}
		if(Payment.union_pay.type!=paymentType) {
			map.put("code", StatusCode._320.status);
			map.put("msg", StatusCode._320.msg);
			request.setAttribute("MSG", map);
			return "error";
		}
		UserMerchant umt=userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
		if(umt==null || umt.getStatus()==0) {
			map.put("code", StatusCode._301.status);
			map.put("msg", StatusCode._301.msg);
			request.setAttribute("MSG", map);
			return "error";			
		}		
		paramap.put("merchantUserId", order.getMerchantUserId()+"");
		paramap.put("outTradeNo", order.getOutTradeNo());		
		paramap.put("currencyType", order.getCurrencyType());
		paramap.put("amount", order.getAmount()+"");		
		paramap.put("paymentType", order.getPaymentType());
		paramap.put("version", order.getVersion());
		paramap.put("subject", order.getSubject());
		paramap.put("body", order.getBody());
		paramap.put("timestamp", order.getTimestamp());		
		paramap.put("notifyUrl", order.getNotifyUrl());
		paramap.put("merchantAccount", order.getMerchantAccount());	
		String line=OrderUtil.createParaOrderStr(paramap);		
	    boolean isPass=RSAUtils.verifySignature(line, order.getSign(), umt.getPublicKey());
	    if(!isPass) {
			map.put("code", StatusCode._304.status);
			map.put("msg", StatusCode._304.msg);
			request.setAttribute("MSG", map);
			return "error";			
	    }
    	if(Idempotent.fx_createOnLineOrder(line)) {
			map.put("code", StatusCode._315.status);
			map.put("msg", StatusCode._315.msg);
			request.setAttribute("MSG", map);
			return "error";			
    	}
	    Currency c=currencyService.selectByPrimaryKey(order.getCurrencyType());
	    if(c==null) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			request.setAttribute("MSG", map);	
			return "error";			
	    }
	    order.setCurrencyAmount(order.getAmount().divide(c.getRate(),SystemConst.decimal_place_num, BigDecimal.ROUND_HALF_UP));
	    order.setExchangeRate(c.getRate());
    	String vtbTradeNo=OrderNumEngender.getOrderNum();
    	order.setTradeNumber(vtbTradeNo);  	
    	paramap.put("currencyAmount", order.getCurrencyAmount()+"");
    	paramap.put("exchangeRate", order.getExchangeRate()+"");
    	line=OrderUtil.createParaOrderStr(paramap);	
	    request.setAttribute("orderDetail", order);
	    String sign=Md5Util.execute(line+SystemConst.onLineKey);
	    OrderUtil.setOrderSign(order.getTradeNumber(), sign);
	    request.setAttribute("sign", sign);	    
		return "orderNew";		
	}
	
	@RequestMapping(value = "/success",method = RequestMethod.GET )
	public String createOnLineOrder(){		
		return "orderSuccess";			
	}	
	@RequestMapping(value = "/error",method = RequestMethod.GET )
	public String returnError(Integer code ,String msg ,HttpServletRequest request){	
		Map<String, Object> map = new HashMap<>();
		map.put("code", code);
		map.put("msg", msg);
		request.setAttribute("MSG", map);		
		return "error";			
	}	
	/**	 * 生成二维码付款页面网页版
	 * tradeNumber:交易流水号
	 * sign:商户订单号
	 * @param Orders
	 * @return
	 */	
	@RequestMapping(value = "/qrCodePay",method=RequestMethod.GET)
	public String qrCodePay(Orders order,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		byte paymentType=Payment.getTypeByCode(order.getPaymentType());
		if(StringUtil.isBlank(order.getTradeNumber())
			|| StringUtil.isBlank(order.getPaymentName())){
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			request.setAttribute("MSG", map);
			return "error";	
		}
	   String sign= OrderUtil.hasOrderSign(order.getTradeNumber());
	   if(StringUtil.isBlank(sign)) {
			map.put("code", StatusCode._304.status);
			map.put("msg", StatusCode._304.msg);
			request.setAttribute("MSG", map);
			return "error";	
	   }			   
	   if(StringUtil.isBlank(order.getMerchantUserId()) 
			|| StringUtil.isBlank(order.getOutTradeNo()) || StringUtil.isBlank(order.getCurrencyType())
			|| StringUtil.isBlank(order.getAmount()) || order.getAmount().compareTo(BigDecimal.ZERO)<=0 
			|| StringUtil.isBlank(order.getVersion()) || !SystemConst.version.equals(order.getVersion())
			|| StringUtil.isBlank(order.getSubject()) || StringUtil.isBlank(order.getTimestamp())	
			|| StringUtil.isBlank(order.getPaymentType()) || paymentType==0
			) {
				map.put("code", StatusCode._303.status);
				map.put("msg", StatusCode._303.msg);
				request.setAttribute("MSG", map);
				return "error";
			}
		if(Payment.union_pay.type!=paymentType) {
			map.put("code", StatusCode._320.status);
			map.put("msg", StatusCode._320.msg);
			request.setAttribute("MSG", map);
			return "error";
		}
		TreeMap<String, String> paramap = new TreeMap<String, String>();
		paramap.put("merchantUserId", order.getMerchantUserId()+"");
		paramap.put("outTradeNo", order.getOutTradeNo());		
		paramap.put("currencyType", order.getCurrencyType());
		paramap.put("amount", order.getAmount()+"");		
		paramap.put("paymentType", order.getPaymentType());
		paramap.put("version", order.getVersion());
		paramap.put("subject", order.getSubject());
		paramap.put("body", order.getBody());
		paramap.put("timestamp", order.getTimestamp());		
		paramap.put("notifyUrl", order.getNotifyUrl());
		paramap.put("merchantAccount", order.getMerchantAccount());	
    	paramap.put("currencyAmount", order.getCurrencyAmount()+"");
    	paramap.put("exchangeRate", order.getExchangeRate()+"");
    	order.setActualAmount(order.getAmount());
		String line=OrderUtil.createParaOrderStr(paramap);
		if(!Md5Util.execute(line+SystemConst.onLineKey).equals(sign)) {
			map.put("code", StatusCode._304.status);
			map.put("msg", StatusCode._304.msg);
			request.setAttribute("MSG", map);
			return "error";	
		}
		  String vtbTradeNo=OrderNumEngender.getOrderNum();
		    order.setTradeNumber(vtbTradeNo);
		    order.setStatus(OrdersStatus._0.status);
		    order.setCreateTime(new Date());		
		    /**
		     * 随机分配订单给币商
		     */
		    int hour=(int)Math.pow(2, java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY));
		    List<ValidUserPayment> list=userWalletService.selectUserWalletByCurrencyAmountAndType(order.getCurrencyAmount(), SystemConst.random_limit, paymentType,hour);	    
		    if(list==null ||list.size()<1) {
				map.put("code", StatusCode._311.status);
				map.put("msg", "暂时无法匹配承兑商，请稍后重试");
				request.setAttribute("MSG", map);
				return "error";			
		    }   
		    ValidUserPayment vup=list.get(ThreadLocalRandom.current().nextInt(list.size()));
		    long userId=vup.getUserId();	
		    usersService.lockUser(userId);
		    order.setUserId(userId);
		    order.setPayTimeout(SystemConst.Order_TimeOut);	    	   
		    order.setType(OrderType._0.type);
			if (!StringUtil.isBlank(order.getNotifyUrl())) {
				order.setNotifyStatus(NotifyStatus.notyet.status);
			}else {
				order.setNotifyStatus(NotifyStatus.notNofity.status);
			}
	    	PaymentMode p=paymentModeService.selectByPrimaryKey(vup.getPaymentId());
			p.setPaymentTypeName(Payment.getMeaningByType(p.getPaymentType()));    		  	
		    order.setPaymentNumber(p.getPaymentNumber());
		    if(p.getPaymentNumber()!=null && p.getPaymentNumber().length()>SystemConst.suffixNumber) {
		    	String paymentNumbe=p.getPaymentNumber().trim();
		    	String suffix=paymentNumbe.substring(paymentNumbe.length()-SystemConst.suffixNumber, paymentNumbe.length());
		    	order.setPaymentNumberSuffix(suffix);
		    }	
		    order.setPaymentTypeName(Payment.getMeaningByType((p.getPaymentType())));
	    	order.setPaymentId(p.getPaymentId());    	
	    	List<PaymentMode> paymentlist=new ArrayList<PaymentMode>();
	    	paymentlist.add(p);
	    	if(Idempotent.fx_qrCodePay(line)) {
				map.put("code", StatusCode._315.status);
				map.put("msg", StatusCode._315.msg);
				request.setAttribute("MSG", map);
				return "error";			
	    	}
		    UserWallet shuw=userWalletService.selectByType(order.getMerchantUserId(), order.getCurrencyType());
		    if(shuw==null) {
				map.put("code", StatusCode._301.status);
				map.put("msg", StatusCode._301.msg);
				request.setAttribute("MSG", map);
				return "error";	
		    }
		    order.setMerchantRate(shuw.getInRate()); 
		    List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>(1);
		    /**
		     * 冻结币商的币 。
		     */
		    UserWallet uw= userWalletService.findByLock(userId, order.getCurrencyType());
		    UserWallet userWallet=new UserWallet();
		    userWallet.setWalletId(uw.getWalletId());
		    userWallet.setCurrencyNumber(uw.getCurrencyNumber().subtract(order.getCurrencyAmount()));
		    userWallet.setFreezeNumber(uw.getFreezeNumber().add(order.getCurrencyAmount()));	    
		    userWalletService.updateByPrimaryKeySelective(userWallet);	    	    	    
		    journalAccountList.add(journalAccountService.logAccountChange(uw, userWallet, "客户下单USDT冻结："+order.getTradeNumber(), order.getCurrencyAmount(),JournalType.freeze.status,CurrencyType.USDT.status,order.getTradeNumber()));		    
		    order.setUnderwriterRate(uw.getInRate());	    
		    ordersService.insert(order);	   
		    journalAccountService.insertBatch(journalAccountList);
	    // 加入大转盘
	    Wheel.pushSlot(order.getTradeNumber());	 
	    /** 
	     *发短信 ，消息通知币商。 
	     */
	    /*Users user= usersService.selectByPrimaryKey(oldOrder.getUserId());	    
	    SMSUtil.sentPayRemind(user.getPhoneNumber(), oldOrder.getTradeNumber(),oldOrder.getPaymentTypeName(),oldOrder.getPaymentNumber(),oldOrder.getAmount()+""," "+SystemConst.CNY,DateTools.DateToStr2(new Date()));*/

		sign=Md5Util.execute(order.getSign()+System.currentTimeMillis()+""+ThreadLocalRandom.current().nextDouble());
	    OrderUtil.setOrderSign(order.getTradeNumber(), sign);
		request.setAttribute("tradeNumber", order.getTradeNumber());
	    request.setAttribute("sign", sign);
	    request.setAttribute("payTimeOut", order.getPayTimeout());
		map.put("code", StatusCode._200.status);
		map.put("msg", StatusCode._200.msg);
		request.setAttribute("data", map);
		request.setAttribute("amount", order.getActualAmount());
		
	    // 旋转收款账号
	    paymentRotateService.spinPaymentRotate(p.getPaymentId(), order.getActualAmount());
		
	    return "orderSec";
	}
	/**
	 * 网页版标记已付款
	 */
	@RequestMapping(value = "/badgeOrderPay",method=RequestMethod.GET)
	public String badgeOrderPay(Orders order,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		if(StringUtil.isBlank(order.getSign()) || StringUtil.isBlank(order.getTradeNumber())){
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			request.setAttribute("MSG", map);
			return "error";	
		}
	   String sign= OrderUtil.hasOrderSign(order.getTradeNumber());
	   if(!order.getSign().equals(sign)) {
			map.put("code", StatusCode._304.status);
			map.put("msg", StatusCode._304.msg);
			request.setAttribute("MSG", map);
			return "error";	
	   }	 
	   Orders oldOrder=ordersService.selectByPrimaryKey(order.getTradeNumber());	
	   UserMerchant umt=userMerchantService.selectByPrimaryKey(oldOrder.getMerchantUserId());
		if(umt==null || umt.getStatus()==0) {
			map.put("code", StatusCode._301.status);
			map.put("msg", StatusCode._301.msg);
			request.setAttribute("MSG", map);
			return "error";	
		}  	       	    
	    if(oldOrder.getStatus()>OrdersStatus._0.status) {
			map.put("code", StatusCode._319.status);
			map.put("msg", StatusCode._319.msg);
			request.setAttribute("MSG", map);
			return "error";	
	    }
	    order.setStatus(OrdersStatus._1.status);
	    order.setPaymentTime(new Date());	    	    
	    ordersService.updateByPrimaryKeySelective(order);
	    //删除超时倒计时。
	    Wheel.removeOrderId(order.getTradeNumber());
	    Wheel.pushSlot(order.getTradeNumber());
	    /** 
	     *发短信 ，消息通知币商。 
	     */
	    /*Users user= usersService.selectByPrimaryKey(oldOrder.getUserId());	    
	    SMSUtil.sentPayRemind(user.getPhoneNumber(), oldOrder.getTradeNumber(),oldOrder.getPaymentTypeName(),oldOrder.getPaymentNumber(),oldOrder.getAmount()+""," "+SystemConst.CNY,DateTools.DateToStr2(new Date()));*/
	    	    
		map.put("code", StatusCode._200.status);
		map.put("msg", StatusCode._200.msg);
		request.setAttribute("MSG", map);
		return "orderSuccess";	
	}	
	
	@RequestMapping(value = "/testOnlinePayOtc",method=RequestMethod.GET)
	public String testOnlinePayOtc(){		
		return "testPayOtc";		
	}
	
	@RequestMapping(value ="/testOnLineOrder",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> testOnLineOrder(HttpServletRequest request,Orders order) throws Exception{
		Map<String, Object> map = new HashMap<>();		
		byte paymentType=Payment.getTypeByCode(order.getPaymentType());
		Users user = usersService.selectByPrimaryKey(order.getMerchantUserId());
		order.setMerchantUserId(user.getUserId());
		TreeMap<String, String> paramap = new TreeMap<String, String>();		
		if(StringUtil.isBlank(order.getMerchantUserId()) 
		|| StringUtil.isBlank(order.getOutTradeNo()) || StringUtil.isBlank(order.getCurrencyType())
		|| (StringUtil.isBlank(order.getAmount()) && order.getAmount().compareTo(BigDecimal.ZERO)<=0) 
		|| StringUtil.isBlank(order.getVersion()) || !SystemConst.version.equals(order.getVersion())
		|| StringUtil.isBlank(order.getSubject()) || StringUtil.isBlank(order.getTimestamp())	
		|| StringUtil.isBlank(order.getPaymentType()) || paymentType==0 || user.getType()!=UsersType._1.status
		) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			return map;
		}
		paramap.put("merchantUserId", order.getMerchantUserId()+"");
		paramap.put("outTradeNo", order.getOutTradeNo());		
		paramap.put("currencyType", order.getCurrencyType());
		paramap.put("amount", order.getAmount()+"");		
		paramap.put("paymentType", order.getPaymentType());
		paramap.put("version", order.getVersion());
		paramap.put("subject", order.getSubject());
		paramap.put("body", order.getBody());
		paramap.put("timestamp", order.getTimestamp());		
		paramap.put("notifyUrl", order.getNotifyUrl());
		paramap.put("merchantAccount", order.getMerchantAccount());	
		String line=OrderUtil.createParaOrderStr(paramap);
		UserMerchant umt=userMerchantService.selectByPrimaryKey(user.getUserId());
		String sign=RSAUtils.signature(line, umt.getPrivateKey());		
		Map<String, String> data = new HashMap<>();		
		data.put("paraStr", line);
		data.put("sign", java.net.URLEncoder.encode(sign,RSAUtils.CHARSET));
		map.put("code", StatusCode._200.status);
		map.put("msg", StatusCode._200.msg);
		map.put("data", data);
		
		return map;
	}
	/**
	 * 手机回调确认付款。
	 */
	@RequestMapping(value ="/phoneCallback",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> phoneCallback(HttpServletRequest request,String amount,String paymentName,
			String paymentNumberSuffix,String sign) throws Exception{
		Map<String, Object> map = new HashMap<>();			
		OrderQueryPara oqp=new OrderQueryPara();
		oqp.setAmount(amount);
		oqp.setPaymentName(paymentName);
		oqp.setPaymentNumberSuffix(paymentNumberSuffix);
		oqp.setSign(sign);
		if(oqp.getAmount()==null || oqp.getPaymentName()==null || oqp.getPaymentNumberSuffix()==null || oqp.getSign()==null) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			return map;
		}
//		if(!Tools.verifySign( amount, paymentName,paymentNumberSuffix, sign)) {
//			map.put("code", StatusCode._304.status);
//			map.put("msg", StatusCode._304.msg);
//			return map;
//		}
		List<Orders> list=ordersService.selectOrderByPhoneCallback(oqp);
		if(list!=null && list.size()>0) {
			Orders order=list.get(0);
			if(list.size()>1 || order.getStatus()>=OrdersStatus._2.status){			
				map.put("code", StatusCode._201.status);
				map.put("msg", StatusCode._201.msg);
				return map;
			}			
	    	if(Idempotent.fx_phoneCallback(order.getTradeNumber())) {
				map.put("code", StatusCode._315.status);
				map.put("msg", StatusCode._315.msg);
				return map;
	    	}
	    	List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>(3);
	    	List<UserWallet> userWalletList=new ArrayList<UserWallet>(3);
			Orders newOrder=new Orders();
			newOrder.setTradeNumber(order.getTradeNumber());		
			newOrder.setCloseTime(new Date());	
			newOrder.setPaymentTime(newOrder.getCloseTime());
			order.setCloseTime(newOrder.getCloseTime());
			order.setPaymentTime(newOrder.getCloseTime());
			UserWallet uwBs=userWalletService.findByLock(order.getUserId(), order.getCurrencyType());
			usersService.unLockUser(order.getUserId());
			UserWallet uwSh=userWalletService.findByLock(order.getMerchantUserId(), order.getCurrencyType());
			newOrder.setFromAddress(uwBs.getWalletAddress());
			newOrder.setToAddress(uwSh.getWalletAddress());
			newOrder.setStatus(OrdersStatus._2.status);
			if (!StringUtil.isBlank(order.getNotifyUrl())) {
				newOrder.setNotifyStatus(NotifyStatus.notyet.status);
				order.setFromAddress(newOrder.getFromAddress());
				order.setToAddress(newOrder.getToAddress());
			}else {
				newOrder.setNotifyStatus(NotifyStatus.notNofity.status);
			}		
			//是否是申诉状态 。是，修改申诉完成。
			if(order.getStatus()==OrdersStatus._5.status) {
				ComplainOrder newCo=new ComplainOrder();
				newCo.setTradeNumber(order.getTradeNumber());
				newCo.setEndTime(new Date());
				newCo.setComplainStatus(ComplainOrderStatus._4.status);
				newCo.setRemark("币商确认已付款,申诉自动完成.");
				complainOrderService.updateByPrimaryKeySelective(newCo);				
			}			
			// 减去币商卖出的币 。
			UserWallet userWallet = new UserWallet();
			userWallet.setWalletId(uwBs.getWalletId());
			userWallet.setFreezeNumber(uwBs.getFreezeNumber().subtract(order.getCurrencyAmount()));
			userWalletList.add(userWallet);
			userWallet.setCurrencyNumber(uwBs.getCurrencyNumber());
			journalAccountList.add(journalAccountService.logAccountChange(uwBs, userWallet, "订单卖出：" + order.getTradeNumber(),
					order.getCurrencyAmount(),JournalType.sall.status,CurrencyType.USDT.status,order.getTradeNumber()));
			// 扣取入金费率.
			BigDecimal inRate=order.getMerchantRate();
			BigDecimal companyAmount=order.getCurrencyAmount().multiply(inRate);
			BigDecimal shCurrentAmount=order.getCurrencyAmount().subtract(companyAmount);
			newOrder.setActualCurrencyAmount(shCurrentAmount);
			newOrder.setPoundage(companyAmount);
			if(companyAmount.compareTo(BigDecimal.ZERO)>0) {
				
				Users user=usersService.selectByPrimaryKey(uwBs.getUserId());
				BigDecimal fees=usersService.userIncomeMoney(user, order.getTradeNumber(), order.getCurrencyAmount(), new Date());				
				companyAmount=companyAmount.subtract(fees);
				
				UserWallet companyUserWallet=userWalletService.findCompanyUserByLock();
				UserWallet cum=new UserWallet();
				cum.setWalletId(companyUserWallet.getWalletId());				
				cum.setCurrencyNumber(companyUserWallet.getCurrencyNumber().add(companyAmount));
				userWalletList.add(cum);
				cum.setFreezeNumber(companyUserWallet.getFreezeNumber());
				journalAccountList.add(journalAccountService.logAccountChange(companyUserWallet, cum, "公司收取入金费：" + order.getTradeNumber(),
						companyAmount,JournalType.inRate.status,CurrencyType.USDT.status,order.getTradeNumber()));
			}												
			// 商户相应币种增值
			UserWallet merchantWallet = new UserWallet();
			merchantWallet.setWalletId(uwSh.getWalletId());
			merchantWallet.setCurrencyNumber(uwSh.getCurrencyNumber().add(shCurrentAmount));
			userWalletList.add(merchantWallet);
			merchantWallet.setFreezeNumber(uwSh.getFreezeNumber());
			journalAccountList.add(journalAccountService.logAccountChange(uwSh, merchantWallet, "订单买入：" + order.getTradeNumber(),
					shCurrentAmount,JournalType.buy.status,CurrencyType.USDT.status,order.getTradeNumber()));
			// 创建回调
			if (!StringUtil.isBlank(order.getNotifyUrl())){
				// 创建回调
				MerchantNotify mn = notifyCommercial.createNotify(order,TradeStatus.success.meaning);
				UserMerchant um = userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
				// 立即回调
				byte status=notifyCommercial.notifyOrderFinish(mn, um.getPrivateKey());
				if(status>NotifyStatus.notyet.status) {
					if(status==NotifyStatus.success.status) {
						newOrder.setCloseTime(new Date());
					}
					newOrder.setNotifyStatus(status);
				}
			}
			userWalletService.updateBatch(userWalletList);
			ordersService.updateByPrimaryKeySelective(newOrder);
			journalAccountService.insertBatch(journalAccountList);
			map.put("code", StatusCode._200.status);
			map.put("msg", StatusCode._200.msg);							
		}else {
			map.put("code", StatusCode._201.status);
			map.put("msg", StatusCode._201.msg);	
		}
				

		return map;
	}
	
	/*************网页版随机减金额版~~~~~~~~~~~~~~~
	 * 创建订单，返回币商银行账号，二维付款码。
	 * outTradeNo,商户订单号
	 * merchantUserId,商户账号
	 * currencyType,电子币类型
	 * amount ,充值金额
	 * version ,版本号
	 * subject,主题
	 * notifyUrl,回调url
	 * timestamp,时间戳
	 * paymentType,支付方式。
	 * 可选参数有：
	 * merchantAccount: 商户平台用户的唯一标示。 
	 * body,交易描述	 *
	 * @param Orders
	 * @return
	 */
	@RequestMapping(value ="/createOnLineOrder",method = RequestMethod.GET )
	@Transactional
	public String createOnLineOrder(Orders order,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<>();
		byte paymentType=Payment.getTypeByCode(order.getPaymentType());
		TreeMap<String, String> paramap = new TreeMap<String, String>();		
		if(StringUtil.isBlank(order.getMerchantUserId()) 
		|| StringUtil.isBlank(order.getOutTradeNo()) || StringUtil.isBlank(order.getCurrencyType())
		|| StringUtil.isBlank(order.getAmount()) || order.getAmount().compareTo(BigDecimal.ZERO)<=0 
		|| StringUtil.isBlank(order.getVersion()) || !SystemConst.version.equals(order.getVersion())
		|| StringUtil.isBlank(order.getSubject()) || StringUtil.isBlank(order.getTimestamp())	
		|| StringUtil.isBlank(order.getPaymentType()) || paymentType==0
		) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			request.setAttribute("MSG", map);
			return "error";
		}
		if(order.getAmount().compareTo(new BigDecimal(SystemConst.randomMoneyMax/100.0))<0) {
			map.put("code", StatusCode._321.status);
			map.put("msg", StatusCode._321.msg);
			request.setAttribute("MSG", map);
			return "error";
		}
		if(Payment.union_pay.type!=paymentType) {
			map.put("code", StatusCode._320.status);
			map.put("msg", StatusCode._320.msg);
			request.setAttribute("MSG", map);
			return "error";
		}
		UserMerchant umt=userMerchantService.selectByPrimaryKey(order.getMerchantUserId());
		if(umt==null || umt.getStatus()==0) {
			map.put("code", StatusCode._301.status);
			map.put("msg", StatusCode._301.msg);
			request.setAttribute("MSG", map);
			return "error";			
		}		
		paramap.put("merchantUserId", order.getMerchantUserId()+"");
		paramap.put("outTradeNo", order.getOutTradeNo());		
		paramap.put("currencyType", order.getCurrencyType());
		paramap.put("amount", order.getAmount()+"");		
		paramap.put("paymentType", order.getPaymentType());
		paramap.put("version", order.getVersion());
		paramap.put("subject", order.getSubject());
		paramap.put("body", order.getBody());
		paramap.put("timestamp", order.getTimestamp());		
		paramap.put("notifyUrl", order.getNotifyUrl());
		paramap.put("merchantAccount", order.getMerchantAccount());	
		String line=OrderUtil.createParaOrderStr(paramap);		
	    boolean isPass=RSAUtils.verifySignature(line, order.getSign(), umt.getPublicKey());
	    if(!isPass) {
			map.put("code", StatusCode._304.status);
			map.put("msg", StatusCode._304.msg);
			request.setAttribute("MSG", map);
			return "error";			
	    }
    	if(Idempotent.fx_randomMinusOnLineOrder(line)) {
			map.put("code", StatusCode._315.status);
			map.put("msg", StatusCode._315.msg);
			request.setAttribute("MSG", map);
			return "error";			
    	}
	    Currency c=currencyService.selectByPrimaryKey(order.getCurrencyType());
	    if(c==null) {
			map.put("code", StatusCode._303.status);
			map.put("msg", StatusCode._303.msg);
			request.setAttribute("MSG", map);	
			return "error";			
	    }	    
	    //随机减去几分钱
	    BigDecimal amount=order.getAmount().subtract(new BigDecimal(Tools.getRandomNumberInRange()/100.0));	 
	    amount=amount.setScale(SystemConst.decimal_place_money_num, BigDecimal.ROUND_HALF_UP);
	    order.setActualAmount(amount);    
	    order.setCurrencyAmount(amount.divide(c.getRate(),SystemConst.decimal_place_num, BigDecimal.ROUND_HALF_UP));
	    order.setExchangeRate(c.getRate());
    	String vtbTradeNo=OrderNumEngender.getOrderNum();
	    order.setTradeNumber(vtbTradeNo);
	    order.setStatus(OrdersStatus._0.status);
	    order.setCreateTime(new Date());
	    /**
	     * 随机分配订单给币商
	     */
	    int hour=(int)Math.pow(2, java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY));
	    List<ValidUserPayment> list=userWalletService.selectUserWalletByCurrencyAmountAndType(order.getCurrencyAmount(), SystemConst.random_limit, paymentType,hour);	    
	    if(list==null ||list.size()<1) {
			map.put("code", StatusCode._311.status);
			map.put("msg", "暂时无法匹配承兑商，请稍后重试");
			request.setAttribute("MSG", map);
			return "error";			
	    }
	    ValidUserPayment vup=list.get(ThreadLocalRandom.current().nextInt(list.size()));
	    long userId=vup.getUserId();	
	    usersService.lockUser(userId);
	    order.setUserId(userId);
	    order.setPayTimeout(SystemConst.Order_TimeOut);	    	   
	    order.setType(OrderType._0.type);
		if (!StringUtil.isBlank(order.getNotifyUrl())) {
			order.setNotifyStatus(NotifyStatus.notyet.status);
		}else {
			order.setNotifyStatus(NotifyStatus.notNofity.status);
		}		
    	PaymentMode p=paymentModeService.selectByPrimaryKey(vup.getPaymentId());
		p.setPaymentTypeName(Payment.getMeaningByType(p.getPaymentType()));    		  	
	    order.setPaymentNumber(p.getPaymentNumber());
	    if(p.getPaymentNumber()!=null && p.getPaymentNumber().length()>SystemConst.suffixNumber) {
	    	String paymentNumbe=p.getPaymentNumber().trim();
	    	String suffix=paymentNumbe.substring(paymentNumbe.length()-SystemConst.suffixNumber, paymentNumbe.length());
	    	order.setPaymentNumberSuffix(suffix);
	    }	
	    order.setPaymentTypeName(Payment.getMeaningByType((p.getPaymentType())));
    	order.setPaymentId(p.getPaymentId());
	    UserWallet shuw=userWalletService.selectByType(order.getMerchantUserId(), order.getCurrencyType());
	    if(shuw==null) {
			map.put("code", StatusCode._301.status);
			map.put("msg", StatusCode._301.msg);
			request.setAttribute("MSG", map);
			return "error";	
	    }
	    order.setMerchantRate(shuw.getInRate()); 
	    List<JournalAccount> journalAccountList=new ArrayList<JournalAccount>(1);
	    /**
	     * 冻结币商的币 。
	     */
	    UserWallet uw= userWalletService.findByLock(userId, order.getCurrencyType());
	    UserWallet userWallet=new UserWallet();
	    userWallet.setWalletId(uw.getWalletId());
	    userWallet.setCurrencyNumber(uw.getCurrencyNumber().subtract(order.getCurrencyAmount()));
	    userWallet.setFreezeNumber(uw.getFreezeNumber().add(order.getCurrencyAmount()));	    
	    userWalletService.updateByPrimaryKeySelective(userWallet);	    	    	    
	    journalAccountList.add(journalAccountService.logAccountChange(uw, userWallet, "客户下单USDT冻结："+order.getTradeNumber(), order.getCurrencyAmount(),JournalType.freeze.status,CurrencyType.USDT.status,order.getTradeNumber()));		    
	    order.setUnderwriterRate(uw.getInRate());	    
	    ordersService.insert(order);	   
	    journalAccountService.insertBatch(journalAccountList);
	    // 加入大转盘
	    Wheel.pushSlot(order.getTradeNumber());	 
	    /** 
	     *发短信 ，消息通知币商。 
	     */
	    /*Users user= usersService.selectByPrimaryKey(oldOrder.getUserId());	    
	    SMSUtil.sentPayRemind(user.getPhoneNumber(), oldOrder.getTradeNumber(),oldOrder.getPaymentTypeName(),oldOrder.getPaymentNumber(),oldOrder.getAmount()+""," "+SystemConst.CNY,DateTools.DateToStr2(new Date()));*/
	
		String sign=Md5Util.execute(order.getSign()+System.currentTimeMillis()+""+new java.util.Random().nextDouble());
	    OrderUtil.setOrderSign(order.getTradeNumber(), sign);	   
	    // 旋转收款账号
	    paymentRotateService.spinPaymentRotate(p.getPaymentId(), order.getActualAmount());
			
	    return "redirect:"+Tools.getUrlHead(request, "/api/onLineOrderPay/"+order.getTradeNumber()+"/"+sign);
	}
	/**
	 * 
	 * 跳到扫码页面
	 * @param tradeNumber
	 * @param sign
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/onLineOrderPay/{tradeNumber}/{sign}",method = RequestMethod.GET )
	public String onLineOrderPay(@PathVariable("tradeNumber") String tradeNumber,@PathVariable("sign") String sign
			,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<>();		
		Orders orders=ordersService.selectByPrimaryKey(tradeNumber);		
		if(orders!=null) {
			long surplusTime=(System.currentTimeMillis()-orders.getCreateTime().getTime())/1000;			
			if(surplusTime>SystemConst.Order_TimeOut || orders.getStatus()>OrdersStatus._1.status){
				map.put("code", StatusCode._201.status);
				map.put("msg", "订单已过期");
				request.setAttribute("MSG", map);
				return "error";					
			}
			request.setAttribute("amount", orders.getActualAmount());
			request.setAttribute("payTimeOut", (SystemConst.Order_TimeOut-surplusTime));
			request.setAttribute("paymentId", orders.getPaymentId());	
			request.setAttribute("tradeNumber", orders.getTradeNumber());
			request.setAttribute("sign", sign);
		}else {
			map.put("code", StatusCode._201.status);
			map.put("msg", StatusCode._201.msg);
			request.setAttribute("MSG", map);
			return "error";		
		}
		/**
		 * "orderSec2" 直接扫码版，orderSec跳转另一个网页后再扫码。
		 */
		return "orderSec";		
	}
	
}
