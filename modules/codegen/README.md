# Swagger Cucumber Feature Codegen 

Generates default Cucumber feature files from a Swagger definition

## Usage

You will need [Swagger Codegen](https://github.com/swagger-api/swagger-codegen) locally - 
then clone this repo and build it with 

```
mvn clean package
```

Then you're all set to use it with 

```
java -cp target/testserver-cucumber-codegen-1.0.0.jar:<path to swagger-codegen-cli.jar> 
    io.swagger.codegen.SwaggerCodegen generate -l TestServerCucumberCodegen 
    -i <url/path to Swagger definition> -o output
```

which will generate one feature file for each operation in the specified Swagger Definition. By default
files will be generated into the {build.dir}/generated-test-resources/features folder.

