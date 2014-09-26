package org.jetbrains.teamcity.buildChainExport.nodes;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * @author Yegor.Yarko
 *         Date: 11.09.2014
 */
public interface GraphNode {
  @NotNull
  String getId();

  @NotNull
  String getName();

  @NotNull
  List<GraphNode> getChildren();
}
