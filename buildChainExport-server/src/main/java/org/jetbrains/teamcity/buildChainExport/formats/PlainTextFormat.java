package org.jetbrains.teamcity.buildChainExport.formats;

import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.dependency.BuildDependency;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public abstract class PlainTextFormat extends GraphFormat {
  @Override
  public final String export(@NotNull BuildPromotion startFrom) {
    StringBuilder result = new StringBuilder();

    result.append(prefix(startFrom));

    Set<BuildPromotion> processed = new HashSet<BuildPromotion>();

    Queue<BuildPromotion> toProcess = new LinkedList<BuildPromotion>();
    toProcess.add(startFrom);

    while (!toProcess.isEmpty()) {
      BuildPromotion promo = toProcess.poll();
      for (BuildDependency dep: promo.getDependencies()) {
        if (!processed.contains(dep.getDependOn())) {
          toProcess.add(dep.getDependOn());
        }

        result.append(edge(dep, processed));
      }
    }

    result.append(suffix(startFrom));

    return result.toString();
  }

  @NotNull
  protected abstract String edge(@NotNull BuildDependency dependency, @NotNull Set<BuildPromotion> processed);

  @NotNull
  protected abstract String prefix(@NotNull BuildPromotion startFrom);

  @NotNull
  protected abstract String suffix(@NotNull BuildPromotion startFrom);
}
