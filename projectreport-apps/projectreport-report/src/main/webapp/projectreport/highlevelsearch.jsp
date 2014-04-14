<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>报表平台_高级搜索</title>
    <jsp:include page="common/include.jsp"></jsp:include>
    <link rel="stylesheet" href="<%=basePath%>thirdparty/jquery-autocomplete/jquery.autocomplete.css">
    <script type="text/javascript"
            src="<%=basePath%>thirdparty/jquery-autocomplete/jquery.autocomplete.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>projectreport/common/searchTemplate.js"></script>
    <script type="text/javascript">
        var path = "<%=basePath%>";
    </script>
</head>

<body>
<!--顶部 begin-->
<jsp:include page="/projectreport/common/top.jsp">
    <jsp:param value="2" name="code"/>
</jsp:include>
<!--顶部 end-->

<div class="m-nav">
    <div class="wrap">当前所在位置：高级查询</div>
</div>
<div class="wrap min550 clear">
    <form id="frmTemplete" action="queryProjectByFilter.do" method="post">
        <div class="r-tit mt10">项目筛选</div>
        <div class="r-saixuan-l mt20 fl">
            <!--回显模板id  -->
            <input type="hidden" id="modifyTemplateID" value=""/>

            <div class="clear">
                <label class="for-lab">项目类别：</label>
                <select class="sel-s1 w132 mr35 fl" id="className" name="reportProjectVo.categoryID">
                    <option value="0">全部</option>
                </select>
                <label class="for-lab">项目状态：</label>
                <select class="sel-s1 w132 fl" id="projectStatus" name="reportProjectVo.projectStatus">
                    <option value="0">全部</option>
                    <option value="4">正在销售</option>
                    <option value="5">已结束</option>

                </select>
            </div>
            <div class="clear mt15 por">
                <label class="for-lab">项目ID：</label>

                <div class="s-inp-f fl" style="position: relative;">
                    <input class="inp-txt1 w110 mr35" type="text" pid="pro-id" id="projectId"
                           name="reportProjectVo.piaoCnId" value=""/>
                </div>
                <label class="for-lab fl">项目城市：</label>

                <div class="s-inp-f fl">
                    <input class="inp-txt1 w110" pid="pro-city" type="text" id="performCity"
                           name="reportProjectVo.performCity" value=""/>
                </div>
            </div>

            <div class="clear mt15">
                <label class="for-lab">项目名称：</label>

                <div class="s-inp-f fl">
                    <input class="inp-txt1 w353" pid="pro-name" type="text" id="projectName"
                           name="reportProjectVo.projectName" value=""/>
                </div>
            </div>

            <div class="clear mt15">
                <label class="for-lab">演出时间：</label>
                <input class="inp-txt1 fl w157 Wdate" type="text" id="startTime" name="reportProjectVo.startTime"
                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endTime\');}'})" value=""/>
                <span class="r-flag fl">至</span>
                <input class="inp-txt1 w157 fl Wdate" type="text" id="endTime" name="reportProjectVo.endTime"
                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endTime\');}'})" value=""/>
            </div>

            <div class="clear mt15">
                <label class="for-lab">演出场馆：</label>

                <div class="s-inp-f fl">
                    <input class="inp-txt1 w353" pid="pro-name" type="text" id="performField"
                           name="reportProjectVo.performField" value=""/>
                </div>
            </div>

            <div class="r-pl mt30">
                <a class="btn mr10" href="javascript:queryProject();" id="sure">查询</a>
                <!--onclick="popLayer('#layer_save'); return false"  -->
                <a href="javascript:checkTemplate();" id="saveTemplate" class="blue-link mr10">保存为模板</a>
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
    </form>
</div>

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
    <div class="layer_btn"><a class="btn" href="#" onclick="saveTemplate(this);">确定</a><a class="btn" href="#"
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

    <div class="layer_btn"><a class="btn" href="#" onclick="deleteTemplate(this);">确定</a><a class="btn" href="#"
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

    <div class="layer_btn"><s:hidden id="templateID"/><a class="btn" href="#" onclick="MergeTemplate()">确定</a><a
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

    //查询项目类别
    $.ajax({
        url: "<%=basePath%>queryProjectClass.do",
        success: function (returnData) {
            var rows = eval('(' + returnData + ')');
            for (var index = 0; index < rows.length; index++) {
                $("#className").append("<option value=" + rows[index].projectClassID + ">" + rows[index].className + "</option>");
            }
        }
    });
</script>
</body>

</html>
