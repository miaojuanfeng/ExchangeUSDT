package com.contactsImprove.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.contactsImprove.constant.MerchantOrderConst.NotifyStatus;
import com.contactsImprove.constant.MerchantOrderConst.OrderType;
import com.contactsImprove.constant.MerchantOrderConst.OrdersStatus;
import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.entity.admin.Notionalpooling;
import com.contactsImprove.entity.api.JournalAccount;
import com.contactsImprove.entity.api.Orders;
import com.contactsImprove.entity.api.UserWallet;
import com.contactsImprove.service.admin.NotionalpoolingService;
import com.contactsImprove.service.api.JournalAccountService;
import com.contactsImprove.service.api.OrdersService;
import com.contactsImprove.service.api.UserWalletService;

@Service("uSDTUtils")
public class USDTUtils {

	@Autowired
	private NotionalpoolingService notionalPoolingService;

	@Autowired
	private UserWalletService userWalletService;
	
	@Autowired
	private JournalAccountService journalAccountService;
	
	@Autowired
	private OrdersService ordersService;

	// 警告  正式环境账户请勿泄露，否则会造成币丢失。
	public static String RPC_USER = "otcpay";// 钱包用户名
	public static String RPC_PASSWORD = "AvBw2hZ4";// 钱包密码
	public String RPC_ALLOWIP = "127.0.0.1";// 钱包地址
	public String RPC_PORT = "19002";// 端口号
	
	private static String RESULT = "result";
	private String URL = "https://bitcoinfees.earn.com/api/v1/fees/recommended";
	
	private String companyAccount = "1FEXHwbD7tYDGRnZJKnj86wtJ5pHH6aWLz";// 公司总账户
	
	/**
	 * 取得钱包相关信息
	 * @return
	 * @throws Exception
	 */
	public JSONObject getInfo() {
		JSONObject json = doRequest("omni_getinfo");
		return json;
	}

	/**
	 * 创建账号
	 * @return
	 */
	public String getNewAddress() {
		JSONObject json = doRequest("getnewaddress");
		return json.getString(RESULT);
	}

	/**
	 * 查询余额
	 * @param address 地址
	 * @return
	 */
	public String getBalance(String address) {
		JSONObject json = doRequest("omni_getbalance", address, 31);
		return json.getString(RESULT);
	}
	
	/**
	 * 获取地址私钥（!!!切勿泄露私钥）
	 * @param address
	 * @return
	 */
	public String dumpprivkey(String address) {
		JSONObject json = doRequest("dumpprivkey", address);
		return json.getString(RESULT);
	}

	/**
	 * 根据hash获取交易信息
	 * @param txid
	 * @return
	 */
	public JSONObject getgettransaction(String txid) {
		JSONObject json = doRequest("omni_gettransaction", txid);
		return json;
	}

	/**
	 * 验证地址
	 * @param address
	 * @return
	 */
	public boolean vailedAddress(String address) {
		JSONObject json = doRequest("validateaddress", address);
		if (isError(json)) {
			return false;
		} else {
			return json.getJSONObject(RESULT).getBoolean("isvalid");
		}
	}

	/**
	 * 查询区块高度
	 * @return
	 */
	public int getBlockCount() {
		JSONObject json = null;
		try {
			json = doRequest("getblockcount");
			if (!isError(json)) {
				return json.getInteger(RESULT);
			} else {
				return 0;
			}
		} catch (Exception e) {
			LoggerUtil.error(e.toString(), e);
			return 0;
		}
	}
	
