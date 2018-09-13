<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>充电</title>
    <%@include file="../common/css.jsp"%>
    <style>
    .input-code{
	    height:54px;    
    }
    .input-device-code{
        border:1px solid #efefef;
        margin-top:10px;
        margin-bottom:10px;
        padding:5px 10px;
    }
    .input-code input{
        line-height:44px;
        padding-left:10px;
        padding-right:10px;
        flex:0 1 66.7%;
    }
    .input-code .weui-btn{
        border-radius:0;
        flex:0 1 33.3%;
        font-size:1rem;
        height: inherit;
    }
    .weui-panel:after{
        border-bottom:none;
    }
    </style>
   
   

</head>
<body ontouchstart>
 
<div class="pullContent bg-init">
	<div class="weui-cell wui-cell-noborder wui-cell-nopadding-top wui-cell-nopadding-left load-before">
		<div class="weui-cell__bd" style="height:100%;">
			<div class="weui-panel calc-height">
				<div class="charge-box-top">
					<div class="text-white text-center">
						<p class="font-r1">总充电金额(元)</p>
						<h2 class="charge-box-account">0.00</h2>
					</div>
					<div class="charge-data text-white btn-margin-20">
						<div class="charge-data-item">
							<p>总时长(分)</p>
							<span class="total-time">0</span>
						</div>
						<div class="charge-data-item">
							<p>总充电量(度)</p>
							<span class="total-degree">0</span>
						</div>
						<div class="charge-data-item">
							<p>总充电次数(次)</p>
							<span class="total-index">0</span>
						</div>
					</div>
				</div>
			<div class="deep-light-gray text-right padding-15 padding-10">
				<span class="input-code text-yellow font-16">手动输入设备编号</span>
	  <!--   <input class="weui-input" type="number" placeholder="输入设备编号" >
	    <button class="weui-btn btn-yellow btn-int">输入充电</button> -->
			</div>
				
			</div>
			<div class="charge-box-content">
				<a href="javascript:void(0);" class="charging-btn">
					<img src="res/images/scan_code.png" alt="" class="charge-scan-btn">
					<p class="deep-light-gray font-16 margin-10">扫码充电</p>
				</a>
			</div>
		</div>
	</div>
	
</div>

<div class="weui-tabbar">
	<a href="common/nearby.action" class="weui-tabbar__item">
		<div class="weui-tabbar__icon">
			 <img src="res/images/nearby.png" alt="">
		</div>
		<p class="weui-tabbar__label">附近</p>
	</a>
	<a href="charging/charging.action" class="weui-tabbar__item weui-bar__item--on">
		<div class="weui-tabbar__icon">
			<img src="res/images/charging_active.png" alt="">
		</div>
		<p class="weui-tabbar__label">充电</p>
	</a>
	<a href="account/accountInfo.action" class="weui-tabbar__item">
		<div class="weui-tabbar__icon">
			<img src="res/images/my.png" alt="">
		</div>
		<p class="weui-tabbar__label">我的</p>
	</a>
</div>

    <%@include file="../common/js.jsp"%>
    <script src="<%=request.getContextPath()%>/res/charging/charging.js?v=${version}"></script>
</body>
</html>