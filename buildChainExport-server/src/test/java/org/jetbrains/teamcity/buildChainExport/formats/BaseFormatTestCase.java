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
