<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path=request.getContextPath();
String scheme="http";
if(request.getServerPort()==443) {
	scheme="https";
}
String basePath=scheme+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
request.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title>PayOtc币商登录</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="format-detection" content="telephone=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <link rel="stylesheet" href="${basePath}/css/order/amazeui.min.css"/>
  <style>
  	body {
  		/*background: url("assets/i/pag.jpg") 0% 0% / 200% no-repeat,  0% 0% / 100%;*/
  	}
    .header {
      text-align: center;
    }
    .header h1 {
      font-size: 200%;
      color: #333;
      margin-top: 30px;
    }
    .header p {
      font-size: 14px;
    }
    .loginTop {
    	width:100px;
    	
    	border-radius:10px;
    }
    .colorGreen {
    	color:green;
    }
    .colorRed {
    	color:red;
    }
  </style>
</head>
<body>
<div class="header">
  <div class="am-g" style="margin-top:35px;">    
    <img src="img/logo-min.jpg" alt="" class="loginTop"/>         
  </div>
</div>
<div class="am-g">
  <div class="am-u-lg-6 am-u-md-8 am-u-sm-centered"> 
  	<div style="margin-left:2rem;margin-right:2rem;">
  			<p style="width:100%;text-align:center;height:40px;line-height:40px;color:#000;font-weight:700;font-size:16px;margin-top:10px;">
		  		币商登录
		    </p>
  	</div>
    <br>
    <form method="post" class="am-form" style="padding-left:2rem;padding-right:2rem;">
      <label for="email">手机号:</label>
      <input type="text" name="" id="nickName" value="" placeholder="请输入手机号">
      <br>
      <label for="password">密码:</label>
      <input type="password" name="" id="password" value="" placeholder="请输入您的密码">
      <br>      
      <br />
      <div class="am-cf">
        <input type="button" name="" value="登录系统" class="am-btn am-btn-primary am-btn-sm am-fl" style="width:100%;padding:0.6em 1em;font-size:1.6rem;border-radius: 5px;" onclick="login()">        
      </div>
    </form>    
  </div>
</div>

<!--弹出框-->
<div class="am-modal am-modal-alert" tabindex="-1" id="my-alert">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">系统提示</div>
    <div class="am-modal-bd">
      
    </div>
    <!--<div class="am-modal-footer">
      <span class="am-modal-btn">确定</span>
    </div>-->
  </div>
</div>
</body>
<script src="${basePath}/scripts/order/js/jquery.min.js"></script>
<script src="${basePath}/scripts/order/js/amazeui.min.js"></script>
<script src="${basePath}/scripts/order/js/config.js"></script>
<script type="text/javascript">
	let myreg=/^[1][3,4,5,7,8,6,9][0-9]{9}$/;
	//登录
	function login(){
		let nickName = $("#nickName").val();
		let password = $("#password").val();
		
		if(!myreg.test(nickName)){
			$("#my-alert").modal();
			$(".am-modal-bd").addClass('colorRed');
			$(".am-modal-bd").html('请输入正确的手机号');
			setTimeout(function(){
				$('#my-alert').modal('close');
			},1500)
			
			return 
		}		
		
		if(!password){
			$("#my-alert").modal();
			$(".am-modal-bd").addClass('colorRed');
			$(".am-modal-bd").html('密码不能为空');
			setTimeout(function(){
				$('#my-alert').modal('close');
			},1500)
			
			return 
		}
		
		$.ajax({
			type:"post",
			url:"${basePath}/api/login",
			async:false,
			data:{
				phoneNumber:nickName,
				password:password
			},
			success:function(data){
				
				console.log(data);
				if(data.code == 200){
					$("#my-alert").modal();
					$(".am-modal-bd").addClass('colorGreen');
					$(".am-modal-bd").html('登录成功！');
					
					localStorage.setItem('phoneNumber',data.data.phoneNumber);
					localStorage.setItem('type',data.data.type);
					localStorage.setItem('userId',data.data.userId);
					localStorage.setItem('workHours',data.data.workHours);
					localStorage.setItem('role',data.data.role);
					
					setTimeout(function(){
						window.location.href = '${basePath}/api/mobile/index'
					},1500)
					
					
				}else {
					$("#my-alert").modal();
					$(".am-modal-bd").addClass('colorRed');
					$(".am-modal-bd").html(data.msg);
					
					setTimeout(function(){
						$('#my-alert').modal('close');
					},1500)
				}
			},
			error:function(err){
					$('#my-alert').modal();
					$(".am-modal-bd-alert").addClass('colorRed');
					$(".am-modal-bd-alert").html("服务器错误");
					setTimeout(function(){
						$('#my-alert').modal('close');
					},1500)
			}
		});
	}
</script>
</html>