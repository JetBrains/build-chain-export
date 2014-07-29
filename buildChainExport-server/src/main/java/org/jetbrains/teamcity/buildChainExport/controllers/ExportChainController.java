package org.jetbrains.teamcity.buildChainExport.controllers;

import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.BuildPromotion;
import jetbrains.buildServer.serverSide.BuildPromotionManager;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.teamcity.buildChainExport.formats.DotFormat;
import org.jetbrains.teamcity.buildChainExport.formats.GmlFormat;
import org.jetbrains.teamcity.buildChainExport.formats.GraphFormat;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ExportChainController extends BaseController {
  private final BuildPromotionManager myBuildPromotionManager;
  private Map<String, GraphFormat> myFormats = new HashMap<String, GraphFormat>();

  public ExportChainController(@NotNull BuildPromotionManager buildPromotionManager, @NotNull WebControllerManager webControllerManager) {
    myBuildPromotionManager = buildPromotionManager;
    webControllerManager.registerController("/exportBuildChain.html", this);

    myFormats.put("dot", new DotFormat());
    myFormats.put("gml", new GmlFormat());
  }

  @Nullable
  @Override
  protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
    String fileFormat = StringUtil.emptyIfNull(request.getParameter("format"));
    String promoId = request.getParameter("promotionId");
    if (promoId == null) return simpleView("Build promotion is not specified");

    GraphFormat format = myFormats.get(fileFormat.toLowerCase());
    if (format == null) {
      return simpleView("Supported formats: " + myFormats.keySet());
    }

    BuildPromotion promotion = myBuildPromotionManager.findPromotionById(Long.parseLong(promoId));
    if (promotion == null) return simpleView("Build promotion does not exist: " + promoId);

    String result = format.export(promotion);

    response.setContentType("text/plain");
    PrintWriter writer = response.getWriter();
    writer.print(result);
    writer.close();
    return null;
  }
}
