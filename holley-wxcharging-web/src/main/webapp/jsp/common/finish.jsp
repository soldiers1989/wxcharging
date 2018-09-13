<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>支付完成</title>
    <%@include file="../common/css.jsp"%>
    <style>
    
    </style>
    <script>
    
    </script>
</head>
<body ontouchstart>

    <div class="weui-msg" style="padding-top:30%;">
      <div class="weui-msg__icon-area"><i class="weui-icon-success weui-icon_msg"></i></div>
      <div class="weui-msg__text-area">
        <h2 class="weui-msg__title">支付成功</h2>
        <p class="weui-msg__desc"><a href="javascript:void(0);"></a></p>
      </div>
      <div class="weui-msg__opr-area">
        <p class="weui-btn-area font-16" style="padding-top:10%;">
                                    请回到微信公众号界面<br/>点击完成付款
        </p>
      </div>

    </div>



<%@include file="../common/js.jsp"%>

</body>
</html>