## Ready! API TestServer Cucumber Integration

This project provides a generic Cucumber vocabulary for testing APIs with the Ready! API TestServer,
with special support for Swagger to remove some of the technicalities required to define scenarios. 

A quick example for the Petstore API at http://petstore.swagger.io, testing of the 
/pet/findByTags resource could be defined withe following Scenario:

```
  Scenario: Find pet by tags
    Given the API running at http://petstore.swagger.io/v2
    When a GET request to /pet/findByTags is made
    And the tags parameter is test
    And the request expects json
    Then a 200 response is returned within 50ms
    And the response type is json
```

Using the integrated Swagger support this can be shortened to

```
  Scenario: Find pet by tags
    Given the Swagger definition at http://petstore.swagger.io/v2/swagger.json
    # deducts path and method from Swagger definition by operationId
    When a request to findPetsByTags is made
    # deducts type of "tags" parameter (query/path/parameter/body) from Swagger definition
    And tags is test
    And the request expects json
    Then a 200 response is returned within 500ms
    And the response type is json
```

Not a huge difference - but as you can see by the comments the Swagger support removes some of the 
technicalities; read more about Swagger specific steps and vendor extensions below!

### Usage with maven

If you want to run scenarios as part of a maven build you need to add the following 
dependency to your pom:

```
&lt;dependency&gt;
    &lt;groupId&gt;com.smartbear.readyapi.testserver.cucumber&lt;/groupId&gt;
    &lt;artifactId&gt;testserver-cucumber-core&lt;/artifactId&gt;
    &lt;version&gt;1.0.0&lt;/version&gt;
    &lt;scope&gt;test&lt;/scope&gt;
&lt;/dependency&gt;
```

Then create a JUnit runner class that uses Cucumber and point it to your feature files:
 
```java
@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber"},
    features = {"src/test/resources/cucumber"})
public class CucumberTest {
}
```

(see the included samples module for a working project)

### Usage without maven

If you're not running java or simply want to run cucumber tests from the command-line you can use the 
testserver-cucumber-all jar file which includes all required libraries including the Cucumber
runtime. Run tests with:

```
java -jar testserver-cucumber-all-1.0.0.jar &lt;path to feature-files&gt;
```

Internally this will call the regular cucumber.api.cli.Main class with an added -g argument to the
included glue-code, all other options are passed as usual, see https://cucumber.io/docs/reference/jvm#cli-runner

(you will need java8 installed on your path)

### Configuring Ready! API TestServer access
 
The included [Cucumber StepDefs](https://github.com/olensmar/testserver-cucumber/blob/master/modules/core/src/main/java/com/smartbear/readyapi/testserver/cucumber/GenericRestStepDefs.java) 
build and execute test recipes agains the Ready! API TestServer using the 
[testserver-java-client](https://github.com/SmartBear/ready-api-testserver-client), by default they 
will submit recipes to the publicly available TestServer at http://testserver.readyapi.io. If you 
want to run against your own TestServer instance to be able to access internal APIs or not run into 
throttling issues you need to download and install the TestServer from 
https://smartbear.com/product/ready-api/testserver/overview/ and configure access to it by 
specifying the corresponding system properties when running your tests:

- testserver.endpoint=...url to your testserver installation...
- testserver.user=...the configured user to use...
- testserver.password=...the configured password for that user...

(these are picked up by [CucumberRecipeExecutor](https://github.com/olensmar/testserver-cucumber/blob/master/modules/core/src/main/java/com/smartbear/readyapi/testserver/cucumber/CucumberRecipeExecutor.java) during execution)

### Building 

Clone this project and and run
 
```
mvn clean install 
```

To build and install it in your local maven repository.

## API Testing Vocabulary
 
The included glue-code for API testing adds the following vocabulary:

### Given statements

#### the Swagger definition at &lt;swagger endpoint&gt;
The specified endpoint must reference a valid Swagger 2.0 definition

##### the API running at &lt;API endpoint&gt;

### When/And statements

- "a &lt;HTTP Method&gt; request to &lt;path&gt; is made"
- "a request to &lt;Swagger OperationID&gt; is made"
    - will fail if no Swagger definition has been Given

- "the request body is" &lt;text block&gt;
- "the &lt;parameter name&gt; parameter is &lt;parameter value&gt;"
    - adds the specified parameter as a query parameter
- "the &lt;http header&gt; is &lt;header value&gt;
- "the type is &lt;content-type&gt;
    - single word types will be expanded to "application/&lt;content-type&gt;"

- "&lt;parameter name&gt; is &lt;parameter value&gt;"
    - if a valid OperationId has been given the type of parameter will be deduced from its list of parameters
    - if no OperationId has been given this will be added to a map of values that will be sent as the request body
- "&lt;the request expects &lt;content-type&gt;"
    - adds an Accept header

### Then/And statements:

- "a &lt;HTTP Status code&gt; response is returned"
- "a &lt;HTTP Status code&gt; response is returned within &lt;number&gt;ms"
- "the response is &lt;a valid Swagger Response description for the specified operationId&gt;"
- "the response body contains" &lt;text block&gt;
- "the response body matches" &lt;regex text block&gt;
- "the response type is &lt;content-type&gt;"
- "the response contains a &lt;http-header name&gt; header"
- "the response &lt;http header name&gt; is &lt;http header value&gt;"
- "the response body contains &lt;text token&gt;"

## Contribute!

If you're missing something please contribute or open an issue!
