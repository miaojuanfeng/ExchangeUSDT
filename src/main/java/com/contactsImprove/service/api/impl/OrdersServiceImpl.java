package com.contactsImprove.service.api.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.contactsImprove.constant.MerchantOrderConst.Payment;
import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.constant.UsersConst.UsersType;
import com.contactsImprove.dao.api.OrdersDao;
import com.contactsImprove.entity.api.OrderQueryPara;
import com.contactsImprove.entity.api.Orders;
import com.contactsImprove.notefield.DynamicColumn;
import com.contactsImprove.service.api.OrdersService;
import com.contactsImprove.utils.DateTools;
import com.contactsImprove.utils.StringUtil;

@Service("ordersService")
public class OrdersServiceImpl implements OrdersService {
	
	@Autowired
	OrdersDao ordersDao;

	@Override
	public int deleteByPrimaryKey(String tradeNumber) {
		// TODO Auto-generated method stub
		return ordersDao.deleteByPrimaryKey(tradeNumber);
	}

	@Override
	public int insert(Orders record) {
		// TODO Auto-generated method stub
		return ordersDao.insertSelective(record);
	}

	@Override
	public int insertSelective(Orders record) {
		// TODO Auto-generated method stub
		return ordersDao.insertSelective(record);
	}

	@Override
	public Orders selectByPrimaryKey(String tradeNumber) {
		// TODO Auto-generated method stub
		return ordersDao.selectByPrimaryKey(tradeNumber);
	}

