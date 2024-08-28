package com.ericsson.cifwk.flows;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.ericsson.cifwk.test.steps.GeneralCLItestSteps;
import com.google.inject.Inject;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;


public class GeneralCLIFlow implements TestCase{

    @Inject
    GeneralCLItestSteps generalCLITestSteps;
    
    public TestStepFlow generateEvents() {
        TestStepFlow restCalls = flow("Generate Events")
                .addTestStep(annotatedMethod(
                                generalCLITestSteps,
                                GeneralCLItestSteps.GENERATE_EVENTS))
                .build();
        return restCalls;
    }

}
