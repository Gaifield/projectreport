<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div class="mai_above">
    <div class="wrap clear">
        <ul class="above_link fr">
            <li><em>欢迎，<c:out value="${sessionScope.userName}"></c:out> </em><a href="#"></a></li>
            <li><span>|</span></li>
            <li><span>|</span></li>
            <li><a href="<%=basePath%>modifypwd.do">修改密码</a></li>
            <li><span>|</span></li>
            <li><a href="<%=basePath%>userLogOut.do">退出系统</a></li>
        </ul>
        <div class="above_logo">
            <a href="#"><img src="<%=basePath%>content/images/logo_bg.png"
                             alt="" width="86" height="30"></a>
        </div>
        <ul class="above_nav">
            <li ${param.code!=2? 'class="active"':''}><a href="queryProjectSellAndOver.do">快速查询</a></li>
            <li><span>|</span></li>
            <li ${param.code==2? 'class="active"':''}><a href="showTemplate.do">高级查询</a></li>
        </ul>
    </div>
</div>