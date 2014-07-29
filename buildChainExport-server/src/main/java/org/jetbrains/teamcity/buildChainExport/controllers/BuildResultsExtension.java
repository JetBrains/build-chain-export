package org.jetbrains.teamcity.buildChainExport.controllers;

import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimplePageExtension;
import org.jetbrains.annotations.NotNull;

public class BuildResultsExtension extends SimplePageExtension {
  public BuildResultsExtension(@NotNull PagePlaces pagePlaces, @NotNull PluginDescriptor pluginDescriptor) {
    super(pagePlaces);
    setIncludeUrl(pluginDescriptor.getPluginResourcesPath("/buildResults.jsp"));
    setPlaceId(PlaceId.BUILD_RESULTS_FRAGMENT);
    setPluginName(pluginDescriptor.getPluginName());
    register();
  }
}
