package org.jetbrains.teamcity.buildChainExport.formats;

import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.dependency.BuildDependency;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class GmlFormat extends PlainTextFormat {
  @NotNull
  protected String edge(@NotNull BuildDependency dep, @NotNull Set<BuildPromotion> processed) {
    StringBuilder edge = new StringBuilder();
    BuildPromotion dependent = dep.getDependent();
    if (processed.add(dependent)) {
      edge.append(nodeWithLabel(dependent));
    }

    BuildPromotion dependOn = dep.getDependOn();
    if (processed.add(dependOn)) {
      edge.append(nodeWithLabel(dependOn));
    }

    edge.append("edge [\n");
    edge.append("  source ").append(nodeId(dependOn)).append("\n");
    edge.append("  target ").append(nodeId(dependent)).append("\n");
    edge.append("]\n");

    return edge.toString();
  }

  @NotNull
  @Override
  protected String prefix(@NotNull BuildPromotion startFrom) {
    return "graph [\n" +
           "  directed 1\n";
  }

  @NotNull
  @Override
  protected String suffix(@NotNull BuildPromotion startFrom) {
    return "]";
  }

  @NotNull
  private String nodeWithLabel(@NotNull BuildPromotion promotion) {
    return "node [\n" +
        "  id " + nodeId(promotion) + "\n" +
        "  label \"" + nodeLabel(promotion) + "\"\n" +
        "]\n";
  }

  @NotNull
  private String nodeLabel(@NotNull BuildPromotion promotion) {
    return promotion.getBuildTypeExternalId();
  }

  private String nodeId(@NotNull BuildPromotion node) {
    return String.valueOf(node.getId());
  }
}
