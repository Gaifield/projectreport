<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>报表平台_选择项目</title>
    <jsp:include page="common/include.jsp"></jsp:include>
    <link rel="stylesheet" href="<%=basePath%>thirdparty/jquery-autocomplete/jquery.autocomplete.css"/>
    <script type="text/javascript"
            src="<%=basePath%>thirdparty/jquery-autocomplete/jquery.autocomplete.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>projectreport/common/searchTemplate.js"></script>
    <script type="text/javascript">
        var path = "<%=basePath%>";
    </script>
</head>

<body>
<!--顶部 begin-->
<jsp:include page="/projectreport/common/top.jsp"></jsp:include>
<!--顶部 end-->

<div class="m-nav">
    <div class="wrap">当前所在位置：<a class="col-b-link" href="showTemplate.do">高级查询</a><span class="r-cm">&gt;</span>选择项目
    </div>
</div>
<div class="wrap min550 clear">
<form id="frmTemplete" action="queryProjectByFilter.do" method="post">
    <div class="r-tit mt10">项目筛选</div>
    <div class="r-search-t clear hid">
        <div class="r-saixuan-l mt20 fl">
            <!--回显模板id  -->
            <input type="hidden" id="modifyTemplateID" value=""/>

            <div class="clear">
                <label class="for-lab">项目类别：</label>
                <select class="sel-s1 w132 mr35 fl" id="className" name="reportProjectVo.categoryID" value="0">
                    <option value="0" selected="selected">全部</option>
                </select>
                <label class="for-lab">项目状态：</label>
                <select class="sel-s1 w132 fl" id="projectStatus" name="reportProjectVo.projectStatus">
                    <option value="0" selected="selected">全部</option>
                    <option value="4">正在销售</option>
                    <option value="5">已结束</option>
                </select>
            </div>
            <div class="clear mt15 por">
                <label class="for-lab">项目ID：</label>

                <div style="position: relative;" class="s-inp-f fl">
                    <input type="text" pid="pro-id" class="inp-txt1 w110 mr35" id="projectId"
                           name="reportProjectVo.piaoCnId"
                           value="${reportProjectVo.piaoCnId}"/>
                </div>
                <label class="for-lab fl">项目城市：</label>

                <div class="s-inp-f fl">
                    <input type="text" pid="pro-city" class="inp-txt1 w110" id="performCity"
                           name="reportProjectVo.performCity" value="<c:out value="${reportProjectVo.performCity}"/>"/>
                </div>
            </div>
            <div class="clear mt15">
                <label class="for-lab">项目名称：</label>

                <div class="s-inp-f fl">
                    <input type="text" pid="pro-name" class="inp-txt1 w353" id="projectName"
                           name="reportProjectVo.projectName" value="<c:out value="${reportProjectVo.projectName}"/>"/>
                </div>
            </div>
            <div class="clear mt15">
                <label class="for-lab">演出时间：</label>
                <input type="text" class="inp-txt1 fl w157 Wdate" id="startTime"
                       onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endTime\');}'})"
                       name="reportProjectVo.startTime"
                       value="<c:out value="${reportProjectVo.startTime}"/>"/>
                <span class="r-flag fl">至</span>
                <input type="text" class="inp-txt1 w157 fl Wdate" id="endTime"
                       onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'startTime\');}'})"
                       name="reportProjectVo.endTime"
                       value="<c:out value="${reportProjectVo.endTime}"/>"/>

            </div>
            <div class="clear mt15">
                <label class="for-lab">演出场馆：</label>

                <div class="s-inp-f fl">
                    <input type="text" pid="pro-name" class="inp-txt1 w353" id="performField"
                           name="reportProjectVo.performField" value="<c:out value="${reportProjectVo.performField}"/>"/>
                </div>
            </div>
            <div class="r-pl mt30">
                <a href="javascript:queryProjectOther();" class="btn mr10" id="sure">查询</a>
                <a href="javascript:checkTemplate();" class="blue-link mr10" id="saveTemplate">保存为模板</a>
                <a href="#" onclick="empty();" id="reset" class="blue-link">清空</a>
                <a class="btn mr10" href="#" id="sub" onclick="modifyTemplate();" style="display:none;">保存</a>
                <a href="#" onclick="returnSet();" id="returnSet" class="blue-link" style="display:none;">取消</a>
            </div>
        </div>
        <div class="r-saixuan-r mt20 fr">
            <div class="r-sa-tit">项目搜索模板</div>
            <table class="r-edit-tab">
                <tbody>
                <s:iterator value="#request.searchTemplateVos" var="Vos">
                    <tr>
                        <td width="140">
                            <div class="d-w100" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
                                <a title="<s:property value="templateName"/>" href="#"
                                   onclick="templateNameSearcher(<s:property value="templateId"/>);"><s:property
                                        value="templateName"/>
                                    <span class="f-s"></span>
                                </a>
                            </div>
                        </td>
                        <td class="tc">
                            <a href="#" onclick="updateTemplate(<s:property value="templateId"/>);">编辑</a>
                            <span class="plr5">|</span>
                            <a href="#" onclick="openDeleteTemplate(<s:property value="templateId"/>);">删除</a>
                        </td>
                    </tr>
                    <input type="hidden" id="delTemplate<s:property value="templateId"/>"
                           value="<s:property value="templateName"/>"/>
                </s:iterator>
                </tbody>
            </table>
        </div>
    </div>

    <input type="hidden" id="page" name="reportProjectVo.page" value="${reportProjectVo.page}"/>
    <input type="hidden" id="pageSize" name="reportProjectVo.pageSize" value="${reportProjectVo.pageSize}"/>

