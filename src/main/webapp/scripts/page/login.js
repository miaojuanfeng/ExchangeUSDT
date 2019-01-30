layui.use('layer', function() {
	var table = layui.table,
		$ = layui.$,
		layer = layui.layer;
	
	document.onkeydown=function(event){
		var e = event || window.event || arguments.callee.caller.arguments[0];
		if(e.keyCode == "13") {
			login();
		}
	}
	
	$('#login').on('click', function() {
		login();
	});
	
	function login() {
		if($('#userName').val() == null || $('#userName').val() == "") {
			layer.msg("请输入邮箱号");
			return;
		}
		
		if($('#password').val() == null || $('#password').val() == "") {
			layer.msg("请输入密码");
			return;
		}
		
		$.ajax({
			url : "/ContactsImprove/admin/login/login",
			type : "post",
			data : {
				userName : $('#userName').val(),
				password : $('#password').val()
			},
			dateType : 'json',
			success : function success(data) {
				if(data.code == 200) {
					location.href = "/ContactsImprove/admin/login/index";
				} else {
					layer.msg(data.msg);
				}
			}
		});
	}
});