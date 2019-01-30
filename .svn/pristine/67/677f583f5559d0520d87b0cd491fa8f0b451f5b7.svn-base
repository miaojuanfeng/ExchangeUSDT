layui.use(['table', 'laydate'], function() {
	var table = layui.table,
		$ = layui.$,
		laydate = layui.laydate;
	
	table.render({
		elem : '#JournalAccount',
		height : 570,
		url : '/ContactsImprove/admin/JournalAccount/selectByPage',
		title : '流水数据表',
		toolbar: true,
		cols : [ [
			{
				checkbox : true,
				fixed : true
			},
			{
				field : 'journalAccountId',
				title : 'ID',
				width : 120,
				fixed : 'left'
			},
			{
				field : 'users',
				title : '用户手机号',
				width : 120,
				templet : '<div>{{ FormatNotifyUsers(d.users)}}</div>'
			},
			{
				field : 'changeAmount',
				title : '变动值',
				width : 120
			},
			{
				field : 'currencyNumberBefore',
				title : '变动前',
				width : 120
			},
			{
				field : 'currencyNumberAfter',
				title : '变动后',
				width : 120
			},
			{
				field : 'freezeNumberBefore',
				title : '冻结变动前',
				width : 120
			},
			{
				field : 'freezeNumberAfter',
				title : '冻结变动后',
				width : 120
			},
			{
				field : 'type',
				title : '类型',
				width : 100,
				templet : '<div>{{ FormatNotifyType(d.type)}}</div>'
			},
			{
				field : 'createTime',
				title : '创建时间',
				width : 160,
				templet : '<div>{{ Format(d.createTime,"yyyy-MM-dd hh:mm:ss")}}</div>'
			},
			{
				field : 'remark',
				title : '描述',
				width : 200,
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
					phoneNumber : $('#seach_phoneNumber').val(),
					type : $('#seach_type').val(),
					createTime : $('#seach_createTime').val(),
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

function FormatNotifyType(str) {
	if (str != null) {
		if (str == 1) {
			return "购买";
		} else if (str == 2) {
			return "卖出";
		} else if (str == 3) {
			return "充值";
		} else if (str == 4) {
			return "币回退";
		} else if (str == 5) {
			return "提币";
		} else if (str == 6) {
			return "交易冻结";
		}
	} else {
		return "";
	}
}

function FormatNotifyUsers(str) {
	if (str != null) {
		return str.phoneNumber;
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