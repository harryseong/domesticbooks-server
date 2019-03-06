# Run "mvn clean package -DskipTests" to create JAR and Docker image.

FROM openjdk:8-jdk-alpine
LABEL maintainer="harryseong@gmail.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/domestic_books-0.0.3-SNAPSHOT.jar
ADD ${JAR_FILE} domestic_books.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/domestic_books.jar"]
