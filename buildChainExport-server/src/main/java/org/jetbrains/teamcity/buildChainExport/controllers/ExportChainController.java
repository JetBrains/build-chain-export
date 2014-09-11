package org.jetbrains.teamcity.buildChainExport.controllers;

import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.teamcity.buildChainExport.formats.DotFormat;
import org.jetbrains.teamcity.buildChainExport.formats.GmlFormat;
import org.jetbrains.teamcity.buildChainExport.formats.GraphFormat;
import org.jetbrains.teamcity.buildChainExport.nodes.BuildTypeNode;
import org.jetbrains.teamcity.buildChainExport.nodes.PromotionNode;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ExportChainController extends BaseController {
  private final BuildPromotionManager myBuildPromotionManager;
  @NotNull private final ProjectManager myProjectManager;
  private Map<String, GraphFormat> myFormats = new HashMap<String, GraphFormat>();

  public ExportChainController(@NotNull BuildPromotionManager buildPromotionManager,
                               @NotNull WebControllerManager webControllerManager,
                               @NotNull ProjectManager projectManager) {
    myBuildPromotionManager = buildPromotionManager;
    myProjectManager = projectManager;
    webControllerManager.registerController("/exportBuildChain.html", this);

    myFormats.put("dot", new DotFormat());
    myFormats.put("gml", new GmlFormat());
  }

  @Nullable
  @Override
  protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
    String fileFormat = StringUtil.emptyIfNull(request.getParameter("format"));
    GraphFormat format = myFormats.get(fileFormat.toLowerCase());
    if (format == null) {
      return simpleView("Supported formats: " + myFormats.keySet());
    }

    String result;

    String promoId = request.getParameter("promotionId");
    if (promoId != null) {
      BuildPromotion promotion = myBuildPromotionManager.findPromotionById(Long.parseLong(promoId));
      if (promotion == null) return simpleView("Build promotion does not exist: " + promoId);
      result = format.export(new PromotionNode(promotion));
    } else {
      String buildTypeId = request.getParameter("buildTypeId");
      if (buildTypeId == null){
        return simpleView("Neither 'promotionId' nor 'buildTypeId' parameters are specified");
      }
      final SBuildType buildType = myProjectManager.findBuildTypeByExternalId(buildTypeId);
      if (buildType == null){
        return simpleView("Build configuration with id '" + buildTypeId + "' is not found");
      }
      String useDummy = request.getParameter("useDummy");
      if (useDummy == null || (!Boolean.valueOf(useDummy))){
        result = format.export(new BuildTypeNode(buildType));
      }else{
        //Object here is to make it possible to compile the code against both TeamCity 8.0 and 8.1
        final Object dummyPromotion = myBuildPromotionManager.createDummyPromotion((BuildTypeEx)buildType);
        result = format.export(new PromotionNode((BuildPromotion)dummyPromotion));
      }
    }

    response.setContentType("text/plain");
    PrintWriter writer = response.getWriter();
    writer.print(result);
    writer.close();
    return null;
  }
}
