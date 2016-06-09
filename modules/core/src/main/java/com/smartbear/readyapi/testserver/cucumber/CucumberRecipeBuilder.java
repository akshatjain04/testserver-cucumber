package com.smartbear.readyapi.testserver.cucumber;

import com.google.common.collect.Lists;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.runtime.java.guice.ScenarioScoped;

import javax.inject.Inject;
import java.util.List;

/**
 * Class that builds and runs the recipe to be executed
 */

@ScenarioScoped
public class CucumberRecipeBuilder {

    private final CucumberRecipeExecutor executor;
    private List<TestStep> testSteps = Lists.newArrayList();
    private final TestCase testCase;

    @Inject
    public CucumberRecipeBuilder(CucumberRecipeExecutor executor) {
        this.executor = executor;

        testCase = new TestCase();
        testCase.setFailTestCaseOnError(true);
    }

    @After
    public void run(Scenario scenario) {
        testCase.setTestSteps(testSteps);
        executor.runTestCase(testCase, scenario);
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void addTestStep(TestStep testStep) {
        testSteps.add(testStep);
    }

    public CucumberRecipeExecutor getExecutor() {
        return executor;
    }
}
