<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>支付</title>
    <%@include file="../common/css.jsp"%>
    <style>
    .pullContent{
       background:rgba(0,0,0,0.7);
       position:relative;
    }
    .payfor-finish{
       color:#fff;
       width:100%;
       position:absolute;
       bottom:30px;
       text-align:center;
       font-size:18px;
       line-height:30px;
    }
    ul{
       padding:15% 8%;
    }
    ul li{
    	list-style: none;
   	    display: flex;
   	    align-items: center;
   	    font-size:16px;
   	    margin-bottom:14%;
    }
    ul li>h1{
        font-size: 1.5rem;
	    font-weight: 400;
	    padding: 0;
	    margin: 0;
	    border: 1px dashed #fff;
	    color: #fff;
	    padding: 1px 12px;
	    border-radius: 8px;
        flex: 0 4%;
        text-align:center;
    }
    ul li>div{
     	padding-left:10px;
     	flex: 0 1;
    	flex-basis: 80%;
    }
    ul li:nth-child(2){
   		margin-bottom:10px;
    }
    span{
    	margin-right:5px;
   	    white-space: nowrap;
    }
    .share-more{
       	width:36px;
    }
    .flex-right{
        justify-content: center;
    }
    .flex-right img{
        width: 30%;
    }
    .share-here{
        width: 36%;
	    position: absolute;
	    right: 0;
	    top: -20%;
    }
    .open-box{
    	position:relative;
        display: flex;
	    align-items: center;
       
    }
    .open-box>div{
    	display: flex;
	    align-items: center;
    }
    </style>

</head>
<body ontouchstart>
<div class="pullContent hidden" id="payContent">
	<ul>
	   <li class="text-white">
	       <h1>1</h1>
	       <div class="open-box">        
          		<span>点击右上角的</span><img src="res/images/open_more_icon.png" class="share-more" />
                <img src="res/images/share_here_icon.png" class="share-here" />
	       </div>
	      
	   </li>
	   <!--  <li class="flex-right">
	   	   <p></p><img src="res/images/share_here_icon.png" class="share-here" />
	    </li> -->
	   <li class="text-white">
	       <h1>2</h1>
	       <div>
	          	<span>选择在浏览器中打开</span>
	       </div>
	   </li>
	   <li class="flex-right">
	      <div class="text-center">
	       	<img src="res/images/open_browser.png" />
	      </div>
	   </li>
	</ul>
	<p class="payfor-finish hidden">
	若您已完成付款，请在此点击<br/>
		<a href="javascript:void(0);" class="text-primary isPay">已完成付款</a>
	</p>
  
</div>

<%@include file="../common/js.jsp"%>
<script src="<%=request.getContextPath()%>/res/account/payfor.js?v=${version}"></script>
</body>
</html>