	@Override
	public int updateByPrimaryKeySelective(Orders record) {
		// TODO Auto-generated method stub
		return ordersDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public Integer updateOrderTimeoutById(String orderIds) {
		// TODO Auto-generated method stub
		return ordersDao.updateOrderTimeoutById(orderIds);
	}

	@Override
	public List<Orders> refineTimeoutOrder(Integer timeOut) {
		// TODO Auto-generated method stub
		return ordersDao.refineTimeoutOrder(timeOut);
	}
	
	@Override
	public List<Orders> selectOrderByPage(OrderQueryPara oqp) {
		StringBuilder sb=new StringBuilder();		
		sb.append("select  trade_number, out_trade_no, merchant_user_id, user_id, merchant_account, payment_id," + 
				"  payment_type_name, payment_number, payment_name,currency_type, currency_amount, exchange_rate, " + 
				"  amount, type,from_address,to_address, status,deal_type, create_time, payment_time, close_time, version, subject, " + 
				"  body,ext from orders");		
		if(oqp.getUserType()==UsersType._1.status) {
			sb.append(" where merchant_user_id="+oqp.getUserId());
		}else if(oqp.getUserType()==UsersType._2.status) {
			sb.append(" where user_id="+oqp.getUserId());
		}
		DynamicColumn.appendWhere(sb, oqp);		
		if(!StringUtil.isBlank(oqp.getPaymentType())) {
			String name=Payment.getMeaningByCode(oqp.getPaymentType());
			if(!StringUtil.isBlank(name)) {
				sb.append(" and payment_type_name='"+name+"'");
			}			
		}		
		long start=0,end=0;
		if(!StringUtil.isBlank(oqp.getStartTime())) {
			Date s=DateTools.StrToDate(oqp.getStartTime());
			if(s!=null) {
				start=s.getTime();
			}
		}
		if(!StringUtil.isBlank(oqp.getEndTime())) {
			Date e=DateTools.StrToDate(oqp.getEndTime());
			if(e!=null){
				end=e.getTime();
			}
		}
		if(end>start) {
			switch(oqp.getTimeType()) {
			case 0:
				if(start>0)
				sb.append(" and create_time>='"+oqp.getStartTime()+"'");
				if(end>0)
				sb.append(" and create_time<'"+oqp.getEndTime()+"'");
				break;
			case 1:
				if(start>0)
				sb.append(" and payment_time>='"+oqp.getStartTime()+"'");
				if(end>0)
				sb.append(" and payment_time<'"+oqp.getEndTime()+"'");			
				break;
			case 2:
				if(start>0)
				sb.append(" and close_time>='"+oqp.getStartTime()+"'");
				if(end>0)
				sb.append(" and close_time<'"+oqp.getEndTime()+"'");				
				break;			
			}	
		}				
		sb.append(" order by create_time desc");
		oqp.setQueryStr(sb.toString());

		return ordersDao.selectPageByUserId(oqp);
	}
	
	@Override
	public List<Orders> selectByOrderList(Orders record,String phoneNumber) {
		StringBuilder queryStr = new StringBuilder("select o1.trade_Number,o1.out_trade_no,o1.merchant_user_id,o1.user_id,o1.payment_name,o1.payment_type_name,o1.payment_number,o1.currency_amount,o1.amount,o1.actual_amount,o1.status,o1.deal_type," + 
				"o1.notify_status,o1.`subject`,o1.body,o1.create_time,o1.payment_time,o1.close_time,u2.user_name from orders o1 INNER JOIN orders o2 ON o1.trade_number=o2.trade_number LEFT JOIN users u1 on u1.user_id=o1.merchant_user_id LEFT JOIN users u2 on o2.user_id=u2.user_id");
		if(record.getTypes()!=null && record.getTypes().equals("13")) {
			queryStr.append(" where o1.type in('1','3')");
		}
		DynamicColumn.appendWhere(queryStr, record);			
		if(!StringUtil.isBlank(phoneNumber)) {
			if(queryStr.indexOf("where")==-1) {
				queryStr.append(" where u1.phone_number =" + phoneNumber);
			}else {
				queryStr.append(" and u1.phone_number =" + phoneNumber);
			}			
		}						
		queryStr.append(" order by o1.create_time desc");
		record.setQueryStr(queryStr.toString());
		return ordersDao.selectByOrderList(record);
	}
	@Override
	public List<Orders> selectInOrderList(Orders record,String phoneNumber) {
		StringBuilder queryStr = new StringBuilder("select o1.trade_Number,o1.out_trade_no,o1.merchant_user_id,o1.user_id,o1.payment_name,o1.payment_type_name,o1.payment_number,o1.currency_amount,o1.amount,o1.actual_amount,o1.status,o1.deal_type," + 
				"o1.notify_status,o1.`subject`,o1.body,o1.create_time,o1.payment_time,o1.close_time,u2.user_name from orders o1 INNER JOIN orders o2 ON o1.trade_number=o2.trade_number LEFT JOIN users u1 on u1.user_id=o1.merchant_user_id LEFT JOIN users u2 on o2.user_id=u2.user_id");
		queryStr.append(" where o1.type = 0 and o1.status=2");
		DynamicColumn.appendWhere(queryStr, record);			
		if(!StringUtil.isBlank(phoneNumber)) {
			queryStr.append(" and u2.phone_number =" + phoneNumber);					
		}						
		queryStr.append(" order by o1.create_time desc");
		record.setQueryStr(queryStr.toString());
		return ordersDao.selectByOrderList(record);
	}
	@Override
	public HashMap<String,Object> financeStatistics(Orders record,String phoneNumber){
		StringBuilder queryStr = new StringBuilder("select count(o1.trade_number) as totalNum,sum(o1.amount) as totalAmount,sum(o1.actual_amount) as totalActualAmount,sum(o1.actual_currency_amount) as totalActualCurrencyAmount,sum(o1.currency_amount) as totalCurrencyAmount,sum(o1.poundage) as totalPoundage from orders o1 INNER JOIN orders o2 ON o1.trade_number=o2.trade_number LEFT JOIN users u1 on u1.user_id=o1.merchant_user_id LEFT JOIN users u2 on o2.user_id=u2.user_id");
		queryStr.append(" where o1.type = 0 and o1.status=2");
		DynamicColumn.appendWhere(queryStr, record);			
		if(!StringUtil.isBlank(phoneNumber)){
			queryStr.append(" and u2.phone_number ='" + phoneNumber+"'");			
		}
		record.setQueryStr(queryStr.toString());		
		return ordersDao.financeStatistics(queryStr.toString());
	}

	@Override
	public int updateBatchNotifyStatus(List<Orders> orderList) {
		// TODO Auto-generated method stub
		return ordersDao.updateBatchNotifyStatus(orderList);
	}

	@Override
	public List<Object> selectOrderCountByYear(Date date) {
		return ordersDao.selectOrderCountByYear(date);
	}
	
	@Override
	public List<Object> selectJournalAccountEcharts(Date date) {
		return ordersDao.selectJournalAccountEcharts(date);
	}

	@Override
	public List<Orders> selectListByOrderId(String orderIds) {
		// TODO Auto-generated method stub
		return ordersDao.selectListByOrderId(orderIds);
	}

	@Override
	public List<Object> selectOrdercount() {
		return ordersDao.selectOrdercount();
	}

	@Override
	public List<Object> selectOrderTotal(Orders orders) {
		return ordersDao.selectOrderTotal(orders);
	}

	@Override
	public List<HashMap<String, Object>> selectSumOrderByPara(OrderQueryPara oqp) {
		StringBuilder sb=new StringBuilder();		
		sb.append("select sum(amount) as totalAmount,type from orders");
		if(oqp.getUserType()==UsersType._1.status) {
			sb.append(" where merchant_user_id="+oqp.getUserId());
		}else if(oqp.getUserType()==UsersType._2.status) {
			sb.append(" where user_id="+oqp.getUserId());
		}	
		DynamicColumn.appendWhere(sb, oqp);		
		if(!StringUtil.isBlank(oqp.getPaymentType())) {
			String name=Payment.getMeaningByCode(oqp.getPaymentType());
			if(!StringUtil.isBlank(name)) {
				sb.append(" and payment_type_name='"+name+"'");
			}			
		}		
		long start=0,end=0;
		if(!StringUtil.isBlank(oqp.getStartTime())) {
			Date s=DateTools.StrToDate(oqp.getStartTime());
			if(s!=null) {
				start=s.getTime();
			}
		}
		if(!StringUtil.isBlank(oqp.getEndTime())) {
			Date e=DateTools.StrToDate(oqp.getEndTime());
			if(e!=null){
				end=e.getTime();
			}
		}
		if(end>start) {
			switch(oqp.getTimeType()) {
			case 0:
				if(start>0)
				sb.append(" and create_time>='"+oqp.getStartTime()+"'");
				if(end>0)
				sb.append(" and create_time<'"+oqp.getEndTime()+"'");
				break;
			case 1:
				if(start>0)
				sb.append(" and payment_time>='"+oqp.getStartTime()+"'");
				if(end>0)
				sb.append(" and payment_time<'"+oqp.getEndTime()+"'");			
				break;
			case 2:
				if(start>0)
				sb.append(" and close_time>='"+oqp.getStartTime()+"'");
				if(end>0)
				sb.append(" and close_time<'"+oqp.getEndTime()+"'");				
				break;			
			}	
		}				
		sb.append(" GROUP BY type");
		if(sb.indexOf("#")>-1 || sb.indexOf(";")>-1) {
			return null;
		}else {
			return ordersDao.selectSumOrderByPara(sb.toString());
		}
	}

	@Override
	public List<Orders> selectOrderByMerchantUserId(Long merchantUserId, String outTradeNo) {
		// TODO Auto-generated method stub
		return ordersDao.selectOrderByMerchantUserId(merchantUserId, outTradeNo);
	}

	@Override
	public List<Orders> selectOrderByPhoneCallback(OrderQueryPara orders) {
		// TODO Auto-generated method stub			
		StringBuilder sb=new StringBuilder();	
		sb.append("select * from orders");
		sb.append(" where status<2 and type=0");
		sb.append(" and payment_number_suffix='"+orders.getPaymentNumberSuffix()+"'");		
		sb.append(" and actual_amount="+orders.getAmount());
		sb.append(" and amount="+orders.getAmount());
		sb.append(" and payment_name='"+orders.getPaymentName()+"'");		
		Date end=new Date();
		Date Start=new Date(end.getTime()-SystemConst.FourHourMillisecond);
		orders.setStartTime(DateTools.DateToStr2(Start));
		orders.setEndTime(DateTools.DateToStr2(end));
		orders.setPriorMinus((byte)0);
		int dot=orders.getAmount().indexOf(".");
		if(dot>-1) {
			String amount=orders.getAmount().substring(dot+1,orders.getAmount().length());
			if(amount.length()>1) {
				if(Integer.parseInt(amount)>0) {
					orders.setPriorMinus((byte)1);
				}
			}				
		}			
		if(sb.indexOf("#")>-1 || sb.indexOf(";")>-1) {
			return null;
		}else {
			return ordersDao.selectOrderByPhoneCallback(orders);
		}
	}
}