</form>
<div class="show-cnt cl-b pb20">
    <div class="r-show">
        <a class="r-show-btn" href="javascript:;">▲</a>
    </div>
    <div class="tab-border mt30">
        <table class="right_tab">
            <thead>
            <tr>
                <th width="90"><label class="checl-lab"><input type="checkbox" name="cbAllSale"
                                                               onclick="selAll(this.name);"/>类别</label></th>
                <th width="60">项目ID</th>
                <th width="130">项目名称</th>
                <th width="60">项目状态</th>
                <th width="110">演出开始时间</th>
                <th width="110">演出结束时间</th>
                <th width="70">项目城市</th>
                <th width="80">演出场馆</th>
                <th class="tc" width="80">本日销售金额</th>
                <th class="tc" width="80">销售总金额</th>
                <th class="tc">操作</th>
            </tr>
            </thead>
            <tbody>
            <s:set name="dataList" value="#request.pageResult.rows"/>
            <s:iterator value="#dataList" id="reportProjectVo" var="reportProjectVo" status="voStat">
                <tr>
                    <!--复选框-->
                    <td>
                        <label class="checl-lab">
                            <input type="checkbox" name="cbSale" value="<s:property value='projectId'/>"
                                   source="<s:property value='dataResource'/>"/>
                            <s:property value="className"/>
                        </label>
                    </td>
                    <td><s:property value="piaoCnId"/></td>
                    <td>
                        <div class="d-w110" title="<s:property value="projectName"/>"
                             style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
                            <s:property value="projectName"/>
                        </div>
                    </td>
                    <td><s:property value="projectStatusName"/></td>
                    <td>
                        <div class="d-w100" title="<s:property value="startTime"/>"
                             style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
                            <s:property value="startTime"/>
                        </div>
                    </td>
                    <td>
                        <div class="d-w100" title="<s:property value="endTime"/>"
                             style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
                            <s:property value="endTime"/>
                        </div>
                    </td>
                    <td>
                        <div class="d-w60" title="<s:property value="performCity"/>"
                             style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
                            <s:property value="performCity"/>
                        </div>
                    </td>
                    <td title="<s:property value="performField"/>">
                        <div class="d-w70" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
                            <s:property value="performField"/></div>
                    </td>
                    <td class="tc"><s:property value="todayMoney"/></td>
                    <td class="tc"><s:property value="totalMoney"/></td>
                    <td class="tc">
                        <a class="r-cm-link"
                           href="saleReport.do?projectId=<s:property value='projectId'/>&source=<s:property value='dataResource'/>">查看报表</a>
                    </td>
                </tr>
            </s:iterator>

            </tbody>
        </table>
    </div>
    <div class="right_stern">
        <script type="text/javascript">
            var pg = new showPages(
                    {
                        name: 'pg',
                        total: parseInt('${pageResult.total}'),
                        page: parseInt('${reportProjectVo.page}'),
                        size: parseInt('${reportProjectVo.pageSize}'),
                        paging: function (page) {
                            pageChang(page);
                        },
                        info: "项目信息"

                    });
            pg.printHtml(0);
        </script>
    </div>
    <div class="hz-right mt20"><a href="javascript:gotoMultiReport();" class="btn">查看汇总</a></div>
