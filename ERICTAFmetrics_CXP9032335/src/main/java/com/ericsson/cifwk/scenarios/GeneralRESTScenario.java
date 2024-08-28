package com.ericsson.cifwk.scenarios;

import com.google.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.flows.GeneralRESTFlow;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.api.ExceptionHandler;
import com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;

public class GeneralRESTScenario extends TorTestCaseHelper implements TestCase {

	@Inject
	GeneralRESTFlow generalRESTFlow;

	/***
	 * Function is used to handle SCM messages
	 */
	@Test
	public void elasticsearchSCMScenario() {
		TestScenario scenario = scenario("Elasticsearch SCM event Scenario")
				.addFlow(generalRESTFlow.runElasticsearchSCMEvents()).build();
		TestScenarioRunner runner = runner()
				.withListener(new LoggingScenarioListener())
				.withExceptionHandler(ExceptionHandler.PROPAGATE).build();
		runner.start(scenario);

	}

	/***
	 * Function is used to handle CLME DELIVERED/OBSOLETED messages
	 */
	@Test
	public void elasticsearchDeliveryCLMEScenario() {

		TestScenario scenario = scenario(
				"Elasticsearch CLME Delivery Information Scenario").addFlow(
				generalRESTFlow.runElasticsearchCLMEEvents()).build();
		TestScenarioRunner runner = runner()
				.withListener(new LoggingScenarioListener())
				.withExceptionHandler(ExceptionHandler.PROPAGATE).build();
		runner.start(scenario);
	}
	/***
	 * Function is used to handle KGB/CDB Started & Completed (No Initial) CLME
	 * messages
	 */
	@Test
	public void elasticsearchCdbKgbNoInitialFoundCLMEScenario() {

		TestScenario scenario = scenario(
				"Elasticsearch CLME KGB CDB No Initial Delivery Information Scenario")
				.addFlow(
						generalRESTFlow
								.runElasticsearchCLME_KGBCDBNoInitialFoundEvents())
				.build();
		TestScenarioRunner runner = runner()
				.withListener(new LoggingScenarioListener())
				.withExceptionHandler(ExceptionHandler.PROPAGATE).build();
		runner.start(scenario);
	}

	/***
	 * Function is used to handle KGB/CDB Started & Completed (With Initial)
	 * CLME messages
	 */
	@Test
	public void elasticsearchCdbKgbInitialFoundCLMEScenario() {

		TestScenario scenario = scenario(
				"Elasticsearch CLME KGB CDB Initial Delivery Information Scenario")
				.addFlow(
						generalRESTFlow
								.runElasticsearchCLME_KGBCDBInitialFoundEvents())
				.build();
		TestScenarioRunner runner = runner()
				.withListener(new LoggingScenarioListener())
				.withExceptionHandler(ExceptionHandler.PROPAGATE).build();
		runner.start(scenario);
	}

	/***
	 * Function is used to handle CLME GROUP messages
	 */
	@Test
	public void elasticsearchDeliveryGroupScenario() {

		TestScenario scenario = scenario(
				"Elasticsearch CLME Group Delivery Information Scenario")
				.addFlow(generalRESTFlow.runElasticsearchGroupEvents()).build();
		TestScenarioRunner runner = runner()
				.withListener(new LoggingScenarioListener())
				.withExceptionHandler(ExceptionHandler.PROPAGATE).build();
		runner.start(scenario);
	}

	/***
	 * Function is used to handle CNE messages
	 */
	@Test
	public void elasticsearchCneScenario() {

		TestScenario scenario = scenario(
				"Elasticsearch CNE Delivery Information Scenario").addFlow(
				generalRESTFlow.runElasticsearchCNEEvents()).build();
		TestScenarioRunner runner = runner()
				.withListener(new LoggingScenarioListener())
				.withExceptionHandler(ExceptionHandler.PROPAGATE).build();
		runner.start(scenario);
	}
}
