package com.ericsson.cifwk.scenarios;

import com.ericsson.cifwk.flows.*;
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

public class ServicesScenario extends TorTestCaseHelper implements TestCase {

    @Inject
    ServicesFlow servicesFlow;



		@Test
		public void servicesRestScenario() {

				TestScenario scenario = scenario(
						"ServicesRest").addFlow(
						servicesFlow.servicesRestCalls()).build();
				TestScenarioRunner runner = runner()
						.withListener(new LoggingScenarioListener())
						.withExceptionHandler(ExceptionHandler.PROPAGATE).build();
				runner.start(scenario);
		}
}



