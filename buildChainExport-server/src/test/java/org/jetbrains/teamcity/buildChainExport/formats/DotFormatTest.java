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
import org.jetbrains.teamcity.buildChainExport.nodes.GraphNode;
import org.jmock.Mock;
import org.testng.annotations.Test;

@Test
public class DotFormatTest extends BaseFormatTestCase {
  public void test_one_node() {
    GraphFormat fmt = new DotFormat();
    Mock n1 = createNode(1, "bt1");
    Mock n2 = createNode(2, "bt2");
    setChildren(n1, n2);
    setChildren(n2);

    String result = fmt.export((GraphNode) n1.proxy());
    assertNumNodes(result, 2);
    assertNumEdges(result, 1);
    assertHasEdge(result, 2, 1);
  }

  public void test_two_nodes() {
    GraphFormat fmt = new DotFormat();
    Mock n1 = createNode(1, "bt1");
    Mock n2 = createNode(2, "bt2");
    Mock n3 = createNode(3, "bt3");
    setChildren(n1, n2, n3);
    setChildren(n2);
    setChildren(n3);

    String result = fmt.export((GraphNode) n1.proxy());
    assertNumNodes(result, 3);
    assertNumEdges(result, 2);
    assertHasEdge(result, 2, 1);
    assertHasEdge(result, 3, 1);
  }

  public void test_triangle() {
    GraphFormat fmt = new DotFormat();
    Mock n1 = createNode(1, "bt1");
    Mock n2 = createNode(2, "bt2");
    Mock n3 = createNode(3, "bt3");
    setChildren(n1, n2, n3);
    setChildren(n2, n3);
    setChildren(n3);

    String result = fmt.export((GraphNode) n1.proxy());
    assertNumNodes(result, 3);
    assertNumEdges(result, 3);
    assertHasEdge(result, 2, 1);
    assertHasEdge(result, 3, 1);
    assertHasEdge(result, 3, 2);

    System.out.println(result);
  }

  public void test_rhomb() {
    GraphFormat fmt = new DotFormat();
    Mock n1 = createNode(1, "bt1");
    Mock n2 = createNode(2, "bt2");
    Mock n3 = createNode(3, "bt3");
    Mock n4 = createNode(4, "bt4");
    setChildren(n1, n2, n3);
    setChildren(n2, n4);
    setChildren(n3, n4);
    setChildren(n4);

    String result = fmt.export((GraphNode) n1.proxy());
    assertNumNodes(result, 4);
    assertNumEdges(result, 4);
    assertHasEdge(result, 2, 1);
    assertHasEdge(result, 3, 1);
    assertHasEdge(result, 4, 2);
    assertHasEdge(result, 4, 3);
  }

  public void test_rhomb_with_leaf() {
    GraphFormat fmt = new DotFormat();
    Mock n1 = createNode(1, "bt1");
    Mock n2 = createNode(2, "bt2");
    Mock n3 = createNode(3, "bt3");
    Mock n4 = createNode(4, "bt4");
    Mock n5 = createNode(5, "bt5");
    setChildren(n1, n2, n3);
    setChildren(n2, n4);
    setChildren(n3, n4);
    setChildren(n4, n5);
    setChildren(n5);

    String result = fmt.export((GraphNode) n1.proxy());
    assertNumNodes(result, 5);
    assertNumEdges(result, 5);
    assertHasEdge(result, 2, 1);
    assertHasEdge(result, 3, 1);
    assertHasEdge(result, 4, 2);
    assertHasEdge(result, 4, 3);
    assertHasEdge(result, 5, 4);
  }

  private void assertHasEdge(@NotNull String result, long src, long target) {
    assertTrue(result, result.contains("b" + src + " -> b" + target + ";"));
  }

  private void assertNumNodes(@NotNull String result, int expectedNum) {
    assertKeywordOccurrences(result, "[label=\"", expectedNum);
  }

  private void assertNumEdges(@NotNull String result, int expectedNum) {
    assertKeywordOccurrences(result, "->", expectedNum);
  }
}
