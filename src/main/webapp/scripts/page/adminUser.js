layui.use(['table', 'layer', 'form'], function() {
	var table = layui.table, 
		$ = layui.$, 
		form = layui.form;

	$(function() {
		table_reload();
	})
	
	var active = {
		reload : function() {
			table.reload('seach_reload', {
				page : {
					curr : 1 //重新从第 1 页开始
				},
				where : {
					userName : $('#seach_userName').val(),
					phone : $('#seach_phone').val(),
					name : $('#seach_name').val()
				}
			});
		}
	};

	$('.layui-seach .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});

	$('#add').on('click', function() {
		$("#powerId").html("");
		$("#id").val("");
		$("#userName").val("");
		$("#password").val("");
		$("#phone").val("");
		$("#name").val("");

		$.ajax({
			url : "/ContactsImprove/admin/powers/selectPowersByList",
			type : "post",
			dateType : 'json',
			success : function success(data) {
				var str = "";
				for(var i=0; i<data.data.length; i++) {
					str += '<option value="'+data.data[i].id+'">'+data.data[i].powerName+'</option>';
				}
				$("#powerId").html(str);
				form.render('select');
				layer.open({
					type : 1,
					area : [ '580px', '420px' ],
					shade : [ 0.5, '#393D49' ],
					title : "添加",
					content : $("#adminUser_example"),
					btn : [ '确定', '取消' ],
					scrollbar : false,
					resize : true,
					maxmin : true,
					yes : function(index, layero) {
						$.ajax({
							url : "/ContactsImprove/admin/addAdminUser",
							type : "post",
							data : $("#adminUser_example").serialize(),
							dateType : 'json',
							success : function success(resp) {
								if(resp.code == 200) {
									layer.msg("添加" + resp.msg);
									table_reload();
									layer.close(index);	
								} else if(resp.code == 313) {
									layer.msg(resp.msg);
								} else {
									layer.msg("添加" + resp.msg);
								}
							}
						});
					}
				});
			}
		});
	});

	table.on('tool(adminUser)', function(obj) {
		var data = obj.data, //获得当前行数据
			layEvent = obj.event; //获得 lay-event 对应的值
		$.ajax({
			url : "/ContactsImprove/admin/selectAdminUserById",
			type : "post",
			data : {'id': data.id},
			dateType : 'json',
			success : function success(res) {
				if(res.code == 200) {
					if (layEvent === 'del') { //删除操作
						layer.confirm('确认删除？', function(index) {
							obj.del(); //删除对应行的DOM结构
							$.ajax({
								url : "/ContactsImprove/admin/deleteAdminUser",
								type : "post",
								data : {'id': res.data.id},
								dateType : 'json',
								success : function success(resp) {
									layer.msg("删除" + resp.msg);
									if(resp.code == 200) {
										table_reload();
										layer.close(index);
									}
								}
							});
						});
					} else if (layEvent === 'edit') { //修改操作
						var str = "";
						for(var i=0; i<res.data.lpowers.length; i++) {
							str += '<option value="'+res.data.lpowers[i].id+'">'+res.data.lpowers[i].powerName+'</option>';
						}
						$("#powerId").html(str);
						form.render('select');
						$("#id").val(res.data.id);
						$("#userName").val(res.data.userName);
						$("#password").val(res.data.password);
						$("#phone").val(res.data.phone);
						$("#name").val(res.data.name);
						$("#powerId").val(res.data.powerId);
						form.render('select');
						
						layer.open({
							type : 1,
							area : [ '580px', '420px' ],
							shade : [ 0.5, '#393D49' ],
							title : "编辑",
							content : $("#adminUser_example"),
							btn : [ '确定', '取消' ],
							scrollbar : false,
							resize : true,
							maxmin: true,
							yes : function(index, layero) {
								$.ajax({
									url : "/ContactsImprove/admin/updateAdminUser",
									type : "post",
									data : $("#adminUser_example").serialize(),
									dateType : 'json',
									success : function success(resp) {
										if(resp.code == 200) {
											layer.msg("修改" + resp.msg);
											table_reload();
											layer.close(index);
										} else if(resp.code == 313) {
											layer.msg(resp.msg);
										} else {
											layer.msg("修改" + resp.msg);
										}
									}
								});
							}
						});
					}
				} else {
					layer.msg("操作" + res.msg);
				}
			}
		});
	});

	form.on('checkbox(isValid)', function(obj) {
		$.ajax({
			url : "/ContactsImprove/admin/updateAdminUser",
			type : "post",
			data : {
				'id' : this.value,
				'isValid' : obj.elem.checked == true ? 0 : 1,
			},
			dateType : 'json',
			success : function success(data) {
				if (obj.elem.checked == true) {
					layer.msg("锁定" + data.msg);
				} else {
					layer.msg("解锁" + data.msg);
				}
			}
		});
	});
	
	function table_reload() {
		table.render({
			elem : '#adminUser',
			height : 570,
			url : '/ContactsImprove/admin/selectAdminUserByPage',
			where : {
				id : localStorage.getItem("userId")
			},
			title : '后台用户数据表',
			toolbar: true,
			cols : [ [
				{
					checkbox : true,
					fixed : true
				},
				{
					field : 'id',
					title : 'ID',
					width : 60,
					fixed : 'left'
				},
				{
					field : 'userName',
					title : '邮箱',
					width : 180
				},
				{
					field : 'phone',
					title : '手机号',
					width : 120
				},
				{
					field : 'name',
					title : '名字',
					width : 110
				},
				{
					field : 'powers',
					title : '权限',
					width : 110,
					templet : '<div>{{ FormatpowerName(d.powers)}}</div>'
				},
				{
					field : 'createTime',
					title : '创建时间',
					width : 160,
					templet : '<div>{{ Format(d.createTime,"yyyy-MM-dd hh:mm:ss")}}</div>'
				},
				{
					field : 'ip',
					title : '登录IP',
					width : 150
				},
				{
					field : 'loginTime',
					title : '登录时间',
					width : 160,
					templet : '<div>{{ Format(d.loginTime,"yyyy-MM-dd hh:mm:ss")}}</div>'
				},
				{
					field : 'isValid',
					title : '是否锁定',
					width : 130,
					templet : '#checkboxTpl',
					unresize : true
				},
				{
					fixed : 'right',
					title : '操作',
					width : 180,
					align : 'center',
					toolbar : '#barDemo'
				}
			] ],
			id: 'seach_reload',
			page : true,
		});
	}
});

function FormatpowerName(str) {
	if(str != null) {
		return str.powerName;
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