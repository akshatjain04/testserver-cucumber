package com.smartbear.readyapi.testserver.cucumber;

import com.google.common.collect.Lists;
import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.execution.RecipeExecutor;
import com.smartbear.readyapi.client.execution.Scheme;
import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.RestParameter;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CucumberRecipeExecutor {

    private final static Logger LOG = LoggerFactory.getLogger( CucumberRecipeExecutor.class );
    private static final String TESTSERVER_ENDPOINT = "testserver.endpoint";
    private static final String TESTSERVER_USER = "testserver.user";
    private static final String TESTSERVER_PASSWORD = "testserver.password";
    private static final String DEFAULT_TESTSERVER_ENDPOINT = "http://testserver.readyapi.io:8080";
    private static final String DEFAULT_TESTSERVER_USER = "demoUser";
    private static final String DEFAULT_TESTSERVER_PASSWORD = "demoPassword";

    private List<TestStep> testSteps = Lists.newArrayList();
    private RecipeExecutor executor;
    private TestRecipe testRecipe;

    public CucumberRecipeExecutor() throws MalformedURLException {
        Map<String, String> env = System.getenv();
        URL url = new URL( env.getOrDefault( TESTSERVER_ENDPOINT,
            System.getProperty(TESTSERVER_ENDPOINT, DEFAULT_TESTSERVER_ENDPOINT)));

        executor = new RecipeExecutor( Scheme.valueOf(url.getProtocol().toUpperCase()),
            url.getHost(), url.getPort());

        String user = env.getOrDefault( TESTSERVER_USER,
            System.getProperty(TESTSERVER_USER, DEFAULT_TESTSERVER_USER));

        String password = env.getOrDefault(TESTSERVER_PASSWORD,
            System.getProperty(TESTSERVER_PASSWORD, DEFAULT_TESTSERVER_PASSWORD));

        executor.setCredentials( user, password );
    }

    public Execution runTestCase() {
        if( !testSteps.isEmpty() ) {

            TestCase testCase = new TestCase();
            testCase.setFailTestCaseOnError(true);
            testCase.setTestSteps( testSteps );

            testRecipe = new TestRecipe(testCase);

            if( System.getProperty("testserver.debug") != null ){
                LOG.debug( testRecipe.toString());
            }

            Execution execution = executor.executeRecipe(testRecipe);

            assertEquals(Arrays.toString(execution.getErrorMessages().toArray()),
                ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());

            return execution;
        }

        return null;
    }

    public TestRecipe getTestRecipe() {
        return testRecipe;
    }

    public RecipeExecutor getExecutor() {
        return executor;
    }

    public <T extends TestStep> T addTestStep(T testStep) {
        testSteps.add(testStep);
        return testStep;
    }

    public TestStep getLastTestStep() {
        return testSteps.isEmpty() ? null : testSteps.get(0);
    }

    @Deprecated
    public void setAssertions(List<Assertion> assertions) {
        addAssertions( assertions );
    }

    public void addAssertions(List<Assertion> assertions) {
        TestStep testStep = getLastTestStep();
        if( testStep instanceof  RestTestRequestStep ){
            ((RestTestRequestStep)testStep).setAssertions( assertions );
        }
    }

    public void setParameters(List<RestParameter> parameters) {
        TestStep testStep = getLastTestStep();
        if( testStep instanceof  RestTestRequestStep ){
            ((RestTestRequestStep)testStep).setParameters( parameters );
        }
    }

    @Deprecated
    public <T extends TestStep> T setTestStep(T testStep) {
        return addTestStep( testStep );
    }
}
