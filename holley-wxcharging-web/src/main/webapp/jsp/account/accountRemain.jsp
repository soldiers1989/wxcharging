<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>账户余额</title>
    <%@include file="../common/css.jsp"%>
     <script>
    
    </script>
</head>
<body ontouchstart>
<div class="weui-msg">
	<div class="weui-msg__icon-area"><img src="res/images/money_icon.png" style="width:100px;height:100px;"/></div>
	<div class="weui-msg__text-area">
		<p class="deep-light-gray font-r1">账户余额</p>
		<h2 class="weui-msg__title msg-account-money">￥<span class="show-remain">0.00</span></h2>
	</div>
	<div class="weui-msg__opr-area">
		<p class="weui-btn-area">
			<a href="account/remainRecharge.action" class="weui-btn weui-btn_primary">充值</a>
		</p>
	</div>
	<div class="weui-msg__extra-area">
			<p class="weui-footer__links">
				<a href="record/rechargingRecord.action" class="weui-footer__link text-yellow">充值记录</a>
			</p>
	</div>
</div>
<%@include file="../common/js.jsp"%>

<script src="<%=request.getContextPath()%>/res/account/accountRemain.js?v=${version}"></script>
</body>
</html>