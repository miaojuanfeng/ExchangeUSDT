layui.use('table', function() {
	var table = layui.table,
		form = layui.form,
		$ = layui.$;
	
	$(function() {
		render();
	});
	
	function render() {
		table.render({
			elem : '#user',
			height : 570,
			url : '/ContactsImprove/admin/user/examineUser',
			where : {
				status: 3
			},
			title : '商户审核数据表',
			toolbar: true,
			cols : [ [
				{
					checkbox : true,
					fixed : true
				},
				{
					field : 'userId',
					title : 'ID',
					width : 180,
					fixed : 'left'
				},
				{
					field : 'userName',
					title : '用户名',
					width : 130
				},
				{
					field : 'phoneNumber',
					title : '手机号',
					width : 120
				},
				{
					field : 'createTime',
					title : '注册时间',
					width : 200,
					templet : '<div>{{ Format(d.createTime,"yyyy-MM-dd hh:mm:ss")}}</div>'					
				},					
				{
					field : 'status',
					title : '状态',
					width : 115,
					templet: '#switchTpl', unresize: true
				},
			] ],
			id: 'seach_reload',
			page : true,
		});
	}

	var active = {
		reload : function() {
			table.reload('seach_reload', {
				page : {
					curr : 1 //重新从第 1 页开始
				},
				where : {
					userName : $('#seach_userName').val(),
					phoneNumber : $('#seach_phone').val(),
				}
			});
		}
	};

	$('.layui-seach .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	
	form.on('checkbox(status)', function(obj) {
		$.ajax({
			url : "/ContactsImprove/admin/user/updateUser",
			type : "post",
			data : {
				'userId' : this.value,
				'status' : obj.elem.checked == true ? 1 : 3,
			},
			dateType : 'json',
			success : function success(data) {
				layer.msg("审核" + data.msg);
				render();
			}
		});
	});
});

function FormatCurrencyNumber(str) {
	if(str != null) {
		return str.currencyNumber;
	} else {
		return "";
	}
}

function FormatFreezeNumber(str) {
	if(str != null) {
		return str.freezeNumber;
	} else {
		return "";
	}
}

function FormatWalletAddress(str) {
	if(str != null) {
		return str.walletAddress;
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