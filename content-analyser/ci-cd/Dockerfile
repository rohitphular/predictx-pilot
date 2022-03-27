FROM maven:3.8.4-openjdk-11

WORKDIR /app

COPY maven/${project.artifactId}.jar content-analyser.jar

CMD ["java", "-jar", "content-analyser.jar"]