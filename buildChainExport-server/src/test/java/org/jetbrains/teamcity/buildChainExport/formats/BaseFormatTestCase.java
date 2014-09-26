package org.jetbrains.teamcity.buildChainExport.formats;

import jetbrains.buildServer.BaseTestCase;
import jetbrains.buildServer.util.CollectionsUtil;
import jetbrains.buildServer.util.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.teamcity.buildChainExport.nodes.GraphNode;
import org.jmock.Mock;

import java.util.Arrays;

public class BaseFormatTestCase extends BaseTestCase {

  @NotNull
  protected Mock createNode(long id, @NotNull String btId) {
    Mock n1 = mock(GraphNode.class);
    n1.stubs().method("getId").will(returnValue(String.valueOf(id)));
    n1.stubs().method("getName").will(returnValue(btId));
    return n1;
  }

  protected void setChildren(Mock n1, Mock... children) {
    n1.stubs().method("getChildren").will(returnValue(CollectionsUtil.convertCollection(Arrays.asList(children), new Converter<Object, Mock>() {
      public Object createFrom(@NotNull final Mock mock) {
        return mock.proxy();
      }
    })));
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
