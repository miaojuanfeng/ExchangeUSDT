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
  <title>PayOtc收银台</title>
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
	.chooseBox {
		display: flex;
	}
	.chooseLi {
		padding:0 20px;
		height:36px;
		line-height: 36px;
	}
	.chooseLiActive {
		border: 2px solid #3BB4F2;
		border-radius: 5px;
	}
  </style>
</head>
<body>
<div style="padding:20px 0;border-radius: 10px;margin:10px;" class="bgfff">
	<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
		<h2>PayOtc | 收银台</h2>
		<p style="color:red;font-size: 14px;">您购买的USDT已在PayOtc平台系统完成托管锁定，卖家无法单独提走，请放心支付</p>
	</div>
	<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
		<h4>交易方式</h4>
		<ul class="chooseBox">
			<li class="chooseLi chooseLiActive">支付宝扫码</li>
			<!--<li class="chooseLi">支付宝扫码</li>-->
		</ul>
		<div>
			<div id="qrcodeTable" style="width:100%;max-width: 250px;display:block;margin: auto;">
			<img src="${basePath}/api/qrcode?paymentId=${pm.paymentId}&amount=${amount}">
			</div>
			<!--<img src="img/personWX.jpg"/  style="width:100%;max-width: 250px;display:block;margin: auto;">-->
			<p style="text-align: center;font-size: 14px;">请使用支付宝扫描付款</p>
		</div>
		<div>
			<h5>交易须知：</h5>
			<p>1.根据国家相关法律规定，购买数字货币仅支持自然人之间点对点转账交易</p>
			<p>2.卖家收到转账后，您购买的USDT将从PayOtc平台释放到您充值的商家平台</p>
		</div>
	</div>
	<input type="hidden" name="time" id="time" value="" />
	<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
		待支付，请于<span style="color:red;font-weight: 600;" id="payTime">00分00秒</span>内线下支付<span style="color:red;font-weight: 600;font-family: '微软雅黑';">￥${amount}</span>
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
	window.location="${basePath}/api/badgeOrderPay?tradeNumber=${orderDetail.tradeNumber}&sign=${sign}";
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
				if (currentTime < 1) {	    
			    clearInterval(timer);
			    timer = null;
			    currentTime = differMs/1000;			    
			    $("#openModal").attr('disabled',true);
			    $("#openModal").html('交易已过期');
			    $("#openModal").css('background',"red");
			    $("#openModal").css('borderColor',"red");
			    window.location.href="${basePath}/api/error?code=201"+"&msg=交易已过期";;
			  }				
			},1000);
})
</script>
</body>
</html>
