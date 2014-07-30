package org.jetbrains.teamcity.buildChainExport.formats;

import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.dependency.BuildDependency;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class DotFormat extends PlainTextFormat {

  @NotNull
  protected String edge(@NotNull BuildDependency dep, @NotNull Set<BuildPromotion> processed) {
    StringBuilder graph = new StringBuilder();
    BuildPromotion dependent = dep.getDependent();
    if (processed.add(dependent)) {
      graph.append(nodeWithLabel(dependent));
    }

    BuildPromotion dependOn = dep.getDependOn();
    if (processed.add(dependOn)) {
      graph.append(nodeWithLabel(dependOn));
    }

    graph.append(nodeId(dependOn)).append(" -> ").append(nodeId(dependent)).append(";\n");

    return graph.toString();
  }

  @NotNull
  @Override
  protected String prefix(@NotNull BuildPromotion startFrom) {
    return "digraph \"" + nodeLabel(startFrom) + "\" {\n";
  }

  @NotNull
  @Override
  protected String suffix(@NotNull BuildPromotion startFrom) {
    return "}";
  }

  private String nodeId(BuildPromotion node) {
    return "b" + node.getId();
  }

  @NotNull
  private String nodeWithLabel(@NotNull BuildPromotion promotion) {
    return nodeId(promotion) + " [label=\"" + nodeLabel(promotion) + "\"];\n";
  }

  @NotNull
  private String nodeLabel(@NotNull BuildPromotion promotion) {
    return promotion.getBuildTypeExternalId();
  }
}
