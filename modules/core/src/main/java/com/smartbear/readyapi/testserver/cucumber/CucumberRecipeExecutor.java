package com.smartbear.readyapi.testserver.cucumber;

import com.smartbear.readyapi.client.ExecutionListener;
import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.execution.RecipeExecutor;
import com.smartbear.readyapi.client.execution.Scheme;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.TestCase;
import cucumber.api.Scenario;
import io.swagger.util.Json;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CucumberRecipeExecutor {

    private final static Logger LOG = LoggerFactory.getLogger(CucumberRecipeExecutor.class);
    private static final String TESTSERVER_ENDPOINT = "testserver.endpoint";
    private static final String TESTSERVER_USER = "testserver.user";
    private static final String TESTSERVER_PASSWORD = "testserver.password";
    private static final String DEFAULT_TESTSERVER_ENDPOINT = "http://testserver.readyapi.io:8080";
    private static final String DEFAULT_TESTSERVER_USER = "demoUser";
    private static final String DEFAULT_TESTSERVER_PASSWORD = "demoPassword";

    private RecipeExecutor executor;

    public CucumberRecipeExecutor() throws MalformedURLException {
        Map<String, String> env = System.getenv();
        URL url = new URL(env.getOrDefault(TESTSERVER_ENDPOINT,
            System.getProperty(TESTSERVER_ENDPOINT, DEFAULT_TESTSERVER_ENDPOINT)));

        executor = new RecipeExecutor(Scheme.valueOf(url.getProtocol().toUpperCase()),
            url.getHost(), url.getPort());

        String user = env.getOrDefault(TESTSERVER_USER,
            System.getProperty(TESTSERVER_USER, DEFAULT_TESTSERVER_USER));

        String password = env.getOrDefault(TESTSERVER_PASSWORD,
            System.getProperty(TESTSERVER_PASSWORD, DEFAULT_TESTSERVER_PASSWORD));

        executor.setCredentials(user, password);
    }

    public Execution runTestCase(TestCase testCase, Scenario scenario) {

        TestRecipe testRecipe = new TestRecipe(testCase);

        if (LOG.isDebugEnabled()) {
            LOG.debug(testRecipe.toString());
        }

        String logFolder = System.getProperty( "testserver.cucumber.logfolder", null );
        if( logFolder != null ){
            logScenarioToFile(scenario, testRecipe, logFolder);
        }

        Execution execution = executor.executeRecipe(testRecipe);

        assertEquals(Arrays.toString(execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());

        return execution;
    }

    private void logScenarioToFile(Scenario scenario, TestRecipe testRecipe, String logFolder) {
        try {
            File folder = new File( logFolder );
            if( !folder.exists() || !folder.isDirectory()){
                folder.mkdirs();
            }

            String[] pathSegments = scenario.getId().split(";");
            File scenarioFolder = folder;
            int fileIndex = 0;

            if( pathSegments.length > 1 ) {
                scenarioFolder = new File(folder, pathSegments[0]);
                if (scenarioFolder.exists() || !scenarioFolder.isDirectory()) {
                    scenarioFolder.mkdirs();
                }

                fileIndex = 1;
            }

            String filename = pathSegments[fileIndex];
            for( int c = fileIndex+1; c < pathSegments.length; c++ ){
                String segment = pathSegments[c].trim();
                if( !StringUtils.isBlank( segment )){
                    filename += "_" + segment;
                }
            }

            filename += ".json";

            File scenarioFile = new File( scenarioFolder, filename );
            FileWriter writer = new FileWriter( scenarioFile );

            LOG.info("Writing recipe to " + folder.getName() + File.separatorChar + scenarioFolder.getName() +
                File.separatorChar + scenarioFile.getName());

            writer.write( Json.pretty(testRecipe) );
            writer.close();
        } catch (Exception e) {
            LOG.error("Failed to write recipe to logFolder [" + logFolder + "]", e );
        }
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
