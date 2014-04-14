<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报表平台_高级搜索</title>
<jsp:include page="../common/include.jsp"></jsp:include>
<link rel="stylesheet"
	href="<%=basePath%>thirdparty/jquery-autocomplete/jquery.autocomplete.css">
<script type="text/javascript"
	src="<%=basePath%>thirdparty/jquery-autocomplete/jquery.autocomplete.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>projectreport/common/searchTemplate.js"></script>

</head>
<body>
	<!--顶部 begin-->
	<jsp:include page="../common/top.jsp"></jsp:include>
	<!--顶部 end-->
	<div class="author_main clear">
		<div class="fr">
			<a class="back-link fr" href="javascript:history.back(-1);">点击返回上页<span
				class="r-cm">&gt;&gt;</span></a>
		</div>
		<span style="font-weight:bold;color:red;text-align:center;"><font
			size="20">${message}</font></span>
	</div>

</body>
</html>
