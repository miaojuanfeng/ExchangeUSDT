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
<!DOCTYPE html>
<html>

	<head lang="en">
		<meta charset="UTF-8">
		<title>PayOtc个人支付宝免签在线测试</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta name="format-detection" content="telephone=no">
		<meta name="renderer" content="webkit">
		<meta http-equiv="Cache-Control" content="no-siteapp" />
		<link rel="stylesheet" href="${basePath}/css/order/amazeui.min.css" />
		<style>
			.header {
				text-align: center;
			}
			
			.header h1 {
				font-size: 200%;
				color: #333;
				margin-top: 30px;
			}
			
			.header p {
				font-size: 14px;
			}
			
			.loginTop {
				width: 100px;
				border-radius: 10px;
			}
			.padding0 {
				padding:0 !important;
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

	<body id="app">

		<div class="am-g">
			<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
				<form method="post" class="am-form" style="padding-left:2rem;padding-right:2rem;">
			      <label for="email">订单号:</label>
			      <input type="text" name="" id="nickName" value="" placeholder="请输入订单号" v-model="outTradeNo">
			      <br>
			      <label for="password">金额:</label>
			      <input type="number" name="" id="outTradeNo" value="" placeholder="请输入金额" v-model="amount">
			      <br>  
			      <label for="email">主题:</label>
			      <input type="text" name="" id="subject" value="" placeholder="请输入主题" v-model="subject">
			      <br />
			      <div class="am-cf">
			        <input type="button" name="" value="个人支付宝网页免签版" class="am-btn am-btn-primary am-btn-sm am-fl" style="width:100%;padding:0.6em 1em;font-size:1.6rem;border-radius: 5px;" @click="createdOrder()">        
			      </div>
			    </form>
			</div>
		</div>

		<!--弹出框-->
		<!--<div class="am-modal am-modal-alert" tabindex="-1" id="my-alert">
			<div class="am-modal-dialog">
				<div class="am-modal-hd">系统提示</div>
				<div class="am-modal-bd">
					{{tipsMsg}}
				</div>
			</div>
		</div>
		<!--prompt-->
		<!--<div class="am-modal am-modal-prompt" tabindex="-1" id="my-prompt">
		  <div class="am-modal-dialog">
		    <div class="am-modal-hd">payOtc</div>
		    <div class="am-modal-bd">
		      {{promptMsg}}
		      <input type="password" class="am-modal-prompt-input" v-model="personPsw">
		    </div>
		    <div class="am-modal-footer">
		      <span class="am-modal-btn" data-am-modal-cancel>取消</span>
		      <span class="am-modal-btn" data-am-modal-confirm @click="usdtTran">提交</span>
		    </div>
		  </div>
		</div>-->
	</body>
	<script src="${basePath}/scripts/order/js/jquery.min.js"></script>
	<script src="${basePath}/scripts/order/js/amazeui.min.js"></script>
	<script src="${basePath}/scripts/order/js/vue.min.js"></script>
	<script type="text/javascript">
		
		var app = new Vue({
			el:"#app",
			data:{
				amount:'',
				outTradeNo:'',
				subject:''
			},
			methods:{
				createdOrder(){
					let time = new Date().getTime();
					$.ajax({
						type:"post",
						url:"${basePath}/api/testOnLineOrder",
						async:true,
						data:{
							merchantUserId:'1828196931001',
					    	currencyType:'USDT',
					    	outTradeNo:this.outTradeNo,
					    	version:'1.0',
					    	amount:this.amount,
					    	subject:this.subject,
					    	timestamp:time,
					    	paymentType:'UNIONPAY',
					    	merchantAccount:'王工',
					    	notifyUrl:'http://localhost:8080/ContactsImprove/api/notify',
					    	body:'用户充值'
						},
						success:function(data){
							window.open("${basePath}/api/createOnLineOrder?"+data.data.paraStr+'&sign='+data.data.sign);
						},
						error:function(err){
							console.log(err);
						}
					});
				
				}
			},
			created(){

			}
		})
		
	</script>

</html>