package org.jetbrains.teamcity.buildChainExport.formats;

import jetbrains.buildServer.serverSide.BuildPromotion;
import org.jetbrains.annotations.NotNull;

public abstract class GraphFormat {
  public abstract String export(@NotNull BuildPromotion startFrom);
}
