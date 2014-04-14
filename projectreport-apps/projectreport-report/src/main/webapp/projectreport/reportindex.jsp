<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>报表平台_总表</title>
    <jsp:include page="common/include.jsp"></jsp:include>
</head>

<body>
<!--顶部 begin-->
<jsp:include page="/projectreport/common/top.jsp"></jsp:include>
<!--顶部 end-->
<div class="wrap mt20 pb20">
    <div class="r-tit-b clear">
        <span class="fl xs-tit">正在销售项目</span>
        <a href="queryProjectByFilter.do?reportProjectVo.projectStatus=4" class="r-cm-link fr">更多<span class="r-cm">&gt;&gt;</span></a>
    </div>
    <div class="tab-border mt5">
        <table class="right_tab">
            <thead>
            <tr>
                <th width="75"><label class="checl-lab"><input type="checkbox" name="cbAllSale"
                                                               onclick="selAll(this.name);">类别</label></th>
                <th width="60">项目ID</th>
                <th width="130">项目名称</th>
                <th width="60">项目状态</th>
                <th width="110">演出开始时间</th>
                <th width="110">演出结束时间</th>
                <th width="70">项目城市</th>
                <th width="95">演出场馆</th>
                <th class="tc" width="80">本日销售金额</th>
                <th class="tc" width="80">销售总金额</th>
                <th class="tc">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="vo" items="${startVoList}">
                <tr>
                    <td>
                        <label class="checl-lab"><input type="checkbox" name="cbSale" value="${vo.projectId}"
                                                        source="${vo.dataResource}"><c:out value="${vo.className}"/>
                        </label>
                    </td>
                    <td><c:out value="${vo.piaoCnId}"/></td>
                    <td>
                        <div title="${vo.projectName}" class="d-w120"
                             style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
                            <c:out value="${vo.projectName}"/></div>
                    </td>
                    <td><c:out value="${vo.projectStatusName}"/></td>
                    <td>
                        <div title="${vo.startTime}" class="d-w100"
                             style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
                            <c:out value="${vo.startTime}"/></div>
                    </td>
                    <td>
                        <div title="${vo.endTime}" class="d-w100"
                             style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;"><c:out
                                value="${vo.endTime}"/></div>
                    </td>
                    <td title="${vo.performCity}">
                        <div title="${vo.performCity}" class="d-w48"
                             style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;"><c:out
                                value="${vo.performCity}"/></div>
                    </td>
                    <td title="${vo.performField}">
                        <div class="d-w85" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;"><c:out
                                value="${vo.performField}"/></div>
                    </td>
                    <td class="tc"><c:out value="${vo.todayMoney}"/></td>
                    <td class="tc"><c:out value="${vo.totalMoney}"/></td>
                    <td class="tc"><a href="saleReport.do?projectId=${vo.projectId}&source=${vo.dataResource}"
                                      class="r-cm-link">查看报表</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="hz-right mt20"><a class="btn" href="javascript:gotoMultiReport(1);">汇总查看</a></div>
    <div class="r-tit-b clear mt30">
        <span class="fl xs-tit">已结束项目</span>
        <a href="queryProjectByFilter.do?reportProjectVo.projectStatus=5" class="r-cm-link fr">更多<span class="r-cm">&gt;&gt;</span></a>
    </div>
    <div class="tab-border mt5">
        <table class="right_tab">
            <thead>
            <tr>
                <th width="90"><label class="checl-lab"><input type="checkbox" name="cbAllEnd"
                                                               onclick="selAll(this.name);">类别</label></th>
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
            <c:forEach var="vo" items="${endVoList}">
                <tr>
                    <td><label class="checl-lab"><input type="checkbox" name="cbEnd" value="${vo.projectId}"
                                                        source="${vo.dataResource}"><c:out
                            value="${vo.className}"/></label></td>
                    <td><c:out value="${vo.projectId}"/></td>
                    <td>
                        <div title="${vo.projectName}" class="d-w120"
                             style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;"><c:out
                                value="${vo.projectName}"/></div>
                    </td>
                    <td><c:out value="${vo.projectStatusName}"/></td>
                    <td>
                        <div title="${vo.startTime}" class="d-w100"
                             style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;"><c:out
                                value="${vo.startTime}"/></div>
                    </td>
                    <td>
                        <div title="${vo.endTime}" class="d-w100"
                             style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;"><c:out
                                value="${vo.endTime}"/></div>
                    </td>
                    <td title="${vo.performCity}">
                        <div title="${vo.performCity}" class="d-w48"
                             style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;"><c:out
                                value="${vo.performCity}"/></div>
                    </td>
                    <td title="${vo.performField}">
                        <div class="d-w85" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;"><c:out
                                value="${vo.performField}"/></div>
                    </td>
                    <td class="tc"><c:out value="${vo.todayMoney}"/></td>
                    <td class="tc"><c:out value="${vo.totalMoney}"/></td>
                    <td class="tc"><a href="reportIndex.do?projectId=${vo.projectId}&source=${vo.dataResource}"
                                      class="r-cm-link">查看报表</a></td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div>
    <div class="hz-right mt20"><a class="btn" href="javascript:gotoMultiReport(2);">汇总查看</a></div>
