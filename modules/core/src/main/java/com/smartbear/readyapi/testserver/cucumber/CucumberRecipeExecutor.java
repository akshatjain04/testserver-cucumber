package com.smartbear.readyapi.testserver.cucumber;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.execution.RecipeExecutor;
import com.smartbear.readyapi.client.execution.Scheme;
import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.Parameter;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CucumberRecipeExecutor {

    private final static Logger LOG = LoggerFactory.getLogger( CucumberRecipeExecutor.class );

    private RestTestRequestStep testStep;
    private RecipeExecutor executor;

    public CucumberRecipeExecutor() throws MalformedURLException {
        URL url = new URL( System.getProperty( "testserver.endpoint", "http://testserver.readyapi.io:8080" ));

        executor = new RecipeExecutor( Scheme.valueOf(url.getProtocol().toUpperCase()),
            url.getHost(), url.getPort());

        String user = System.getProperty( "testserver.user", "demoUser" );
        String password = System.getProperty( "testserver.password", "demoPassword" );

        executor.setCredentials( user, password );
    }

    public void runTestCase() {
        if( testStep != null ) {

            replacePathParameters();

            TestCase testCase = new TestCase();
            testCase.setFailTestCaseOnError(true);
            testCase.setTestSteps(Arrays.<TestStep>asList(testStep));
            testStep = null;

            TestRecipe recipe = new TestRecipe(testCase);

            if( System.getProperty("testserver.debug") != null ){
                LOG.debug( recipe.toString());
            }

            Execution execution = executor.executeRecipe(recipe);

            assertEquals(Arrays.toString(execution.getErrorMessages().toArray()),
                ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
        }
    }

    private void replacePathParameters() {
        for( Parameter param : testStep.getParameters()){
            if( param.getType().equalsIgnoreCase("PATH")){
                testStep.setURI(testStep.getURI().replaceFirst( "\\{" + param.getName() + "\\}", param.getValue()));
            }
        }
    }

    public void setTestStep(RestTestRequestStep testStep) {
        this.testStep = testStep;
    }

    public RestTestRequestStep getTestStep() {
        return testStep;
    }

    public void setAssertions(List<Assertion> assertions) {
        if( testStep != null ) {
            testStep.setAssertions(assertions);
        }
    }

    public void setParameters(List<Parameter> parameters) {
        if( testStep != null ){
            testStep.setParameters( parameters );
        }
    }
}
