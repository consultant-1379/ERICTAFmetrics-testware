package com.ericsson.cifwk.test.steps;

import javax.inject.Inject;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import com.ericsson.cifwk.monitor.operators.MetricsMonitorToolOperator;
import com.ericsson.cifwk.monitor.operators.MetricsMonitorToolOperatorUI;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.execution.TestExecutionEvent;
import com.ericsson.cifwk.taf.ui.UI;

public class MetricsPageMonitorTestStep extends TorTestCaseHelper implements TestCase{

    @Inject
    MetricsMonitorToolOperatorUI monitorToolOperator;

    @BeforeTest()
    public void setUp() {
        System.out.println("-----Setting up connection-----");
        UI.closeWindow(TestExecutionEvent.ON_SUITE_FINISH);
        monitorToolOperator.setUpBrowser();
    }

    @Test(dependsOnMethods ="setUp" )
    public void verifyTitle() {
        System.out.println("-----verifyTitle-----");
        assertEquals("Radiator", monitorToolOperator.verifyRadiatorPageTitle());
    }

    @Test(dependsOnMethods ="verifyTitle")
    public void verifyENMISO(){
        System.out.println("-----verifyENMISO-----");
        assertEquals("ENM ISO", monitorToolOperator.verifyENM_ISO());
    }

    @Test(dependsOnMethods ="verifyENMISO")
    public void verifyProductSet(){
    System.out.println("-----verifyProductSet-----");
        assertEquals("Product Set", monitorToolOperator.verifyProduct_Set());
    }

    @Test(dependsOnMethods ="verifyProductSet")
    public void verifyUG(){
    System.out.println("-----verifyUG-----");
        assertEquals("UG", monitorToolOperator.verify_UG());
    }

    @Test(dependsOnMethods ="verifyUG")
    public void verifyUGAvailability(){
    System.out.println("-----verifyUGAvailability-----");
        assertEquals("UG Availability", monitorToolOperator.verifyUG_Availability());
    }

    @Test(dependsOnMethods ="verifyUGAvailability")
    public void verifyUGPerformance(){
    System.out.println("-----verifyUGPerformance-----");
        assertEquals("UG Performance", monitorToolOperator.verifyUG_Performance());
    }

    @Test(dependsOnMethods ="verifyUGPerformance")
    public void verifyRFA250(){
    System.out.println("-----verifyRFA250-----");
        assertEquals("RFA 250", monitorToolOperator.verifyRFA_250());
    }

    @Test(dependsOnMethods ="verifyRFA250")
    public void verifyUpgradeBaseline(){
    System.out.println("-----verifyUpgradeBaseline-----");
        assertEquals("Upgrade Baseline", monitorToolOperator.verifyUpgrade_Baseline());
    }

    @Test(dependsOnMethods ="verifyUpgradeBaseline")
    public void verifyMaintrack(){
    System.out.println("-----verifyMaintrack-----");
         assertTrue(monitorToolOperator.verify_Maintrack().contains("Maintrack is"));
    }

    @Test(dependsOnMethods ="verifyMaintrack")
    public void verifyvAppBaseline(){
    System.out.println("-----verifyvAppBaseline-----");
        assertEquals("vApp Baseline", monitorToolOperator.verifyvApp_Baseline());
    }

    @Test(dependsOnMethods ="verifyvAppBaseline")
    public void verifyMicroIIBaseline(){
    System.out.println("-----verifyMicroIIBaseline-----");
        assertEquals("Micro II Baseline", monitorToolOperator.verifyMicro_II_Baseline());
    }

    @Test(dependsOnMethods ="verifyMicroIIBaseline")
    public void verifyInitialInstallBaseline(){
    System.out.println("-----verifyInitialInstallBaseline-----");
        assertEquals("Initial Install Baseline", monitorToolOperator.verifyInitial_Install_Baseline());
    }

    @Test(dependsOnMethods ="verifyInitialInstallBaseline")
    public void verifyRVBBaseline(){
    System.out.println("-----verifyRVBBaseline-----");
        assertEquals("RVB Baseline", monitorToolOperator.verifyRVB_Baseline());
    }

    @Test(dependsOnMethods ="verifyRVBBaseline")
    public void verifyRVBCurrent(){
    System.out.println("-----verifyRVBCurrent-----");
        assertEquals("RVB Current", monitorToolOperator.verifyRVB_Current());
    }

}
