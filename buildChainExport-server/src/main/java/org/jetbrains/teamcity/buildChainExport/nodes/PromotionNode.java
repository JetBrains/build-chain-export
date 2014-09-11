package org.jetbrains.teamcity.buildChainExport.nodes;

import java.util.List;
import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.dependency.BuildDependency;
import jetbrains.buildServer.util.CollectionsUtil;
import jetbrains.buildServer.util.Converter;
import org.jetbrains.annotations.NotNull;

/**
* @author Yegor.Yarko
*         Date: 11.09.2014
*/
public class PromotionNode implements GraphNode {
  private final BuildPromotion myPromotion;

  public PromotionNode(final BuildPromotion promotion) {
    myPromotion = promotion;
  }

  @NotNull
  public String getId() {
    return String.valueOf(myPromotion.getId());
  }

  @NotNull
  public String getName() {
    return myPromotion.getBuildTypeExternalId();
  }

  @NotNull
  public List<GraphNode> getChildren() {
    return CollectionsUtil.convertCollection(myPromotion.getDependencies(), new Converter<GraphNode, BuildDependency>() {
      public GraphNode createFrom(@NotNull final BuildDependency buildDependency) {
        return new PromotionNode(buildDependency.getDependOn());
      }
    });
  }
}
