<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>余额充值</title>
    <%@include file="../common/css.jsp"%>
     <script>
    
    </script>
</head>
<body ontouchstart>
<div class="pullContent balanceIndex">
	<div class="weui-cell wui-cell-noborder wui-cell-nopadding-top wui-cell-nopadding-left">
		<div class="weui-cell__bd balance-header">
				<div class="charge-box-top" style="background:#32b16c;">
					<div class="text-white">
						<p class="padding-5">账户余额(元)</p>
						<h2 class="charge-box-account">0.00</h2>
					</div>
				</div>
			<!--"\EA06"-->
			<div class="weui-cells weui-cells_radio balance-content margin-10">
				<label class="weui-cell balance-title">
					充值方式
				</label>
				<label class="weui-cell weui-check__label" for="x11">
					<div class="weui-cell__bd">
						<p><img src="res/images/weixin.png" alt="" class="cell-item" ><span class="payfor-name">微信支付</span></p>
					</div>
					<div class="weui-cell__ft">
						<input type="radio" class="weui-check" name="radio1" id="x11" checked="checked">
						<span class="weui-icon-checked"></span>
					</div>
				</label>
				<label class="weui-cell weui-check__label" for="x12">
					<div class="weui-cell__bd">
						<p><img src="res/images/alipay.png" alt="" class="cell-item"><span class="payfor-name">支付宝</span></p>
					</div>
					<div class="weui-cell__ft">
						<input type="radio" name="radio1" class="weui-check" id="x12" >
						<span class="weui-icon-checked"></span>
					</div>
				</label>
				<p class="weui-cell weui-cell_access balance-title">
					充值金额
				</p>
				<div class="weui-cell balance-sum-flex padding-br-15">
					<div class="flex-just-between width-100 text-center">
						<a href="javascript:void(0);" class="weui-cell__bd balance-sum-item balance-sum-item-active" data-role=10>
							<h4>10元</h4>
							售价:10.00元
						</a>
						<a href="javascript:void(0);" class="weui-cell__bd balance-sum-item" data-role=50>
							<h4>50元</h4>
							售价:50.00元
						</a>
						<a href="javascript:void(0);" class="weui-cell__bd balance-sum-item" data-role=100>
							<h4>100元</h4>
							售价:100.00元
						</a>
					</div>
					<div class="flex-just-between width-100 text-center">
						<a href="javascript:void(0);" class="weui-cell__bd balance-sum-item" data-role=150>
							<h4>150元</h4>
							售价:150.00元
						</a>
						<a href="javascript:void(0);" class="weui-cell__bd balance-sum-item" data-role=200>
							<h4>200元</h4>
							售价:200.00元
						</a>
						<a href="javascript:void(0);" class="weui-cell__bd balance-sum-item" data-role=300>
							<h4>300元</h4>
							售价:300.00元
						</a>
					</div>
				</div>
				<p class="weui-cell weui-cell_access balance-title padding-10 bg-gray">
					<em>￥</em><input class="weui-input input-val" type="number" placeholder="其它金额">
				</p>
				<div class="padding-b15 padding-15">
					<a href="javascript:void(0);" class="weui-btn weui-btn_primary weui-btn_yellow">下一步</a>
				</div>

			</div>

		</div>

	</div>
</div>

<%@include file="../common/js.jsp"%>

<script src="<%=request.getContextPath()%>/res/account/remainRecharge.js?v=${version}"></script>
</body>
</html>