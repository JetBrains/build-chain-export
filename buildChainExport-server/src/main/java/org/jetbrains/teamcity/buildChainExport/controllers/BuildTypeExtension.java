package org.jetbrains.teamcity.buildChainExport.controllers;

import javax.servlet.http.HttpServletRequest;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimplePageExtension;
import org.jetbrains.annotations.NotNull;

public class BuildTypeExtension extends SimplePageExtension {
  public BuildTypeExtension(@NotNull PagePlaces pagePlaces, @NotNull PluginDescriptor pluginDescriptor) {
    super(pagePlaces);
    setIncludeUrl(pluginDescriptor.getPluginResourcesPath("buildType.jsp"));
    setPlaceId(PlaceId.ALL_PAGES_FOOTER);
    setPluginName(pluginDescriptor.getPluginName());
    register();
  }

  @Override
  public boolean isAvailable(@NotNull final HttpServletRequest request) {
    return request.getRequestURI().contains("/viewType.html");
  }
}
