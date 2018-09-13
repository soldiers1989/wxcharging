<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登陆</title>
    <%@include file="../common/css.jsp"%>
    <style>
    .flex-center-icon{
     	display: flex;
    	align-items: center;
    }
    .flex-center-icon img{
        width:20px;
        margin-right:10px;
    }
    </style>
</head>
<body ontouchstart>

<div class="weui-cells weui-cells_form margin-10 font-16">
	<div class="weui-cell">
		<div class="weui-cell__hd flex-center-icon">
			<img src="res/images/user.png" />
		   <label class="weui-label hidden">账号/手机号</label>
		</div>
		<div class="weui-cell__bd">
			<input class="weui-input userName" type="text" placeholder="请输入账号/手机号" >
		</div>
	</div>
	<div class="weui-cell">
		<div class="weui-cell__hd flex-center-icon">
			<img src="res/images/pwd.png" />
			<label class="weui-label hidden">密码</label>
		</div>
		<div class="weui-cell__bd">
			<input class="weui-input userPwd" type="password" placeholder="请输入密码">
		</div>
	</div>
</div>
<div style="display: flex;justify-content: space-between;padding-right: 15px;">
	<div class="padding-15 text-red text-show" style="height: 30px;">
	</div>
	<a href="frame/forgetPwd.action" class="text-primary">忘记密码？</a>
</div>


<div class="demos-content-padded btn-margin-20">
	<a href="javascript:void(0);" class="weui-btn weui-btn_primary">登录</a>
</div>
<div class="login-ways deep-light-gray font-14">
	<span>您还没有账号？请<a href="frame/register.action" class="text-yellow">注册</a></span>
	<a href="frame/telLogin.action" class="text-primary" >手机验证码登录</a>
</div>

<%@include file="../common/js.jsp"%>
<script src="<%=request.getContextPath()%>/res/frame/login.js?v=${version}"></script>
</body>
</html>