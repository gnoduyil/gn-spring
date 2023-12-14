FROM openjdk:21-jdk-oracle
WORKDIR /app
COPY target/gn-web-app-0.0.1.jar /app/gn-web-app.jar
CMD ["java", "-jar", "gn-web-app.jar"]
