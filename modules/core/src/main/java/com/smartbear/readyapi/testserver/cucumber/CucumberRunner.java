package com.smartbear.readyapi.testserver.cucumber;

import com.google.common.collect.Lists;

import java.util.ArrayList;

public class CucumberRunner {
    public static void main( String [] args) throws Throwable {
        System.out.println( "Ready! API TestServer Cucumber Runner" );

        ArrayList<String> argsList = Lists.newArrayList(args);
        if( !argsList.contains( "-g")){
            argsList.add(0, "com.smartbear.readyapi.testserver.cucumber");
            argsList.add(0, "-g");
        }

        cucumber.api.cli.Main.main( argsList.toArray( new String[argsList.size()]) );
    }
}
