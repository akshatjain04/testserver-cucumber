package com.smartbear.readyapi.testserver.codegen;

import com.google.common.collect.Lists;
import io.swagger.codegen.CliOption;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.DefaultCodegen;
import io.swagger.codegen.SupportingFile;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestServerCucumberFeatureGenerator extends DefaultCodegen implements CodegenConfig {

    protected String sourceFolder = "features";

    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    public String getName() {
        return "TestServerCucumberCodegen";
    }

    public String getHelp() {
        return "Generates Ready API TestServer Feature file(s)";
    }

    public TestServerCucumberFeatureGenerator() {
        super();

        outputFolder = "generated-test-resources/features";
        apiTemplateFiles.put( "feature.mustache", ".feature");
        templateDir = "TestServerCucumberCodeGen";
    }

    @Override
    public void addOperationToGroup(String tag, String resourcePath, Operation operation, CodegenOperation co, Map<String, List<CodegenOperation>> operations) {
        List<CodegenOperation> group = operations.get(operation.getOperationId());
        if( group == null ){
            group = new ArrayList<CodegenOperation>();
        }

        group.add( co );
        operations.put( operation.getOperationId(), group );
    }

    @Override
    public void preprocessSwagger(Swagger swagger) {
        super.preprocessSwagger(swagger);
    }

    @Override
    public String apiFileFolder() {
        return outputFolder + "/" + sourceFolder;
    }
}