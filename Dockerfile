FROM openjdk:11
ARG JAR_FILE=target/collaborative-web-be-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} collaborative-web-be-0.0.1-SNAPSHOT.jar
EXPOSE 8080-8090
ENTRYPOINT ["java", "-jar" , "collaborative-web-be-0.0.1-SNAPSHOT.jar"]

