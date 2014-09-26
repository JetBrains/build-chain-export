package org.jetbrains.teamcity.buildChainExport.nodes;

import java.util.List;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.dependency.Dependency;
import jetbrains.buildServer.util.CollectionsUtil;
import jetbrains.buildServer.util.Converter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Yegor.Yarko
 *         Date: 11.09.2014
 */
public class BuildTypeNode implements GraphNode {
  private final SBuildType myBuildType;

  public BuildTypeNode(final SBuildType buildType) {
    myBuildType = buildType;
  }

  @NotNull
  public String getId() {
    return myBuildType.getInternalId();
  }

  @NotNull
  public String getName() {
    return myBuildType.getExternalId();
  }

  @NotNull
  public List<GraphNode> getChildren() {
    return CollectionsUtil.convertCollection(myBuildType.getDependencies(), new Converter<GraphNode, Dependency>() {
      public GraphNode createFrom(@NotNull final Dependency dependency) {
        return new BuildTypeNode(dependency.getDependOn());
      }
    });
  }
}
