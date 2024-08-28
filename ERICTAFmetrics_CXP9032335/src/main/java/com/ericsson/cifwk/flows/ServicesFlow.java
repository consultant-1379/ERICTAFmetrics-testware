package com.ericsson.cifwk.flows;

import com.google.inject.Inject;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.ericsson.cifwk.test.steps.GeneralRESTtestSteps;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.annotatedMethod;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.flow;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.dataSource;

public class ServicesFlow implements TestCase {

	@Inject
	GeneralRESTtestSteps generalRESTTestSteps;



		public TestStepFlow servicesRestCalls() {
				TestStepFlow restCalls = flow(
						"Services Rest Calls")
						.addTestStep(
								annotatedMethod(
										generalRESTTestSteps,
										GeneralRESTtestSteps.RUN_SERVICES_REST))
						.withDataSources(dataSource("servicesRestData")).build();
				return restCalls;
		}

}
