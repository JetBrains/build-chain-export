<%@include file="/include.jsp" %>
<jsp:useBean id="buildType" type="jetbrains.buildServer.serverSide.SBuildType" scope="request"/>
<c:url var="exportLink" value="/exportBuildChain.html?buildTypeId=${buildType.externalId}&format=gml"/>
<li class="menuItem" title="" id="exportBuildDependenciesGraph"><a href="${exportLink}" target="_blank">Export dependencies graph</a></li>
