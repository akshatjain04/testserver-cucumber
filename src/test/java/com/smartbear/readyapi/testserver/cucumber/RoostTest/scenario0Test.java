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
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import static org.junit.Assert.assertEquals;

public class Scenario0Test {
    @Test
    public void findPetByStatus() {
        List<String> lines = Arrays.asList(StringUtils.split(
                "METHOD^|^URL^|^REQ_HEADERS^|^REQ_BODY^|^RESPONSE_CODE^|^RESPONSE_BODY\n" + 
                "GET^|^http://petstore.swagger.io/v2/pet/findByStatus^|^{ \"Accept\": \"application/json\" }^|^{ \"status\": \"test\" }^|^200^|^{ \"message\": \"Pet found by status successfully\" }\n",
                "\n"));
        for (int i = 1; i < lines.size(); i++) {
            List<String> items = Arrays.asList(StringUtils.split(lines.get(i), "^|^"));
            String methodType = items.get(0);
            String url = items.get(1);
            String headersRaw = items.get(2).substring(1, items.get(2).length() - 1).replaceAll("\\\\", "");
            String reqBody = items.get(3).substring(1, items.get(3).length() - 1).replaceAll("\\\\", "");
            int expectedResponseCode = Integer.parseInt(items.get(4).substring(1, items.get(4).length() - 1));
            String expectedResponseBodyRaw = items.get(5).substring(1, items.get(5).length() - 1).replaceAll("\\\\", "");

            RestAssured.baseURI = url;
            RequestSpecification request = RestAssured.given();
            request.header("Content-Type", "application/json");
            request.header("headersRaw", headersRaw);
            request.body(reqBody);

            Response response;
            if (methodType.equals("GET")) {
                response = request.request(Method.GET);
            } else {
                throw new IllegalArgumentException("Unsupported method type");
            }

            int actualResponseCode = response.getStatusCode();
            assertEquals(expectedResponseCode, actualResponseCode);

            JsonPath jsonPath = response.jsonPath();
            String message = jsonPath.get("message");
            assertEquals(expectedResponseBodyRaw, message);
        }
    }
}
