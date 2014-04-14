<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>授权管理_角色管理-编辑角色</title>
    <jsp:include page="common/include.jsp"></jsp:include>
</head>

<body>
<!--header begin-->
<jsp:include page="common/top.jsp">
<jsp:param value="4" name="code"/>
</jsp:include>
<!--header end-->

<!--内容区 begin-->
<div class="author_main clear">
    <div class="author_title">
        <a href="#" onclick="window.location.href='<%=basePath%>skipToRoleManager.do'">返回角色管理列表 >></a>
        <h4>角色信息</h4>
    </div>
    <div class="author_box">
        <ul class="author_edit">
            <li>
                <p>
                    <span class="author_edit_name">角色名称：</span>
                    <input type="text" class="text" style="width:150px;" id="roleName"
                           value="${reportRoleVo.roleName}"/>
                </p>
            </li>
            <li>
                <p>
                    <span class="author_edit_name">用户类型：</span>
                    <select class="select" style="width:120px;" id="userType">
                        <option id="1" value="1">主办</option>
                        <option id="2" value="2">运营</option>
                        <option id="3" value="3">财务</option>
                        <option id="4" value="4">公安</option>
                    </select>
                </p>
            </li>
            <li>
                <p>报表权限：</p>

                <div class="author_edit_con" id="reportData">
                </div>
            </li>
        </ul>

        <div class="author_btn author_btn2">
            <a href="#" class="btn" onclick="saveRole();">保存</a>
            <a href="#" class="btn" onclick="cancelRole();">取消</a>
        </div>
    </div>
</div>

<!--内容区 end-->

<!--底部 begin-->
<jsp:include page="common/footer.jsp"></jsp:include>
<!--底部 end-->

<!-- 业务处理 -->
<script>

    $(function () {
    	 $("#userType").val('${reportRoleVo.reportUserType}');    	 
        $.ajax({
            type: "POST",
            url: "<%=basePath%>findAllReport.do",
            success: function (resultData) {
            	var ids=",<c:out value='${reportRoleVo.reportVoIds}'/>,";            	
                //加载报表数据
                var rows = eval('(' + resultData + ')').rows;
                for (var index = 0; index < rows.length; index++) {
                    var reportId = ","+rows[index].reportId+",";
                    var reportName = rows[index].reportName;
                    var ck = ids.indexOf(reportId)>-1 ?"checked":"";
                    var value = "<label class='checkbox'><input type='checkbox' name='checkbox' value='"+rows[index].reportId+"'" + ck + " /><span>" + reportName + "</span></label>";
                    $("#reportData").append(value);
                }
            },
            error: function (msg) {
            }
        });
    });

    //保存角色
    function saveRole() {
        if (checkInput()) {
            sendRequest();
        }
    }

    //取消
    function cancelRole() {
        window.location.href = "<%=basePath%>skipToRoleManager.do";
    }

    /**
     * 检查id是否相等
     * @param id
     * @param reportVoIdArr
     * @returns {boolean}
     */
    function checkReportVoIdEqual(id, reportVoIdArr) {
        for (var index = 0; index < reportVoIdArr.length; index++) {
            if (id == reportVoIdArr[index])
                return true;
        }
        return false;
    }

    //输入检查
    function checkInput() {
        var roleName = $("#roleName").val();
        var userType = ($("#userType").val());
        var isCheck = false;
        $("[name='checkbox']").each(function () {
            if ($(this).attr("checked")) {
                isCheck = true;
            }
        });
        var msg = undefined;
        var roleMsg = "请输入角色名称";
        if (roleName == undefined || $.trim(roleName) == "") {
            msg = msg == undefined ? roleMsg : msg + roleMsg;
        }
        var checkMsg = "请选择报表";
        if (!isCheck) {
            msg = msg == undefined ? checkMsg : msg + "\n" + checkMsg;
        }
        if (msg != undefined) {
            alert(msg);
            return false;
        }
        return true;
    }

    //发送请求
    function sendRequest() {
        var roleName = $("#roleName").val();
        var userType = $("#userType").val();
        var reportIds = [];
        $("[name='checkbox']").each(function () {
            if ($(this).attr("checked")) {
            	reportIds.push($(this).attr("value"));
            }
        });
        var url = undefined;
        if ("${reportRoleVo.roleId}" === "-1") {
            url = "<%=basePath%>saveRole.do";
        } else {
            url = "<%=basePath%>modifyRole.do";
        }
        var data = {"reportRoleVo.roleId": '${reportRoleVo.roleId}', "reportRoleVo.roleName": roleName, "reportRoleVo.reportUserType": userType, "reportRoleVo.reportVoIds": reportIds.join(",")};
        $.ajax({
            type: "POST",
            data: data,
            url: url,
            success: function (resultData) {
                window.location.href = "<%=basePath%>skipToRoleManager.do";
            },
            error: function (msg) {
            }
        });
    }

</script>


</body>
</html>
