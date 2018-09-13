<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>附近</title>
    <%@include file="../common/css.jsp"%>
    <style>
    .choose-type{
       position:relative;
    }
     .choose-type:after{
       content:'';
       background:url('res/images/back_icon.png') no-repeat;
       position:absolute;
       display:block;
       background-size: contain;
       width: 14px;
       height: 14px;
       right: 5px;
       top: 6px;
	   transform:rotate(90deg);
	   -ms-transform:rotate(90deg); 	/* IE 9 */
	   -moz-transform:rotate(90deg); 	/* Firefox */
       -webkit-transform:rotate(90deg); /* Safari 和 Chrome */
       -o-transform:rotate(90deg); 
     }
    </style>
   
</head>
<body ontouchstart>

<div class="bg-init deep-light-gray" >
	<div class="weui-search-bar weui-search-bar-noborder nearby-top flex-just-between">
	    <div class="choose-type">
	       <input class="nearby-type weui-input" type="text" style="padding-left:0;">
	    </div>
		
		<form class="weui-search-bar__form" action="#">
			<div class="weui-search-bar__box">
				<i class="weui-icon-search"></i>
				<input type="search" class="weui-search-bar__input" id="searchInput" placeholder="搜索" required="">
				<a href="javascript:" class="weui-icon-clear" id="searchClear"></a>
			</div>
			<label class="weui-search-bar__label" id="searchText" style="transform-origin: 0px 0px 0px; opacity: 1; transform: scale(1, 1);">
				<i class="weui-icon-search"></i>
				<span>搜索</span>
			</label>
		</form>
		<a href="javascript:" class="weui-search-bar__cancel-btn text-primary" id="searchCancel">取消</a>
	</div>
</div>
<div class="pullContent body-fix-96">
	<div class="weui-pull-to-refresh__layer">
		<div class="down">下拉刷新</div>
		<div class="up">释放刷新</div>
		<div class="refresh"><i class="weui-loading"></i>正在刷新</div>
	</div>
	<div class="weui-cell wui-cell-noborder wui-cell-nopadding-top wui-cell-nopadding-left load-before bg-gray bg-top-space-8">
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
	<div class="weui-tabbar">
		<a href="common/nearby.action" class="weui-tabbar__item  weui-bar__item--on">
			<div class="weui-tabbar__icon">
		       <img src="res/images/nearby_active.png" alt="">
			</div>
			<p class="weui-tabbar__label">附近</p>
		</a>
		<a href="charging/charging.action" class="weui-tabbar__item">
			<div class="weui-tabbar__icon">
				<img src="res/images/charging.png" alt="">
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
 <script>
      
 /*    storage.removeItem('openid'); */
       
 </script>
<script src="<%=request.getContextPath()%>/res/common/nearby.js?v=${version}"></script>
</body>
</html>