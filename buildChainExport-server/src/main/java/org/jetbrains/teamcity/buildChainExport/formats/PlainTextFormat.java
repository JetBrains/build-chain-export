package org.jetbrains.teamcity.buildChainExport.formats;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import org.jetbrains.teamcity.buildChainExport.nodes.GraphNode;

public abstract class PlainTextFormat extends GraphFormat {
  @Override
  public final String export(@NotNull GraphNode startFrom) {
    StringBuilder result = new StringBuilder();

    result.append(prefix(startFrom));

    Set<GraphNode> processed = new HashSet<GraphNode>();

    Queue<GraphNode> toProcess = new LinkedList<GraphNode>();
    toProcess.add(startFrom);

    while (!toProcess.isEmpty()) {
      GraphNode promo = toProcess.poll();
      for (GraphNode dep: promo.getChildren()) {
        if (!processed.contains(dep)) {
          toProcess.add(dep);
        }

        result.append(edge(promo, dep, processed));
      }
    }

    result.append(suffix(startFrom));

    return result.toString();
  }

  @NotNull
  protected abstract String edge(@NotNull GraphNode dependent, @NotNull GraphNode dependOn, @NotNull Set<GraphNode> processed);

  @NotNull
  protected abstract String prefix(@NotNull GraphNode startFrom);

  @NotNull
  protected abstract String suffix(@NotNull GraphNode startFrom);
}