</div>
</div>
<form id="frmSearch" action="multiProjectReport.do" method="post">
    <input type="hidden" id="projectIds" name="projectIds" value=""></input>
</form>

<!--底部 begin-->
<jsp:include page="/projectreport/common/footer.jsp"></jsp:include>
<!--底部 end-->
<!--保存模板提示层-->
<div class="author_layer layer_w400" id="layer_save">
    <div class="layer_title">
        <a class="layer_close" href="#" title="关闭"></a>
        <h4>温馨提示</h4>
    </div>
    <div class="layer_con">
        <div class="layer_txt">
            <label>请填写模板名称：</label><input class="inp-txt1 w157" id="Name" type="text"/>
        </div>
    </div>
    <!--popLayer('#layer_re')  -->
    <div class="layer_btn"><a class="btn" href="#" onclick="saveTemplateOther(this);">确定</a><a class="btn" href="#"
                                                                                          onclick="closeLayer(this);">取消</a>
    </div>
</div>

<!--删除模板提示层-->
<div class="author_layer layer_w400" id="layer_del">
    <div class="layer_title">
        <a class="layer_close" href="#" title="关闭"></a>
        <h4>温馨提示</h4>
    </div>
    <s:hidden id="delTemplate"/>
    <div class="layer_con">
        <div class="layer_txt tc" id="delTem">
        </div>
    </div>

    <div class="layer_btn"><a class="btn" href="#" onclick="deleteTemplateOther(this);">确定</a><a class="btn" href="#"
                                                                                            onclick="closeLayer(this);">取消</a>
    </div>
</div>

<!--模板重名提示层-->
<div class="author_layer layer_w400" id="layer_re">
    <div class="layer_title">
        <a class="layer_close" href="#" title="关闭"></a>
        <h4>温馨提示</h4>
    </div>

    <div class="layer_con">
        <div class="layer_txt tc" id="templateMerge">
        </div>
    </div>

    <div class="layer_btn"><s:hidden id="templateID"/><a class="btn" href="#" onclick="MergeTemplateOther()">确定</a><a
            class="btn" href="#" onclick="closeLayer(this);">取消</a></div>
</div>

<!--提示-->
<div class="author_layer layer_w400" id="tishi">
    <div class="layer_title">
        <a class="layer_close" onclick="TanChuangClosed('tishi')" title="关闭"></a>
        <h4>温馨提示</h4>
    </div>

    <div class="layer_con">
        <div class="layer_txt tc" id="tishiMessage"></div>
    </div>
    <div class="layer_btn"><a class="btn" onclick="TanChuangClosed('tishi')" >确定</a></div>
</div>
<script type="text/javascript">

function selAll(ctlName) {
    var ckd = $("[name='" + ctlName + "']").attr("checked") ? true : false;
    $("[name='cbSale']").each(function (i) {
        $(this).attr("checked", ckd);
    });
}

function gotoMultiReport() {
    var sel = [];
    $("[name='cbSale']").each(function (i) {
        if ($(this).attr("checked")) {
            sel.push($(this).attr("value") + ":" + $(this).attr("source"));
        }
    });
    if (sel.length < 1) {
    $("#tishiMessage").text("抱歉，您未选择需要汇总查看项目！");
         popLayer("#tishi");
    } else {
        $("#projectIds").val(sel.join(","));
        $("#frmSearch").submit();
    }
}

