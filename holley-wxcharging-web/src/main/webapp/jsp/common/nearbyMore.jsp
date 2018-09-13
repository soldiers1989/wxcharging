<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>桩详情</title>
    <%@include file="../common/css.jsp"%>
    <meta name="format-detection" content="telephone=no">
	<meta http-equiv="x-rim-auto-match" content="none">
	<style>
		.link-a-function{
			display:block;
			padding:4px 0;
		}
		.charge-data{
		   background:rgba(255,255,255,.2);
		}
		.charge-data-item > a{    
		    display: flex;
    		align-items: center;
    		justify-content: center;
		}
		.charge-data-item img{
		 	width:22px;
		 	margin-right:10px;
		}
	    .weui-panel_access{
        	background:url("res/images/pile_infor_topbar.png") no-repeat;
        	background-size: 100%;
   		 }
	</style>
     <script>
    
    </script>
</head>
<body ontouchstart>
<div class="pullContent">
	<div class="weui-cell wui-cell-noborder wui-cell-nopadding-top wui-cell-nopadding-left load-before">
		<div class="weui-cell__bd more-list-content" style="margin-bottom:50px;">
		</div>
	</div>
</div>

<div class="weui-tabbar tabbar_bar tabbar_bar_over text-white btn-yellow">
		扫码充电
</div>
<%@include file="../common/js.jsp"%>

<script src="<%=request.getContextPath()%>/res/common/nearbyMore.js?v=${version}"></script>
</body>
</html>