<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>账户管理</title>
    <%@include file="../common/css.jsp"%>
     <script>
    
    </script>
</head>
<body ontouchstart>
<div class="pullContent">
	<div class="weui-cell wui-cell-noborder wui-cell-nopadding-top wui-cell-nopadding-left load-before margin-10">
		<div class="weui-cell__bd">
			<div class="weui-panel weui-panel_access bg-gray">
					<div class="weui-cells">
						<a class="weui-cell" href="javascript:;">
							<div class="weui-cell__hd"></div>
							<div class="weui-cell__bd">
								<p>头像</p>
							</div>
							<div class="weui-cell__ft head_inline_portait"><img class="head-imgurl" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAC4AAAAuCAMAAABgZ9sFAAAAVFBMVEXx8fHMzMzr6+vn5+fv7+/t7e3d3d2+vr7W1tbHx8eysrKdnZ3p6enk5OTR0dG7u7u3t7ejo6PY2Njh4eHf39/T09PExMSvr6+goKCqqqqnp6e4uLgcLY/OAAAAnklEQVRIx+3RSRLDIAxE0QYhAbGZPNu5/z0zrXHiqiz5W72FqhqtVuuXAl3iOV7iPV/iSsAqZa9BS7YOmMXnNNX4TWGxRMn3R6SxRNgy0bzXOW8EBO8SAClsPdB3psqlvG+Lw7ONXg/pTld52BjgSSkA3PV2OOemjIDcZQWgVvONw60q7sIpR38EnHPSMDQ4MjDjLPozhAkGrVbr/z0ANjAF4AcbXmYAAAAASUVORK5CYII=" alt=""></div>
						</a>
						
						<a class="weui-cell" href="javascript:;">
							<div class="weui-cell__hd"></div>
							<div class="weui-cell__bd">
								<p>微信昵称</p>
							</div>
							<div class="weui-cell__ft nick-name">西米的提莫</div>
						</a>
						<a class="weui-cell" href="javascript:;">
							<div class="weui-cell__hd"></div>
							<div class="weui-cell__bd">
								<p>城市</p>
							</div>
							<div class="weui-cell__ft user-city"></div>
						</a>
						
					</div>
					<div class="weui-cells margin-10">
						<a class="weui-cell" href="javascript:;">
								<div class="weui-cell__hd"></div>
								<div class="weui-cell__bd">
									<p>用户名</p>
								</div>
								<div class="weui-cell__ft user-name"></div>
						</a>
							<a class="weui-cell" href="javascript:;">
							<div class="weui-cell__hd"></div>
							<div class="weui-cell__bd">
								<p >手机号</p>
							</div>
							<div class="weui-cell__ft user-phone"></div>
						</a>
						<a class="weui-cell weui-cell_access" href="account/modifyPwd.action">
							<div class="weui-cell__hd"></div>
							<div class="weui-cell__bd">
								<p>修改密码</p>
							</div>
							<div class="weui-cell__ft"></div>
						</a>
					</div>

			</div>
			<div class="padding-15">
				<a href="javascript:void(0);" class="weui-btn weui-btn_yellow register-out" style="margin-top:30px;">注销</a>
			</div>
		</div>
	</div>

</div>

<%@include file="../common/js.jsp"%>

<script src="<%=request.getContextPath()%>/res/account/userInfo.js?v=${version}"></script>
</body>
</html>