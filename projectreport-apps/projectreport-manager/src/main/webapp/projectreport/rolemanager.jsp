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
    <title>授权管理_角色管理</title>
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
    <div class="author_con" style="border-bottom:none;">
        <table id="role-datagrid">
            <thead>
            <tr>
                <th data-options="field:'title01'" width="170">角色名</th>
                <th data-options="field:'title02',align:'center'" width="400">报表权限</th>
                <th data-options="field:'title04',align:'center'" width="240">用户类型</th>
                <th data-options="field:'title05',align:'center'" width="160">操作</th>
            </tr>
            </thead>
        </table>
    </div>

    <div class="author_btn mt20">
        <a href="#" class="btn" style="float:right; margin:0;" onclick="createRole();">新建角色</a>
    </div>
</div>

<!--内容区 end-->

<!--底部 begin-->
<jsp:include page="common/footer.jsp"></jsp:include>
<!--底部 end-->

<script type="text/javascript">
    $(function () {
        $("#role-datagrid").datagrid({
            url: '<%=basePath%>queryAllReportRoles.do',
            columns: [
                [
                    {
                        field: 'roleName',
                        title: '角色名',
                        width: '170',
                        formatter: function (value, row, index) {
                            return '<div class="tab_w120">' + row.roleName + '</div>';
                        }
                    },
                    {
                        field: 'reportName',
                        title: '报表权限',
                        width: '400',
                        formatter: function (value, row, index) {
                            var reportVoList = row.reportVoList;
                            var value = '<div class="tab_total">';
                            for (var index = 0; index < reportVoList.length; index++) {
                                var reportVo = reportVoList[index];
                                value += '<p>' + reportVo.reportName + '</p>';
                            }
                            value += '</div>';
                            return value;
                        }
                    },
                    {
                        field: 'reportUserTypeName',
                        title: '用户类型',
                        width: '240'
                    },
                    {
                        field: 'userName',
                        title: '操作',
                        width: '160',
                        formatter: function (value, row, index) {
                            var value = '<a href="#" onclick="modifyRole(' + index + ');">编辑</a>';
                            return value;
                        }
                    }

                ]
            ],
            onBeforeLoad: function () {
                $("#role-datagrid").datagrid("loadData", {"dataObj": null, "total": "0", "rows": []});
                $("#role-datagrid").datagrid("clearSelections");
            },
            onLoadSuccess: function (returnData) {
            }
        });
    })


    //新建角色
    function createRole() {
        window.location.href = "<%=basePath%>skipToRoleEdit.do";
    }


    //编辑角色
    function modifyRole(index) {
        $("#role-datagrid").datagrid("selectRow", index);
        var roleId = $("#role-datagrid").datagrid("getSelected").roleId;
        window.location.href = "<%=basePath%>skipToRoleEdit.do?reportRoleVo.roleId=" + roleId;
    }

</script>
</body>
</html>
