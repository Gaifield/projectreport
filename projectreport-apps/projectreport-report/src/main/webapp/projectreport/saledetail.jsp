<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>报表平台_报表查询-出票明细</title>
    <jsp:include page="common/include.jsp"></jsp:include>
</head>

<body>
<!--顶部 begin-->
<jsp:include page="/projectreport/common/top.jsp"></jsp:include>
<!--顶部 end-->
<div class="m-nav">
    <div class="wrap">
        当前所在位置：
        <jsp:include page="/projectreport/common/location.jsp"></jsp:include>
        出票汇总表
    </div>
</div>
<!--内容区 begin-->
<div class="mai_main clear">
    <!-- 左侧导航-->
    <jsp:include page="/projectreport/common/navLeft.jsp"></jsp:include>
    <!--右侧内容-->
    <div class="mai_wrap">
        <div class="right_ticket por">
            <div class="ticket_nav-bt">
                <ul class="ticket_nav fl">
                    <li><a href="javascript:void(0)" onclick="skipToSale();">汇总</a></li>
                    <li class="active"><a href="javascript:void(0)">明细</a></li>
                </ul>
                <div class="fr">
                    <a class="back-link fr" href="javascript:history.back(-1);">返回<span class="r-cm">&gt;&gt;</span></a>
                    <a class="down-btn mt7 mr10 fr"
                       href="javascript:searchPageData(2);"> <span><em
                            class="down-ico"></em>导出pdf</span>
                    </a> <a class="down-btn mt7 mr10 fr"
                            href="javascript:searchPageData(1);"> <span><em
                        class="down-ico"></em>导出excel</span>
                </a>
                </div>
            </div>
            <div class="r-cnt">
                <div class="r-biaoti">
                    <span><c:out value="${saleVoList[0].projectName}"></c:out></span>
                    <a class="r-choose fr" href="javascript:;" onclick="showPop()">重选场次</a>
                </div>
                <form id="saleFormByTime" method="post" action="saleReportDetail.do">
                    <input type="hidden" name="source" value="<c:out value="${source}"></c:out>"/>
                    <input type="hidden" name="action" value="0">
                    <input type="hidden" name="projectId" value="<c:out value="${projectId}"></c:out>">
                    <input type="hidden" name="performIds" value="<c:out value="${performIds}"/>">
                    <input type="hidden" name="page" value="<c:out value="${page}"/>">

                    <div class="r-rep-cnt">
                        <span class="fl">按销售时间查看：</span>
                        <label class="radio-label fl mr20">
                            <input type="radio" name="filterVo.dateRadio" value="0">
                            全部
                        </label>
                        <label class="radio-label fl mr20">
                            <input type="radio" name="filterVo.dateRadio" value="1">
                            本月
                        </label>
                        <label class="radio-label fl mr20">
                            <input type="radio" name="filterVo.dateRadio" value="2">
                            上月
                        </label>
                        <label class="radio-label fl mr20">
                            <input type="radio" name="filterVo.dateRadio" value="3">
                            自定义
                        </label>

                        <div class="r-date-cnt">
                            <input class="inp-txt1 w110 Wdate" type="text" pid="pro-id" name="filterVo.startTime"
                                   id="saleStartTime" value="<c:out value="${filterVo.startTime}"/>"
                                   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endTime\');}'})">
                            <span>至</span>
                            <input class="inp-txt1 w110 Wdate" type="text" pid="pro-id" name="filterVo.endTime"
                                   id="saleEndTime" value="<c:out value="${filterVo.endTime}"/>"
                                   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'startTime\');}'})">
                        </div>

                        <a class="cho_btn cho_btn-pos" href="javascript:void(0)" onclick="querySaleByTime(0);">搜索</a>
                    </div>
                </form>

                <c:forEach var="saleVo" items="${pageData.rows}">
                    <div class="cc-title-itme mt20">
                            <c:out value="${saleVo.performName}---"></c:out><span class="col-b-link"><c:out value="${saleVo.performTime}"></c:out></span>
                    </div>

                    <div class="r-table-cnt mt10">
                        <table class="up-report-table"
                               id="saleTable_${saleVo.performId}_${saleVo.allDisaccountVoList.size()+3}">
                            <thead>
                            <tr>
                                <th colspan="2" rowspan="2">票价</th>
                                <th rowspan="2">可售总票房</th>
                                <th class="border-bot" colspan="${saleVo.allDisaccountVoList.size()+3}">出票</th>
                                <th width="140" rowspan="2">剩余票房</th>
                                <th width="140" rowspan="2">预留票房</th>
                                <th width="140" rowspan="2">当前可售票房</th>
                            </tr>
                            <tr class="min100">
                                <c:forEach var="disaccountName"
                                           items="${saleVo.allDisaccountVoList}">
                                    <th><c:out value="${disaccountName}"></c:out></th>
                                </c:forEach>
                                <th>赠票出票</th>
                                <th>工作票出票</th>
                                <th>小计</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="saleRowVo" items="${saleVo.saleRowVoList}">
                                <tr>
                                    <td width="120" rowspan="2">
                                            <c:out value="${saleRowVo.priceVo.priceShowName}"></c:out></td>
                                    <td width="120">数量（张）</td>
                                    <td width="139"><c:out value="${saleRowVo.priceTotalSale.quantity}"></c:out></td>
                                    <c:forEach var="disaccountVo"
                                               items="${saleRowVo.disaccountVoList}">
                                        <td width="139"><c:out value="${disaccountVo.quantity}"></c:out></td>
                                    </c:forEach>
                                    <td width="139"><c:out value="${saleRowVo.presentSale.quantity}"></c:out></td>
                                    <td width="139"><c:out value="${saleRowVo.staffSale.quantity}"></c:out></td>
                                    <td width="139"><c:out value="${saleRowVo.totalSaleCell.quantity}"></c:out></td>
                                    <td width="139"><c:out value="${saleRowVo.leftSale.quantity}"></c:out></td>
                                    <td width="139"><c:out value="${saleRowVo.reserveSale.quantity}"></c:out></td>
                                    <td width="139"><c:out value="${saleRowVo.availableSale.quantity}"></c:out></td>
                                </tr>
                                <tr>
                                    <td width="110">金额（元）</td>
                                    <td width="139"><c:out value="${saleRowVo.priceTotalSale.amount}"></c:out></td>
                                    <c:forEach var="disaccountVo"
                                               items="${saleRowVo.disaccountVoList}">
                                        <td width="139"><em class="cash">￥</em><c:out value="${disaccountVo.amount}"></c:out></td>
                                    </c:forEach>
                                    <td width="139"><em class="cash">￥</em><c:out value="${saleRowVo.presentSale.amount}"></c:out></td>
                                    <td width="139"><em class="cash">￥</em><c:out value="${saleRowVo.staffSale.amount}"></c:out></td>
                                    <td width="139"><em class="cash">￥</em><c:out value="${saleRowVo.totalSaleCell.amount}"></c:out></td>
                                    <td width="139"><em class="cash">￥</em><c:out value="${saleRowVo.leftSale.amount}"></c:out></td>
                                    <td width="139"><em class="cash">￥</em><c:out value="${saleRowVo.reserveSale.amount}"></c:out></td>
                                    <td width="139"><em class="cash">￥</em><c:out value="${saleRowVo.availableSale.amount}"></c:out></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                            <tfoot>
                            <tr class="fb">
                                <td class="tc" width="231">附加出票：</td>
                                <td width="139">工作票（张）：</td>
                                <td width="139"><c:out value="${saleVo.staffQuantity}" ></c:out></td>
                                <td width="139">防涨票（张）：</td>
                                <td width="139"><c:out value="${saleVo.protectQuantity}" ></c:out></td>
                                <td class="tc" width="695" colspan="${saleVo.allDisaccountVoList.size()+4}">
                                    注：附加出票为场馆，公安工作票，防涨票出票
                                </td>
                            </tr>
                            <tr class="fb">
                                <td class="tc">总计出票数量（张）：</td>
                                <td colspan="2"><c:out value="${saleVo.totalQuantity}" ></c:out></td>
                                <td colspan="2">
                                    <div class="fix-fr">总计金额（元）：</div>
                                <td colspan="${saleVo.allDisaccountVoList.size()+4}">
                                    <em class="cash">￥</em><c:out value="${saleVo.totalAmount}" ></c:out></td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>

                </c:forEach>
                <div class="right_stern">
                    <form id="frmSearch" method="post"
                          action="<%=basePath%>saleReportDetail.do">
                        <input type="hidden" id="source" name="source" value="<c:out value="${source}" ></c:out>"/>
                        <input type="hidden" id="action" name="action" value="0"/>
                        <input type="hidden" id="projectId" name="projectId" value="<c:out value="${projectId}" ></c:out>"/>
                        <input type="hidden" id="performIds" name="performIds" value="<c:out value="${performIds}" ></c:out>"/>
                        <input type="hidden" id="page" name="page" value="<c:out value="${page}" ></c:out>"/>
                    </form>
                    <script type="text/javascript">
                        var pg = new showPages(
                                {
                                    name: 'pg',
                                    total: parseInt('${pageData.total}'),
                                    size: parseInt('${rows}'),
                                    page: parseInt('${page}'),
                                    paging: function (page) {
                                        pageChang(page);
                                    }
                                });
                        pg.printHtml(0);
                    </script>
                </div>
            </div>
        </div>
    </div>
