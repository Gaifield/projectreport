<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>content/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>content/easyui/themes/icon.css">
<link rel="stylesheet" href="<%=basePath%>content/css/public.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>content/css/style.css" type="text/css">
<script type="text/javascript" src="http://dui.damai.cn/damai_v2/common/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>content/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>content/js/script.js"></script>