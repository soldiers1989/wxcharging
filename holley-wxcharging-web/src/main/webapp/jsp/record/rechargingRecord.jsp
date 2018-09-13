<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>充值记录</title>
    <%@include file="../common/css.jsp"%>
</head>
<body ontouchstart>
<div class="bg-init top-flag-bar deep-light-gray" >
		<p>充值总金额：&nbsp;<span class="accout-all text-red">￥0.00</span></p>
	</div>
<div class="pullContent body-fix-32">
	<div class="weui-pull-to-refresh__layer">
		<div class="down">下拉刷新</div>
		<div class="up">释放刷新</div>
		<div class="refresh"><i class="weui-loading"></i>正在刷新</div>
	</div>
<div class="weui-cell wui-cell-noborder wui-cell-nopadding-top wui-cell-nopadding-left load-before bg-gray">
	<div class="weui-cell__bd more-list-content">
		
	</div>
</div>
	<div class="weui-loadmore">
		<i class="weui-loading"></i>
		<span class="weui-loadmore__tips">正在加载</span>
	</div>
</div>
<%@include file="../common/js.jsp"%>

<script src="<%=request.getContextPath()%>/res/record/rechargingRecord.js?v=${version}"></script>
</body>
</html>