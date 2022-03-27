FROM maven:3.8.4-openjdk-11

WORKDIR /app

COPY maven/${project.artifactId}.jar analysis-service.jar

CMD ["java", "-jar", "analysis-service.jar"]