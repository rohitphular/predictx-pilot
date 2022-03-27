# Content Analyser

APIs to process content

## Running service locally

Use spring profile to run locally

```bash
-Dspring.profiles.active=local
```

## Swagger URL

```bash
http://localhost:7004/api/predictx/swagger-ui/index.html
```

## Running JUnit & Integration test

Use test goal of maven. Both are combined in different execution.
More info inside pom.xml

```bash
mvn test
```

## Packaging

Use package goal of maven to create artifact and docker image 

```bash
mvn package
```