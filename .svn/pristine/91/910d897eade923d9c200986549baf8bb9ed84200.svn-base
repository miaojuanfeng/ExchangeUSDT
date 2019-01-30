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
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>${MSG.code}</title>
  <meta name="description" content="">
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <link rel="stylesheet" href="${basePath}/css/order/amazeui.min.css">
  <style type="text/css">
  	body {  	
  		background-color:#eee;
  	}

  </style>
</head>
<body>
<div style="padding:20px 0;">
	<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
		<img src="${basePath}/images/order/img/error.png" alt="" style="width:100%;max-width: 400px;display: block;margin:auto;"/>
		<h1 style="text-align: center;margin-top:1em;color:red;">${MSG.msg}</h1>
	</div>
</div>
</body>
</html>