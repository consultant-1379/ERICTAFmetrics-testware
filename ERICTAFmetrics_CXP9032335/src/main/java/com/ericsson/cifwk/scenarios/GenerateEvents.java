package com.ericsson.cifwk.scenarios;

import com.ericsson.cifwk.flows.GeneralCLIFlow;
import com.ericsson.cifwk.flows.GeneralRESTFlow;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.api.ExceptionHandler;
import com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;

public class GenerateEvents extends TorTestCaseHelper implements TestCase {

    @Inject
    GeneralCLIFlow generalCLIFlow;

    @Test
    public void elasticsearchScenario() {
        TestScenario scenario = scenario("generate events")
                .addFlow(generalCLIFlow.generateEvents())
                .build();
        TestScenarioRunner runner = runner()
                .withListener(new LoggingScenarioListener())
                .withExceptionHandler(ExceptionHandler.PROPAGATE)
                .build();
        runner.start(scenario);

    }


}



