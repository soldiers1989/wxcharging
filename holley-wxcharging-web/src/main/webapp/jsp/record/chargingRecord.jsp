<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>订单管理</title>
    <%@include file="../common/css.jsp"%>
    <style>
    .nearby-top .nearby-type:after, .topbar-relative .topbar__item:after{
      right:15%;
    }
    </style>
     <script>
    
    </script>
</head>
<body ontouchstart>
	<div class="weui-tabbar topbar-relative">
		<div class="topbar__item">
			<input class="weui-input charge-datetime deep-light-gray" type="text">
		</div>
		<div class="topbar__item">
			<input class="weui-input charge-type deep-light-gray" type="text" placeholder="" />
		</div>
	</div>
	<div class="pullContent body-fix-44">
			<div class="weui-pull-to-refresh__layer">
				<div class="down">下拉刷新</div>
				<div class="up">释放刷新</div>
				<div class="refresh"><i class="weui-loading"></i>正在刷新</div>
			</div>
			<div class="weui-cell wui-cell-noborder wui-cell-nopadding-top wui-cell-nopadding-left load-before bg-gray margin-5">
				<div class="weui-cell__bd">
					<div class="weui-panel weui-panel_access more-list-content">
						
					</div>
				</div>
			</div>
			<div class="weui-loadmore">
				<i class="weui-loading"></i>
				<span class="weui-loadmore__tips">正在加载</span>
			</div>

</div>
<%@include file="../common/js.jsp"%>

<script src="<%=request.getContextPath()%>/res/record/chargingRecord.js?v=${version}"></script>
</body>
</html>