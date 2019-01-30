<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path=request.getContextPath();
String scheme="http";
if(request.getServerPort()==443) {
	scheme="https";
}
String basePath=scheme+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
request.setAttribute("basePath", basePath);
%>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>确认支付</title>
  <meta name="description" content="">
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <link rel="stylesheet" href="${basePath}/css/order/amazeui.min.css">
  <style type="text/css">
  	body {  	
  		background-color:#eee;
  	}
  	.center_btn {
  		background: #0e90d2;
  		border:1px solid #0e90d2;
  		color:#fff;
  		border-radius: 5px;
  	}
  	ul,li {
  		list-style: none;
  	}
  	.bgfff {
  		background: #fff;
  	}
  </style>
</head>
<body>
<div style="padding:20px 0;">
	<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered" style="color:#999;">
		充值单号：#<span class="otcPayNumber">${orderDetail.tradeNumber}</span><i class="am-icon-clone" style="font-size:12px;padding-left:10px;" data-clipboard-target=".otcPayNumber"></i>
	</div>
	<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered" style="margin-top:20px;margin-bottom: 20px;">
		<span style="font-size: 22px;font-weight: 600;">充值金额</span>
		<span style="color:red;font-weight:700;font-family: '微软雅黑';">￥${orderDetail.amount}</span>
	</div>
	<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered" style="display: flex;align-items:center;">
		<span style="width:120px;color:#999;">充值支付方式</span>
		<span style="border:1px solid #eee;width:80%;height:0px;display: block;"></span>
	</div>
	<ul class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
	<c:if test="${ orderDetail.paymentType=='UNIONPAY'}">
	  <li style="padding:10px;margin-bottom: 10px;" class="bgfff">
	  	<div style="display:flex;">
	  		<img src="${basePath}/images/order/img/yhk.png" alt="" style="width:30px;height:30px;margin-right:10px;"/>
		   	<div>
		   	 	<p>
		   	 		开户行：<span class="bankName">${paymentList[0].paymentName}</span><i class="am-icon-clone" style="font-size:12px;padding-left:10px;" data-clipboard-target=".bankName"></i>
		   	 	</p>
		   	 	<p>
		   	 		开户人：<span class="name">${paymentList[0].userName }</span><i class="am-icon-clone" style="font-size:12px;padding-left:10px;" data-clipboard-target=".name"></i>
		   	 	</p>
		   	 	<p>
		   	 		银行卡：<span class="bankCard">${paymentList[0].paymentNumber }</span><i class="am-icon-clone" style="font-size:12px;padding-left:10px;" data-clipboard-target=".bankCard"></i>
		   	 	</p>
		   	</div>
	  	</div>	  	
	  </li>
	</c:if>
	<c:if test="${ orderDetail.paymentType=='ALIPAY'}">		  
	  <li style="padding:10px;margin-bottom: 10px;" class="bgfff">
	  	<div style="display:flex;">
	  		<img src="${basePath}/images/order/img/zfb.png" alt="" style="width:30px;height:30px;margin-right:10px;"/>
	   	<div>
	   	 	<p>
	   	 		支付宝
	   	 	</p>
	   	 	<p>
	   	 		账号：<span class="zfbNumber">${paymentList[0].paymentNumber }</span><i class="am-icon-clone" style="font-size:12px;padding-left:10px;" data-clipboard-target=".zfbNumber"></i>
	   	 	</p>	   	 	
	   	</div>
	  	</div>
	  	
	   	<div>
	   		<img src="${paymentList[0].paymentImage}" alt="" style="max-width: 250px;display:block;margin: auto;"/>
	   	</div>
	  </li>
