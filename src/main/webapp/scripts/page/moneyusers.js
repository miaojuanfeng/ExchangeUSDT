layui.use(['table','form'], function() {
	var table = layui.table,
		form = layui.form,
		$ = layui.$;
	
	$(function() {
		render();
	});
	
	var active = {
		reload : function() {
			table.reload('seach_reload', {
				page : {
					curr : 1 //重新从第 1 页开始
				},
				where : {
					userName : $('#seach_userName').val(),
					phoneNumber : $('#seach_phone').val(),
					status : $('#seach_status').val()
				}
			});
		}
	};
	
	table.on('tool(moneyuser)', function(obj) {
		var data = obj.data, //获得当前行数据
			layEvent = obj.event; //获得 lay-event 对应的值
		
		if (layEvent === 'edit') { //修改操作
			$("#userId").val(data.userId);
			$("#userName").val(data.userName);
			$("#phoneNumber").val(data.phoneNumber);
			$("#availableCredit").val("");
			$("#currencyNumber").val("");
			$("#role").val(data.role);
			form.render('select');
			layer.open({
				type : 1,
				area : [ '480px', '390px' ],
				shade : [ 0.5, '#393D49' ],
				title : "编辑",
				content : $("#moneyusers_example"),
				btn : [ '确定', '取消' ],
				scrollbar : false,
				resize : true,
				maxmin: true,
				yes : function(index, layero) {
					$.ajax({
						url : "/ContactsImprove//admin/user/updateUser",
						type : "post",
						data : $("#moneyusers_example").serialize(),
						dateType : 'json',
						success : function success(resp) {
							layer.msg("修改" + resp.msg);
							layer.close(index);
							render();
						}
					});
				}
			});
		}
	});

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
				'status' : obj.elem.checked == true ? 0 : 1,
			},
			dateType : 'json',
			success : function success(data) {
				if (obj.elem.checked == true) {
					layer.msg("锁定" + data.msg);
					render();
				} else {
					layer.msg("解锁" + data.msg);
				}
			}
		});
	});
	
	function render() {
		table.render({
			elem : '#moneyuser',
			height : 570,
			url : '/ContactsImprove/admin/user/selectUsersByType',
			where : {
				type: 2
			},
			title : '币商数据表',
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
					field : 'availableCredit',
					title : '可用额度',
					width : 100,
				},
				{
					field : 'freezingQuota',
					title : '冻结额度',
					width : 100,
				},
				{
					field : 'userWallet',
					title : '可用USDT数量',
					width : 130,
					templet : '<div>{{ FormatCurrencyNumber(d.userWallet)}}</div>'
				},
				{
					field : 'userWallet',
					title : '冻结USDT数量',
					width : 130,
					templet : '<div>{{ FormatFreezeNumber(d.userWallet)}}</div>'
				},
				{
					field : 'userWallet',
					title : '钱包地址',
					width : 310,
					templet : '<div>{{ FormatWalletAddress(d.userWallet)}}</div>'
				},
				{
					field : 'createTime',
					title : '创建时间',
					width : 160,
					templet : '<div>{{ Format(d.createTime,"yyyy-MM-dd hh:mm:ss")}}</div>'
				},
				{
					field : 'role',
					title : '用户角色',
					width : 100,
					templet : '<div>{{FormatRole(d.role)}}</div>'
				},
				{
					field : 'status',
					title : '状态',
					width : 115,
					templet: '#switchTpl', unresize: true
				},
				{
					fixed : 'right',
					title : '操作',
					width : 160,
					align : 'center',
					toolbar : '#barDemo'
				}
			] ],
			id: 'seach_reload',
			page : true,
		});
	}
});
function FormatRole(str) {
	if(str != null) {
		if(str==0){
			return "正式用户";
		}else if(str==1){
			return "测试用户";
		}			
	} else {
		return "";
	}
}
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