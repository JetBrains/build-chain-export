<%@include file="/include.jsp"%>
<jsp:useBean id="buildData" type="jetbrains.buildServer.serverSide.SBuild" scope="request"/>
<c:url var="exportLink" value="/exportBuildChain.html?promotionId=${buildData.buildPromotion.id}&format=gml"/>
<script type="text/javascript">
BS.ChartExport = {
    installAction: function() {
        $j('#bdDetails ul.menuList').append('<li class="menuItem" title="Export chain graph"><a href="${exportLink}" target="_blank">Export build chain graph</a></li>');
    }
};

BS.ChartExport.installAction();
</script>
