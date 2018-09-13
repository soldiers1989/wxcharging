<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>订单详情（充电记录详情）</title>
    <%@include file="../common/css.jsp"%>
    <style>
    .nearby-list-item>div img{
 		margin-bottom:2px;
    }
    </style>

</head>
<body ontouchstart>
<div class="pullContent">
	<div class="weui-cell wui-cell-noborder wui-cell-nopadding-top wui-cell-nopadding-left load-before">
			<div class="weui-cell__bd">
				<div class="weui-panel weui-panel_access font-15 more-list-content">
				</div>
				<div class="weui-panel weui-panel_access font-15">
					<div class="weui-media-box weui-media-box_appmsg wui-cell-nopadding-top">
						
						<div class="weui-media-box__bd">
						    <div class="weui-cells my-list-box">
						    	<a class="weui-cell wui-cell-nopadding-left hidden total-charge" href="javascript:void(0)" >
									<div class="weui-cell__hd"></div>
									<div class="weui-cell__bd">
										<!-- <img src="res/images/recharge_record.png" alt=""> -->
										<p>支付方式</p>
									</div>
									<div class="weui-cell__ft"><span class="charge-payWay">0</span></div>
								</a>
								<a class="weui-cell wui-cell-nopadding-left hidden total-charge" href="javascript:void(0)" >
									<div class="weui-cell__hd"></div>
									<div class="weui-cell__bd">
										<!-- <img src="res/images/recharge_record.png" alt=""> -->
										<p>预计充电时长(分钟)</p>
									</div>
									<div class="weui-cell__ft"><span class="total-time">0</span></div>
								</a>
								<a class="weui-cell wui-cell-nopadding-left"  href="javascript:void(0)">
									<div class="weui-cell__hd"></div>
									<div class="weui-cell__bd">
										<p>实际充电时长(分钟)</p>
									</div>
									<div class="weui-cell__ft"><span class="charge-time">0</span></div>
								</a>
							</div>
<!-- 							<p class="" style="padding:10px 0;border-bottom:1px solid #efefef;">
								充电时长(分钟)<span class="charge-time deep-light-gray" style="float:right;">0</span>
						    </p> -->
							<div class="nearby-list-item flex-just-between padding-10">
		
								<div class="text-center">
								    <div><img src="res/images/starttime.png" /></div>
									<h4 class="weui-media-box__title text-blue">充电开始时间</h4>
									<p class="charge-starttime deep-light-gray"></p>
								</div>
								<div class="text-center">
									<div><img src="res/images/endtime.png" /></div>
									<span class="deep-light-gray text-yellow">充电结束时间</span>
									<p class="charge-endtime deep-light-gray"></p>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>
	</div>
</div>

<div class="weui-tabbar text-white tabbar_bar">
</div>
<%@include file="../common/js.jsp"%>

<script src="<%=request.getContextPath()%>/res/stat/chargingRecordMore.js?v=${version}"></script>
</body>
</html>