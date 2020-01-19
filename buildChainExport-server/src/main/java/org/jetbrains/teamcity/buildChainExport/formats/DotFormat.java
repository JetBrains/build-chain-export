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
