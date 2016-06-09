package com.smartbear.readyapi.testserver.cucumber;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.smartbear.readyapi.client.ExecutionListener;
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

import java.io.File;
import java.io.IOException;
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

    private RecipeExecutor executor;

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

    public Execution runTestCase(TestCase testCase) {

        TestRecipe testRecipe = new TestRecipe(testCase);

        if( LOG.isDebugEnabled() ){
            LOG.debug( testRecipe.toString());
        }

        Execution execution = executor.executeRecipe(testRecipe);

        assertEquals(Arrays.toString(execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());

        return execution;
    }

    public void addExecutionListener(ExecutionListener listener) {
        executor.addExecutionListener(listener);
    }

    public void removeExecutionListener(ExecutionListener listener) {
        executor.removeExecutionListener(listener);
    }

    public RecipeExecutor getExecutor() {
        return executor;
    }
}
