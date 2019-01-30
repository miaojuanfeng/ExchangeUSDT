layui.use([ 'jquery', 'tree', 'element' ], function() {
	var $ = layui.$,
		element = layui.element;
	
	localStorage.setItem("userId",$('#user_id').val());
	
	$.ajax({
		url : "/ContactsImprove/admin/resources/selectResourcesBypower",
		type : "post",
		data : {
			'id' : $('#user_id').val()
		},
		dateType : 'json',
		success : function success(data) {
			if (data.code == 200) {
				var str;
				for (var i = 0; i < data.data.length; i++) {
					str += '<li class="layui-nav-item"><a href="javascript:;"><i class="layui-icon">'+data.data[i].icon+'</i>'+data.data[i].resourceName+'</a>';
					for (var j = 0; j < data.data[i].children.length; j++) {
						str += '<dl class="layui-nav-child"><dd><a href="javascript:;" href-url="'+data.data[i].children[j].url+'"><i class="layui-icon">'+data.data[i].children[j].icon+'</i>'+data.data[i].children[j].resourceName+'</a></dd></dl>';
						for(var k=0; k<data.data[i].children[j].menuList.length; k++) {
							
						}
					}
					str += '</li>';
				}
				$('#layui-nav-tree').html(str);
				element.init()
			}
		}
	});
	
	$('#adminUser_msg').on('click', function() {
		$.ajax({
			url : "/ContactsImprove/admin/selectAdminUserById",
			type : "post",
			data : {
				id : $('#user_id').val()
			},
			dateType : 'json',
			success : function success(data) {
				$('#id').val(data.data.id);
				$('#userName').val(data.data.userName);
				$('#password').val(data.data.password);
				$('#phone').val(data.data.phone);
				$('#name').val(data.data.name);
				layer.open({
					type : 1,
					area : [ '450px', '345px' ],
					shade : [ 0.5, '#393D49' ],
					title : "修改个人信息",
					content : $("#adminUser_example"),
					btn : [ '确定', '取消' ],
					scrollbar : false,
					resize : true,
					maxmin : true,
					yes : function(index, layero) {
						$.ajax({
							url : "/ContactsImprove/admin/updateAdminUser",
							type : "post",
							data : $("#adminUser_example").serialize(),
							dateType : 'json',
							success : function success(resp) {
								layer.msg("修改" + resp.msg);
								if(resp.code == 200) {
									layer.close(index);
								}
							}
						});
					}
				});
			}
		});
	});
});
