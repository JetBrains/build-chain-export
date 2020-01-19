/*
 * Copyright 2000-2020 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
