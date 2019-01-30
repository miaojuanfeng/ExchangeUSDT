layui.use(['table', 'laydate','form'], function() {
	var table = layui.table,
		form=layui.form,
		$ = layui.$,
		laydate = layui.laydate;
	$(function() {
		render();
	});
	function render() {
	table.render({
		 done: function(res, curr, count){
			    //如果是异步请求数据方式，res即为你接口返回的信息。curr :当前页码,count:得到数据总量
			 	$("#totalNum").html(res.statistics.totalNum);
			 	$("#totalAmount").html(res.statistics.totalAmount==undefined ? 0 : res.statistics.totalAmount);
			 	$("#totalActualAmount").html(res.statistics.totalActualAmount==undefined ? 0 : res.statistics.totalActualAmount);
			 	$("#totalCurrencyAmount").html(res.statistics.totalCurrencyAmount==undefined ? 0 : res.statistics.totalCurrencyAmount);
			 	$("#totalActualCurrencyAmount").html(res.statistics.totalActualCurrencyAmount==undefined ? 0 : res.statistics.totalActualCurrencyAmount);
			 	$("#totalPoundage").html(res.statistics.totalPoundage==undefined ? 0 : res.statistics.totalPoundage);
		 },
		elem : '#order',
		height : 610,
		url : '/ContactsImprove/admin/order/statisticsOrderList',
		title : '订单数据表',
		toolbar: true,
		totalRow: true,
		cols : [ [
			{
				checkbox : true,
				fixed : true
			},
			{
				field : 'tradeNumber',
				title : '交易流水号',
				width : 160,
				fixed : 'left',
				unresize : true,
				sort : true,
				totalRowText : '合计'
			},
			{
				field : 'outTradeNo',
				title : '商户订单号',
				width : 120
			},
			{
				field : 'merchantUserId',
				title : '商户号',
				width : 130
			},
			{
				field : 'userId',
				title : '币商id',
				width : 130
			},
			{
				field : 'userName',
				title : '币商名称',
				width : 90
			},
			{
				field : 'paymentName',
				title : '付款姓名',
				width : 90
			},
			{
				field : 'paymentTypeName',
				title : '支付方式',
				width : 100
			},
			{
				field : 'paymentNumber',
				title : '支付号码',
				width : 130
			},
			{
				field : 'currencyAmount',
				title : '币种数量',
				width : 120,
				totalRow: true
			},
			{
				field : 'amount',
				title : '金额数量',
				width : 90,
				totalRow: true
			},
			{
				field : 'actualAmount',
				title : '实际收款',
				width : 90,
				totalRow: true
			},
			{
				field : 'status',
				title : '状态',
				width : 80,
				templet : '<div>{{ FormatStatus(d.status)}}</div>'
			},
			{
				field : 'dealType',
				title : '交易类型',
				width : 80,
				templet : '<div>{{ FormatDealType(d.dealType)}}</div>'
			},
			{
				field : 'notifyStatus',
				title : '回调状态',
				width : 90,
				templet : '<div>{{ FormatNotifyStatus(d.notifyStatus)}}</div>'
			},
			{
				field : 'subject',
				title : '主题',
				width : 100,
			},
			{
				field : 'body',
				title : '交易描述',
				width : 130,
			},
			{
				field : 'createTime',
				title : '创建时间',
				width : 160,
				templet : '<div>{{ Format(d.createTime,"yyyy-MM-dd hh:mm:ss")}}</div>'
			},
			{
				field : 'paymentTime',
				title : '支付时间',
				width : 160,
				templet : '<div>{{ Format(d.paymentTime,"yyyy-MM-dd hh:mm:ss")}}</div>'
			},
			{
				field : 'closeTime',
				title : '完成时间',
				width : 160,
				templet : '<div>{{ Format(d.closeTime,"yyyy-MM-dd hh:mm:ss")}}</div>'
			}
		] ],
		id : 'seach_reload',
		page : true
	});
	}

	var active = {
		reload : function() {
			table.reload('seach_reload', {
				page : {
					curr : 1 //重新从第 1 页开始
				},
				where : {
					userId : $('#seach_userId').val(),
					phoneNumber : $('#seach_phoneNumber').val(),
					paymentNumber : $('#seach_paymentNumber').val(),
					startTime : $('#seach_startTime').val(),
					endTime : $('#seach_endTime').val(),
					merchantUserId:$('#seach_merchantUserId').val(),
					userName:$('#seach_userName').val()
				}
			});
		}
	};
	
	$('.layui-seach .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});	
	
	laydate.render({
		elem : '#seach_startTime',
	});
	
	laydate.render({
		elem : '#seach_endTime',
	});
	
});
function FormatStatus(str) {
	if (str != null) {
		if (str == 0) {
			return "未付款";
		} else if (str == 1) {
			return "已付款";
		} else if (str == 2) {
			return "已完成";
		} else if (str == 3) {
			return "已取消";
		} else if (str == 4) {
			return "交易失败";
		}else if (str == 5) {
			return "未回退申诉";
		}else if (str == 6) {
			return "审核中";
		}else if(str==7){
			return "已回退申诉";
		}		
	} else {
		return "";
	}
}
function FormatDealType(str) {
	if (str != null) {
		if (str == 0) {
			return "正常交易";
		} else if (str == 1) {
			return "补单交易";
		}
	} else {
		return "";
	}
}
function FormatNotifyStatus(str) {
	if (str != null) {
		if(str == -1) {
			return "未回调";
		}else if (str == 0) {
			return "回调中";
		} else if (str == 1) {
			return "回调成功";
		} else if (str == 2) {
			return "回调失败";
		}else if (str == 3) {
			return "无需回调";
		}
	} else {
		return "";
	}
}
function Format(datetime, fmt) {
	if (datetime != null && datetime != "") {
		datetime = new Date(datetime);
		var o = {
			"M+" : datetime.getMonth() + 1, //月份   
			"d+" : datetime.getDate(), //日   
			"h+" : datetime.getHours(), //小时   
			"m+" : datetime.getMinutes(), //分   
			"s+" : datetime.getSeconds(), //秒   
			"q+" : Math.floor((datetime.getMonth() + 3) / 3), //季度   
			"S" : datetime.getMilliseconds() //毫秒   
		};
		if (/(y+)/.test(fmt))
			fmt = fmt.replace(RegExp.$1, (datetime.getFullYear() + "").substr(4 - RegExp.$1.length));
		for (var k in o)
			if (new RegExp("(" + k + ")").test(fmt))
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	} else {
		return ""
	}
}