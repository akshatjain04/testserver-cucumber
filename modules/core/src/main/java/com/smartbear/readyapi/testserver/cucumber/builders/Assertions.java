package com.smartbear.readyapi.testserver.cucumber.builders;

import com.smartbear.readyapi.client.model.GroovyScriptAssertion;
import com.smartbear.readyapi.client.model.ResponseSLAAssertion;
import com.smartbear.readyapi.client.model.SimpleContainsAssertion;
import com.smartbear.readyapi.client.model.ValidHttpStatusCodesAssertion;

import java.util.List;

/**
 * Utility class with static method for building various types of assertions
 */

public class Assertions {
    public static ValidHttpStatusCodesAssertion validStatusCodes(List<Integer> statusCodes ){
        ValidHttpStatusCodesAssertion httpStatusCodesAssertion = new ValidHttpStatusCodesAssertion();
        httpStatusCodesAssertion.setValidStatusCodes(statusCodes);
        httpStatusCodesAssertion.setType("Valid HTTP Status Codes");
        return httpStatusCodesAssertion;
    }

    public static ResponseSLAAssertion timeout(int timeout) {
        ResponseSLAAssertion slaAssertion = new ResponseSLAAssertion();
        slaAssertion.setMaxResponseTime(timeout);
        slaAssertion.setType("Response SLA");
        return slaAssertion;
    }

    public static SimpleContainsAssertion bodyContains(String responseBody) {
        SimpleContainsAssertion contentAssertion = new SimpleContainsAssertion();
        contentAssertion.setToken(responseBody.trim());
        contentAssertion.setType("Contains");
        contentAssertion.setIgnoreCase(true);
        return contentAssertion;
    }

    public static SimpleContainsAssertion bodyMatches(String responseBodyRegEx) {
        SimpleContainsAssertion contentAssertion = new SimpleContainsAssertion();
        contentAssertion.setToken(responseBodyRegEx.trim());
        contentAssertion.setType("Contains");
        contentAssertion.setUseRegexp(true);
        return contentAssertion;
    }

    public static GroovyScriptAssertion responseType(String format) {
        GroovyScriptAssertion scriptAssertion = new GroovyScriptAssertion();
        scriptAssertion.setType("Script Assertion");
        scriptAssertion.setScript(
            "assert messageExchange.responseHeaders[\"Content-Type\"].contains( \"" + Support.expandContentType(format) + "\")");
        return scriptAssertion;
    }

    public static GroovyScriptAssertion responseContainsHeader(String header) {
        GroovyScriptAssertion scriptAssertion = new GroovyScriptAssertion();
        scriptAssertion.setType("Script Assertion");
        scriptAssertion.setScript(
            "assert messageExchange.responseHeaders.containsKey(\"" + header + "\")");

        return scriptAssertion;
    }

    public static GroovyScriptAssertion responseHeader(String header, String value) {
        GroovyScriptAssertion scriptAssertion = new GroovyScriptAssertion();
        scriptAssertion.setType("Script Assertion");
        scriptAssertion.setScript(
            "assert messageExchange.responseHeaders[\"" + header + "\"].contains( \"" + value + "\")");

        return scriptAssertion;
    }

}
