<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>授权管理_用户管理</title>
    <jsp:include page="common/include.jsp"></jsp:include>
    <script type="text/javascript">
        var userList = "";
        $(function () {
            query();
            $("#dg").datagrid({

                columns: [
                    [
                        {checkbox: true},
                        {field: 'userName', title: '用户名', width: '400'},
                        {field: 'userStatusName', title: '用户状态', width: '300'},
                        {field: 'roleName', title: '用户角色', width: '300',
                            formatter: function (value, row, index) {
                                var userId = row.userId;
                                if (row.editing) {
                                    // $('#dg').datagrid('reload');
                                    if (row.roleId != 0) {
                                        var o = '<select id="role' + userId + '" class="select" style="width: 200px;height: 30px;"><option value="'
                                                + row.roleId + '" style="width: 200px;height: 30px;">' + row.roleName + '</option></select><a href="#" class="ml5" onclick="saveRoleChange('
                                                + userId + ',' + index + ')">保存</a>';
                                    } else {
                                        var o = '<select id="role' + userId + '" class="select" style="width: 200px;height: 30px;"><option  style="width: 200px;height: 30px;">' + row.roleName + '</option></select><a href="#" class="ml5" onclick="saveRoleChange('
                                                + userId + ',' + index + ')">保存</a>';
                                    }

                                    return o;
                                } else {
                                    var o = '<span>' + value + '</span><a href="#" class="ml5" onclick="editRole(' + index + ', ' + userId + ')">编辑</a>';
                                    return o;
                                }
                            }
                        }
                    ]
                ],
                pagination: true,
                onBeforeEdit: function (index, row) {
                    row.editing = true;
                    updateActions();
                },
                onAfterEdit: function (index, row) {
                    row.editing = false;
                    updateActions();
                },
                onCancelEdit: function (index, row) {
                    row.editing = false;
                    updateActions();
                },
                onBeforeLoad: function () {
                    $("#dg").datagrid("clearSelections");
                },
                onLoadSuccess: function (data) {
                    $("#dg").datagrid("clearSelections");
                },
                onLoadError: function () {
                }
            });
        });

        //加载用户所有角色
        function roleSelect(id) {
            $("#role" + id).combobox({
                url: '<%=basePath%>findRoleList.do?id=' + id,
                valueField: 'key',
                textField: 'value',
                editable: false,
                multiple: false
            });
        }

        //启动编辑功能
        function editRole(index, userId) {
            //编辑时，取消其他编辑器的编辑
            var rowcount = $('#dg').datagrid('getRows').length;
            for (var i = 0; i < rowcount; i++) {
                if (i != index) {
                    $('#dg').datagrid('cancelEdit', i);
                }
            }
            //打开编辑器，开始编辑
            $('#dg').datagrid('beginEdit', index);
            roleSelect(userId);
        }

        //更新表格
        function updateActions() {
            var rowcount = $('#dg').datagrid('getRows').length;
            for (var i = 0; i < rowcount; i++) {
                $('#dg').datagrid('updateRow', {
                    index: i,
                    row: {action: ''}
                });
            }
        }

        //保存角色
        function saveRoleChange(userId, index) {
            $.ajax({
                url: '<%=basePath%>saveUserRole.do',
                data: {userIds: userId, userRoleId: $("#role" + userId).combobox("getValue")},
                success: function (msg) {
                    $("#dg").datagrid("reload");
                }
            });
        }


        //执行搜索方法
        function query() {
            //获取查询参数
            var queryData = {"userName": $("#userName").val(),
                "userRoleId": $("#userRoleId").combobox('getValue'),
                "userStatusId": $("#userStatusId").combobox('getValue')};
            $("#dg").datagrid({url: '<%=basePath%>findUserList.do', queryParams: queryData, pageNumber: 1}, 'load');
        }


        //汇总编辑
        function sumModifyRole() {
            var data = $("#dg").datagrid("getSelections");
            var dataLength = data.length;
            if (dataLength < 1) {
                return;
            }

            //初始化数据
            var ids = [];
            $("#userNames").text("");
            for (var i = 0; i < dataLength; i++) {
                ids.push(data[i].userId);
                $("#userNames").append(data[i].userName);
                if (i < dataLength - 1) {
                    $("#userNames").append("、");
                }
            }
            userList = ids.join(",");
            //console.info(userList);
            //弹窗展现
            popLayer($("#layer_edit"));
        }

        //汇总保存
        function sumSaveRoles() {
            cancel();

            $.ajax({
                url: '<%=basePath%>saveUserRole.do',
                data: {
                    userIds: userList,
                    userRoleId: $("#selRole").val()
                },
                success: function (msg) {
                    msg = eval("(" + msg + ")");
                    if (msg.status == 200 && msg.data == 1) {
                        $("#dg").datagrid("reload");
                    } else {
                        alert("编辑失败！");
                    }
                }
            });
        }

        //取消
        function cancel() {
            //            $(".mask").hide();
            //            $(".author_main").hide();
            $(".layer_close").click();
        }
    </script>