</c:if>
<c:if test="${ orderDetail.paymentType=='WEIPAY'}">	  
	  <li style="padding:10px;margin-bottom: 10px;" class="bgfff">
	  	<div style="display:flex;">
	  		<img src="${basePath}/images/order/img/wx.png" alt="" style="width:30px;height:30px;margin-right:10px;"/>
	   	<div>
	   	 	<p>
	   	 		微信
	   	 	</p>
	   	 	<p>
	   	 		账号：<span class="wxNumber">${paymentList[0].paymentNumber }</span><i class="am-icon-clone" style="font-size:12px;padding-left:10px;" data-clipboard-target=".wxNumber"></i>
	   	 	</p>	   	 	
	   	</div>
	  	</div>	  	
	   	<div>
	   		<img src="${paymentList[0].paymentImage}" alt="" style="max-width: 250px;display:block;margin: auto;"/>
	   	</div>
	  </li>
	  </c:if>
	</ul>
	<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
		<p style="color:red;font-weight: 600;font-size: 16px;">充值须知</p>
		<p style="padding-left:20px;color:red;font-weight: 600;font-size:14px;">请选择以上支付方式进行线下支付且付款金额和订单金额必须保持一致！支付完成后，请及时点击确认付款按钮，否则超时将会自动取消订单，造成充值失败（资金损失）</p>
	</div>
	<input type="hidden" name="time" id="time" value="" />
	<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
		待支付，请于<span style="color:red;font-weight: 600;" id="payTime">00分00秒</span>内线下支付<span style="color:red;font-weight: 600;font-family: '微软雅黑';">￥${orderDetail.amount}</span>
	</div>
	<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered" style="margin-top:20px;">
		<button type="button" class="am-btn am-btn-primary" style="border-radius: 5px;padding-left:40px;padding-right: 40px;font-size:20px;font-weight: 600;" onclick="openModal()">确认付款</button>
	</div>
</div>
<div class="am-modal am-modal-confirm" tabindex="-1" id="my-confirm">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">确认已付款</div>
    <div class="am-modal-bd" style="color:red;">
      是否已线下完成付款？
    </div>
    <div class="am-modal-footer">
      <span class="am-modal-btn" data-am-modal-cancel>取消</span>
      <span class="am-modal-btn" data-am-modal-confirm>确定</span>
    </div>
  </div>
</div>
<script src="${basePath}/scripts/order/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${basePath}/scripts/order/js/clipboard.js" type="text/javascript" charset="utf-8"></script>
<script src="${basePath}/scripts/order/js/amazeui.min.js"></script>
<script type="text/javascript">new ClipboardJS('.am-icon-clone');
	function openModal(){
		$('#my-confirm').modal({
        relatedTarget: this,
        onConfirm: function(options) {          
          pay();    
        },
        onCancel: function() {
          
        }
      });
	}	
	function pay(){
		 $.post("${basePath}/api/onlineOrderPay",
		    {
			 tradeNumber:"${orderDetail.tradeNumber}",
		     sign:"${sign}"
		    },
		    function(data,status){$(window).unbind('beforeunload');
		    	if(data.code==200){
		    		window.location="${basePath}/api/success";
		    	}else{
		    		window.location="${basePath}/api/error?code="+data.code+"&msg="+data.msg;
		    	}		     
		});
	}
	var timer = null;
	var differMs = ${orderDetail.payTimeout};//相差的毫秒数
	var currentTime = differMs;
$(function(){
		  if (timer) return ;		 
			//开启定时器
			timer = setInterval(function(){		
				currentTime--;
				var minutes =parseInt(currentTime/60);//分钟
				var seconds = parseInt(currentTime) - parseInt(minutes*60);
				minutes = minutes<10?"0"+minutes:minutes;
				seconds = seconds<10?"0"+seconds:seconds;				
				$("#payTime").html(minutes+"分"+ seconds+"秒");							
				if (currentTime < 1) {$(window).unbind('beforeunload');
					window.location="${basePath}/api/error?code=201"+"&msg=交易取消";
				    clearInterval(timer);
				    timer = null;
				    currentTime = differMs;			   
			  }				
			},1000);
})	
$(window).bind('beforeunload',function(){return '如果已经付款务必要确认付款，否则15分钟订单将会取消，造成资金流失，而客户不上分的风险，确定离开此页面吗？';});
</script>
</body>
</html>