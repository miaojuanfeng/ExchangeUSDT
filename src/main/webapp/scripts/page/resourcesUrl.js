layui.use([ 'layer', 'treetable', 'form' ], function() {
	var layer = layui.layer,
		$ = layui.$,
		treetable = layui.treetable,
		form = layui.form,
		layer = layui.layer;

	$(function() { render();})

	treetable.on('treetable(edit)', function(data) {
		$.ajax({
			url : "/ContactsImprove/admin/resources/selectResourcesById",
			type : "post",
			data : {
				id : data.item.id
			},
			dateType : 'json',
			success : function success(res) {
				$.ajax({
					url : "/ContactsImprove/admin/resources/selectResourcesByList",
					type : "post",
					dateType : 'json',
					success : function success(ldata) {
						var str = "";
						for (var i = 0; i < ldata.data.length; i++) {
							if(ldata.data[i].type == (res.data.type-1)) {
								str += '<option value="' + ldata.data[i].id + '">' + ldata.data[i].resourceName + '</option>';
							}
						}
						$('#parentId').html(str);
						form.render('select');
						
						form.on('select(s_type)', function(da) {
							var str = "";
							for (var i = 0; i < ldata.data.length; i++) {
								if(ldata.data[i].type == (da.value-1)) {
									str += '<option value="' + ldata.data[i].id + '">' + ldata.data[i].resourceName + '</option>';
								}
							}
							$('#parentId').html(str);
							form.render('select');
						});
						
						$("#id").val(data.item.id);
						$("#resourceName").val(res.data.resourceName);
						$("#url").val(res.data.url);
						$("#icon").val(res.data.icon);
						$("#sort").val(res.data.sort);
						$("#type").val(res.data.type);
						$("#parentId").val(res.data.parentId);
						form.render('select');
						layer.open({
							type : 1,
							area : [ '645px', '500px' ],
							shade : [ 0.5, '#393D49' ],
							title : "编辑",
							content : $("#resources_example"),
							btn : [ '确定', '取消' ],
							scrollbar : false,
							resize : true,
							maxmin: true,
							yes : function(index, layero) {
								$.ajax({
									url : "/ContactsImprove/admin/resources/updateResourcesById",
									type : "post",
									data : $("#resources_example").serialize(),
									dateType : 'json',
									success : function success(rsp) {
										layer.msg("编辑" + rsp.msg);
										if(rsp.code == 200) {
											layer.close(index);
											render();
										}
									}
								});
							}
						});
					}
				});
			}
		});
	})

	treetable.on('treetable(del)', function(data) {
		layer.confirm('确认删除？', {
			btn : [ '确定', '取消' ] //按钮
		}, function() {
			$.ajax({
				url : data.elem.context.value,
				type : "post",
				data : {
					'id' : data.item.id
				},
				dateType : 'json',
				success : function success(res) {
					layer.msg("删除" + res.msg);
					if(res.code == 200) {
						render();
					}
				}
			});
		});
	})

	form.on('switch(isValid)', function(data) {
		$.ajax({
			url : "/ContactsImprove/admin/resources/updateResourcesById",
			type : "post",
			data : {
				'id' : data.value,
				'isValid' : data.elem.checked == true ? 1 : 0
			},
			dateType : 'json',
			success : function success(res) {
				if (data.elem.checked) {
					layer.msg("开启" + res.msg);
				} else {
					layer.msg("关闭" + res.msg);
				}
			}
		});
	})

	$('#add').on('click', function() {
		$("#id").val("");
		$("#resourceName").val("");
		$("#url").val("");
		$("#icon").val("");
		$("#sort").val("");
		$("#type").val("");
		$("#parentId").val("");
		form.render('select');
		$.ajax({
			url : "/ContactsImprove/admin/resources/selectResourcesByList",
			type : "post",
			dateType : 'json',
			success : function success(data) {
				form.on('select(s_type)', function(da) {
					var str = "";
					for (var i = 0; i < data.data.length; i++) {
						if(data.data[i].type == (da.value-1)) {
							str += '<option value="' + data.data[i].id + '">' + data.data[i].resourceName + '</option>';
						}
					}
					$('#parentId').html(str);
					form.render('select');
				});
				
				layer.open({
					type : 1,
					area : [ '645px', '500px' ],
					shade : [ 0.5, '#393D49' ],
					title : "添加",
					content : $("#resources_example"),
					btn : [ '确定', '取消' ],
					scrollbar : false,
					resize : true,
					maxmin : true,
					yes : function(index, layero) {
						$.ajax({
							url : "/ContactsImprove/admin/resources/addResiurces",
							type : "post",
							data : $("#resources_example").serialize(),
							dateType : 'json',
							success : function success(resp) {
								layer.msg("添加" + resp.msg);
								if(resp.code == 200) {
									layer.close(index);
									render();
								}
							}
						});
					}
				});
			}
		});
	});

	function render() {
		$.ajax({
			url : "/ContactsImprove/admin/resources/selectResourcesByList",
			type : "post",
			dateType : 'json',
			success : function success(data) {
				treetable.render({
					elem : '#tree-table',
					data : data.data,
					field : 'resourceName',
					is_checkbox : true,
					cols : [
						{
							field : 'id',
							title : 'ID',
							width : '5%'
						},
						{
							field : 'resourceName',
							title : '标题',
							width : '15%'
						},
						{
							field : 'url',
							title : '地址',
							width : '20%'
						},
						{
							title : '图标',
							width : '10%',
							template : function(item) {
								return item.icon;
							}
						},
						{
							field : 'sort',
							title : '排序',
							width : '5%'
						},
						{
							title : '类型',
							width : '5%',
							template : function(item) {
								if (item.type == 1) {
									return "菜单";
								} else if (item.type == 2) {
									return "子菜单";
								} else if (item.type == 3) {
									return "按钮";
								}
							}
						},
						{
							title : '状态',
							width : '7%',
							template : function(item) {
								var str = null;
								if (item.isValid == 1) {
									str = '<input type="checkbox" lay-skin="switch" name="isValid" value="' + item.id + '" lay-filter="isValid" lay-text="开启|关闭" checked>';
								} else if (item.isValid == 0) {
									str = '<input type="checkbox" lay-skin="switch" name="isValid" value="' + item.id + '" lay-filter="isValid" lay-text="开启|关闭">';
								}
								return str
							}
						},
						{
							field : 'actions',
							title : '操作',
							width : '31%',
							template : function(item) {
								var tem = [];
								tem.push('<button class="layui-btn" value="/ContactsImprove/admin/resources/updateResourcesById" lay-filter="edit">编辑</button>');
								tem.push('<button class="layui-btn layui-btn-danger" value="/ContactsImprove/admin/resources/deleteResourcesById" lay-filter="del">删除</button>');
								return tem.join(' <font></font> ')
							},
						},
					]
				});
			}
		});
	}
})