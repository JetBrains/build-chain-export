<%@include file="/include.jsp"%>
<jsp:useBean id="buildData" type="jetbrains.buildServer.serverSide.SBuild" scope="request"/>
<c:url var="exportLink" value="/exportBuildChain.html?promotionId=${buildData.buildPromotion.id}&format=gml"/>
<li class="menuItem" title="Export chain graph" id="exportBuildChainGraph"><a href="${exportLink}" target="_blank">Export build chain graph</a></li>
