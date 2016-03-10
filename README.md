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
<dependency>
    <groupId>com.smartbear.readyapi.testserver.cucumber</groupId>
    <artifactId>testserver-cucumber-core</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
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
java -jar testserver-cucumber-all-1.0.0.jar <path to feature-files>
```

Internally this will call the regular cucumber.api.cli.Main class with an added -g argument to the
included glue-code, all other options are passed as usual, see https://cucumber.io/docs/reference/jvm#cli-runner

(you will need java8 installed on your path)

### Configuring Ready! API TestServer access
 
The included [Cucumber StepDefs](https://github.com/olensmar/testserver-cucumber/blob/master/modules/core/src/main/java/com/smartbear/readyapi/testserver/cucumber/GenericRestStepDefs.java) 
build and execute test recipes agains the Ready! API TestServer using the 
[testserver-java-client](https://github.com/SmartBear/ready-api-testserver-client), by default they 
will submit recipes to the publically available TestServer at http://testserver.readyapi.io. If you 
want to run against your own TestServer instance to be able to use some of the advanced features 
(DataSources, etc) or not run into throttling issues you need to download and install the TestServer 
from https://smartbear.com/product/ready-api/testserver/overview/ and configure access to it by 
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

...


