package org.jetbrains.teamcity.buildChainExport.formats;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.dependency.BuildDependency;
import org.jetbrains.annotations.NotNull;
import org.jmock.Mock;

import java.util.Arrays;

public class BaseFormatTestCase extends BaseTestCase {
  @NotNull
  protected BuildDependency createEdge(@NotNull Mock n1, @NotNull Mock n2) {
    Mock edge = mock(BuildDependency.class);
    edge.stubs().method("getDependOn").will(returnValue(n2.proxy()));
    edge.stubs().method("getDependent").will(returnValue(n1.proxy()));
    return (BuildDependency)edge.proxy();
  }

  @NotNull
  protected Mock createNode(long id, @NotNull String btId) {
    Mock n1 = mock(BuildPromotion.class);
    n1.stubs().method("getId").will(returnValue(id));
    n1.stubs().method("getBuildTypeExternalId").will(returnValue(btId));
    return n1;
  }

  protected void setEdges(Mock n1, BuildDependency... edges) {
    n1.stubs().method("getDependencies").will(returnValue(Arrays.asList(edges)));
  }

  protected void assertKeywordOccurrences(@NotNull String result, @NotNull String kwd, int expectedNum) {
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
