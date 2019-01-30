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
		<title>PayOtc订单列表</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta name="format-detection" content="telephone=no">
		<meta name="renderer" content="webkit">
		<meta http-equiv="Cache-Control" content="no-siteapp" />
		<link rel="stylesheet" href="${basePath}/css/order/amazeui.min.css" />
		<style>
		[v-cloak]{
			display:none;
		}
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

	<body id="app" v-cloak>

		<div class="am-g">
			<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
				<div data-am-widget="tabs" class="am-tabs am-tabs-default">
					<ul class="am-tabs-nav am-cf">
						<li class="am-active">
							<a href="[data-tab-panel-0]" @click="getTranList(0)">待付款</a>
						</li>
						<li class="">
							<a href="[data-tab-panel-1]" @click="getTranList(1)">已付款</a>
						</li>
					</ul>
					<div class="am-tabs-bd">
						<div data-tab-panel-0 class="am-tab-panel am-active padding0">
							<section data-am-widget="accordion" class="am-accordion am-accordion-gapped" data-am-accordion='{  "multiple": true }' v-if="orderTranList.length">
								<dl class="am-accordion-item am-active" v-for="item in orderTranList">
									<dt class="am-accordion-title">
							          订单号：{{item.tradeNumber}}
							        </dt>
									<dd class="am-accordion-bd am-collapse am-in">
										<!-- 规避 Collapase 处理有 padding 的折叠内容计算计算有误问题， 加一个容器 -->
										<div class="am-accordion-content">
											<!--<p>订单号：{{item.tradeNumber}}</p>-->
											<p>金额（元）：<span class="colorRed">{{item.amount}}</span></p>
											<p>USDT：{{item.currencyAmount}}</p>
											<p>支付方式：{{item.paymentTypeName}}</p>
											<p>付款姓名：<span class="colorRed">{{item.paymentName}}</span></p>
											<p>收款账号：<span class="colorRed">{{item.paymentNumber}}</span></p>
											<p>交易类型：{{item.type | type}}</p>
											<p>状态：<span :class="item.status >= 3  ? 'colorRed' : 'colorGreen'">{{item.status | status}}</span></p>
											<p>创建时间：{{item.createTime | time}}</p>
											<button type="button" class="am-btn am-btn-primary am-round am-btn-sm" @click="openPsw($index,item.tradeNumber,'出金')">确认转账</button>
										</div>
									</dd>
								</dl>								
							</section>
							<p v-else style="text-align: center;">暂无交易数据</p>
						</div>
						<div data-tab-panel-1 class="am-tab-panel padding0">
							<section data-am-widget="accordion" class="am-accordion am-accordion-gapped" data-am-accordion='{  "multiple": true }' v-if="orderHavePay.length">
								<dl class="am-accordion-item am-active" v-for="item in orderHavePay">
									<dt class="am-accordion-title">
							          订单号：{{item.tradeNumber}}
							        </dt>
									<dd class="am-accordion-bd am-collapse am-in">
										<!-- 规避 Collapase 处理有 padding 的折叠内容计算计算有误问题， 加一个容器 -->
										<div class="am-accordion-content">
											<!--<p>订单号：{{item.tradeNumber}}</p>-->
											<p>金额（元）：<span class="colorRed">{{item.amount}}</span></p>
											<p>USDT：{{item.currencyAmount}}</p>
											<p>支付方式：{{item.paymentTypeName}}</p>
											<p>付款姓名：<span class="colorRed">{{item.paymentName}}</span></p>
											<p>收款账号：<span class="colorRed">{{item.paymentNumber}}</span></p>
											<p>交易类型：{{item.type | type}}</p>
											<p>状态：<span :class="item.status >= 3  ? 'colorRed' : 'colorGreen'">{{item.status | status}}</span></p>
											<p>创建时间：{{item.createTime | time}}</p>
											<button type="button" class="am-btn am-btn-primary am-round am-btn-sm" @click="openPsw($index,item.tradeNumber,'入金')">确认转账</button>
										</div>
									</dd>
								</dl>
								
							</section>
							<p v-else style="text-align: center;">暂无交易数据</p>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!--弹出框-->
		<div class="am-modal am-modal-alert" tabindex="-1" id="my-alert">
			<div class="am-modal-dialog">
				<div class="am-modal-hd">系统提示</div>
				<div class="am-modal-bd">
					{{tipsMsg}}
				</div>
			</div>
		</div>
		<!--prompt-->
		<div class="am-modal am-modal-prompt" tabindex="-1" id="my-prompt">
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
		</div>
	</body>
	<script src="${basePath}/scripts/order/js/jquery.min.js"></script>
	<script src="${basePath}/scripts/order/js/amazeui.min.js"></script>
	<script src="${basePath}/scripts/order/js/config.js"></script>
	<script src="${basePath}/scripts/order/js/vue.min.js"></script>
	<script type="text/javascript">
		
		var app = new Vue({
			el:"#app",
			data:{
				orderTranList:[],
				orderHavePay:[],
				tipsMsg:'',
				personPsw:'',
				promptMsg:"输入密码",
				tradeNumber:'',
				type:'2'
			},
			methods:{
				getTranList(status){
					let _this = this;
					$.ajax({
						type:"post",
						url:"${basePath}/api/userOrdersByStatus",
						async:false,
						data:{
							pageNumber:1,
							pageSize:10,
							status:status
						},
						success:function(data){
							//console.log(data);
							if(data.code == 200){								
								
								//未付款选取出金的数据
								let orderTranList = data.data.Orders.OrdersList;
								let list = [];
								if(status == 0){
									/* orderTranList.forEach(function(value,index,array){
										
										if(value.type == 2){
											list[index] = {											
													amount:value.amount,
									                currencyAmount:value.currencyAmount,
									                paymentTypeName:value.paymentTypeName,					              
									                paymentNumber:value.paymentNumber,
									                type:value.type,
									                status:value.status,
									                createTime:value.createTime,
									                tradeNumber:value.tradeNumber
												}	
										}
										
																			
										
									}) */
									
									_this.orderTranList = orderTranList;									
								//已付款选取入金的数据	
								}else if(status == 1){
									_this.orderHavePay = orderTranList;
								}
								//_this.orderTranList = orderTranList;
							}else {
								
								$("#my-alert").modal('open');								
								
								_this.tipsMsg = data.msg;
							}
							
						},
						error:function(err){
							$("#my-alert").modal('open');																
							_this.tipsMsg = '服务器错误';
							
							setTimeout(function(){
								$("#my-alert").modal('close');
							},1500)
						}
					});
				},
				openPsw(index,tradeNumber,type){
					
					$("#my-prompt").modal('open');
					
					this.tradeNumber = tradeNumber;
					this.type = type;
				},
				usdtTran(){
					
					let _this = this;
					
					console.log(_this.type)
					console.log(_this.tradeNumber)
					//币商确认放行
					if(_this.type == '出金'){
						$.ajax({
							type:"post",
							url:"${basePath}/api/confirmWithdrawal",
							async:false,
							data:{
								tradeNumber:this.tradeNumber,
								password:this.personPsw
							},
							success:function(data){
								//console.log(data);
								if(data.code == 200){								
									$("#my-alert").modal('open');
									_this.tipsMsg = '提交成功';
									_this.tradeNumber = "";
									
									location=location;
								}else {								
									$("#my-alert").modal('open');																
									_this.tipsMsg = data.msg;
									
									setTimeout(function(){
										$("#my-alert").modal('close');
									},1500)
								}
								
							},
							error:function(err){
								$("#my-alert").modal('open');																
								_this.tipsMsg = '服务器错误';
								
								setTimeout(function(){
									$("#my-alert").modal('close');
								},1500);
							}
						});
					}else if(_this.type == '入金'){
						$.ajax({
							type:"post",
							url:"${basePath}/api/confirmPay",
							async:false,
							data:{
								tradeNumber:this.tradeNumber,
								password:this.personPsw
							},
							success:function(data){
								//console.log(data);
								if(data.code == 200){								
									$("#my-alert").modal('open');
									_this.tipsMsg = '提交成功';
									
									_this.tradeNumber = "";
									location=location;
								}else {								
									$("#my-alert").modal('open');																
									_this.tipsMsg = data.msg;
									
									setTimeout(function(){
										$("#my-alert").modal('close');
									},1500)
								}
							},
							error:function(err){
								$("#my-alert").modal('open');																
								_this.tipsMsg = '服务器错误';
								
								setTimeout(function(){
									$("#my-alert").modal('close');
								},1500)
								
								console.log(err)
							}
						});
					}												
				}
			},
			filters:{
				type(type){
					if(type == '0'){
						return '入金';
					}else if(type == '1'){
						return '提币';
					}else if(type == '2'){
						return '出金';
					}else if(type == '3'){
						return '充币';
					}
				},
				status(status){
					if(status == '0'){
						return "待付款";
					}else if(status == '1'){
						return '已付款';
					}else if(status == '2'){
						return '已完成';
					}else if(status == '3'){
						return '已取消';
					}
				},
				time(value){
				    let date = new Date(value);  //时间戳为10位需*1000，时间戳为13位的话不需乘1000
				    let Y = date.getFullYear() + '-';
				    let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) + '-' : (date.getMonth() + 1) + '-';
				    let D = parseInt(date.getDate()) < 10 ? '0' + date.getDate() + ' ' : date.getDate() + ' ';
				    let h = parseInt(date.getHours()) < 10 ? '0' + date.getHours() + ':' : date.getHours() + ':';
				    let m = parseInt(date.getMinutes()) < 10 ? '0' + date.getMinutes() + ':' : date.getMinutes() + ':';
				    let s = parseInt(date.getSeconds()) < 10 ? '0' + date.getSeconds() : date.getSeconds();
				    return Y + M + D + h + m + s;
				}
			},
			created(){
				this.getTranList(0);
				this.getTranList(1);
			}
		})
						
	</script>

</html>