<%@include file="/include.jsp" %>
<jsp:useBean id="buildType" type="jetbrains.buildServer.serverSide.SBuildType" scope="request"/>
<c:url var="exportLink" value="/exportBuildChain.html?buildTypeId=${buildType.externalId}&format=gml"/>
<script type="text/javascript">
  BS.ChartExport = {
    installAction: function () {
      $j('#btDetails ul.menuList').append('<li class="menuItem" title="" id="exportBuildDependenciesGraph"><a href="${exportLink}" target="_blank">Export dependencies graph</a></li>');
    }
  };

  if (!$('exportBuildDependenciesGraph')) {
      BS.ChartExport.installAction();
  }
</script>
