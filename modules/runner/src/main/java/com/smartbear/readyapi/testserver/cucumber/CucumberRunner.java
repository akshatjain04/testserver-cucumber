package com.smartbear.readyapi.testserver.cucumber;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;

public class CucumberRunner {

    private static final String STEPDEFS_PACKAGE = "com.smartbear.readyapi.testserver.cucumber";

    public static void main(String [] args) throws Throwable {
        System.out.println( "Ready! API TestServer Cucumber Runner" );

        ArrayList<String> argsList = Lists.newArrayList(args);
        argsList.add(0, STEPDEFS_PACKAGE);
        argsList.add(0, "-g");

        cucumber.api.cli.Main.main( argsList.toArray( new String[argsList.size()]) );
    }
}
