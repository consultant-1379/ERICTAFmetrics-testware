package com.ericsson.cifwk.monitor.operators;

public interface MetricsMonitorToolOperator {

  public void setUpBrowser();
  public String verifyMetricsPageTitle();
  public String verifyRadiatorPageTitle();
  public String verifyENM_ISO();
  public String verifyProduct_Set();
  public String verify_UG();
  public String verifyUG_Availability();
  public String verifyUG_Performance();
  public String verifyRFA_250();
  public String verifyUpgrade_Baseline();
  public String verify_Maintrack();
  public String verifyvApp_Baseline();
  public String verifyMicro_II_Baseline();
  public String verifyInitial_Install_Baseline();
  public String verifyRVB_Baseline();
  public String verifyRVB_Current();
  //public String verifyWholeText();

}
