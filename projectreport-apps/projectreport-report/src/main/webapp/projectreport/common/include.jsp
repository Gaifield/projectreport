<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<link rel="stylesheet" href="<%=basePath%>content/css/report.css" type="text/css">	
<script type="text/javascript" src="http://dui.damai.cn/damai_v2/common/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>thirdparty/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=basePath%>content/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>content/js/page.js"></script>
<script>
    //用户退出
    function userLogOut() {
        $.ajax({
            type: "POST",
            url: "<%=basePath%>projectreport/userLogOut.do",
            success: function (resultData) {
                window.location.href = "<%=basePath%>projectreport/welcome.do";
            },
            error: function (msg) {

            }
        });
    }
	var ssid = '<c:out value="${ssid}"></c:out>';
</script>