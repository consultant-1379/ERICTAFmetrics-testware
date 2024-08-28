package com.ericsson.cifwk.flows;

import com.google.inject.Inject;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.ericsson.cifwk.test.steps.GeneralRESTtestSteps;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.annotatedMethod;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.flow;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.dataSource;

public class GeneralRESTFlow implements TestCase {

	@Inject
	GeneralRESTtestSteps generalRESTTestSteps;

	public TestStepFlow runElasticsearchSCMEvents() {
		TestStepFlow restCalls = flow(
				"Json Return From ELASTICSEARCH for SCM events")
				.addTestStep(
						annotatedMethod(
								generalRESTTestSteps,
								GeneralRESTtestSteps.RUN_SCM_EVENTS_ELASTICSEARCH))
				.withDataSources(dataSource("elasticsearchSCMData")).build();
		return restCalls;
	}

	public TestStepFlow runElasticsearchSCMEnrichedEvents() {
		TestStepFlow restCalls = flow(
				"Json Return From ELASTICSEARCH for SCM Enriched events")
				.addTestStep(
						annotatedMethod(
								generalRESTTestSteps,
								GeneralRESTtestSteps.RUN_SCM_ENRICHED_EVENTS_ELASTICSEARCH))
				.withDataSources(dataSource("elasticsearchSCMEnrichedData"))
				.build();
		return restCalls;
	}

	public TestStepFlow runElasticsearchCLMEEvents() {
		TestStepFlow restCalls = flow(
				"Json Return From ELASTICSEARCH for Delivery Information")
				.addTestStep(
						annotatedMethod(
								generalRESTTestSteps,
								GeneralRESTtestSteps.RUN_CLME_EVENTS_ELASTICSEARCH))
				.withDataSources(dataSource("elasticsearchCLMEData")).build();
		return restCalls;
	}

	public TestStepFlow runElasticsearchCLMEDeliveredObsoletedEvents() {
		TestStepFlow restCalls = flow(
				"Json Return From ELASTICSEARCH for Delivery Information")
				.addTestStep(
						annotatedMethod(
								generalRESTTestSteps,
								GeneralRESTtestSteps.RUN_CLME_DELIVERED_OBSOLETED_EVENTS_ELASTICSEARCH))
				.withDataSources(
						dataSource("elasticsearchCLMEDeliveredObsoletedData"))
				.build();
		return restCalls;
	}

	public TestStepFlow runElasticsearchCLME_KGBCDBNoInitialFoundEvents() {
		TestStepFlow restCalls = flow(
				"Json Return From ELASTICSEARCH for CDB/KGB with no Initial event Information")
				.addTestStep(
						annotatedMethod(
								generalRESTTestSteps,
								GeneralRESTtestSteps.RUN_CLME_KGB_CDB_NO_INITIAL_EVENTS_ELASTICSEARCH))
				.withDataSources(
						dataSource("elasticsearchCLMEKGBCDBNoInitialFoundData"))
				.build();
		return restCalls;
	}

	public TestStepFlow runElasticsearchCLME_KGBCDBInitialFoundEvents() {
		TestStepFlow restCalls = flow(
				"Json Return From ELASTICSEARCH for CDB/KGB with Initial event Information")
				.addTestStep(
						annotatedMethod(
								generalRESTTestSteps,
								GeneralRESTtestSteps.RUN_CLME_KGB_CDB_INITIAL_EVENTS_ELASTICSEARCH))
				.withDataSources(
						dataSource("elasticsearchCLMEKGBCDBInitialFoundData"))
				.build();
		return restCalls;
	}

	public TestStepFlow runElasticsearchGroupEvents() {
		TestStepFlow restCalls = flow(
				"Json Return From ELASTICSEARCH for Group Delivery Information")
				.addTestStep(
						annotatedMethod(
								generalRESTTestSteps,
								GeneralRESTtestSteps.RUN_GROUPS_EVENTS_ELASTICSEARCH))
				.withDataSources(dataSource("elasticsearchGroupData")).build();
		return restCalls;
	}

	public TestStepFlow runElasticsearchCNEEvents() {
		TestStepFlow restCalls = flow(
				"Json Return From ELASTICSEARCH for CNE Delivery Information")
				.addTestStep(
						annotatedMethod(
								generalRESTTestSteps,
								GeneralRESTtestSteps.RUN_CNE_EVENTS_ELASTICSEARCH))
				.withDataSources(dataSource("elasticsearchCNEData")).build();
		return restCalls;
	}

}
