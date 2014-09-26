package org.jetbrains.teamcity.buildChainExport.formats;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.teamcity.buildChainExport.nodes.GraphNode;
import org.jmock.Mock;
import org.testng.annotations.Test;

@Test
public class GmlFormatTest extends BaseFormatTestCase {
  public void test_one_node() {
    GmlFormat gml = new GmlFormat();
    Mock n1 = createNode(1, "bt1");
    Mock n2 = createNode(2, "bt2");
    setChildren(n1, n2);
    setChildren(n2);

    String result = gml.export((GraphNode) n1.proxy());
    assertNumNodes(result, 2);
    assertNumEdges(result, 1);
    assertHasEdge(result, 2, 1);
  }

  public void test_two_nodes() {
    GmlFormat gml = new GmlFormat();
    Mock n1 = createNode(1, "bt1");
    Mock n2 = createNode(2, "bt2");
    Mock n3 = createNode(3, "bt3");
    setChildren(n1, n2, n3);
    setChildren(n2);
    setChildren(n3);

    String result = gml.export((GraphNode) n1.proxy());
    assertNumNodes(result, 3);
    assertNumEdges(result, 2);
    assertHasEdge(result, 2, 1);
    assertHasEdge(result, 3, 1);
  }

  public void test_triangle() {
    GmlFormat gml = new GmlFormat();
    Mock n1 = createNode(1, "bt1");
    Mock n2 = createNode(2, "bt2");
    Mock n3 = createNode(3, "bt3");
    setChildren(n1, n2, n3);
    setChildren(n2, n3);
    setChildren(n3);

    String result = gml.export((GraphNode) n1.proxy());
    assertNumNodes(result, 3);
    assertNumEdges(result, 3);
    assertHasEdge(result, 2, 1);
    assertHasEdge(result, 3, 1);
    assertHasEdge(result, 3, 2);

    System.out.println(result);
  }

  public void test_rhomb() {
    GmlFormat gml = new GmlFormat();
    Mock n1 = createNode(1, "bt1");
    Mock n2 = createNode(2, "bt2");
    Mock n3 = createNode(3, "bt3");
    Mock n4 = createNode(4, "bt4");
    setChildren(n1, n2, n3);
    setChildren(n2, n4);
    setChildren(n3, n4);
    setChildren(n4);

    String result = gml.export((GraphNode) n1.proxy());
    assertNumNodes(result, 4);
    assertNumEdges(result, 4);
    assertHasEdge(result, 2, 1);
    assertHasEdge(result, 3, 1);
    assertHasEdge(result, 4, 2);
    assertHasEdge(result, 4, 3);

    System.out.println(result);
  }

  public void test_rhomb_with_leaf() {
    GmlFormat gml = new GmlFormat();
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

    String result = gml.export((GraphNode) n1.proxy());
    assertNumNodes(result, 5);
    assertNumEdges(result, 5);
    assertHasEdge(result, 2, 1);
    assertHasEdge(result, 3, 1);
    assertHasEdge(result, 4, 2);
    assertHasEdge(result, 4, 3);
    assertHasEdge(result, 5, 4);

    System.out.println(result);
  }

  private void assertHasEdge(@NotNull String result, long src, long target) {
    assertTrue(result.contains("edge [\n" +
        "  source " + src + "\n" +
        "  target " + target + "\n" +
        "]"));
  }

  private void assertNumNodes(@NotNull String result, int expectedNum) {
    assertKeywordOccurrences(result, "node [", expectedNum);
  }

  private void assertNumEdges(@NotNull String result, int expectedNum) {
    assertKeywordOccurrences(result, "edge [", expectedNum);
  }
}
