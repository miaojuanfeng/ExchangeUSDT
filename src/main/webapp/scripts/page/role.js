layui.use(['table', 'layer', 'authtree', 'laydate'], function() {
	var table = layui.table,
		$ = layui.jquery,
		layer = layui.layer,
		laydate = layui.laydate,
		authtree = layui.authtree;
	
	laydate.render({
		 elem: '#seach_createTime',
	});

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
					roleName : $('#seach_roleName').val(),
					createTime : $('#seach_createTime').val()
				}
			});
		}
	};

	$('.layui-seach .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	
	$('#add').on('click', function() {
		layer.open({
			type : 1,
			area : [ '530px', '255px' ],
			shade : [ 0.5, '#393D49' ],
			title : "添加",
			content : $("#add_role_example"),
			btn : [ '确定', '取消' ],
			scrollbar : false,
			resize : true,
			maxmin : true,
			yes : function(index, layero) {
				$.ajax({
					url : "/ContactsImprove/admin/role/addRole",
					type : "post",
					data : $("#add_role_example").serialize(),
					dateType : 'json',
					success : function success(resp) {
						layer.msg("添加" + resp.msg);
						if(resp.code == 200) {
							layer.close(index);
							table_reload();
						}
					}
				});
			}
		});
	});

	table.on('tool(role)', function(obj) {
		var data = obj.data;
		$.ajax({
			url : "/ContactsImprove/admin/resources/selectResourcesByRolesId",
			type : "post",
			data : {'id': data.id},
			dateType : 'json',
			success : function success(resp) {
				if(resp.code == 0) {
					if (obj.event === 'edit') {
						authtree.render('#LAY-auth-tree-index', resp.data, {
							inputname: 'authids[]',
							layfilter: 'lay-check-auth',
							autowidth: true
						});
						authtree.showAll('#LAY-auth-tree-index');
						
						layer.open({
							type : 1,
							area : [ '850px', '570px' ],
							shade : [ 0.5, '#393D49' ],
							title : "编辑",
							content : $("#role_example"),
							btn : [ '确定', '取消' ],
							scrollbar : false,
							resize : true,
							maxmin: true,
							yes : function(index, layero) {
								var checked = authtree.getChecked('#LAY-auth-tree-index');
								var resourcesIdstr = "";
								for(var i=0;i<checked.length;i++) {
									resourcesIdstr += checked[i] + ",";
								}
								$.ajax({
									url : "/ContactsImprove/admin/roleResourceDetail/updateRoleResourceDetail",
									type : "post",
									data : {'roleId': data.id, resourcesIdstr: resourcesIdstr.substring(0, resourcesIdstr.length-1)},
									dateType : 'json',
									success : function success(resp) {
										layer.msg("设置" + resp.msg);
										if(resp.code == 200) {
											table_reload();
											layer.close(index);
										}
									}
								});
							}
						});
					} else if(obj.event === 'del') {
						layer.confirm('确定删除？', function(index) {
							obj.del(); //删除对应行的DOM结构
							$.ajax({
								url : "/ContactsImprove/admin/role/delRole",
								type : "post",
								data : {'id': data.id},
								dateType : 'json',
								success : function success(dresp) {
									layer.msg("删除" + dresp.msg);
									if(dresp.code == 200) {
										table_reload();
										layer.close(index);
										$('.layui-layer-shade').remove();
									}
								}
							});
						});
					}
				}
			}
		});
	});
	
	function table_reload() {
		table.render({
			elem : '#role',
			height : 570,
			url : '/ContactsImprove/admin/role/selectRoleByList',
			title : '用户数据表',
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
					field : 'roleName',
					title : '名称',
					width : 120
				},
				{
					field : 'description',
					title : '描述',
					width : 260
				},
				{
					field : 'createTime',
					title : '创建时间',
					width : 180,
					templet : '<div>{{ Format(d.createTime,"yyyy-MM-dd")}}</div>'
				},
				{
					fixed : 'right',
					title : '操作',
					toolbar : '#barDemo',
					width : 200
				}
			] ],
			id: 'seach_reload',
			page : true
		});
	} 
});


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