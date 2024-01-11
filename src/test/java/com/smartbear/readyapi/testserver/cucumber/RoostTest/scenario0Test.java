/*
Test generated by RoostGPT for test MiniProjects using AI Type Azure Open AI and AI Model roost-gpt4-32k

{
  background: null,
  rule: null,
  scenario: 'Find pet by status","    Given the API running at http://petstore.swagger.io/v2","    And an endpoint of http://petstore.swagger.io/v2/pet/findByStatus","    When a GET request is made","    And the status parameter is test","    And the Accepts header is application/json","    Then a 200 response is returned within 50ms","","',
  title: 'Find pet by status","    Given the API running at http://petstore.swagger.io/v2","    And an endpoint of http://petstore.swagger.io/v2/pet/findByStatus","    When a GET request is made","    And the status parameter is test","    And the Accepts header is application/json","    Then a 200 response is returned within 50ms","","'
}

*/

package com.smartbear.readyapi.testserver.cucumber.RoostTest;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class scenario0Test {

    @DisplayName("Test to find pet by status")
    @Test
    public void scenarioFindPetByStatus() throws IOException {

        // to load data from csv file
        Scanner scanner = new Scanner(new File("scenario0Test.csv"));
        scanner.useDelimiter("^|^");

        // to ignore headers
        if (scanner.hasNext()) {
            scanner.next();
        }

        while (scanner.hasNext()) {
            String row = scanner.next();
            String[] data = row.split("\\^|\\^"); // split per the given delimiter

            String methodType = data[0];  // GET
            String url = data[1];  // http://petstore.swagger.io/v2/pet/findByStatus
            String headers = data[2];  // Accepts: application/json
            String requestBody = data[3];  // status: test
            int responseCode = Integer.parseInt(data[4]);  // 200
            String responseBody = data[5];  // id:int64,category:object,category.id:int64,category.name:string,name:string,photoUrls:array,tags:array,status:string

            Map<String, Object> requestHeaders = new HashMap<>();
            String[] rawHeaders = headers.split(",");
            for (String rawHeader : rawHeaders) {
                String[] keyValue = rawHeader.split(":");
                requestHeaders.put(keyValue[0].trim(), keyValue[1].trim());
            }

            Map<String, Object> expectedResponse = new HashMap<>();
            String[] responseBodyAttributes = responseBody.split(",");
            for (String attribute : responseBodyAttributes) {
                String[] attributeNameAndType = attribute.split(":");
                expectedResponse.put(attributeNameAndType[0].trim(), attributeNameAndType[1].trim());
            }

            Response response = RestAssured.given()
                    .headers(requestHeaders)
                    .when()
                    .get(url);

            Assertions.assertEquals(responseCode, response.getStatusCode());

            JsonPath jsonPathValidator = response.jsonPath();
            for (Map.Entry<String, Object> entry : expectedResponse.entrySet()) {
                Assertions.assertEquals(entry.getValue(), jsonPathValidator.get(entry.getKey()));
            }

        }
        scanner.close();

    }
}
