layui.use(['table', 'laydate'], function() {
	var table = layui.table,
		$ = layui.$,
		laydate = layui.laydate;
	
	table.render({
		elem : '#order',
		height : 570,
		url : '/ContactsImprove/admin/order/selectOrderByList',
		where : {
			types: '13'
		},
		title : '充提币数据表',
		toolbar: true,
		cols : [ [
			{
				checkbox : true,
				fixed : true
			},
			{
				field : 'tradeNumber',
				title : '交易流水号',
				width : 250,
				fixed : 'left'
			},
			{
				field : 'userId',
				title : '币商ID',
				width : 150
			},
			{
				field : 'currencyAmount',
				title : '数量',
				width : 120
			},
			{
				field : 'fromAddress',
				title : '发送地址',
				width : 310
			},
			{
				field : 'toAddress',
				title : '接收地址',
				width : 310
			},
			{
				field : 'status',
				title : '状态',
				width : 80,
				templet : '<div>{{ FormatStatus(d.status)}}</div>'
			},
			{
				field : 'subject',
				title : '主题',
				width : 100,
			},
			{
				field : 'body',
				title : '交易描述',
				width : 159,
			},
			{
				field : 'createTime',
				title : '创建时间',
				width : 160,
				templet : '<div>{{ Format(d.createTime,"yyyy-MM-dd hh:mm:ss")}}</div>'
			},
		] ],
		id : 'seach_reload',
		page : true
	});

	var active = {
		reload : function() {
			table.reload('seach_reload', {
				page : {
					curr : 1 //重新从第 1 页开始
				},
				where : {
					tradeNumber : $('#seach_tradeNumber').val(),
					createTime : $('#seach_createTime').val(),
					type : $('#seach_type').val()
				}
			});
		}
	};

	$('.layui-seach .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	
	laydate.render({
		elem : '#seach_createTime',
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
			return "申述中";
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