</div>

<!--内容区 end-->

<!--底部 begin-->
<jsp:include page="/projectreport/common/footer.jsp"></jsp:include>
<!--底部 end-->

<!--重选场次弹出层 开始-->
<div class="choose-cc">
    <a class="c-pop-close" href="javascript:;">✖</a>

    <form id="frmSearchPerform">
        <input type="hidden" name="source" value="<c:out value="${source}"/>"/> <input
            type="hidden" name="projectId" value="<c:out value="${projectId}"/>"/>

        <div class="r-search pl10 clear">
            <label>按演出时间搜索：</label>
            <input class="inp-txt1 w157 Wdate" type="text" id="startTime" name="startTime" pattern="yyyy-MM-dd HH:mm"
                   onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endTime\');}'})"/>
            <span class="r-flag">至</span> <input class="inp-txt1 w157 Wdate"
                                                 type="text" id="endTime" name="endTime"
                                                 pattern="yyyy-MM-dd HH:mm"
                                                 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'startTime\');}'})"/>
            <a class="cho_btn" href="javascript:queryPerforms();">搜索</a>
        </div>
    </form>
    <div class="r-s-table-cnt">
        <table class="r-s-table">
            <thead>
            <tr>
                <th width="120"><label class="radio-label"><input
                        type="checkbox" id="cbAll" onclick="checkAll();"/>场次ID</label></th>
                <th width="160">场次名称</th>
                <th width="160">演出时间</th>
                <th>演出场馆</th>
            </tr>
            </thead>
            <tbody id="performs">
            </tbody>
        </table>
    </div>

    <div class="s-btn-line">
        <span class="btn btn-dis mr20" id="btnDisabled">查看报表</span> <a
            class="btn mr20" href="javascript:searchPageData(0);"
            style="display:none;" id="btnEnabled">查看报表</a> <a class="btn"
                                                              href="javascript:closePop();">取消</a>
    </div>