</div>
<form id="frmSearch" action="multiProjectReport.do" method="post">
    <input type="hidden" id="projectIds" name="projectIds" value=""></input>
    <input type="hidden" id="ssid" name="ssid" value="<c:out value="${ssid}"></c:out>"></input>
</form>
<!--底部 begin-->
<jsp:include page="/projectreport/common/footer.jsp"></jsp:include>
<!--底部 end-->

<!--汇总提示-->
<div class="author_layer layer_w400" id="huizongtishi">
    <div class="layer_title">
        <a class="layer_close" onclick="TanChuangClosed('huizongtishi')" title="关闭"></a>
        <h4>温馨提示</h4>
    </div>

    <div class="layer_con">
        <div class="layer_txt tc">抱歉，您未选择需要汇总查看项目！ </div>
    </div>
    <div class="layer_btn"><a class="btn" onclick="TanChuangClosed('huizongtishi')" >确定</a></div>
</div>

<!--项目ID下拉层-->
<div class="query-down" pid="pro-id">
    <ul class="query-down-list">
        <li><a href="#">项目ID1</a></li>
        <li><a href="#">项目ID1</a></li>
        <li><a href="#">项目ID1</a></li>
    </ul>
</div>


<script type="text/javascript">
    function selAll(ctlName) {
        var ckd = $("[name='" + ctlName + "']").attr("checked") ? true : false;
        if (ctlName == "cbAllSale") {
            $("[name='cbSale']").each(function (i) {
                $(this).attr("checked", ckd);
            });
        }
        if (ctlName == "cbAllEnd") {
            $("[name='cbEnd']").each(function (i) {
                $(this).attr("checked", ckd);
            });
        }
    }

    function gotoMultiReport(type) {
        var sel = [];
        if (type == 1) {
            $("[name='cbSale']").each(function (i) {
                if ($(this).attr("checked")) {
                    sel.push($(this).attr("value") + ":" + $(this).attr("source"));
                }
            });
        } else if (type == 2) {
            $("[name='cbEnd']").each(function (i) {
                if ($(this).attr("checked")) {
                    sel.push($(this).attr("value") + ":" + $(this).attr("source"));
                }
            });
        }
        if (sel.length < 1) {
            popLayer("#huizongtishi");
        } else {
            $("#projectIds").val(sel.join(","));
            /* var projectIds = $("#projectIds").val();
             alert(projectIds); */
            $("#frmSearch").submit();
        }
    }


    $(function () {
        showDropFun(".inp-txt1")
    })

    function showDropFun(ele) {
        var $ele = $(ele);
        $ele.each(function () {
            $(this).click(function () {
                var $this = $(this),
                        $pid = $this.attr("pid"),
                        $X = $this.offset().left,
                        $Y = $this.offset().top,
                        $W = $this.outerWidth() - 2,
                        $pop = $('div[pid|=' + $pid + ']');
                $pop.show().css({
                    "width": $W + "px",
                    "left": $X + "px",
                    "top": $Y + 20 + "px"
                });

                $("*").click(function (e) {
                    if (!($(this).attr("pid") == $pid)) {
                        $pop.hide()
                    }
                    e.stopPropagation();
                    return false;
                })

            })
        })
    }
    
    //<!--弹层-->
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
    }
    
</script>
</body>
</html>
