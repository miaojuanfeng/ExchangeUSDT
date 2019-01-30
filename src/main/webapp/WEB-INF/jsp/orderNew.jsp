<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
  	.colorGreen {
		color: green;
		font-weight: 700;
	}			
	.colorRed {
		color: red;
		font-weight: 700;				
	}
  </style>
</head>
<body>
<div style="padding:20px 0;border-radius: 10px;margin:10px;" class="bgfff">
	<ul class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
	  <li style="padding:10px;margin-bottom: 10px;">
	  	<div>
	  		<p>第1步（共2步）：<span style="font-weight: 800;font-size: 18px;">确认付款信息</span></p>
		   	<div style="background: #3BB4F2;padding:20px;border-radius: 10px;color:#000;">
		   	 	<p>
		   	 		购买数量：<span class="bankName" style="margin-left:20px;">${orderDetail.currencyAmount}</span>USDT
		   	 	</p>
		   	 	<p>
		   	 		支付金额：<span style="color:red;margin-left:20px;font-size: 12px;">￥</span><span class="colorRed" style="font-size:20px;">${orderDetail.amount}</span><span style="color:red;font-size: 12px;">元</span>
		   	 	</p>		   	 	
		   	</div>
	  	</div>	  	
	  </li>
	  <li style="padding:10px;margin-bottom: 10px;" class="bgfff">
	  	<div>
	  		<p>第2步（共2步）：<span style="font-weight: 800;font-size: 18px;">填写付款姓名</span></p>
		   	<div>
		   	 	<span>您的付款姓名：</span>
		   	 	<input type="text" id="paymentName" value="" placeholder="填入您的付款姓名" style="background: #3BB4F2;border:1px solid #fff;border-radius: 5px;color:#000;height:36px;line-height: 36px;padding-left:10px;"/>
		   	 	<p style="font-size:14px;color:red;">*请输入您在付款姓名，必须与支付宝账号实名姓名一致，否则会给您带来损失。</p>
		   	</div>
	  	</div>
	  </li>	  
	</ul>
	<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered" style="margin-top:20px;padding-left:20px;">
		<button type="button" class="am-btn am-btn-primary" style="border-radius: 5px;padding-left:20px;padding-right:20px;font-size:16px;font-weight: 600;" onclick="pay()" id="openModal">前往支付</button>
	</div>	
</div>
<!-- <div class="am-modal am-modal-confirm" tabindex="-1" id="my-confirm">
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
</div> -->
<div class="am-modal am-modal-alert" tabindex="-1" id="my-alert">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">系统提示</div>
    <div class="am-modal-bd">
      请输入付款人姓名
    </div>
    <!--<div class="am-modal-footer">
      <span class="am-modal-btn">确定</span>
    </div>-->
  </div>
</div>
<script src="${basePath}/scripts/order/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${basePath}/scripts/order/js/clipboard.js" type="text/javascript" charset="utf-8"></script>
<script src="${basePath}/scripts/order/js/amazeui.min.js"></script>
<script type="text/javascript">
	new ClipboardJS('.am-icon-clone');
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
		var paymentNameValue=$('#paymentName').val().replace(/\s+/g,"");
		if(paymentNameValue.length<1) {
			$("#my-alert").modal("open");
			setTimeout(function(){
				$("#my-alert").modal("close");
			},1500)
			return;
		}
		var url="${basePath}/api/qrCodePay?tradeNumber=${orderDetail.tradeNumber}&paymentName="+encodeURI(paymentNameValue)+"&currencyAmount=${orderDetail.currencyAmount}&amount=${orderDetail.amount}&paymentNumberSuffix=${orderDetail.paymentNumberSuffix}";
		url=url+"&merchantUserId=${orderDetail.merchantUserId}&outTradeNo=${orderDetail.outTradeNo}&currencyType=${orderDetail.currencyType}&paymentType=${orderDetail.paymentType}&version=${orderDetail.version}&exchangeRate=${orderDetail.exchangeRate}";
		url=url+"&subject="+encodeURI("${orderDetail.subject}")+"&body="+encodeURI("${orderDetail.body}")+"&timestamp=${orderDetail.timestamp}"+"&notifyUrl="+encodeURI("${orderDetail.notifyUrl}")+"&merchantAccount="+encodeURI("${orderDetail.merchantAccount}");
		window.location=url;
	}
</script>
</body>
</html>
