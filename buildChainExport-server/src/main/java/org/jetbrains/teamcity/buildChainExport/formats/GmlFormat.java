package org.jetbrains.teamcity.buildChainExport.formats;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import org.jetbrains.teamcity.buildChainExport.nodes.GraphNode;

public class GmlFormat extends PlainTextFormat {
  @NotNull
  protected String edge(@NotNull GraphNode dependent, @NotNull GraphNode dependOn, @NotNull Set<GraphNode> processed) {
    StringBuilder edge = new StringBuilder();
    if (processed.add(dependent)) {
      edge.append(nodeWithLabel(dependent));
    }

    if (processed.add(dependOn)) {
      edge.append(nodeWithLabel(dependOn));
    }

    edge.append("edge [\n");
    edge.append("  source ").append(dependOn.getId()).append("\n");
    edge.append("  target ").append(dependent.getId()).append("\n");
    edge.append("]\n");

    return edge.toString();
  }

  @NotNull
  @Override
  protected String prefix(@NotNull final GraphNode startFrom) {
    return "graph [\n" +
           "  directed 1\n";
  }

  @NotNull
  @Override
  protected String suffix(@NotNull final GraphNode startFrom) {
    return "]";
  }

  @NotNull
  private String nodeWithLabel(@NotNull final GraphNode promotion) {
    return "node [\n" +
        "  id " + promotion.getId() + "\n" +
        "  label \"" + promotion.getName() + "\"\n" +
        "]\n";
  }
}
