package com.contactsImprove.service.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.contactsImprove.entity.api.OrderQueryPara;
import com.contactsImprove.entity.api.Orders;

public interface OrdersService {
	
    int deleteByPrimaryKey(String tradeNumber);

    int insert(Orders record);

    int insertSelective(Orders record);

    Orders selectByPrimaryKey(String tradeNumber);

    int updateByPrimaryKeySelective(Orders record);

    Integer updateOrderTimeoutById(String orderIds);

    List<Orders> refineTimeoutOrder(Integer timeOut);
    
    List<Orders> selectOrderByPage(OrderQueryPara oqp);
    
    List<HashMap<String,Object>> selectSumOrderByPara(OrderQueryPara oqp);

    List<Orders> selectByOrderList(Orders record, String phoneNumber);
    
    int updateBatchNotifyStatus(List<Orders> orderList);
    
    List<Object> selectOrderCountByYear(Date date);
    
    List<Orders> selectOrderByPhoneCallback(OrderQueryPara orders);
    
    List<Object> selectOrdercount();
    
    List<Object> selectJournalAccountEcharts(Date date);
    
    List<Object> selectOrderTotal(Orders orders);
    
    List<Orders> selectListByOrderId(String orderIds);
    
    List<Orders> selectOrderByMerchantUserId(Long merchantUserId,String outTradeNo);
    
    HashMap<String,Object> financeStatistics(Orders record,String phoneNumber);
    
    List<Orders> selectInOrderList(Orders record,String phoneNumber);
}
