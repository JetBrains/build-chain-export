<%@include file="/include.jsp" %>
<%--
  ~ Copyright 2000-2020 JetBrains s.r.o.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

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