function queryProjectOther() {
    var tag = true;
    var intZ = /^[1-9]\d*$/;
    var projectId = $("#projectId").val();
    if (projectId != "" && !intZ.test(projectId)) {
        tag = false;
    }
    if (tag == false) {
        $("#tishiMessage").text("项目ID格式错误！请重新输入！");
         popLayer("#tishi");
        return;
    }
    $("#page").attr("value", 0);
    $("#frmTemplete").submit();
}
$(function () {
    //查询面板显示、隐藏
    var ibtn = true;
    $(".r-show-btn").click(function () {
        if (ibtn) {
            $(".r-search-t").show();
            $(this).html("▲");
            ibtn = false;
        } else {
            $(".r-search-t").hide();
            $(this).html("▼");
            ibtn = true;
        }
    })

    //查询项目类别
    $.ajax({
        url: "<%=basePath%>queryProjectClass.do",
        type: "post",
        success: function (returnData) {
            var rows = eval('(' + returnData + ')');
            for (var index = 0; index < rows.length; index++) {
                $("#className").append("<option value=" + rows[index].categoryID + ">" + rows[index].className + "</option>");
            }
            $("#className").attr("value", '${reportProjectVo.categoryID}');
            $("#projectStatus").attr("value", '${reportProjectVo.projectStatus}');
            if ($("#className").val() == null || $("#className").val() == "") {
                $("#className").attr("value", 0);
            }
        }
    });


    //查询城市
    $("#performCity").autocomplete("<%=basePath%>queryPerformCityByKeyWord.do", {
        extraParams: {
            "reportProjectVo.performCity": function () {
                return $('#performCity').val();
            }
        },
        delay: 10,
        matchCase: false,
        selectFirst: true,
        cacheLength: 3,
        matchSubset: false,
        minChars: 1,     //最少输入字条
        max: 30,
        autoFill: false,    //是否选多个,用","分开
        //mustMatch: true,    //是否全匹配, 如数据中没有此数据,将无法输入
        matchContains: false,   //是否全文搜索,否则只是前面作为标准
        autoFill: false,    //自动填充
        scrollHeight: 220,
        width: 130,
        multiple: false,
        dataType: 'json',
        parse: function (data) {
            var rows = new Array();
            for (var index = 0; index < data.length; index++) {
                var tempData = data[index];
                rows[index] = {data: tempData.areaName, value: tempData, result: tempData.areaName};
            }
            return rows;
        },
        formatItem: function (row, i, n) {
            return row;
        }

    });

    //查询项目名称
    $("#projectName").autocomplete("<%=basePath%>queryProjectNameByKeyWord.do", {
        extraParams: {
            "reportProjectVo.projectName": function () {
                return $('#projectName').val();
            }
        },
        delay: 10,
        matchCase: false,
        selectFirst: true,
        cacheLength: 3,
        matchSubset: false,
        minChars: 1,     //最少输入字条
        max: 30,
        autoFill: false,    //是否选多个,用","分开
        //mustMatch: true,    //是否全匹配, 如数据中没有此数据,将无法输入
        matchContains: false,   //是否全文搜索,否则只是前面作为标准
        autoFill: false,    //自动填充
        scrollHeight: 220,
        width: 358,
        multiple: false,
        dataType: 'json',
        parse: function (data) {
            var rows = new Array();
            for (var index = 0; index < data.length; index++) {
                var tempData = data[index];
                rows[index] = {data: tempData.projectName, value: tempData, result: tempData.projectName};
            }
            return rows;
        },
        formatItem: function (row, i, n) {
            return row;
        }

    });

    //查询演出场馆
    $("#performField").autocomplete("<%=basePath%>queryPerformFieldByKeyWord.do", {
        extraParams: {
            "reportProjectVo.performField": function () {
                return $('#performField').val();
            }
        },
        delay: 10,
        matchCase: false,
        selectFirst: true,
        cacheLength: 3,
        matchSubset: false,
        minChars: 1,     //最少输入字条
        max: 30,
        autoFill: false,    //是否选多个,用","分开
        //mustMatch: true,    //是否全匹配, 如数据中没有此数据,将无法输入
        matchContains: false,   //是否全文搜索,否则只是前面作为标准
        autoFill: false,    //自动填充
        scrollHeight: 220,
        width: 357,
        multiple: false,
        dataType: 'json',
        parse: function (data) {
            var rows = new Array();
            for (var index = 0; index < data.length; index++) {
                var tempData = data[index];
                rows[index] = {data: tempData.name, value: tempData, result: tempData.name};
            }
            return rows;
        },
        formatItem: function (row, i, n) {
            return row;
        }

    });
})

function pageChang(page) {
    $("#page").val(page);
    $("#frmTemplete").submit();
}

<!--弹层-->
function popLayer(ele) {
    var $ele = $(ele),
            $win = $(window),
            $winH = $win.height(),
            $winW = $win.width(),
            $mask = $("<div class='mask'></div>"),
            $eleW = 0,
            $eleH = 0,
            $docS = $(document).scrollTop();
    $("body").append($mask.css({
        "height": $(document).height() + "px"
    }))

    $ele.show().css({
        "top": ($winH - $ele.height()) / 2 + $docS + "px",
        "left": ($winW - $ele.width()) / 2 + "px",
        "zIndex": $mask.css("z-index") + 1
    })

    $(".layer_close").click(function () {
        $(this).parents(".author_layer").hide();
        $mask.remove();
        return false;
    })
}


</script>
</body>
</html>
