package com.ericsson.cifwk.monitor.operators;

import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.model.pages.MetricsMonitorToolRadiatorView;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;

import javax.inject.Singleton;

@Operator(context = Context.UI)
@Singleton
public class MetricsMonitorToolOperatorUI implements MetricsMonitorToolOperator{

    private String testPage;
    private String titlePage;
    private Browser browser;
    private BrowserTab browserTab;
    private ViewModel viewModel;
    private UiComponent components;
    private MetricsMonitorToolRadiatorView MetricsMonitorToolRadiatorView;

     @Override
     public void setUpBrowser() {
        //testPage = "http://de.lmera.ericsson.se/metrics/#ci-metrics";
        testPage = "http://de.lmera.ericsson.se/metrics/#maintrack-radiator";
        this.browser = UI.newBrowser(BrowserType.FIREFOX);
        this.browserTab = browser.open(testPage);
        viewModel = browserTab.getGenericView();
        //components = viewModel.getLabel(SelectorType.XPATH, "//div[@class='ebLayout-AppHeading elLayouts-AppHeading']/h1[contains(text(),'Metrics Dashboard')]");
        components = viewModel.getLabel(SelectorType.XPATH, "//span[@class='elCommonLib-wMaintrackStatusBanner-titleText'][contains(text(),'Maintrack is')]");
        browserTab.waitUntilComponentIsDisplayed(components, 5000);
     }

     @Override
     public String verifyMetricsPageTitle() {
     // TODO Auto-generated method stub
     return testPage;
     }

     @Override
     public String verifyRadiatorPageTitle() {
     // TODO Auto-generated method stub
     titlePage= browserTab.getTitle();
     //Assert.assertEquals(testPage, "Radiator");
     return titlePage;
     }

     @Override
     public String verifyENM_ISO() {
     // TODO Auto-generated method stub
     MetricsMonitorToolRadiatorView = browserTab.getView(MetricsMonitorToolRadiatorView.class);
     String ENM = MetricsMonitorToolRadiatorView.ENM_ISOLabel();
       return ENM;
       //return testPage;
     }

    @Override
    public String verifyProduct_Set() {

    return MetricsMonitorToolRadiatorView.Product_Set.getText();
    }

    @Override
    public String verify_UG() {
    return MetricsMonitorToolRadiatorView.UG.getText();
    }

    @Override
    public String verifyUG_Availability() {
    return MetricsMonitorToolRadiatorView.UG_Availability.getText();
    }

    @Override
    public String verifyUG_Performance() {
    return MetricsMonitorToolRadiatorView.UG_Performance.getText();
    }

    @Override
    public String verifyRFA_250() {
    return MetricsMonitorToolRadiatorView.RFA_250.getText();
    }

    @Override
    public String verifyUpgrade_Baseline() {
    return MetricsMonitorToolRadiatorView.Upgrade_Baseline.getText();
    }

    @Override
    public String verify_Maintrack() {
    return MetricsMonitorToolRadiatorView.Maintrack.getText();
    }

    @Override
    public String verifyvApp_Baseline() {
    return MetricsMonitorToolRadiatorView.vApp_Baseline.getText();
    }

    @Override
    public String verifyMicro_II_Baseline() {
    return MetricsMonitorToolRadiatorView.Micro_II_Baseline.getText();
    }

    @Override
    public String verifyInitial_Install_Baseline() {
    return MetricsMonitorToolRadiatorView.Initial_Install_Baseline.getText();
    }

    @Override
    public String verifyRVB_Baseline() {
    return MetricsMonitorToolRadiatorView.RVB_Baseline.getText();
    }

    @Override
    public String verifyRVB_Current() {
    return MetricsMonitorToolRadiatorView.RVB_Current.getText();
    }

    //@Override
    //public String verifyWholeText() {
    //    return MetricsMonitorToolRadiatorView.WholeText.getText();
    //}

    public boolean tearDown(){
        browser.close();
        return browser.isClosed();
    }

}
