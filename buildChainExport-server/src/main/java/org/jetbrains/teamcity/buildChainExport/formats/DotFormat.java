package org.jetbrains.teamcity.buildChainExport.formats;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import org.jetbrains.teamcity.buildChainExport.nodes.GraphNode;

public class DotFormat extends PlainTextFormat {

  @NotNull
  protected String edge(@NotNull final GraphNode dependent, @NotNull final GraphNode dependOn, @NotNull final Set<GraphNode> processed) {
    StringBuilder graph = new StringBuilder();
    if (processed.add(dependent)) {
      graph.append(nodeWithLabel(dependent));
    }

    if (processed.add(dependOn)) {
      graph.append(nodeWithLabel(dependOn));
    }

    graph.append(nodeId(dependOn)).append(" -> ").append(nodeId(dependent)).append(";\n");

    return graph.toString();
  }

  @NotNull
  @Override
  protected String prefix(@NotNull final GraphNode startFrom) {
    return "digraph \"" + startFrom.getName() + "\" {\n";
  }

  @NotNull
  @Override
  protected String suffix(@NotNull final GraphNode startFrom) {
    return "}";
  }

  private String nodeId(@NotNull final GraphNode node) {
    return "b" + node.getId();
  }

  @NotNull
  private String nodeWithLabel(@NotNull final GraphNode promotion) {
    return nodeId(promotion) + " [label=\"" + promotion.getName() + "\"];\n"; //todo: add escaping of quotes
  }
}
