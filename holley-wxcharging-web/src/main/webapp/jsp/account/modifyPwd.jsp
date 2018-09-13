<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>修改密码</title>
    <%@include file="../common/css.jsp"%>
     <script>
    
    </script>
</head>
<body ontouchstart>
<div class="weui-cells weui-cells_form margin-10 font-16">

	<div class="weui-cell">
		<div class="weui-cell__hd">
			<label class="weui-label">原密码</label>
		</div>
		<div class="weui-cell__bd">
			<input class="weui-input" type="password" placeholder="原密码" id="oldPassword" data-role="1" >
		</div>
	</div>

	<div class="weui-cell">
		<div class="weui-cell__hd">
			<label class="weui-label">密码</label>
		</div>
		<div class="weui-cell__bd">
			<input class="weui-input" type="password" placeholder="****" id="password" data-role="4">
		</div>
	</div>
	<div class="weui-cell">
		<div class="weui-cell__hd">
			<label class="weui-label">重复密码</label>
		</div>
		<div class="weui-cell__bd">
			<input class="weui-input" type="password" placeholder="****" id="confirmPassword" data-role="5">
		</div>
	</div>
</div>
<div class="padding-15 text-red text-show" style="height: 30px;">
</div>

<div class="demos-content-padded btn-margin-20">
	<a href="javascript:void(0);" class="weui-btn exper-user weui-btn_default weui-btn_primary">确认修改</a>
</div>
<%@include file="../common/js.jsp"%>

<script src="<%=request.getContextPath()%>/res/account/modifyPwd.js??v=${version}"></script>
</body>
</html>