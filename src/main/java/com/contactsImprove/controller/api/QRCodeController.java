package com.contactsImprove.controller.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactsImprove.constant.MerchantOrderConst.OrdersStatus;
import com.contactsImprove.constant.ResponseCode.StatusCode;
import com.contactsImprove.constant.SystemConst;
import com.contactsImprove.entity.api.Orders;
import com.contactsImprove.entity.api.PaymentMode;
import com.contactsImprove.service.api.OrdersService;
import com.contactsImprove.service.api.PaymentModeService;
import com.contactsImprove.utils.LoggerUtil;
import com.contactsImprove.utils.OrderUtil;
import com.contactsImprove.utils.QRCode;
import com.contactsImprove.utils.Tools;

@Controller
@RequestMapping("/api")
public class QRCodeController {
	@Autowired
	PaymentModeService paymentModeService;
	@Autowired
	OrdersService ordersService;	
	
	byte[] lock=new byte[0];
	String alipayUrl="https://ds.alipay.com/?from=mobilecodec&scheme=alipays%3A%2F%2Fplatformapi%2Fstartapp%3FsaId%3D10000007%26qrcode=";
	
	/**
	 * 生成收款二维码跳转版
	 * @param payUrl
	 * @param response
	 */
	@RequestMapping(value = "/qrcodeRedirect/{tradeNumber}/{sign}",method=RequestMethod.GET)
	public void qrcodeRedirect(@PathVariable("tradeNumber") String tradeNumber,@PathVariable("sign") String sign,
			HttpServletRequest request,HttpServletResponse response){
		initQrCode(request);
		try {
			QRCode.createQRCode(alipayUrl+Tools.getUrlHead(request, "/api/gotoQrCodePay/"+tradeNumber+"/"+sign), response.getOutputStream());
		} catch (IOException e) {
			LoggerUtil.debug(e.getMessage(), e);
		}							
	}
	
	/**
	 * 生成收款二维码直接扫码版
	 * @param payUrl
	 * @param response
	 */
	@RequestMapping(value = "/qrcode/{paymentId}/{amount}",method=RequestMethod.GET)
	public void qrcode(@PathVariable("paymentId")Long paymentId,@PathVariable("amount")String amount,
			HttpServletRequest request,HttpServletResponse response){
		initQrCode(request);
		if(paymentId>0){
			PaymentMode pm=paymentModeService.selectByPrimaryKey(paymentId);
			try {
				QRCode.createQRCode(qrCodeString( pm, amount), response.getOutputStream());
			} catch (IOException e) {
				LoggerUtil.error(e.toString(), e);
			}					
		}
	}	

	/**
	 * 到达收款二维码页面
	 * @param payUrl
	 * @param response
	 */
/*	@RequestMapping(value ="/goQrCodePayqrcodePay",method=RequestMethod.GET)
	public String qrcodePay(@RequestParam("tradeNumber")String tradeNumber,@RequestParam("sign")String sign,*/
	@RequestMapping(value = "/gotoQrCodePay/{tradeNumber}/{sign}",method=RequestMethod.GET)
	public String gotoQrCodePay(@PathVariable("tradeNumber") String tradeNumber,@PathVariable("sign") String sign,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
//		String memSign= OrderUtil.hasOrderSign(tradeNumber);
//	    if(!memSign.equals(sign)) {
//			map.put("code", StatusCode._304.status);
//			map.put("msg", StatusCode._304.msg);
//			request.setAttribute("MSG", map);
//			return "error";	
//	    }
		Orders orders=ordersService.selectByPrimaryKey(tradeNumber);
		if(orders==null || orders.getStatus()>OrdersStatus._1.status){
			map.put("code", StatusCode._319.status);
			map.put("msg", StatusCode._319.msg);
			request.setAttribute("MSG", map);
			return "error";	
		}		
		if(orders.getPaymentId()>0){
			PaymentMode pm=paymentModeService.selectByPrimaryKey(orders.getPaymentId());
			//扫码点击支付。
			request.setAttribute("payUrl", qrCodeString(pm,orders.getActualAmount()+""));
			//生成二维码图片链接
//			request.setAttribute("payUrl", Tools.getUrlHead(request, "/api/qrcode/"+orders.getPaymentId()+"/"+orders.getActualAmount()));
		}
		request.setAttribute("payTimeOut", SystemConst.Order_TimeOut-((System.currentTimeMillis()-orders.getCreateTime().getTime())/1000));
		request.setAttribute("tradeNumber", tradeNumber);
		request.setAttribute("sign", sign);
		request.setAttribute("amount", orders.getActualAmount());
		return "orderSec";	
	}	
	
	String qrCodeString(PaymentMode pm,String amount) {
		StringBuilder sb=new StringBuilder();
		sb.append(SystemConst.payUrl);
		sb.append("&cardNo="+pm.getPaymentNumber());
		sb.append("&bankAccount="+pm.getUserName());
		sb.append("&money="+amount);
		sb.append("&amount="+amount);
		sb.append("&bankMark="+pm.getBankMark());
		sb.append("&bankName="+pm.getPaymentName());
		sb.append("&cardIndex=&cardChannel=HISTORY_CARD&cardNoHidden=true");
		return sb.toString();
	}
	void initQrCode(HttpServletRequest request) {
		if(QRCode.logo==null) {
			synchronized(lock) {
				if(QRCode.logo==null) {
					String alipayLogo=request.getSession().getServletContext().getRealPath("");
					File f=new File(alipayLogo,"images/order/img/alipay.png");
					QRCode.init(f);
					f=null;
				}
			}			
		}
	}
}
