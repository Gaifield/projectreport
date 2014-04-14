<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div class="author_above">
    <div class="wrap clear">
        <ul class="above_link fr">
            <li><em>欢迎，</em><a href="#">${sessionScope.userName}</a></li>
            <li><span>|</span></li>
            <li><a href="<%=basePath%>userLogOut.do">退出系统</a></li>
        </ul>

        <div class="above_logo">
            <a href="#"><img src="<%=basePath%>content/images/logo_bg2.jpg"
                             alt="" width="80" height="30"/></a>
        </div>
        <ul class="above_nav">
            <li ${param.code==1? 'class="active"':''}><a href="<%=basePath%>userManager.do">用户管理</a>
            </li>
            <shiro:hasRole name="admin">
                <li><span>|</span></li>
                <li ${param.code==2? 'class="active"':''}><a
                        href="<%=basePath%>operatorManage.do">操作员管理</a>
                </li>

                <li><span>|</span></li>
                <li ${param.code==3? 'class="active"':''}><a href="<%=basePath%>skitOperator.do">新建操作员</a>
                </li>
                <li><span>|</span></li>
                <li ${param.code==4? 'class="active"':''}><a
                        href="<%=basePath%>skipToRoleManager.do">角色管理</a>
                </li>
            </shiro:hasRole>
        </ul>
    </div>
</div>