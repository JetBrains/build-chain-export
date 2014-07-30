package org.jetbrains.teamcity.buildChainExport.formats;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.dependency.BuildDependency;
import org.jetbrains.annotations.NotNull;
import org.jmock.Mock;
import org.testng.annotations.Test;

import java.util.Arrays;

@Test
public class GmlFormatTest extends BaseTestCase {
  public void test_one_node() {
    GmlFormat gml = new GmlFormat();
    Mock n1 = createNode(1, "bt1");
    Mock n2 = createNode(2, "bt2");
    BuildDependency edge = createEdge(n1, n2);
    setEdges(n1, edge);
    setEdges(n2);

    String result = gml.export((BuildPromotion) n1.proxy());
    assertNumNodes(result, 2);
    assertNumEdges(result, 1);
    assertHasEdge(result, 2, 1);
  }

  private void setEdges(Mock n1, BuildDependency... edges) {
    n1.stubs().method("getDependencies").will(returnValue(Arrays.asList(edges)));
  }

  public void test_two_nodes() {
    GmlFormat gml = new GmlFormat();
    Mock n1 = createNode(1, "bt1");
    Mock n2 = createNode(2, "bt2");
    Mock n3 = createNode(3, "bt3");
    BuildDependency edge1 = createEdge(n1, n2);
    BuildDependency edge2 = createEdge(n1, n3);
    setEdges(n1, edge1, edge2);
    setEdges(n2);
    setEdges(n3);

    String result = gml.export((BuildPromotion) n1.proxy());
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
    BuildDependency edge1 = createEdge(n1, n2);
    BuildDependency edge2 = createEdge(n1, n3);
    BuildDependency edge3 = createEdge(n2, n3);
    setEdges(n1, edge1, edge2);
    setEdges(n2, edge3);
    setEdges(n3);

    String result = gml.export((BuildPromotion) n1.proxy());
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
    BuildDependency edge1 = createEdge(n1, n2);
    BuildDependency edge2 = createEdge(n1, n3);
    BuildDependency edge3 = createEdge(n2, n4);
    BuildDependency edge4 = createEdge(n3, n4);
    setEdges(n1, edge1, edge2);
    setEdges(n2, edge3);
    setEdges(n3, edge4);
    setEdges(n4);

    String result = gml.export((BuildPromotion) n1.proxy());
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
    BuildDependency edge1 = createEdge(n1, n2);
    BuildDependency edge2 = createEdge(n1, n3);
    BuildDependency edge3 = createEdge(n2, n4);
    BuildDependency edge4 = createEdge(n3, n4);
    BuildDependency edge5 = createEdge(n4, n5);
    setEdges(n1, edge1, edge2);
    setEdges(n2, edge3);
    setEdges(n3, edge4);
    setEdges(n4, edge5);
    setEdges(n5);

    String result = gml.export((BuildPromotion) n1.proxy());
    assertNumNodes(result, 5);
    assertNumEdges(result, 5);
    assertHasEdge(result, 2, 1);
    assertHasEdge(result, 3, 1);
    assertHasEdge(result, 4, 2);
    assertHasEdge(result, 4, 3);
    assertHasEdge(result, 5, 4);

    System.out.println(result);
  }

  private BuildDependency createEdge(@NotNull Mock n1, @NotNull Mock n2) {
    Mock edge = mock(BuildDependency.class);
    edge.stubs().method("getDependOn").will(returnValue(n2.proxy()));
    edge.stubs().method("getDependent").will(returnValue(n1.proxy()));
    return (BuildDependency)edge.proxy();
  }

  private Mock createNode(long id, @NotNull String btId) {
    Mock n1 = mock(BuildPromotion.class);
    n1.stubs().method("getId").will(returnValue(id));
    n1.stubs().method("getBuildTypeExternalId").will(returnValue(btId));
    return n1;
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

  private void assertKeywordOccurrences(@NotNull String result, @NotNull String kwd, int expectedNum) {
    int num = 0;
    int idx = 0;
    String str = result;
    do {
      str = str.substring(idx);
      idx = str.indexOf(kwd);
      if (idx != -1) {
        num++;
        idx += kwd.length();
      }
    }
    while (idx != -1);

    assertEquals("Incorrect number of occurrences of keyword: " + kwd + "\n" + result, expectedNum, num);
  }
}