</head>

<body>
<!--header begin-->
<jsp:include page="common/top.jsp">
    <jsp:param value="1" name="code"/>
</jsp:include>
<!--header end-->

<!--内容区 begin-->
<div class="author_main clear">
    <div class="author_cho">
        <div class="cho_line">
            <span class="cho_title">用户名：</span> <input id="userName" type="text"
                                                       class="text" style="width: 175px;"/> <span
                class="cho_title ml30">用户角色：</span>
            <select id="userRoleId" class="easyui-combobox" name="userRoleId"
                    style="width: 120px;">
                <option value="0" <c:if test='${userRoleId==0}'>selected</c:if>>全部</option>
                <c:forEach var="item" items="${roles}">
                    <option value="${item.roleId}"
                            <c:if test='${userRoleId==item.roleId}'>selected</c:if>>${item.roleName}</option>
                </c:forEach>
            </select><span class="cho_title ml30">用户状态：</span> <select id="userStatusId"
                                                                       class="easyui-combobox" name="userStatusId"
                                                                       style="width: 120px;">
            <option value="0">全部</option>
            <option value="1">启用</option>
            <option value="2">禁用</option>
        </select> <a class="cho_btn" href="#" onclick="query()">搜索</a>
        </div>
    </div>
    <div class="author_con">
        <table id="dg">
            <thead>
            <tr>
                <th data-options="field:'title01'" width="400">用户名</th>
                <th data-options="field:'title03'" width="300">用户状态</th>
                <th data-options="field:'title04'" width="300">用户角色</th>
            </tr>
            </thead>
        </table>

        <div class="author_btn mt20">
            <a style="float: right; margin: 0;" class="btn" href="#"
               onclick="sumModifyRole()">汇总编辑</a>
        </div>
    </div>
</div>
<!--内容区 end-->

<!--底部 begin-->
<jsp:include page="common/footer.jsp"></jsp:include>
<!--底部 end-->

<!--弹层-->
<div class="author_layer layer_w500" id="layer_edit">
    <div class="layer_title">
        <a class="layer_close" href="#" title="关闭"></a>
        <h4>汇总编辑</h4>
    </div>
    <div class="layer_con">
        <dl class="layer_edit">
            <dt>已选择用户为：</dt>
            <dd id="userNames"></dd>
        </dl>
        <dl class="layer_edit">
            <dt>赋予用户角色：</dt>
            <dd>
                <select id="selRole" class="select"
                        style="width: 120px;">
                    <c:forEach var="item" items="${roles}">
                        <option value="${item.roleId}"
                                <c:if test='${userRoleId==item.roleId}'>selected</c:if>>${item.roleName}</option>
                    </c:forEach>
                </select>
            </dd>
        </dl>
    </div>
    <div class="layer_btn">
        <a class="btn" href="#" onclick="sumSaveRoles();">保存</a><a class="btn" href="#"
                                                                   onclick="cancel()">取消</a>
    </div>
</div>
</body>
</html>