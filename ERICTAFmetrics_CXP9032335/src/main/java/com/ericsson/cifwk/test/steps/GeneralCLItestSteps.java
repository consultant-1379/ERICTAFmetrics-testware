package com.ericsson.cifwk.test.steps;



import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;
import org.apache.log4j.Logger;

public class GeneralCLItestSteps extends TorTestCaseHelper {

    Logger logger = Logger.getLogger(GeneralCLItestSteps.class);

    private Host host = DataHandler.getHostByName("metrics");
    public static final String GENERATE_EVENTS = "CIP-5996_Func_1";

    @TestStep(id = GENERATE_EVENTS)
    public void verifyManagePyFunctionality() {
        CLICommandHelper cmdHelper = new CLICommandHelper(host);
        logger.info("start sending events");
            cmdHelper.execute("java -cp \"/root/opt/ericsson/*\" com.ericsson.cifwk.metricsepe.EiffelEventsSender"); //Execute some script which echos output
        cmdHelper.execute("curl -XPOST  -H \"Content-Type: application/json\"  http://localhost:8888/metrics-services/sprint -d'{\"name\": \"15.1\",\"startDate\": \"2016-01-25T00:00:00.000Z\",\"endDate\": \"2027-02-14T23:59:59.999Z\",\"release\": \"1A\"}'"); //Execute some script which echos output
        cmdHelper.disconnect(); // Closes all open shell instances and closes all sessions.
        logger.info("start sending events");
        setTestStep("Verifying exit value");
        assertEquals(true, cmdHelper.getCommandExitValue() == 0);
    }
}
