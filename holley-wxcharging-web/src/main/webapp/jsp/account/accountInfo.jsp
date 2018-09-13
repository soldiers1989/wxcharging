<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>我的</title>
    <%@include file="../common/css.jsp"%>
    <%@include file="../common/js.jsp"%>
    <script type="text/javascript">
        var storage= window.localStorage;
        storage = storage ? storage : [];
   /*       storage.removeItem('accountKey'); */ 
	    if(storage.getItem('accountKey')===null){	 
	    	    getPages(
	    				'frame/frame_queryIsLogin.action',
	    		       {
	    					openId:storage.getItem('openid')
	    		       },function (res) {
	    		       	  	 console.log(res)
	    					 if(res.ret===0){ 
	    						 if(res.data.failReason===0){
	          						 $.hideLoading();
	          						 if(res.data.loginStatus===1){
	          							storage.setItem('accountKey',res.data.accountKey);
	          						 }else{
	          							window.location.href="frame/login.action?tartHref=account/accountInfo.action";
	          						 }
	          						
	          					 }else{
	          						 $.hideLoading();
	          						 $.toast(res.data.failReasonDesc,"cancel",function() {
	          			
	          						 });
	          					 }
	    					 }else{
	    						 $.hideLoading();
	    						 $.toast(res.msg,"cancel",function(toast) {});
	    					 }

	    		     },function (msg){
	    		    	 	$.hideLoading();
	    					$.toast(msg,"cancel",function(toast) {});

	    		     },function(){
	    		      
	    		     });
	    }
    </script>
</head>
<body ontouchstart>
<div class="pullContent">
	<div class="weui-cell wui-cell-noborder wui-cell-nopadding-top wui-cell-nopadding-left load-before">
		<div class="weui-cell__bd">
			<div class="weui-panel weui-panel_access bg-gray">
					<a href="account/userInfo.action" class="my-head-box">
						<img src="" alt="" class="head-imgurl">
					    <div class="text-white">
							<p class="nick-name font-16"></p>
							<p class="font-14 user-city"></p>
						</div>
					</a>
				    <div class="weui-cells my-list-box">
						<a class="weui-cell weui-cell_access" href="account/accountRemain.action">
							<div class="weui-cell__hd"></div>
							<div class="weui-cell__bd">
								<img src="res/images/recharging.png" alt="">
								<p>
									我要充值</p>
							</div>
							<div class="weui-cell__ft"></div>
						</a>

					</div>
				<div class="weui-cells margin-10 my-list-box">
					<a class="weui-cell weui-cell_access" href="record/rechargingRecord.action">
						<div class="weui-cell__hd"></div>
						<div class="weui-cell__bd">
							<img src="res/images/recharge_record.png" alt="">
							<p>充值记录</p>
						</div>
						<div class="weui-cell__ft"></div>
					</a>
					<a class="weui-cell weui-cell_access" href="record/chargingRecord.action">
						<div class="weui-cell__hd"></div>
						<div class="weui-cell__bd">
							<img src="res/images/charge_record.png" alt="">
							<p>订单管理</p>
						</div>
						<div class="weui-cell__ft"></div>
					</a>
				</div>

			</div>
		</div>
	</div>

</div>

<div class="weui-tabbar ">
	<a href="common/nearby.action" class="weui-tabbar__item">
		<div class="weui-tabbar__icon">
			<img src="res/images/nearby.png" alt="">
		</div>
		<p class="weui-tabbar__label">附近</p>
	</a>
	<a href="charging/charging.action" class="weui-tabbar__item">
		<div class="weui-tabbar__icon">
			<img src="res/images/charging.png" alt="">
		</div>
		<p class="weui-tabbar__label">充电</p>
	</a>
	<a href="account/accountInfo.action" class="weui-tabbar__item weui-bar__item--on">
		<div class="weui-tabbar__icon">
			<img src="res/images/my_active.png" alt="">
		</div>
		<p class="weui-tabbar__label">我的</p>
	</a>
</div>
<script src="<%=request.getContextPath()%>/res/account/accountInfo.js?v=${version}"></script>
</body>
</html>