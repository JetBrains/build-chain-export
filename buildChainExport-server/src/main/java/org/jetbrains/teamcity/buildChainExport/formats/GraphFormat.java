package org.jetbrains.teamcity.buildChainExport.formats;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.teamcity.buildChainExport.nodes.GraphNode;

public abstract class GraphFormat {
  public abstract String export(@NotNull GraphNode startFrom);
}
