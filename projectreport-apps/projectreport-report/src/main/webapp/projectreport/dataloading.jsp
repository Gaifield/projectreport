<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>报表平台_loading</title>
    <link rel="stylesheet" href="css/report.css" type="text/css">
    <jsp:include page="common/include.jsp"></jsp:include>
</head>

<body>
<!--顶部 begin-->
<jsp:include page="/projectreport/common/top.jsp"></jsp:include>
<!--顶部 end-->
<div class="m-nav">
    <div class="wrap">当前所在位置：
        <jsp:include page="/projectreport/common/location.jsp"></jsp:include>出票汇总表
    </div>
</div>
<!--内容区 begin-->
<div class="mai_main clear">
    <!-- 左侧导航-->
    <div class="mai_left">
        <dl class="left_nav">
            <dt>报表分类</dt>
            <dd>
                <p class="active"><a href="#">总销售金额统计</a></p>
            </dd>
        </dl>
    </div>
    <!--右侧内容-->
    <div class="mai_wrap">
        <div class="right_ticket por">
            <div class="ticket_nav-bt">
                <ul class="ticket_nav fl">
                    <li class="active"><a href="javascript:void(0)">汇总</a></li>
                </ul>
                <div class="fr">
                    <a class="back-link fr" href="#">返回<span class="r-cm">&gt;&gt;</span></a>
                    <a class="down-btn mt7 mr10 fr" href="#">
                        <span><em class="down-ico"></em>导出pdf</span>
                    </a>
                    <a class="down-btn mt7 mr10 fr" href="#">
                        <span><em class="down-ico"></em>导出excel</span>
                    </a>
                </div>
            </div>
            <div class="r-cnt rc-loading">
                <div class="r-biaoti">
                    <span>2014莎拉布莱曼福州演唱会</span>
                    <a class="r-choose fr" href="javascript:;" onclick="showPop()">重选场次</a>
                </div>
            </div>
        </div>
    </div>


</div>

<!--内容区 end-->

<!--底部 begin-->
<jsp:include page="/projectreport/common/footer.jsp"></jsp:include>
<!--底部 end-->
</body>
</html>
