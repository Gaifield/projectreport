<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报表平台_高级搜索</title>
<jsp:include page="../common/include.jsp"></jsp:include>
<link rel="stylesheet"
	href="<%=basePath%>thirdparty/jquery-autocomplete/jquery.autocomplete.css" />
<script type="text/javascript"
	src="<%=basePath%>thirdparty/jquery-autocomplete/jquery.autocomplete.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>projectreport/common/searchTemplate.js"></script>
</head>
<body>
	<!--顶部 begin-->
	<div class="mai_above">
		<div class="wrap clear">
			<ul class="above_link fr">
				<li><em>欢迎，<c:out value="${sessionScope.userName}"></c:out></em><a
					href="#"></a></li>
				<li><span>|</span></li>
				<li><span>|</span></li>
				<li><a href="<%=basePath%>modifypwd.do">修改密码</a></li>
				<li><span>|</span></li>
				<li><a href="<%=basePath%>userLogOut.do">退出系统</a></li>
			</ul>
			<div class="above_logo">
				<a href="#"><img src="<%=basePath%>content/images/logo_bg.png"
					alt="" width="86" height="30" /></a>
			</div>
		</div>
	</div>
	<!--顶部 end-->
	<!--内容区 begin-->
	<div class="author_main clear">
		<p>
			<a href="<%=request.getHeader("referer")==null ?"javascript:history.back(-1);":request.getHeader("referer")%>" style="color: #00F">点击返回上一页</a>
		</p>
		<p>
			<span style="color: #F00; font-size: 32px;">系统错误：</span>
			<c:out value="${requestScope.exception.message}"></c:out>
		</p>
	</div>
	<!--底部 begin-->
	<jsp:include page="/projectreport/common/footer.jsp"></jsp:include>
	<!--底部 end-->
</body>
</html>