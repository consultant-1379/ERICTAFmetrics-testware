package com.ericsson.cifwk.model.pages;

import com.ericsson.cifwk.taf.ui.core.*;
import com.ericsson.cifwk.taf.ui.sdk.*;

public class MetricsMonitorToolRadiatorView extends GenericViewModel {

   //@UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[@class='eaMaintrackRadiator-wrapper']")
   //public Label WholeText;

  @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//span[@class='elCommonLib-wMaintrackStatusBanner-titleText']")
  public Label Maintrack;

  @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[1][@class='elCommonLib-wMaintrackStatusTableHighlights-colHead']")
  private Label ENM_ISO;

  public String ENM_ISOLabel(){
  String enm = ENM_ISO.getText();
  return enm;
  }

   @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[2][@class='elCommonLib-wMaintrackStatusTableHighlights-colHead']")
   public Label Product_Set;

   @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[3][@class='elCommonLib-wMaintrackStatusTableHighlights-colHead']")
   public Label UG;

   @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[4][@class='elCommonLib-wMaintrackStatusTableHighlights-colHead']")
   public Label UG_Availability;

   @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[5][@class='elCommonLib-wMaintrackStatusTableHighlights-colHead']")
   public Label UG_Performance;

   @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[6][@class='elCommonLib-wMaintrackStatusTableHighlights-colHead']")
   public Label RFA_250;

   @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[1][@class='elCommonLib-eaMaintrackRadiator-panels-name']")
   public Label Upgrade_Baseline;

   @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[@class='elCommonLib-eaMaintrackRadiator-panels-name'][contains(text(),'vApp')]")
   public Label vApp_Baseline;

   @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[@class='elCommonLib-eaMaintrackRadiator-panels-name'][contains(text(),'Micro')]")
   public Label Micro_II_Baseline;

   @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[@class='elCommonLib-eaMaintrackRadiator-panels-name'][contains(text(),'Initial')]")
   public Label Initial_Install_Baseline;

   @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[@class='elCommonLib-eaMaintrackRadiator-panels-name'][contains(text(),'RVB')]")
   public Label RVB_Baseline;

   @UiComponentMapping(selectorType=SelectorType.XPATH, selector="//div[@class='elCommonLib-eaMaintrackRadiator-current-rvbpanel-title']")
   public Label RVB_Current;
}


