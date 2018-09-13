<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>充电过程</title>
    <%@include file="../common/css.jsp"%>
</head>
<body ontouchstart>
<div class="pullContent">
	<div class="weui-cell wui-cell-noborder wui-cell-nopadding-top wui-cell-nopadding-left load-before">
		<div class="weui-cell__bd">
			<div class="weui-panel weui-panel_access">
				<div class="charge-box-top flex-just-between text-white">
					<div class="padding-10">
						<h2 class="font-wt-500">站名称:<span class="charge-stationName"></span></h2>
						<p class="font-r1 padding-br-15">桩编码:<span class="charge-pileCode"></span></p>
					</div>
					<div class="charge-pileToType">
					
					</div>
				</div>
			</div>
			<div class="weui-cell__bd more-list-content">
				<div class="weui-panel weui-panel_access">
					<div class="weui-media-box weui-media-box_appmsg wui-cell-nopadding-top padding-10">
						<div class="weui-media-box__bd nearby-list">
							<div class="nearby-list-item">
								<h4 class="weui-media-box__title">桩名称:<span class="charge-pileName"></span></h4>
								<p class="text-light-gray"></p>
							</div>
							<div class="nearby-list-item">
								
							</div>
						</div>
					</div>

				</div>
			</div>
			<div class="weui-cells weui-cells_radio charge-mode margin-10 charge-mode-bic hidden">
			
					<div class="weui-cell__bd">
						<div class="weui-cell flex-just-between" >
							<div class="charge-mode-header">
								按时收费<span class="font-14 deep-light-gray">&nbsp;(在充电过程中，充电器过载引起意外终端，插座被人拔出等情况，充电剩余金额不予返还)</span>
							   
							</div>
					    </div>
						<div class="weui-cell charge-mode-content font-12">
						</div>
					</div>
					
				
			</div>


			<div class="weui-cells weui-cells_radio charge-mode margin-10 charge-mode-car hidden">
				
					<div class="weui-cell__bd">
						<label class="weui-cell weui-check__label flex-just-between charge-mode-auto" for="car2">
							<div class="charge-mode-header">
								自动<span class="font-14 deep-light-gray">&nbsp;(充电器过载引起意外终端，插座被人拔出等情况，充电剩余金额不予返还)</span>
							</div>
							<div class="weui-cell__ft">
								<input type="radio" name="radio2" class="weui-check" id="car2" checked="checked">
								<span class="weui-icon-checked"></span>
							</div>
						</label>
					</div>
					
				
					<div class="weui-cell__bd">
						<label class="weui-cell weui-check__label charge-mode-item flex-just-between" for="car1">
							<div class="charge-mode-header">
								金额<span class="font-14 deep-light-gray">&nbsp;(输入的金额不能高于用户的余额)</span>	
							</div>
							<div class="weui-cell__ft">
									<input type="radio" class="weui-check" name="radio2" id="car1">
									<span class="weui-icon-checked"></span>
								</div>
						</label>
						
						<div class="weui-cell charge-mode-content font-12 flex-just-between bg-gray">
							<div class="deep-light-gray charge-mode-range">
								<input class="weui-input userName" type="number" placeholder="请输入余额 (/元)" disabled >
							</div>
							<p class="deep-light-gray">可用的余额:￥<span class="text-primary remain-sum"></span></p>
						</div>
					</div>
			</div>
		</div>
	</div>
</div>

<div class="weui-tabbar tabbar_bar tabbar_btn tabbar_bar_over">
	<a href="javascript:void(0);" class="text-white">
		启动充电
	</a>
</div>
<%@include file="../common/js.jsp"%>

<script src="<%=request.getContextPath()%>/res/charging/chargingProcess.js?v=${version}"></script>
</body>
</html>