	/**
	 * 扫描区块
	 * @param index
	 * @return
	 */
	@Transactional
	public boolean parseBlock(int index) {
		try {
			JSONObject jsonBlock = doRequest("omni_listblocktransactions", index);
			if (isError(jsonBlock)) {
				return false;
			}
			
			JSONArray jsonArrayTx = jsonBlock.getJSONArray(RESULT);
			if (jsonArrayTx != null && jsonArrayTx.size() > 0) {
				Iterator<Object> iteratorTxs = jsonArrayTx.iterator();
				while (iteratorTxs.hasNext()) {
					String txid = (String) iteratorTxs.next();
					JSONObject jsonTransaction = doRequest("omni_gettransaction", txid);
					if (isError(jsonTransaction)) {// 错误的返回信息
						continue;
					}
					
					JSONObject jsonTResult = jsonTransaction.getJSONObject(RESULT);
					if (!jsonTResult.getBoolean("valid")) { // 转账失败
						continue;
					}
					
					int propertyidResult = jsonTResult.getIntValue("propertyid");
					if (propertyidResult != 31) { // 非USDT数据
						continue;
					}

					double value = jsonTResult.getDouble("amount");// 金额
					if (value > 0) {
						String sendaddress = jsonTResult.getString("sendingaddress");// 发送地址
						String referenceaddress = jsonTResult.getString("referenceaddress");// 接收地址

						List<UserWallet> list = userWalletService.selectUserWallerList();
						for (UserWallet u : list) {
							if (referenceaddress != null && referenceaddress.equals(u.getWalletAddress())) {
								// 给用户加上充值的币
								UserWallet uw = new UserWallet();
								uw.setWalletId(u.getWalletId());
								uw.setCurrencyNumber(u.getCurrencyNumber().add(new BigDecimal(value)));
								userWalletService.updateByPrimaryKeySelective(uw);
								
								Orders orders = new Orders();
								orders.setTradeNumber(OrderNumEngender.getOrderNum());
								orders.setUserId(u.getUserId());
								orders.setCurrencyType(SystemConst.USDT);
								orders.setCurrencyAmount(new BigDecimal(value));
								orders.setFromAddress(sendaddress);
								orders.setToAddress(referenceaddress);
								orders.setType(OrderType._3.type);
								orders.setStatus(OrdersStatus._2.status);
								orders.setCloseTime(new Date());
								orders.setSubject("USDT充值");
								orders.setBody("币商充值");
								orders.setNotifyStatus(NotifyStatus.notNofity.status);
								ordersService.insertSelective(orders);
								
								//记录充值流水账
								JournalAccount ja = new JournalAccount();
								ja.setUserId(u.getUserId());
								ja.setChangeAmount(new BigDecimal(value));
								ja.setCurrencyNumberBefore(u.getCurrencyNumber());
								ja.setCurrencyNumberAfter(u.getCurrencyNumber().add(new BigDecimal(value)));
								ja.setFreezeNumberBefore(u.getFreezeNumber());
								ja.setFreezeNumberAfter(u.getFreezeNumber());
								ja.setCreateTime(new Date());
								ja.setRemark("币商-USDT充值");
								ja.setType((byte)3);
								journalAccountService.insertSelective(ja);

								// 新增归集表
								Notionalpooling np = new Notionalpooling();
								np.setSendingAddress(sendaddress);
								np.setReferenceAddress(referenceaddress);
								np.setAmount(new BigDecimal(value));
								np.setStatus(0);
								notionalPoolingService.insertSelective(np);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LoggerUtil.error(e.toString(), e);
		}
		return false;
	}

	/**
	 * 0.3.1版本归集
	 * @return
	 */
	@Transactional
	public void USDTTransaction() {
		try {
			List<Notionalpooling> list = notionalPoolingService.selectListBystatus();
			for (Notionalpooling n : list) {
				if (!n.getReferenceAddress().equals(companyAccount)) {
					JSONObject signrawresult = doRequest("omni_funded_send", n.getReferenceAddress(), companyAccount,
							31, n.getAmount(), companyAccount);
					Notionalpooling notionalPooling = new Notionalpooling();
					if (!isError(signrawresult)) {
						String rs = signrawresult.getString(RESULT);
						if (rs != null) {
							notionalPooling.setId(n.getId());
							notionalPooling.setStatus(1);// 状态 : 1、成功
							notionalPooling.setCreateTime(new Date());// 归集时间
							notionalPoolingService.updateByPrimaryKeySelective(notionalPooling);
						}
					}
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LoggerUtil.error(e.toString(), e);
		}
	}
	
	/**
	 * 手续费计算
	 * @return
	 */
	public BigDecimal fee() {
		String result = HttpUtil.get(URL);
		JSONObject feejson = JSONObject.parseObject(result);
		BigDecimal fee = (BigDecimal.valueOf(1 * 148 + 34 * 2 + 10))
				.multiply(new BigDecimal(feejson.getInteger("fastestFee")))
				.divide(new BigDecimal("100000000"), 8, RoundingMode.HALF_UP);
		return fee;
	}
	
	public static void main(String[] args) {
		String result = HttpUtil.get("https://bitcoinfees.earn.com/api/v1/fees/recommended");
		JSONObject feejson = JSONObject.parseObject(result);
		BigDecimal fee = (BigDecimal.valueOf(1 * 148 + 34 * 2 + 10))
				.multiply(new BigDecimal(feejson.getInteger("fastestFee")))
				.divide(new BigDecimal("100000000"), 8, RoundingMode.HALF_UP);
		System.out.println(fee);
	}
	
	/**
	 * 原始交易归集
	 * @param sendAddress
	 * @param value
	 * @return
	 */
	@Transactional
	public void Transaction() {
		try {
			JSONObject json = doRequest("listunspent");
			if (!isError(json)) {
				List<Notionalpooling> list = notionalPoolingService.selectListBystatus();
				JSONArray spentarr = json.getJSONArray(RESULT);
				
				JSONObject out1 =  new JSONObject();
				JSONObject out2 = new JSONObject();
				for (int i = 0; i < spentarr.size(); i++) {
					JSONObject obj = spentarr.getJSONObject(i);
					String address = obj.getString("address");
					
					String txid = obj.getString("txid");
					int vout = obj.getInteger("vout");
					Double amount = obj.getDouble("amount");
					String scriptPubKey = obj.getString("scriptPubKey");
					
					if(address.equals(companyAccount)) {
						if(out1.size() == 0) {
							out1.put("txid", txid);
							out1.put("vout", vout);
						}
						
						if(out2.size() == 0) {
							out2.put("txid", txid);
							out2.put("vout", vout);
							out2.put("scriptPubKey", scriptPubKey);
							out2.put("value", amount);
						} 
					}
					if (address != null && !address.equals(companyAccount)) {// 地址为空、公司自己充值都不进行归集
						for (Notionalpooling n : list) {
							
							 if (n.getReferenceAddress().equals(address)) {
								BigDecimal svalue = notionalPoolingService.selectCountByreFerenceAddress(address);

								JSONArray putParam1 = new JSONArray();
								JSONObject put1 = new JSONObject();
								put1.put("txid", txid);
								put1.put("vout", vout);
								putParam1.add(put1);
								putParam1.add(out1);

								JSONArray putParam2 = new JSONArray();
								JSONObject put2 = new JSONObject();
								put2.put("txid", txid);
								put2.put("vout", vout);
								put2.put("scriptPubKey", scriptPubKey);
								put2.put("value", amount);
								putParam2.add(put2);
								putParam2.add(out2);

								String transactionResult = doRequest("createrawtransaction", putParam1, new JSONObject())
										.getString(RESULT);

								String simplesendResult = doRequest("omni_createpayload_simplesend", 31, svalue.toString())
										.getString(RESULT);

								String opreturnResult = doRequest("omni_createrawtx_opreturn", transactionResult,
										simplesendResult).getString(RESULT);

								String referenceResult = doRequest("omni_createrawtx_reference", opreturnResult,
										companyAccount).getString(RESULT);

								String changeResult = doRequest("omni_createrawtx_change", referenceResult, putParam2,
										companyAccount, fee().toString()).getString(RESULT);

								String signrawResult = JSONObject
										.parseObject(doRequest("signrawtransaction", changeResult).getString(RESULT))
										.getString("hex");

								String rs = doRequest("sendrawtransaction", signrawResult).getString(RESULT);
								if (rs != null) {
									System.out.println("---归集成功---：" + rs);
									Notionalpooling notionalPooling = new Notionalpooling();
									notionalPooling.setStatus(1);// 状态 : 1、成功
									notionalPooling.setCreateTime(new Date());// 归集时间
									if(svalue.compareTo(n.getAmount()) == 0) {
										notionalPooling.setId(n.getId());
										notionalPoolingService.updateByPrimaryKeySelective(notionalPooling);
									} else {
										notionalPooling.setReferenceAddress(address);
										notionalPoolingService.updateByreferenceAddress(notionalPooling);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LoggerUtil.error(e.toString(), e);
		}
	}

	/**
	 * 转账 转账账户必须有足够的BTC才能使用此方法
	 * @param senderaddr 发送者地址
	 * @param Recipientaddr 接收者地址
	 * @param value  数量
	 * @return
	 */
	public Map<String, Object> send(String senderaddr, String Recipientaddr, String value) {		
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		if (vailedAddress(Recipientaddr)) {
			try {
				JSONObject json = doRequest("omni_send", companyAccount, Recipientaddr, 31, value);
				if (isError(json)) {
					resultMap.put("code", 201);
					resultMap.put("msg", "转账失败");
					return resultMap;
				}
				resultMap.put("code", 200);
				resultMap.put("msg", "转账成功");
				resultMap.put("data", json.getString(RESULT));
				return resultMap;
			} catch (Exception e) {
				LoggerUtil.error(e.toString(), e);
				e.printStackTrace();
				resultMap.put("code", 201);
				resultMap.put("msg", "转账失败");
				return resultMap;
			}
		} else {
			resultMap.put("code", 244);
			resultMap.put("msg", "地址不正确");
			resultMap.put("data", null);
			return resultMap;
		}
	}

	/**
	 * 发送请求
	 * @param method
	 * @param params
	 * @return
	 */
	private JSONObject doRequest(String method, Object... params) {
		try {
			JSONObject param = new JSONObject();
			param.put("id", System.currentTimeMillis() + "");
			param.put("jsonrpc", "2.0");
			param.put("method", method);
			if (params != null) {
				param.put("params", params);
			}
			String creb = Base64.encodeBase64String((RPC_USER + ":" + RPC_PASSWORD).getBytes());
			Map<String, String> headers = new HashMap<>(2);
			headers.put("Authorization", "Basic " + creb);
			String resp = "";
			if ("omni_gettransaction".equals(method)) {

				resp = HttpUtil.jsonPost("http://" + RPC_ALLOWIP + ":" + RPC_PORT, headers, param.toJSONString());
			} else {
				resp = HttpUtil.jsonPost("http://" + RPC_ALLOWIP + ":" + RPC_PORT, headers, param.toJSONString());
			}
			return JSON.parseObject(resp);
		} catch (Exception e) {
			LoggerUtil.error(e.toString(), e);
			return null;
		}
	}

	private boolean isError(JSONObject json) {
		if (json == null || (StringUtils.isNotEmpty(json.getString("error")) && json.get("error") != "null")) {
			return true;
		}
		return false;
	}
}