</div>
<!--重选场次弹出层 结束-->
<script type="text/javascript">
    //初始化表格样式
    $(function () {
        var otherWidth = 140 * 4 + 120 * 2;
        $('.up-report-table').each(function () {
            var tableId = $(this).attr('id');
            var size = tableId.split("_")[2];
            var disaccountWidht = size * 100;
            var width = disaccountWidht + otherWidth;
            $("#" + tableId).width(width);
        });
    });

    //初始化查询条件
    $(function () {
    	var dateRaio = '<c:out value="${filterVo.dateRadio}"/>';
        $("input[name='filterVo.dateRadio']").get(dateRaio).checked = true;
        if (dateRaio == 3) {
            $(".r-date-cnt").show();
        } else {
            $("#saleStartTime").val("");
            $("#saleEndTime").val("");
        }
    });

    function pageChang(page) {
        $("#page").val(page);
        $("#action").val("0");
        $("#frmSearch").submit();
    }

    function searchPageData(action) {
        $("#action").val(action);
        $("#page").val(1);
        var performIds = Utils.getValueByName("performId", ",");
        $("#performIds").val(performIds);
        $("#frmSearch").submit();
        if(action != 0){
        	$(".rc-loading").removeClass("rc-loading");
        }
    }

    function queryPerforms() {
        $("#performs").empty();
        var dataPara = getFormJson($("#frmSearchPerform"));
        $.ajax({
            type: "GET",
            data: dataPara,
            dataType: "json",
            url: "<%=basePath%>queryPerform.do",
            success: function (resultData) {
                //场次列表
                var strLine = '<tr>'
                        + '<td><label class="radio-label"><input type="checkbox" name="performId" value="$1" onclick="switchButton();" $6>$2</label></td>'
                        + '<td>$3</td>' + '<td>$4</td>'
                        + '<td title="$5">$5</td>' + '</tr>';
                var ckAll = false;
                var ids = $("#performIds").val();
                if (ids == "") {
                    ckAll = true;
                } else {
                    ids = "," + ids + ",";
                }
                var ckd = false;
                for (var i = 0; i < resultData.rows.length; i++) {
                    var item = resultData.rows[i];
                    var strCk = "";
                    if (ckAll
                            || ids.indexOf("," + item.performInfoID
                            + ",") > -1) {
                        strCk = "checked";
                        ckd = true;
                    }
                    $("#performs").append(
                            strLine.format(item.performInfoID,
                                    item.performId,
                                    item.performName,
                                    item.performTime.replace("T",
                                            " "), item.fieldName,
                                    strCk));
                }
                switchButton(ckd);
            },
            error: function (msg) {
            }
        });
    }

    //全选/取消全选
    function checkAll() {
        var st = $("#cbAll").attr("checked");
        $("input[name=performId]").each(function (i) {
            if (st) {
                $(this).attr("checked", true);
            } else {
                $(this).attr("checked", false);
            }
        });
        switchButton(st);
    }

    //切换场次检索按钮
    function switchButton(checked) {
        if (checked == undefined) {
            $("input[name=performId]").each(function (i) {
                if ($(this).attr("checked")) {
                    checked = true;
                    return false;
                }
            });
        }
        if (checked) {
            $("#btnEnabled").show();
            $("#btnDisabled").hide();

        } else {
            $("#btnEnabled").hide();
            $("#btnDisabled").show();
        }
    }

    $(function () {
        $("input[name='filterVo.dateRadio']").click(function () {
            var $vale = $(this).val();
            if ($vale == 3) {
                $(".r-date-cnt").show();
            } else {
                $(".r-date-cnt").hide();
            }
        });
    });

    //重选场次取消
    function closePop() {
        $(".choose-cc").hide();
        $(".p-mask").fadeOut().remove();
    }

    function showPop() {
        queryPerforms();
        var $mask = '<div class="p-mask"></div>', $e = $(".choose-cc"), $w = $e
                .width(), $h = $e.height(), $W = $(window).width(), $H = $(
                window).height();

        $("body").append($mask);
        $(".p-mask").css({
            "height": $("html").height() + "px"
        })
        $e.show().css({
            "left": ($W - $w) / 2 + "px",
            "top": ($H - $h) / 2 + "px"
        })

        $(".c-pop-close").click(function () {
            $e.fadeOut();
            $(".p-mask").fadeOut().remove();
        })

        $("body").delegate(".p-mask", "click", function () {
            $e.fadeOut();
            $(this).fadeOut().remove();
        })
    }
    /**
     *跳转到出票汇总
     */
    function skipToSale() {
        window.location = "<%=basePath%>saleReport.do?projectId=${projectId}&source=${source}";
    }

    //查询出票汇总信息，按照时间过滤
    function querySaleByTime(action) {
        $("#action").val(action);
        $("#page").val(1);
        $("#saleFormByTime").submit();
    }
</script>
</body>
</html>
